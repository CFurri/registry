package cat.uvic.teknos.dam.registry;

import java.time.LocalDate;

public interface EmployeeTraining {
    // Clau composta (employee_id, training_id)
    int getEmployeeId();
    void setEmployeeId(int employeeId);

    int getTrainingId();
    void setTrainingId(int trainingId);

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

