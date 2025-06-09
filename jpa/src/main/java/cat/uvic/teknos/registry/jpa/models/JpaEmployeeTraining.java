package cat.uvic.teknos.registry.jpa.models;

import cat.uvic.teknos.registry.models.Employee;
import cat.uvic.teknos.registry.models.EmployeeTraining;
import cat.uvic.teknos.registry.models.Training;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.math.BigDecimal;
import java.sql.Date;

@Entity
@Table(name = "EMPLOYEE_TRAINING")
@IdClass(JpaEmployeeTrainingId.class) // Specifies the composite key class
@Getter
@Setter
@EqualsAndHashCode(of = {"employee", "training"})
public class JpaEmployeeTraining implements EmployeeTraining {

    @Id
    @ManyToOne(targetEntity = JpaEmployee.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id")
    @ToString.Exclude
    private Employee employee;

    @Id
    @ManyToOne(targetEntity = JpaTraining.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "training_id")
    @ToString.Exclude
    private Training training;

    @Column(name = "completion_date")
    private Date completionDate;

    private boolean passed;
    private BigDecimal score;
}