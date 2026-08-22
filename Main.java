import java.util.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;

// ===================== ENUMS =====================
enum Role { EMPLOYEE, HR }
enum LeaveType { PAID, SICK, UNPAID }
enum LeaveStatus { PENDING, APPROVED, REJECTED }
enum AttendanceStatus { PRESENT, ABSENT, HALF_DAY, LEAVE }

// ===================== PROFILE =====================
class Profile {
    String name = "Not set";
    String phone = "Not set";
    String address = "Not set";
    String profilePicture = "No picture uploaded";
    String jobTitle = "Not assigned";
    String department = "Not assigned";
    String documents = "No documents uploaded";
    double basicSalary = 0.0;
    double allowances = 0.0;
    double deductions = 0.0;

    double netSalary() {
        return basicSalary + allowances - deductions;
    }

    void printFull() {
        System.out.println("Name           : " + name);
        System.out.println("Phone          : " + phone);
        System.out.println("Address        : " + address);
        System.out.println("Profile Picture: " + profilePicture);
        System.out.println("Job Title      : " + jobTitle);
        System.out.println("Department     : " + department);
        System.out.println("Documents      : " + documents);
        System.out.printf ("Basic Salary   : $%.2f%n", basicSalary);
        System.out.printf ("Allowances     : $%.2f%n", allowances);
        System.out.printf ("Deductions     : $%.2f%n", deductions);
        System.out.printf ("Net Salary     : $%.2f%n", netSalary());
    }
}

// ===================== ATTENDANCE =====================
class Attendance {
    LocalDate date;
    String checkInTime = "--";
    String checkOutTime = "--";
    AttendanceStatus status;

    Attendance(LocalDate date, String checkInTime, AttendanceStatus status) {
        this.date = date;
        this.checkInTime = checkInTime;
        this.status = status;
    }

    String display() {
        return "Date: " + date + " | In: " + checkInTime + " | Out: " + checkOutTime + " | Status: " + status;
    }
}

// ===================== LEAVE REQUEST =====================
class LeaveRequest {
    LeaveType type;
    LocalDate startDate;
    LocalDate endDate;
    String remarks;
    LeaveStatus status = LeaveStatus.PENDING;
    String comment = "No comment yet";

    LeaveRequest(LeaveType type, LocalDate startDate, LocalDate endDate, String remarks) {
        this.type = type;
        this.startDate = startDate;
        this.endDate = endDate;
        this.remarks = remarks;
    }

    boolean overlaps(LocalDate s, LocalDate e) {
        return !(e.isBefore(startDate) || s.isAfter(endDate));
    }

    String display() {
        return "Type: " + type + " | From: " + startDate + " To: " + endDate
                + " | Remarks: " + remarks + " | Status: " + status + " | Comment: " + comment;
    }
}

// ===================== USER =====================
class User {
    String employeeId;
    String email;
    String password;
    Role role;
    boolean verified = false;
    Profile profile = new Profile();
    List<LeaveRequest> leaves = new ArrayList<>();
    List<Attendance> attendanceRecords = new ArrayList<>();

    User(String employeeId, String email, String password, Role role) {
        this.employeeId = employeeId;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    Attendance findRecordForDate(LocalDate date) {
        for (Attendance a : attendanceRecords) {
            if (a.date.equals(date)) return a;
        }
        return null;
    }

    boolean hasApprovedOrPendingLeaveOverlap(LocalDate s, LocalDate e) {
        for (LeaveRequest lr : leaves) {
            if (lr.status != LeaveStatus.REJECTED && lr.overlaps(s, e)) return true;
        }
        return false;
    }
}

// ===================== MAIN PROGRAM =====================
public class Main {
    private static final List<User> database = new ArrayList<>();
    private static final Scanner sc = new Scanner(System.in);
    private static User loggedInUser = null;
    private static final DateTimeFormatter DATE_FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter TIME_FMT = DateTimeFormatter.ofPattern("HH:mm:ss");

    public static void main(String[] args) {
        int choice;
        do {
            System.out.println("\n===== DAYFLOW HRMS =====");
            System.out.println("Every workday, perfectly aligned.");
            System.out.println("1. Sign Up");
            System.out.println("2. Sign In");
            System.out.println("3. Exit");
            choice = readInt("Enter choice: ", 1, 3);

            switch (choice) {
                case 1: signUp(); break;
                case 2: signIn(); break;
                case 3: System.out.println("Thank you for using Dayflow."); break;
            }
        } while (choice != 3);
        sc.close();
    }

    // ----------------- INPUT HELPERS -----------------
    private static int readInt(String prompt, int min, int max) {
        while (true) {
            System.out.print(prompt);
            String line = sc.nextLine().trim();
            try {
                int val = Integer.parseInt(line);
                if (val < min || val > max) {
                    System.out.println("Please enter a number between " + min + " and " + max + ".");
                    continue;
                }
                return val;
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
            }
        }
    }

    private static String readNonEmpty(String prompt) {
        while (true) {
            System.out.print(prompt);
            String line = sc.nextLine().trim();
            if (!line.isEmpty()) return line;
            System.out.println("This field cannot be empty.");
        }
    }

    private static double readDouble(String prompt) {
        while (true) {
            System.out.print(prompt);
            String line = sc.nextLine().trim();
            try {
                double val = Double.parseDouble(line);
                if (val < 0) {
                    System.out.println("Value cannot be negative.");
                    continue;
                }
                return val;
            } catch (NumberFormatException e) {
                System.out.println("Invalid number. Try again.");
            }
        }
    }

    private static LocalDate readDate(String prompt) {
        while (true) {
            System.out.print(prompt + " (yyyy-MM-dd): ");
            String line = sc.nextLine().trim();
            try {
                return LocalDate.parse(line, DATE_FMT);
            } catch (DateTimeParseException e) {
                System.out.println("Invalid date format. Example: 2026-08-22");
            }
        }
    }

    private static String readYesNo(String prompt) {
        while (true) {
            System.out.print(prompt + " (Y/N): ");
            String line = sc.nextLine().trim();
            if (line.equalsIgnoreCase("Y") || line.equalsIgnoreCase("N")) return line.toUpperCase();
            System.out.println("Please enter Y or N.");
        }
    }

    // ----------------- AUTH -----------------
    private static boolean employeeIdExists(String id) {
        for (User u : database) if (u.employeeId.equalsIgnoreCase(id)) return true;
        return false;
    }

    private static boolean emailExists(String email) {
        for (User u : database) if (u.email.equalsIgnoreCase(email)) return true;
        return false;
    }

    private static void signUp() {
        System.out.println("\n--- SIGN UP ---");

        String id = readNonEmpty("Enter Employee ID: ");
        if (employeeIdExists(id)) {
            System.out.println("Error: Employee ID already registered.");
            return;
        }

        String email = readNonEmpty("Enter Email: ");
        if (emailExists(email)) {
            System.out.println("Error: Email already registered.");
            return;
        }

        String password = readNonEmpty("Enter Password (min 8 chars, 1 uppercase, 1 number): ");
        if (!password.matches(".*[A-Z].*") || !password.matches(".*[0-9].*") || password.length() < 8) {
            System.out.println("Error: Password does not meet security rules.");
            return;
        }

        Role role;
        String roleInput = readNonEmpty("Enter Role (Employee/HR): ");
        if (roleInput.equalsIgnoreCase("Employee")) role = Role.EMPLOYEE;
        else if (roleInput.equalsIgnoreCase("HR")) role = Role.HR;
        else {
            System.out.println("Error: Invalid role.");
            return;
        }

        User newUser = new User(id, email, password, role);
        newUser.profile.name = readNonEmpty("Enter Full Name: ");

        System.out.println("\nRegistration successful. Verification email sent to " + email);
        String code = readNonEmpty("Enter verification code (1234): ");
        if (code.equals("1234")) {
            newUser.verified = true;
            database.add(newUser);
            System.out.println("Email verified successfully. You can now sign in.");
        } else {
            System.out.println("Email verification failed. Account not created.");
        }
    }

    private static void signIn() {
        System.out.println("\n--- SIGN IN ---");
        String email = readNonEmpty("Enter Email: ");
        String password = readNonEmpty("Enter Password: ");

        for (User u : database) {
            if (u.email.equalsIgnoreCase(email) && u.password.equals(password)) {
                if (!u.verified) {
                    System.out.println("Please verify your email before signing in.");
                    return;
                }
                loggedInUser = u;
                System.out.println("\nLogin successful! Welcome, " + u.profile.name);
                routeToDashboard();
                return;
            }
        }
        System.out.println("Incorrect email or password.");
    }

    private static void routeToDashboard() {
        if (loggedInUser.role == Role.HR) {
            hrDashboard();
        } else {
            employeeDashboard();
        }
        loggedInUser = null;
    }

    // ================= EMPLOYEE DASHBOARD =================
    private static void employeeDashboard() {
        int choice;
        do {
            System.out.println("\n===== EMPLOYEE DASHBOARD =====");
            System.out.println("1. Profile");
            System.out.println("2. Attendance");
            System.out.println("3. Leave Requests");
            System.out.println("4. Salary Details");
            System.out.println("5. Logout");
            choice = readInt("Enter choice: ", 1, 5);

            switch (choice) {
                case 1: employeeProfileMenu(); break;
                case 2: employeeAttendanceMenu(); break;
                case 3: employeeLeaveMenu(); break;
                case 4: employeeSalaryView(); break;
                case 5: System.out.println("Logging out..."); break;
            }
        } while (choice != 5);
    }

    private static void employeeProfileMenu() {
        System.out.println("\n--- MY PROFILE ---");
        System.out.println("Employee ID: " + loggedInUser.employeeId);
        System.out.println("Email      : " + loggedInUser.email);
        loggedInUser.profile.printFull();

        if (readYesNo("\nEdit allowed details (Phone/Address/Profile Picture)?").equals("Y")) {
            loggedInUser.profile.phone = readNonEmpty("New Phone: ");
            loggedInUser.profile.address = readNonEmpty("New Address: ");
            loggedInUser.profile.profilePicture = readNonEmpty("New Profile Picture (filename/description): ");
            System.out.println("Profile updated successfully.");
        }
    }

    private static void employeeAttendanceMenu() {
        int choice;
        do {
            System.out.println("\n--- ATTENDANCE ---");
            System.out.println("1. Check In");
            System.out.println("2. Check Out");
            System.out.println("3. View Daily Attendance (Today)");
            System.out.println("4. View Weekly Attendance (Last 7 Days)");
            System.out.println("5. View All Attendance");
            System.out.println("6. Back");
            choice = readInt("Enter choice: ", 1, 6);

            LocalDate today = LocalDate.now();
            String nowTime = TIME_FMT.format(LocalDateTime.now());

            switch (choice) {
                case 1:
                    if (loggedInUser.findRecordForDate(today) != null) {
                        System.out.println("You have already checked in today.");
                    } else {
                        loggedInUser.attendanceRecords.add(new Attendance(today, nowTime, AttendanceStatus.PRESENT));
                        System.out.println("Checked in at " + nowTime);
                    }
                    break;
                case 2:
                    Attendance rec = loggedInUser.findRecordForDate(today);
                    if (rec == null) {
                        System.out.println("Error: You must check in before checking out.");
                    } else if (!rec.checkOutTime.equals("--")) {
                        System.out.println("You have already checked out today.");
                    } else {
                        rec.checkOutTime = nowTime;
                        System.out.println("Checked out at " + nowTime);
                    }
                    break;
                case 3:
                    Attendance todayRec = loggedInUser.findRecordForDate(today);
                    System.out.println("\n--- TODAY'S ATTENDANCE ---");
                    System.out.println(todayRec != null ? todayRec.display() : "No record for today (Absent).");
                    break;
                case 4:
                    System.out.println("\n--- WEEKLY ATTENDANCE (Last 7 Days) ---");
                    boolean any = false;
                    for (Attendance a : loggedInUser.attendanceRecords) {
                        if (ChronoUnit.DAYS.between(a.date, today) < 7 && !a.date.isAfter(today)) {
                            System.out.println(a.display());
                            any = true;
                        }
                    }
                    if (!any) System.out.println("No attendance records in the last 7 days.");
                    break;
                case 5:
                    System.out.println("\n--- ALL ATTENDANCE RECORDS ---");
                    if (loggedInUser.attendanceRecords.isEmpty()) {
                        System.out.println("No attendance records found.");
                    } else {
                        for (Attendance a : loggedInUser.attendanceRecords) System.out.println(a.display());
                    }
                    break;
                case 6: break;
            }
        } while (choice != 6);
    }

    private static void employeeLeaveMenu() {
        int choice;
        do {
            System.out.println("\n--- LEAVE REQUESTS ---");
            System.out.println("1. Apply for Leave");
            System.out.println("2. View My Leave Requests");
            System.out.println("3. Back");
            choice = readInt("Enter choice: ", 1, 3);

            switch (choice) {
                case 1: applyForLeave(); break;
                case 2:
                    System.out.println("\n--- MY LEAVE REQUESTS ---");
                    if (loggedInUser.leaves.isEmpty()) {
                        System.out.println("You have not submitted any leave requests.");
                    } else {
                        for (LeaveRequest lr : loggedInUser.leaves) System.out.println(lr.display());
                    }
                    break;
                case 3: break;
            }
        } while (choice != 3);
    }

    private static void applyForLeave() {
        System.out.println("\n--- APPLY FOR LEAVE ---");
        System.out.println("Leave Type: 1. Paid  2. Sick  3. Unpaid");
        int t = readInt("Choose type: ", 1, 3);
        LeaveType type = t == 1 ? LeaveType.PAID : t == 2 ? LeaveType.SICK : LeaveType.UNPAID;

        LocalDate start = readDate("Start Date");
        LocalDate end = readDate("End Date");
        if (end.isBefore(start)) {
            System.out.println("Error: End date cannot be before start date.");
            return;
        }
        if (loggedInUser.hasApprovedOrPendingLeaveOverlap(start, end)) {
            System.out.println("Error: You already have a pending/approved leave overlapping these dates.");
            return;
        }

        String remarks = readNonEmpty("Remarks: ");
        loggedInUser.leaves.add(new LeaveRequest(type, start, end, remarks));
        System.out.println("Leave request submitted. Status: Pending");
    }

    private static void employeeSalaryView() {
        System.out.println("\n--- SALARY DETAILS (Read-Only) ---");
        Profile p = loggedInUser.profile;
        System.out.printf("Basic Salary : $%.2f%n", p.basicSalary);
        System.out.printf("Allowances   : $%.2f%n", p.allowances);
        System.out.printf("Deductions   : $%.2f%n", p.deductions);
        System.out.printf("Net Salary   : $%.2f%n", p.netSalary());
    }

    // ================= HR DASHBOARD =================
    private static void hrDashboard() {
        int choice;
        do {
            System.out.println("\n===== HR DASHBOARD =====");
            System.out.println("1. View Employees");
            System.out.println("2. Edit Employee");
            System.out.println("3. View Attendance");
            System.out.println("4. Manage Leave Requests");
            System.out.println("5. Manage Payroll");
            System.out.println("6. Logout");
            choice = readInt("Enter choice: ", 1, 6);

            switch (choice) {
                case 1: viewAllEmployees(); break;
                case 2: editEmployee(); break;
                case 3: viewAttendanceHR(); break;
                case 4: manageLeaveRequests(); break;
                case 5: managePayroll(); break;
                case 6: System.out.println("Logging out..."); break;
            }
        } while (choice != 6);
    }

    private static void viewAllEmployees() {
        System.out.println("\n--- EMPLOYEE DIRECTORY ---");
        if (database.isEmpty()) {
            System.out.println("No employees registered.");
            return;
        }
        for (User u : database) {
            System.out.println("ID: " + u.employeeId + " | Name: " + u.profile.name
                    + " | Email: " + u.email + " | Role: " + u.role
                    + " | Dept: " + u.profile.department + " | Salary: $" + u.profile.basicSalary);
        }
    }

    private static User selectEmployeeById() {
        String id = readNonEmpty("Enter Employee ID: ");
        for (User u : database) {
            if (u.employeeId.equalsIgnoreCase(id)) return u;
        }
        System.out.println("Employee not found.");
        return null;
    }

    private static void editEmployee() {
        System.out.println("\n--- EDIT EMPLOYEE ---");
        User target = selectEmployeeById();
        if (target == null) return;

        System.out.println("\nEmployee Details");
        System.out.println("----------------");
        System.out.println("ID     : " + target.employeeId);
        System.out.println("Email  : " + target.email);
        target.profile.printFull();

        System.out.println("\nConfirm this is the employee you want to edit.");
        if (!readYesNo("Proceed with editing this employee?").equals("Y")) {
            System.out.println("Edit cancelled.");
            return;
        }

        int choice;
        do {
            System.out.println("\n1. Edit Name\n2. Edit Phone\n3. Edit Address\n4. Edit Job Title"
                    + "\n5. Edit Department\n6. Edit Documents\n7. Back");
            choice = readInt("Choose field to edit: ", 1, 7);
            switch (choice) {
                case 1: target.profile.name = readNonEmpty("New Name: "); break;
                case 2: target.profile.phone = readNonEmpty("New Phone: "); break;
                case 3: target.profile.address = readNonEmpty("New Address: "); break;
                case 4: target.profile.jobTitle = readNonEmpty("New Job Title: "); break;
                case 5: target.profile.department = readNonEmpty("New Department: "); break;
                case 6: target.profile.documents = readNonEmpty("New Documents info: "); break;
                case 7: break;
            }
            if (choice != 7) System.out.println("Updated successfully.");
        } while (choice != 7);
    }

    private static void viewAttendanceHR() {
        System.out.println("\n--- VIEW ATTENDANCE ---");
        System.out.println("1. View All Employees' Attendance");
        System.out.println("2. View a Specific Employee's Attendance");
        int mode = readInt("Choose option: ", 1, 2);

        List<User> targets = new ArrayList<>();
        if (mode == 1) {
            targets.addAll(database);
        } else {
            User u = selectEmployeeById();
            if (u == null) return;
            targets.add(u);
        }

        System.out.println("1. Daily (Today)\n2. Weekly (Last 7 Days)\n3. All Records");
        int range = readInt("Choose range: ", 1, 3);
        LocalDate today = LocalDate.now();

        for (User u : targets) {
            System.out.println("\nEmployee: " + u.employeeId + " (" + u.profile.name + ")");
            boolean any = false;
            for (Attendance a : u.attendanceRecords) {
                boolean show = switch (range) {
                    case 1 -> a.date.equals(today);
                    case 2 -> !a.date.isAfter(today) && ChronoUnit.DAYS.between(a.date, today) < 7;
                    default -> true;
                };
                if (show) {
                    System.out.println("  " + a.display());
                    any = true;
                }
            }
            if (!any) System.out.println("  No matching attendance records.");
        }
    }

    private static void manageLeaveRequests() {
        System.out.println("\n--- PENDING LEAVE REQUESTS ---");
        boolean anyPending = false;

        for (User u : database) {
            for (LeaveRequest lr : u.leaves) {
                if (lr.status == LeaveStatus.PENDING) {
                    anyPending = true;
                    System.out.println("\nEmployee: " + u.employeeId + " (" + u.profile.name + ")");
                    System.out.println(lr.display());
                    if (readYesNo("Approve this request?").equals("Y")) {
                        lr.status = LeaveStatus.APPROVED;
                    } else {
                        lr.status = LeaveStatus.REJECTED;
                    }
                    lr.comment = readNonEmpty("Add a comment: ");
                    System.out.println("Request marked as " + lr.status + ".");
                }
            }
        }
        if (!anyPending) System.out.println("No pending leave requests.");
    }

    private static void managePayroll() {
        System.out.println("\n--- MANAGE PAYROLL ---");
        System.out.println("1. View Payroll of All Employees");
        System.out.println("2. Update Salary Structure for an Employee");
        int choice = readInt("Choose option: ", 1, 2);

        if (choice == 1) {
            for (User u : database) {
                Profile p = u.profile;
                System.out.printf("ID: %s | Basic: $%.2f | Allowances: $%.2f | Deductions: $%.2f | Net: $%.2f%n",
                        u.employeeId, p.basicSalary, p.allowances, p.deductions, p.netSalary());
            }
        } else {
            User target = selectEmployeeById();
            if (target == null) return;
            target.profile.basicSalary = readDouble("New Basic Salary: ");
            target.profile.allowances = readDouble("New Allowances: ");
            target.profile.deductions = readDouble("New Deductions: ");
            System.out.printf("Payroll updated. Net Salary: $%.2f%n", target.profile.netSalary());
        }
    }
}
