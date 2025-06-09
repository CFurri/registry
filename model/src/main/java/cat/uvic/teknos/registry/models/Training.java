package cat.uvic.teknos.registry.models;

import java.util.Set;

public interface Training {
    // training_id (PK auto-incremental)
    int getId();
    void setId(int id);

    // title
    String getTitle();
    void setTitle(String title);

    // description
    String getDescription();
    void setDescription(String description);

    // duration_hours
    int getDurationHours();
    void setDurationHours(int durationHours);

    // mandatory
    boolean isMandatory();
    void setMandatory(boolean mandatory);

    // Relation N:M with Employee through EmployeeTraining
    Set<EmployeeTraining> getEmployeeTrainings();
    void setEmployeeTrainings(Set<EmployeeTraining> employeeTrainings);
}