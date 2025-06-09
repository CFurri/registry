package cat.uvic.teknos.registry.jpa.models;

import cat.uvic.teknos.registry.models.EmployeeTraining;
import cat.uvic.teknos.registry.models.Training;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "TRAINING")
@Getter
@Setter
@EqualsAndHashCode(of = "id")
public class JpaTraining implements Training {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "training_id")
    private int id;

    private String title;
    private String description;

    @Column(name = "duration_hours")
    private int durationHours;

    private boolean mandatory;

    @OneToMany(targetEntity = JpaEmployeeTraining.class, mappedBy = "training", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    private Set<EmployeeTraining> employeeTrainings = new HashSet<>();
}