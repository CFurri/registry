package cat.uvic.teknos.registry.jdbc.models;

import cat.uvic.teknos.registry.models.EmployeeTraining; // Corrected import path
import cat.uvic.teknos.registry.models.Training; // Corrected import path
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TrainingJdbc implements Training {
    private int id;
    private String title;
    private String description;
    private int durationHours;
    private boolean mandatory;

    private Set<EmployeeTraining> employeeTrainings;
}