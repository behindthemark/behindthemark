public class Payroll {
    private static final double OVERTIME_RATE_MULTIPLIER = 1.5;
    private static final double REGULAR_HOURS_PER_WEEK = 40.0;

    // Tax brackets (simplified)
    private static final double TAX_RATE_LOW    = 0.10; // up to PHP 10,000
    private static final double TAX_RATE_MID    = 0.15; // up to PHP 30,000
    private static final double TAX_RATE_HIGH   = 0.20; // above PHP 30,000

    // Fixed deduction rates
    private static final double SSS_RATE        = 0.045;
    private static final double PHILHEALTH_RATE = 0.025;
    private static final double PAGIBIG_RATE    = 0.02;

    private Employee employee;
    private double hoursWorked;

    public Payroll(Employee employee, double hoursWorked) {
        this.employee = employee;
        this.hoursWorked = hoursWorked;
    }

    public double getRegularHours() {
        return Math.min(hoursWorked, REGULAR_HOURS_PER_WEEK);
    }

    public double getOvertimeHours() {
        return Math.max(0, hoursWorked - REGULAR_HOURS_PER_WEEK);
    }

    public double getRegularPay() {
        return getRegularHours() * employee.getHourlyRate();
    }

    public double getOvertimePay() {
        return getOvertimeHours() * employee.getHourlyRate() * OVERTIME_RATE_MULTIPLIER;
    }

    public double getGrossPay() {
        return getRegularPay() + getOvertimePay();
    }

    public double getSSSDeduction() {
        return getGrossPay() * SSS_RATE;
    }

    public double getPhilHealthDeduction() {
        return getGrossPay() * PHILHEALTH_RATE;
    }

    public double getPagIbigDeduction() {
        return getGrossPay() * PAGIBIG_RATE;
    }

    public double getTaxDeduction() {
        double gross = getGrossPay();
        double tax = 0.0;
        if (gross <= 10_000) {
            tax = gross * TAX_RATE_LOW;
        } else if (gross <= 30_000) {
            tax = 10_000 * TAX_RATE_LOW
                + (gross - 10_000) * TAX_RATE_MID;
        } else {
            tax = 10_000 * TAX_RATE_LOW
                + 20_000 * TAX_RATE_MID
                + (gross - 30_000) * TAX_RATE_HIGH;
        }
        return tax;
    }

    public double getTotalDeductions() {
        return getSSSDeduction() + getPhilHealthDeduction() + getPagIbigDeduction() + getTaxDeduction();
    }

    public double getNetPay() {
        return getGrossPay() - getTotalDeductions();
    }

    public void printPayslip() {
        System.out.println("=".repeat(55));
        System.out.println("                  PAYSLIP");
        System.out.println("=".repeat(55));
        System.out.printf("Employee ID   : %d%n", employee.getId());
        System.out.printf("Name          : %s%n", employee.getName());
        System.out.printf("Position      : %s%n", employee.getPosition());
        System.out.printf("Hourly Rate   : PHP %.2f%n", employee.getHourlyRate());
        System.out.println("-".repeat(55));
        System.out.printf("Regular Hours : %.2f hrs%n", getRegularHours());
        System.out.printf("Overtime Hours: %.2f hrs%n", getOvertimeHours());
        System.out.printf("Regular Pay   : PHP %,.2f%n", getRegularPay());
        System.out.printf("Overtime Pay  : PHP %,.2f%n", getOvertimePay());
        System.out.printf("Gross Pay     : PHP %,.2f%n", getGrossPay());
        System.out.println("-".repeat(55));
        System.out.println("DEDUCTIONS:");
        System.out.printf("  SSS         : PHP %,.2f%n", getSSSDeduction());
        System.out.printf("  PhilHealth  : PHP %,.2f%n", getPhilHealthDeduction());
        System.out.printf("  Pag-IBIG    : PHP %,.2f%n", getPagIbigDeduction());
        System.out.printf("  Tax         : PHP %,.2f%n", getTaxDeduction());
        System.out.printf("Total Deduct. : PHP %,.2f%n", getTotalDeductions());
        System.out.println("-".repeat(55));
        System.out.printf("NET PAY       : PHP %,.2f%n", getNetPay());
        System.out.println("=".repeat(55));
    }
}
