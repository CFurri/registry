package cat.uvic.teknos.dam.registry;

import java.time.LocalDate;

public interface EmployeeTraining {
    // Clave compuesta (employee_id, training_id)
    int getEmployeeId();
    void setEmployeeId(int employeeId);
    
    // Relación con Employee
    Employee getEmployee();
    void setEmployee(Employee employee);

    int getTrainingId();
    void setTrainingId(int trainingId);
    
    // Relación con Training
    Training getTraining();
    void setTraining(Training training);

    // completion_date (DATE → LocalDate)
    LocalDate getCompletionDate();
    void setCompletionDate(LocalDate completionDate);

    // passed (BOOLEAN)
    boolean isPassed();
    void setPassed(boolean passed);

    // score (DECIMAL(5,2))
    double getScore();
    void setScore(double score);
}
