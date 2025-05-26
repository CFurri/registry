package cat.uvic.teknos.dam.registry;


import cat.uvic.teknos.dam.model.*;

import java.time.LocalDate;
import java.util.Set;


public interface EmployeeRepository extends Repository<Integer, Employee> {
    // Métodos básicos para Employee
    Set<Employee> findByLastName(String lastName);
    
    // Métodos para gestionar EmployeeDetail (parte del aggregate)
    EmployeeDetail getEmployeeDetail(Integer employeeId);
    void saveEmployeeDetail(EmployeeDetail employeeDetail);
    
    // Métodos para gestionar TimeLogs (parte del aggregate)
    Set<TimeLog> getEmployeeTimeLogs(Integer employeeId);
    void saveTimeLog(TimeLog timeLog);
    void deleteTimeLog(Integer logId);
    
    // Métodos para gestionar la relación con Shift (N:M)
    void assignShiftToEmployee(Integer employeeId, Integer shiftId);
    void removeShiftFromEmployee(Integer employeeId, Integer shiftId);
    Set<Shift> getEmployeeShifts(Integer employeeId);
    
    // Métodos para gestionar la relación con Training (N:M)
    void assignTrainingToEmployee(Integer employeeId, Integer trainingId, LocalDate completionDate, boolean passed, double score);
    void updateEmployeeTraining(Integer employeeId, Integer trainingId, LocalDate completionDate, boolean passed, double score);
    void removeTrainingFromEmployee(Integer employeeId, Integer trainingId);
    Set<Training> getEmployeeTrainings(Integer employeeId);
    
    // Métodos personalizados (solo 3 de los más útiles)
    Set<Employee> findByHireDateRange(LocalDate startDate, LocalDate endDate);
    double calculateTotalHoursWorked(Integer employeeId, LocalDate startDate, LocalDate endDate);
    Set<Training> getPendingMandatoryTrainings(Integer employeeId);
}