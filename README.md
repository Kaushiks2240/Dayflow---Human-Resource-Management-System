# Dayflow – Human Resource Management System

[![Java](https://img.shields.io/badge/Java-17%2B-orange?style=for-the-badge&logo=openjdk)](https://www.java.com/)
[![Hackathon](https://img.shields.io/badge/Odoo%20%C3%97%20NMIT-Hackathon%202026-blue?style=for-the-badge)](https://hackathon.odoo.com/)
[![Team](https://img.shields.io/badge/Team-Nexus-purple?style=for-the-badge)](#-team)
[![Status](https://img.shields.io/badge/Status-Hackathon%20Submission-yellow?style=for-the-badge)](#-project-status)

# Dayflow

> **Every workday, perfectly aligned.**

Dayflow is a Human Resource Management System (HRMS) developed by **Team Nexus** for the **Odoo × NMIT Bangalore Hackathon 2026**.

The system is designed to digitize and streamline core HR operations including employee onboarding, profile management, attendance tracking, leave management, payroll visibility, and approval workflows for employees and HR officers.

---

## 📑 Table of Contents

- [Quick Start](#-quick-start)
- [Features](#-features)
- [Authentication](#-authentication)
- [Role-Based Access](#-role-based-access)
- [Attendance Management](#-attendance-management)
- [Leave Management](#-leave-management)
- [Payroll Management](#-payroll-management)
- [Dashboards](#-dashboards)
- [Technologies](#️-technologies)
- [Project Structure](#-project-structure)
- [Known Limitations](#️-known-limitations-hackathon-scope)
- [Odoo × NMIT Bangalore Hackathon 2026](#-odoo--nmit-bangalore-hackathon-2026)
- [Future Enhancements](#️-future-enhancements)
- [Team](#-team)
- [Project Status](#-project-status)

---

## 🚀 Quick Start

### Requirements

- Java JDK 17 or later
- VS Code / IntelliJ IDEA / Eclipse
- Windows, Linux, or macOS

### Clone the Repository

```bash
git clone https://github.com/Kaushiks2240/Dayflow---Human-Resource-Management-System-.git
cd Dayflow---Human-Resource-Management-System-
```

### Compile

```bash
javac Main.java
```

### Run

```bash
java Main
```

---

## ✨ Features

### Employee

- 🔐 Sign Up / Sign In
- ✉️ Email verification
- 🔑 Password validation
- 👤 Profile management
- 📋 Personal details
- 💼 Job details
- 📞 Phone and address management
- 🖼️ Profile picture information
- 📄 Document information
- ⏰ Check-in / Check-out
- 📅 Daily attendance
- 📊 Weekly attendance
- 📝 Leave applications
- 💼 Paid Leave
- 🤒 Sick Leave
- 📅 Unpaid Leave
- 🔎 Leave request tracking
- 💰 Salary details
- 🔒 Read-only salary access
- 🚪 Logout

### Admin / HR

- 🔐 HR authentication
- 👥 Employee directory
- 🔎 Employee selection
- 👤 Employee profile management
- ✏️ Edit employee information
- 📊 Attendance monitoring
- 📅 Daily attendance
- 📊 Weekly attendance
- 📝 Leave request management
- ✅ Approve leave requests
- ❌ Reject leave requests
- 💬 Add comments to leave decisions
- 💰 Payroll management
- 💵 Salary structure updates
- 🚪 Logout

---

## 🔐 Authentication

Dayflow provides role-based authentication for Employees and HR Officers.

### Sign Up

Users register using:

- Employee ID
- Email
- Password
- Role

Available roles:

```text
Employee
HR
```

### Password Requirements

Passwords must contain:

- Minimum 8 characters
- At least 1 uppercase letter
- At least 1 number

The system also prevents duplicate Employee IDs and email addresses.

### Email Verification

New accounts use a verification-code simulation before they can sign in.

---

## 👥 Role-Based Access

Dayflow provides different access levels depending on the user's role.

### Employee

```text
Employee Dashboard
│
├── Profile
├── Attendance
├── Leave Requests
├── Salary Details
└── Logout
```

Employees can access and manage their own permitted information.

### Admin / HR

```text
HR Dashboard
│
├── View Employees
├── Edit Employee
├── View Attendance
├── Manage Leave Requests
├── Manage Payroll
└── Logout
```

HR users can manage information across employees.

---

## ⏰ Attendance Management

Dayflow provides attendance tracking with the following statuses:

| Status     | Description                 |
| ---------- | ---------------------------- |
| `PRESENT`  | Employee is present          |
| `ABSENT`   | Employee is absent           |
| `HALF_DAY` | Employee worked a half day   |
| `LEAVE`    | Employee is on leave         |

Employees can:

- Check in
- Check out
- View today's attendance
- View weekly attendance
- View attendance history

HR can view attendance records for all employees or a selected employee.

The system prevents duplicate check-ins and invalid check-outs.

---

## 📝 Leave Management

Employees can apply for:

- Paid Leave
- Sick Leave
- Unpaid Leave

Each leave request contains:

- Leave type
- Start date
- End date
- Remarks
- Status
- HR comment

### Leave Status

```text
PENDING
APPROVED
REJECTED
```

### Leave Workflow

```text
Employee
    │
    ▼
Apply for Leave
    │
    ▼
 PENDING
    │
    ├──────────────┐
    ▼              ▼
APPROVED        REJECTED
    │              │
    └──────┬───────┘
           ▼
       HR Comment
```

---

## 💰 Payroll Management

Employees have read-only access to their salary information.

HR can manage:

- Basic Salary
- Allowances
- Deductions
- Net Salary

### Salary Calculation

```text
Net Salary = Basic Salary + Allowances - Deductions
```

---

## 📊 Dashboards

### Employee Dashboard

```text
===== EMPLOYEE DASHBOARD =====

1. Profile
2. Attendance
3. Leave Requests
4. Salary Details
5. Logout
```

### HR Dashboard

```text
===== HR DASHBOARD =====

1. View Employees
2. Edit Employee
3. View Attendance
4. Manage Leave Requests
5. Manage Payroll
6. Logout
```

---

## 🛠️ Technologies

- Java
- Object-Oriented Programming
- Java Collections Framework
- ArrayList
- Java Time API
- Console-based interface

---

## 📁 Project Structure

```text
Dayflow---Human-Resource-Management-System-/
│
├── Main.java
├── README.md
├── .gitignore
│
├── docs/
│   └── Dayflow-Requirements.pdf
│
└── screenshots/
    ├── login.png
    ├── employee-dashboard.png
    └── hr-dashboard.png
```



---

## ⚠️ Known Limitations (Hackathon Scope)

Dayflow was built within a hackathon timeframe and prioritizes core functionality over production hardening:

- 🗄️ **In-memory storage only** — all data resets when the program restarts; no database is connected yet.
- 🔓 **Passwords stored in plain text** — hashing is planned (see [Future Enhancements](#️-future-enhancements)) but not yet implemented.
- ✉️ **Simulated email verification** — the verification code is hardcoded for demo purposes rather than sent via a real mail service.
- 🖥️ **Console-based interface only** — no GUI or web front-end yet.

These are intentional scope trade-offs for the hackathon submission, not oversights — see below for what's planned next.

---

## 🏆 Odoo × NMIT Bangalore Hackathon 2026

**Team:** Nexus

Dayflow was developed as part of the **Odoo × NMIT Bangalore Hackathon 2026**.

### Project Repository

[https://github.com/Kaushiks2240/Dayflow---Human-Resource-Management-System-](https://github.com/Kaushiks2240/Dayflow---Human-Resource-Management-System-)

---

## 🗺️ Future Enhancements

- 📧 Real email notifications
- 🔔 Notification alerts
- 📊 Analytics dashboard
- 📄 Salary slips
- 📈 Attendance reports
- 🗄️ Database integration
- 💾 Persistent data storage
- 🌐 Web-based interface
- 🔐 Secure password hashing
- ☁️ Cloud deployment

---

## 👨‍💻 Team

### Nexus

<a href="https://github.com/Kaushiks2240">
<img src="https://github.com/Kaushiks2240.png" width="70px" alt="Kaushik S"/>
</a>
<a href="https://github.com/arham475">
<img src="https://github.com/arham475.png" width="70px" alt="Arham Jain"/>
</a>
<a href="https://github.com/vaibhav07vital-ops">
<img src="https://github.com/vaibhav07vital-ops.png" width="70px" alt="Vaibhav Upadhyay"/>
</a>

| Member               | GitHub                                                        |
| -------------------- | -------------------------------------------------------------- |
| **Kaushik S**        | [@Kaushiks2240](https://github.com/Kaushiks2240)               |
| **Arham Jain**       | [@arham475](https://github.com/arham475)                       |
| **Vaibhav Upadhyay** | [@vaibhav07vital-ops](https://github.com/vaibhav07vital-ops)   |

---

## 📌 Project Status

**Hackathon Submission**

Dayflow implements the core HRMS functionality for Employee and Admin/HR workflows.

---

<p align="center">

**Dayflow — Every workday, perfectly aligned.**

Built with Java by **Team Nexus**.

</p>
