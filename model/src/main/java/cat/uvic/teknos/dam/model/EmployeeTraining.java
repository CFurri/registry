package cat.uvic.teknos.dam.model;

import java.time.LocalDate;

public interface EmployeeTraining {
    // Clave compuesta (employee_id, training_id)
    int getPersonId();
    void setPersonId(int personId);
    
    // Relación con Employee
    Employee getEmployee();
    void setEmployee(Employee employee);

    int getCourseId();
    void setCourseId(int courseId);
    
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
