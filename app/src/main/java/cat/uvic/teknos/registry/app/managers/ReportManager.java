package cat.uvic.teknos.registry.app.managers;

import cat.uvic.teknos.registry.models.Employee;
import cat.uvic.teknos.registry.repositories.EmployeeRepository;
import cat.uvic.teknos.registry.repositories.RepositoryFactory;
import com.github.freva.asciitable.AsciiTable;
import com.github.freva.asciitable.Column;
import com.github.freva.asciitable.ColumnData;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class ReportManager {
    private final Scanner scanner;
    private final EmployeeRepository employeeRepository;

    // A simple helper class to hold the flattened data for the report
    private static class LateLogReportEntry {
        private final Employee employee;
        private final cat.uvic.teknos.registry.models.TimeLog timeLog;

        public LateLogReportEntry(Employee employee, cat.uvic.teknos.registry.models.TimeLog timeLog) {
            this.employee = employee;
            this.timeLog = timeLog;
        }
    }

    public ReportManager(Scanner scanner, RepositoryFactory repositoryFactory) {
        this.scanner = scanner;
        this.employeeRepository = repositoryFactory.getEmployeeRepository();
    }

    public void manage() {
        System.out.println("\n--- Reports ---");
        generateLateArrivalsReport();

        System.out.println("\nPress Enter to return to the main menu...");
        scanner.nextLine();
    }

    private void generateLateArrivalsReport() {
        System.out.println("Generating Late Arrivals Report...");

        var allEmployees = employeeRepository.getAll();

        // Collect all late log entries into a list of our helper objects
        List<LateLogReportEntry> lateLogs = new ArrayList<>();
        allEmployees.forEach(employee -> {
            if (employee.getTimeLogs() != null) {
                employee.getTimeLogs().stream()
                    .filter(log -> log.isLate())
                    .forEach(log -> lateLogs.add(new LateLogReportEntry(employee, log)));
            }
        });

        System.out.println("\n--- Late Arrivals Report ---");
        if (lateLogs.isEmpty()) {
            System.out.println("No late arrivals found.");
        } else {
            // Use AsciiTable to format the collected report data
            System.out.println(AsciiTable.getTable(lateLogs, Arrays.asList(
                    new Column().header("Employee ID").with(entry -> String.valueOf(entry.employee.getId())),
                    new Column().header("Name").with(entry -> entry.employee.getFirstName() + " " + entry.employee.getLastName()),
                    new Column().header("Late on Date").with(entry -> String.valueOf(entry.timeLog.getLogDate())),
                    new Column().header("Check-in Time").with(entry -> String.valueOf(entry.timeLog.getCheckInTime()))
            )));
        }
    }
}
