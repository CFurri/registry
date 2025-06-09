package cat.uvic.teknos.registry.app.managers;

import cat.uvic.teknos.registry.models.ModelFactory;
import cat.uvic.teknos.registry.models.Shift;
import cat.uvic.teknos.registry.repositories.RepositoryFactory;
import cat.uvic.teknos.registry.repositories.ShiftRepository;
import com.github.freva.asciitable.AsciiTable;
import com.github.freva.asciitable.Column;

import java.sql.Time;
import java.util.Arrays;
import java.util.Scanner;

public class ShiftManager {
    private final Scanner scanner;
    private final ShiftRepository shiftRepository;
    private final ModelFactory modelFactory;

    public ShiftManager(Scanner scanner, RepositoryFactory repositoryFactory, ModelFactory modelFactory) {
        this.scanner = scanner;
        this.shiftRepository = repositoryFactory.getShiftRepository();
        this.modelFactory = modelFactory;
    }

    public void manage() {
        boolean running = true;
        while(running) {
            printSubMenu();
            int choice = scanner.nextInt(); scanner.nextLine();
            switch (choice) {
                case 1 -> viewAllShifts();
                case 2 -> addShift();
                case 0 -> running = false;
                default -> System.out.println("Invalid option.");
            }
        }
    }

    private void addShift() {
        System.out.println("\n--- Add New Shift ---");
        Shift shift = modelFactory.newShift();

        System.out.print("Enter Shift Name: ");
        shift.setName(scanner.nextLine());

        System.out.print("Enter Start Time (HH:MM:SS): ");
        shift.setStartTime(Time.valueOf(scanner.nextLine()));

        System.out.print("Enter End Time (HH:MM:SS): ");
        shift.setEndTime(Time.valueOf(scanner.nextLine()));

        System.out.print("Enter Location: ");
        shift.setLocation(scanner.nextLine());

        shiftRepository.save(shift);
        System.out.println("Shift added successfully!");
    }

    private void viewAllShifts() {
        System.out.println("\n--- All Shifts ---");
        var shifts = shiftRepository.getAll();
        if (shifts.isEmpty()) {
            System.out.println("No shifts found.");
            return;
        }

        // Use AsciiTable to format the output
        System.out.println(AsciiTable.getTable(shifts, Arrays.asList(
                new Column().header("ID").with(s -> String.valueOf(s.getId())),
                new Column().header("Name").with(Shift::getName),
                new Column().header("Location").with(Shift::getLocation),
                new Column().header("Start Time").with(s -> String.valueOf(s.getStartTime())),
                new Column().header("End Time").with(s -> String.valueOf(s.getEndTime()))
        )));
    }

    private void printSubMenu() {
        System.out.println("\n- Shift Management -");
        System.out.println("1. View All Shifts");
        System.out.println("2. Add Shift");
        System.out.println("0. Back to Main Menu");
        System.out.print("Choose an option: ");
    }
}
