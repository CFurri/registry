package cat.uvic.teknos.registry.app.managers;

import cat.uvic.teknos.registry.models.ModelFactory;
import cat.uvic.teknos.registry.models.Training;
import cat.uvic.teknos.registry.repositories.RepositoryFactory;
import cat.uvic.teknos.registry.repositories.TrainingRepository;
import com.github.freva.asciitable.AsciiTable;
import com.github.freva.asciitable.Column;

import java.util.Arrays;
import java.util.Scanner;

public class TrainingManager {
    private final Scanner scanner;
    private final TrainingRepository trainingRepository;
    private final ModelFactory modelFactory;

    public TrainingManager(Scanner scanner, RepositoryFactory repositoryFactory, ModelFactory modelFactory) {
        this.scanner = scanner;
        this.trainingRepository = repositoryFactory.getTrainingRepository();
        this.modelFactory = modelFactory;
    }

    public void manage() {
        boolean running = true;
        while(running) {
            printSubMenu();
            int choice = scanner.nextInt(); scanner.nextLine();
            switch (choice) {
                case 1 -> viewAllTrainings();
                case 2 -> addTraining();
                case 0 -> running = false;
                default -> System.out.println("Invalid option.");
            }
        }
    }

    private void addTraining() {
        System.out.println("\n--- Add New Training Course ---");
        Training training = modelFactory.newTraining();

        System.out.print("Enter Title: ");
        training.setTitle(scanner.nextLine());

        System.out.print("Enter Description: ");
        training.setDescription(scanner.nextLine());

        System.out.print("Enter Duration (hours): ");
        training.setDurationHours(scanner.nextInt()); scanner.nextLine();

        System.out.print("Is it mandatory? (true/false): ");
        training.setMandatory(scanner.nextBoolean()); scanner.nextLine();

        trainingRepository.save(training);
        System.out.println("Training course added successfully!");
    }

    private void viewAllTrainings() {
        System.out.println("\n--- All Training Courses ---");
        var trainings = trainingRepository.getAll();
        if (trainings.isEmpty()) {
            System.out.println("No trainings found.");
            return;
        }

        // Use AsciiTable to format the output
        System.out.println(AsciiTable.getTable(trainings, Arrays.asList(
                new Column().header("ID").with(t -> String.valueOf(t.getId())),
                new Column().header("Title").with(Training::getTitle),
                new Column().header("Duration (h)").with(t -> String.valueOf(t.getDurationHours())),
                new Column().header("Mandatory").with(t -> String.valueOf(t.isMandatory()).toUpperCase())
        )));
    }

    private void printSubMenu() {
        System.out.println("\n- Training Management -");
        System.out.println("1. View All Training Courses");
        System.out.println("2. Add Training Course");
        System.out.println("0. Back to Main Menu");
        System.out.print("Choose an option: ");
    }
}
