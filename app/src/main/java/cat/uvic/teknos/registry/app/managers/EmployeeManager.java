package cat.uvic.teknos.registry.app.managers;

import cat.uvic.teknos.registry.models.Employee;
import cat.uvic.teknos.registry.models.ModelFactory;
import cat.uvic.teknos.registry.models.Shift;
import cat.uvic.teknos.registry.repositories.EmployeeRepository;
import cat.uvic.teknos.registry.repositories.RepositoryFactory;
import cat.uvic.teknos.registry.repositories.ShiftRepository;
import com.github.freva.asciitable.AsciiTable;
import com.github.freva.asciitable.Column;

import java.sql.Date;
import java.util.Arrays;
import java.util.InputMismatchException;
import java.util.Scanner;

public class EmployeeManager {
    private final Scanner scanner;
    private final EmployeeRepository employeeRepository;
    private final ShiftRepository shiftRepository;
    private final ModelFactory modelFactory;

    public EmployeeManager(Scanner scanner, RepositoryFactory repositoryFactory, ModelFactory modelFactory) {
        this.scanner = scanner;
        this.employeeRepository = repositoryFactory.getEmployeeRepository();
        this.shiftRepository = repositoryFactory.getShiftRepository();
        this.modelFactory = modelFactory;
    }

    public void manage() {
        boolean running = true;
        while (running) {
            printSubMenu();
            try {
                int choice = scanner.nextInt();
                scanner.nextLine(); // Consume newline
                switch (choice) {
                    case 1 -> viewAllEmployees();
                    case 2 -> addEmployee();
                    case 3 -> updateEmployee(); // New option
                    case 4 -> assignShift();
                    case 5 -> deleteEmployee();
                    case 0 -> running = false;
                    default -> System.out.println("Invalid option.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a number.");
                scanner.nextLine(); // Clear the buffer
            }
        }
    }

    private void addEmployee() {
        System.out.println("\n--- Add New Employee ---");
        Employee employee = modelFactory.newEmployee();

        System.out.print("Enter First Name: ");
        employee.setFirstName(scanner.nextLine());

        System.out.print("Enter Last Name: ");
        employee.setLastName(scanner.nextLine());

        System.out.print("Enter Email: ");
        employee.setEmail(scanner.nextLine());

        System.out.print("Enter Phone Number: ");
        employee.setPhoneNumber(scanner.nextLine());

        System.out.print("Enter Hire Date (YYYY-MM-DD): ");
        employee.setHireDate(Date.valueOf(scanner.nextLine()));

        employeeRepository.save(employee);
        System.out.println("Employee added successfully!");
    }

    private void updateEmployee() {
        System.out.println("\n--- Update Employee ---");
        System.out.print("Enter the ID of the employee to update: ");
        int employeeId = scanner.nextInt(); scanner.nextLine();

        Employee employee = employeeRepository.get(employeeId);
        if (employee == null) {
            System.out.println("Employee not found.");
            return;
        }

        System.out.printf("Enter First Name [%s]: ", employee.getFirstName());
        String firstName = scanner.nextLine();
        if (!firstName.isBlank()) {
            employee.setFirstName(firstName);
        }

        System.out.printf("Enter Last Name [%s]: ", employee.getLastName());
        String lastName = scanner.nextLine();
        if (!lastName.isBlank()) {
            employee.setLastName(lastName);
        }

        System.out.printf("Enter Email [%s]: ", employee.getEmail());
        String email = scanner.nextLine();
        if (!email.isBlank()) {
            employee.setEmail(email);
        }

        System.out.printf("Enter Phone Number [%s]: ", employee.getPhoneNumber());
        String phone = scanner.nextLine();
        if (!phone.isBlank()) {
            employee.setPhoneNumber(phone);
        }

        employeeRepository.save(employee);
        System.out.println("Employee updated successfully!");
    }


    private void viewAllEmployees() {
        System.out.println("\n--- All Employees ---");
        var employees = employeeRepository.getAll();
        if (employees.isEmpty()) {
            System.out.println("No employees found.");
            return;
        }

        System.out.println(AsciiTable.getTable(employees, Arrays.asList(
                new Column().header("ID").with(e -> String.valueOf(e.getId())),
                new Column().header("First Name").with(Employee::getFirstName),
                new Column().header("Last Name").with(Employee::getLastName),
                new Column().header("Email").with(Employee::getEmail),
                new Column().header("Hire Date").with(e -> String.valueOf(e.getHireDate()))
        )));
    }

    private void assignShift() {
        System.out.println("\n--- Assign Shift to Employee ---");
        System.out.print("Enter Employee ID: ");
        int employeeId = scanner.nextInt();
        scanner.nextLine();

        Employee employee = employeeRepository.get(employeeId);
        if (employee == null) {
            System.out.println("Employee not found.");
            return;
        }

        System.out.println("\n--- Available Shifts ---");
        var allShifts = shiftRepository.getAll();
        if (allShifts.isEmpty()){
            System.out.println("No shifts available to assign.");
            return;
        }
        System.out.println(AsciiTable.getTable(allShifts, Arrays.asList(
                new Column().header("ID").with(s -> String.valueOf(s.getId())),
                new Column().header("Name").with(Shift::getName),
                new Column().header("Location").with(Shift::getLocation)
        )));

        System.out.print("Enter Shift ID to assign: ");
        int shiftId = scanner.nextInt();
        scanner.nextLine();
        var shiftToAssign = shiftRepository.get(shiftId);
        if (shiftToAssign == null) {
            System.out.println("Shift not found.");
            return;
        }

        employee.getShifts().add(shiftToAssign);
        employeeRepository.save(employee);

        System.out.println("Shift assigned successfully.");
    }

    private void deleteEmployee() {
        System.out.println("\n--- Delete Employee ---");
        System.out.print("Enter the ID of the employee to delete: ");
        int employeeId = scanner.nextInt();
        scanner.nextLine(); // consume newline

        Employee employee = employeeRepository.get(employeeId);
        if (employee == null) {
            System.out.println("Employee with ID " + employeeId + " not found.");
            return;
        }

        System.out.printf("Are you sure you want to delete %s %s? (y/n): ", employee.getFirstName(), employee.getLastName());
        String confirmation = scanner.nextLine();

        if (confirmation.equalsIgnoreCase("y")) {
            employeeRepository.delete(employee);
            System.out.println("Employee deleted successfully.");
        } else {
            System.out.println("Deletion cancelled.");
        }
    }

    private void printSubMenu() {
        System.out.println("\n- Employee Management -");
        System.out.println("1. View All Employees");
        System.out.println("2. Add Employee");
        System.out.println("3. Update Employee");
        System.out.println("4. Assign Shift to Employee");
        System.out.println("5. Delete Employee");
        System.out.println("0. Back to Main Menu");
        System.out.print("Choose an option: ");
    }
}
