package cat.uvic.teknos.dam.registry.jdbc.repository;

import cat.uvic.teknos.dam.model.*;
import cat.uvic.teknos.dam.registry.*;

import java.time.LocalDate;
import java.util.Set;

public class JdbcEmployee implements EmployeeRepository {

    @Override
    public Set<Employee> findByLastName(String lastName) {
        return Set.of();
    }

    @Override
    public EmployeeDetail getEmployeeDetail(Integer employeeId) {
        return null;
    }

    @Override
    public void saveEmployeeDetail(EmployeeDetail employeeDetail) {

    }

    @Override
    public Set<TimeLog> getEmployeeTimeLogs(Integer employeeId) {
        return Set.of();
    }

    @Override
    public void saveTimeLog(TimeLog timeLog) {

    }

    @Override
    public void deleteTimeLog(Integer logId) {

    }

    @Override
    public void assignShiftToEmployee(Integer employeeId, Integer shiftId) {

    }

    @Override
    public void removeShiftFromEmployee(Integer employeeId, Integer shiftId) {

    }

    @Override
    public Set<Shift> getEmployeeShifts(Integer employeeId) {
        return Set.of();
    }

    @Override
    public void assignTrainingToEmployee(Integer employeeId, Integer trainingId, LocalDate completionDate, boolean passed, double score) {

    }

    @Override
    public void updateEmployeeTraining(Integer employeeId, Integer trainingId, LocalDate completionDate, boolean passed, double score) {

    }

    @Override
    public void removeTrainingFromEmployee(Integer employeeId, Integer trainingId) {

    }

    @Override
    public Set<Training> getEmployeeTrainings(Integer employeeId) {
        return Set.of();
    }

    @Override
    public Set<Employee> findByHireDateRange(LocalDate startDate, LocalDate endDate) {
        return Set.of();
    }

    @Override
    public double calculateTotalHoursWorked(Integer employeeId, LocalDate startDate, LocalDate endDate) {
        return 0;
    }

    @Override
    public Set<Training> getPendingMandatoryTrainings(Integer employeeId) {
        return Set.of();
    }

    @Override
    public void save(Employee value) {

    }

    @Override
    public void delete(Employee value) {

    }

    @Override
    public Employee get(Integer id) {
        return null;
    }

    @Override
    public Set<Employee> getAll() {
        return Set.of();
    }
}

