package cat.uvic.teknos.registry.jdbc.models;

import cat.uvic.teknos.registry.models.Employee; // Corrected import path
import cat.uvic.teknos.registry.models.EmployeeTraining; // Corrected import path
import cat.uvic.teknos.registry.models.Training; // Corrected import path
import lombok.*;

import java.math.BigDecimal;
import java.sql.Date;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
// Exclude BOTH back-references to break all possible loops
@EqualsAndHashCode(exclude = {"employee", "training"})
public class EmployeeTrainingJdbc implements EmployeeTraining {
    private Employee employee;
    private Training training;

    private Date completionDate;
    private boolean passed;
    private BigDecimal score;
}