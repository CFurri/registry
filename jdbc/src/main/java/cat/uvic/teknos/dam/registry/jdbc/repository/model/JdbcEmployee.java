package cat.uvic.teknos.dam.registry.jdbc.repository.model;

import cat.uvic.teknos.dam.registry.*;

import java.time.LocalDate;
import java.util.Set;

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

    @Override
    public EmployeeDetail getEmployeeDetail() {
        return null;
    }

    @Override
    public void setEmployeeDetail(EmployeeDetail employeeDetail) {

    }

    @Override
    public Set<TimeLog> getTimeLogs() {
        return Set.of();
    }

    @Override
    public void setTimeLogs(Set<TimeLog> timeLogs) {

    }

    @Override
    public void addTimeLog(TimeLog timeLog) {

    }

    @Override
    public void removeTimeLog(TimeLog timeLog) {

    }

    @Override
    public Set<Shift> getShifts() {
        return Set.of();
    }

    @Override
    public void setShifts(Set<Shift> shifts) {

    }

    @Override
    public void addShift(Shift shift) {

    }

    @Override
    public void removeShift(Shift shift) {

    }

    @Override
    public Set<Training> getTrainings() {
        return Set.of();
    }

    @Override
    public void setTrainings(Set<Training> trainings) {

    }

    @Override
    public void addTraining(Training training) {

    }

    @Override
    public void removeTraining(Training training) {

    }
}
