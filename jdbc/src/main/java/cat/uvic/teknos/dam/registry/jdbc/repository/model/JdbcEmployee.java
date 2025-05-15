package cat.uvic.teknos.dam.registry.jdbc.repository.model;

import cat.uvic.teknos.dam.registry.Employee;

import java.time.LocalDate;

public class JdbcEmployee implements Employee {
    @Override
    public int getId() {
        return 0;
    }

    @Override
    public void setId(int id) {

    }

    @Override
    public String getFirstName() {
        return "";
    }

    @Override
    public void setFirstName(String firstName) {

    }

    @Override
    public String getLastName() {
        return "";
    }

    @Override
    public void setLastName(String lastName) {

    }

    @Override
    public String getEmail() {
        return "";
    }

    @Override
    public void setEmail(String email) {

    }

    @Override
    public String getPhoneNumber() {
        return "";
    }

    @Override
    public void setPhoneNumber(String phoneNumber) {

    }

    @Override
    public LocalDate getHireDate() {
        return null;
    }

    @Override
    public void setHireDate(LocalDate hireDate) {

    }
}
