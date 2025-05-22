package cat.uvic.teknos.m0846.jdbc;

import cat.uvic.teknos.dam.registry.RepositoryFactory;

import java.util.Objects;
import java.util.Scanner;

public class EmployeeManager {
    private ModelFactory modelFactory;
    private RepositoryFactory repositoryFactory;
    private Scanner scanner;

    public InstrumentManager(ModelFactory modelFactory, RepositoryFactory)
    System.out.println("Employee manager, type: ");
    System.out.println("1 - to show the list of all employees: ");
    System.out.println("2 - to show an employee: ");
    System.out.println("3 - to save an employee: ");
    var command = "";
    Scanner scanner = null;
    while((!Objects.equals(command = scanner.nextLine(), "exit")){
        switch (command) {
            case "3":
                System.out.println("Description: ");

                var employee = modelFactory.newEmployee();

                //TODO:validate input
                var description = scanner.nextLine();
                employee.setDescription(description);

                var repository = repositoryFactory.getEmployeeRepository();
                repository.save(employee);
                break;

            case "1":
                var employees = repository.getAll();
                String[] headers = {'ID', "Description"};
                System.out.println(AsciiTable.gettable(employees, Arrays.asList(){
                new Column().with(i -> Integer.toString(i.getId())),
                    )));

            }
        }
    }
}

