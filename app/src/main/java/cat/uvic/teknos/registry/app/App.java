package cat.uvic.teknos.registry.app;

import cat.uvic.teknos.registry.app.exceptions.DIException;
import cat.uvic.teknos.registry.app.managers.EmployeeManager;
import cat.uvic.teknos.registry.app.managers.ReportManager;
import cat.uvic.teknos.registry.app.managers.ShiftManager;
import cat.uvic.teknos.registry.app.managers.TrainingManager;
import cat.uvic.teknos.registry.models.ModelFactory;
import cat.uvic.teknos.registry.repositories.RepositoryFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.InputMismatchException;
import java.util.Scanner;

public class App {
    private static DIManager diManager;
    private static Scanner scanner;

    public static void main(String[] args) {
        try {
            printBanner();

            // Initialize the Dependency Injection Manager
            diManager = new DIManager();

            // Get singleton factory instances
            RepositoryFactory repositoryFactory = diManager.get("repository_factory");
            ModelFactory modelFactory = diManager.get("model_factory");

            scanner = new Scanner(System.in);

            // Instantiate manager classes with their required dependencies
            var employeeManager = new EmployeeManager(scanner, repositoryFactory, modelFactory);
            var shiftManager = new ShiftManager(scanner, repositoryFactory, modelFactory);
            var trainingManager = new TrainingManager(scanner, repositoryFactory, modelFactory);
            var reportManager = new ReportManager(scanner, repositoryFactory);

            boolean running = true;
            while (running) {
                printMainMenu();
                try {
                    int choice = scanner.nextInt();
                    scanner.nextLine(); // Consume newline

                    switch (choice) {
                        case 1 -> employeeManager.manage();
                        case 2 -> shiftManager.manage();
                        case 3 -> trainingManager.manage();
                        case 4 -> reportManager.manage();
                        case 0 -> running = false;
                        default -> System.out.println("Invalid option, please try again.");
                    }
                } catch (InputMismatchException e) {
                    System.out.println("Invalid input. Please enter a number.");
                    scanner.nextLine(); // Clear the invalid input
                }
            }
        } catch (DIException e) {
            System.err.println("FATAL ERROR: Could not initialize application dependencies: " + e.getMessage());
            e.printStackTrace();
        } finally {
            System.out.println("Exiting application. Goodbye!");
            if (scanner != null) {
                scanner.close();
            }
            if (diManager != null) {
                // This will gracefully close any connection pools
                diManager.close();
            }
        }
    }

    private static void printMainMenu() {
        System.out.println("\n--- MAIN MENU ---");
        System.out.println("1. Employee Management");
        System.out.println("2. Shift Management");
        System.out.println("3. Training Management");
        System.out.println("4. Reports");
        System.out.println("0. Exit");
        System.out.print("Choose an option: ");
    }

    private static void printBanner() {
        try (InputStream is = App.class.getClassLoader().getResourceAsStream("banner.txt")) {
            if (is != null) {
                String banner = new String(is.readAllBytes());
                System.out.println(banner);
            }
        } catch (IOException e) {
            // It's okay if banner fails, the app can continue
        }
    }
}
