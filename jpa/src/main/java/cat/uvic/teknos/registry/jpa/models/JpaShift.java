package cat.uvic.teknos.registry.jpa.models;

import cat.uvic.teknos.registry.models.Employee;
import cat.uvic.teknos.registry.models.Shift;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.sql.Time;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "SHIFT")
@Getter
@Setter
@EqualsAndHashCode(of = "id")
public class JpaShift implements Shift {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "shift_id")
    private int id;

    private String name;

    @Column(name = "start_time")
    private Time startTime;

    @Column(name = "end_time")
    private Time endTime;

    private String location;

    @ManyToMany(targetEntity = JpaEmployee.class, mappedBy = "shifts")
    @ToString.Exclude
    private Set<Employee> employees = new HashSet<>();
}