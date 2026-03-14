# Simple Java Payroll System

A basic console-based payroll system written in Java.

## Features

- List all employees
- Add new employees
- Generate a payslip for a specific employee
- Generate payslips for all employees
- Overtime pay calculated at **1.5×** the hourly rate for hours beyond 40/week
- Automatic deductions: **SSS**, **PhilHealth**, **Pag-IBIG**, and simplified **income tax**

## How to Compile & Run

```bash
# From the PayrollSystem/ directory
javac -d out src/*.java
java -cp out PayrollSystem
```

## Project Structure

```
PayrollSystem/
├── src/
│   ├── Employee.java        # Employee model
│   ├── Payroll.java         # Payroll/payslip calculation logic
│   └── PayrollSystem.java   # Main driver with console menu
└── README.md
```

## Sample Payslip Output

```
=======================================================
                  PAYSLIP
=======================================================
Employee ID   : 1
Name          : Juan dela Cruz
Position      : Software Engineer
Hourly Rate   : PHP 250.00
-------------------------------------------------------
Regular Hours : 40.00 hrs
Overtime Hours: 5.00 hrs
Regular Pay   : PHP 10,000.00
Overtime Pay  : PHP 1,875.00
Gross Pay     : PHP 11,875.00
-------------------------------------------------------
DEDUCTIONS:
  SSS         : PHP 534.38
  PhilHealth  : PHP 296.88
  Pag-IBIG    : PHP 237.50
  Tax         : PHP 1,781.25
Total Deduct. : PHP 2,850.00
-------------------------------------------------------
NET PAY       : PHP 9,025.00
=======================================================
```
