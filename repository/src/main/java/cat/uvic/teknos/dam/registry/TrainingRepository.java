package cat.uvic.teknos.dam.registry;

import cat.uvic.teknos.dam.model.Employee;
import cat.uvic.teknos.dam.model.Training;

import java.time.LocalDate;
import java.util.Set;

public interface TrainingRepository extends Repository<Integer, Training> {
    // Métodos básicos para Training
    Set<Training> findByTitle(String title);
    Set<Training> findByMandatory(boolean mandatory);
    
    // Métodos para gestionar la relación con Employee (N:M)
    Set<Employee> getTrainingEmployees(Integer trainingId);
    void assignEmployeeToTraining(Integer trainingId, Integer employeeId, LocalDate completionDate, boolean passed, double score);
    void updateTrainingEmployee(Integer trainingId, Integer employeeId, LocalDate completionDate, boolean passed, double score);
    void removeEmployeeFromTraining(Integer trainingId, Integer employeeId);
    
    // Métodos personalizados (solo 3 de los más útiles)
    double calculatePassRate(Integer trainingId);
    Set<Employee> findEmployeesWithoutMandatoryTrainings();
    Set<Training> findByDurationLessThan(int hours);
}
