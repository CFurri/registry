package cat.uvic.teknos.m0846.jdbc;


import cat.uvic.teknos.dam.registry.RepositoryFactory;
import cat.uvic.teknos.dam.registry.jdbc.repository.JdbcRepositoryFactory;

import java.util.Objects;
import java.util.Scanner;

public class App {
    public static void main(String[] args) {
        //with factory
        System.out.println("Welcome to the Registry Back-Office. Type:");
        System.out.println("Type 1 to manage employees:");
        var repositoryFactory = new JdbcRepositoryFactory();
        var modelFactory = newModelFactoryImpl();
        var scanner = new Scanner(System.in);

        var command = "";
        while((!Objects.equals(command = scanner.nextLine(), "exit")){
            switch (command) {
                case "employee":
                    manageEmployee(repositoryFactory);
            }
        }

    }private static void manageEmployee(RepositoryFactory repositoryFactory, ModelFactory modelFactory){
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
}


