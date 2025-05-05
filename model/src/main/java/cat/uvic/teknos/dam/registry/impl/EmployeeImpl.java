package cat.uvic.teknos.dam.registry.impl;

import cat.uvic.teknos.dam.registry.Employee;

import java.time.LocalDate;
import java.util.Set;

public class EmployeeImpl implements Employee {
    private int id;
    private String firstName; // Canviat de Set<Employee> a String
    private String lastName;  // Canviat de Set<Employee> a String
    private String email;     // Canviat de Set<Employee> a String
    private String phoneNumber;
    private LocalDate hireDate;

    @Override
    public int getId() {
        return id;
    }

    @Override
    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String getFirstName() {  // El tipus de retorn ha de ser String, no Set<Employee>
        return firstName;
    }

    @Override
    public void setFirstName(String firstName) {  // El paràmetre ha de ser String, no Set<Employee>
        this.firstName = firstName;
    }

    @Override
    public String getLastName() {  // El tipus de retorn ha de ser String, no Set<Employee>
        return lastName;
    }

    @Override
    public void setLastName(String lastName) {
        this.lastName = lastName;  // Assignem a lastName, no a employees
    }

    @Override
    public String getEmail() {  // El tipus de retorn ha de ser String, no Set<Employee>
        return email;
    }

    @Override
    public void setEmail(String email) {
        this.email = email;  // Assignem a email, no a employees
    }

    @Override
    public String getPhoneNumber() {
        return phoneNumber;  // Retornem phoneNumber, no una cadena buida
    }

    @Override
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;  // Implementació del setter
    }

    @Override
    public LocalDate getHireDate() {
        return hireDate;  // Retornem hireDate, no null
    }

    @Override
    public void setHireDate(LocalDate hireDate) {
        this.hireDate = hireDate;  // Implementació del setter
    }
}