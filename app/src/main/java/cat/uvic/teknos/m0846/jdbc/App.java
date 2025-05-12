package cat.uvic.teknos.m0846.jdbc;

import cat.uvic.teknos.dam.registry.impl.EmployeeImpl;

import java.sql.DriverManager;
import java.time.LocalDate;
import java.util.Set;

public class App {
    public static void main(String[] args) {
        var employee = new Employee();
        employee.setId(1);
        employee.setFirstName("Albert");
        employee.setLastName("Smith");

        var
