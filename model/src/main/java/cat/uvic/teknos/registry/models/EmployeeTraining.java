package cat.uvic.teknos.registry.models;

import java.sql.Date;
import java.math.BigDecimal;

public interface EmployeeTraining {

    // Relation N:1 to Employee
    Employee getEmployee();
    void setEmployee(Employee employee);

    // Relation N:1 to Training
    Training getTraining();
    void setTraining(Training training);

    // completion_date
    Date getCompletionDate();
    void setCompletionDate(Date completionDate);

    // passed
    boolean isPassed();
    void setPassed(boolean passed);

    // score (DECIMAL(5,2) -> BigDecimal)
    BigDecimal getScore();
    void setScore(BigDecimal score);
}