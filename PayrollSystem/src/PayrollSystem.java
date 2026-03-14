import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class PayrollSystem {

    private static List<Employee> employees = new ArrayList<>();
    private static int nextId = 1;
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        // Seed with some sample employees
        employees.add(new Employee(nextId++, "Juan dela Cruz",    "Software Engineer", 250.0));
        employees.add(new Employee(nextId++, "Maria Santos",      "Project Manager",   350.0));
        employees.add(new Employee(nextId++, "Pedro Reyes",       "QA Analyst",        200.0));

        boolean running = true;
        while (running) {
            printMenu();
            int choice = readInt("Enter choice: ");
            switch (choice) {
                case 1 -> listEmployees();
                case 2 -> addEmployee();
                case 3 -> generatePayslip();
                case 4 -> generateAllPayslips();
                case 5 -> {
                    System.out.println("Exiting Payroll System. Goodbye!");
                    running = false;
                }
                default -> System.out.println("Invalid choice. Please try again.\n");
            }
        }
        scanner.close();
    }

    private static void printMenu() {
        System.out.println("\n" + "=".repeat(40));
        System.out.println("      SIMPLE JAVA PAYROLL SYSTEM");
        System.out.println("=".repeat(40));
        System.out.println(" 1. List Employees");
        System.out.println(" 2. Add Employee");
        System.out.println(" 3. Generate Payslip for Employee");
        System.out.println(" 4. Generate All Payslips");
        System.out.println(" 5. Exit");
        System.out.println("=".repeat(40));
    }

    private static void listEmployees() {
        if (employees.isEmpty()) {
            System.out.println("No employees found.");
            return;
        }
        System.out.println("\n--- Employee List ---");
        for (Employee e : employees) {
            System.out.println(e);
        }
    }

    private static void addEmployee() {
        System.out.println("\n--- Add New Employee ---");
        System.out.print("Name     : ");
        String name = scanner.nextLine().trim();
        System.out.print("Position : ");
        String position = scanner.nextLine().trim();
        double rate = readDouble("Hourly Rate (PHP): ");

        Employee emp = new Employee(nextId++, name, position, rate);
        employees.add(emp);
        System.out.println("Employee added successfully: " + emp);
    }

    private static void generatePayslip() {
        if (employees.isEmpty()) {
            System.out.println("No employees found.");
            return;
        }
        listEmployees();
        int id = readInt("\nEnter Employee ID to generate payslip: ");
        Employee target = findEmployeeById(id);
        if (target == null) {
            System.out.println("Employee not found with ID: " + id);
            return;
        }
        double hours = readDouble("Enter hours worked this week: ");
        Payroll payroll = new Payroll(target, hours);
        payroll.printPayslip();
    }

    private static void generateAllPayslips() {
        if (employees.isEmpty()) {
            System.out.println("No employees found.");
            return;
        }
        System.out.println("\n--- Generate Payslips for All Employees ---");
        for (Employee emp : employees) {
            double hours = readDouble("Hours worked for " + emp.getName() + ": ");
            Payroll payroll = new Payroll(emp, hours);
            payroll.printPayslip();
        }
    }

    private static Employee findEmployeeById(int id) {
        for (Employee e : employees) {
            if (e.getId() == id) {
                return e;
            }
        }
        return null;
    }

    private static int readInt(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                int value = Integer.parseInt(scanner.nextLine().trim());
                return value;
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a whole number.");
            }
        }
    }

    private static double readDouble(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                double value = Double.parseDouble(scanner.nextLine().trim());
                if (value < 0) {
                    System.out.println("Value cannot be negative. Please try again.");
                    continue;
                }
                return value;
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid number.");
            }
        }
    }
}
