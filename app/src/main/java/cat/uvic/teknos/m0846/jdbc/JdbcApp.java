package cat.uvic.teknos.m0846.jdbc;

import cat.uvic.teknos.dam.registry.Employee;
import cat.uvic.teknos.dam.registry.impl.EmployeeImpl;

import java.time.LocalDate;
import java.util.Set;

public class JdbcApp {
    public static void main(String[] args) {
        try {
            executeLogic();
        } catch (Exception e) {
            System.out.println("Error in application: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void executeLogic() {
        EmployeeJdbcRepository repository = new EmployeeJdbcRepository();

        // Crear i guardar empleats
        Employee emp1 = new EmployeeImpl();
        emp1.setFirstName("John");
        emp1.setLastName("Smith");
        emp1.setEmail("john.smith@example.com");
        emp1.setPhoneNumber("123456789");
        emp1.setHireDate(LocalDate.of(2023, 1, 10));
        repository.save(emp1);

        Employee emp2 = new EmployeeImpl();
        emp2.setFirstName("David");
        emp2.setLastName("Gilmour");
        emp2.setEmail("david.gilmour@example.com");
        emp2.setPhoneNumber("987654321");
        emp2.setHireDate(LocalDate.of(2022, 5, 5));
        repository.save(emp2);

        // Mostrar empleats
        Set<Employee> allEmployees = repository.getAll();
        for (Employee employee : allEmployees) {
            System.out.println("ID: " + employee.getId());
            System.out.println("FIRST NAME: " + employee.getFirstName());
            System.out.println("LAST NAME: " + employee.getLastName());
            System.out.println("EMAIL: " + employee.getEmail());
            System.out.println("PHONE: " + employee.getPhoneNumber());
            System.out.println("HIRE DATE: " + employee.getHireDate());
            System.out.println("----------");
        }
    }
}
