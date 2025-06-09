package cat.uvic.teknos.registry.jpa.models;

import cat.uvic.teknos.registry.models.*;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.sql.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "EMPLOYEE")
@Getter
@Setter
@EqualsAndHashCode(of = "id") // Only use the ID for equals and hashCode
public class JpaEmployee implements Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "employee_id")
    private int id;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    private String email;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "hire_date")
    private Date hireDate;

    @OneToOne(targetEntity = JpaEmployeeDetail.class, mappedBy = "employee", cascade = CascadeType.ALL)
    @ToString.Exclude
    private EmployeeDetail employeeDetail;

    @OneToMany(targetEntity = JpaTimeLog.class, mappedBy = "employee", cascade = CascadeType.ALL, orphanRemoval = true,fetch = FetchType.EAGER)
    @ToString.Exclude
    private Set<TimeLog> timeLogs = new HashSet<>();

    @ManyToMany(targetEntity = JpaShift.class, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
        name = "EMPLOYEE_SHIFT",
        joinColumns = @JoinColumn(name = "employee_id"),
        inverseJoinColumns = @JoinColumn(name = "shift_id")
    )
    @ToString.Exclude
    private Set<Shift> shifts = new HashSet<>();

    @OneToMany(targetEntity = JpaEmployeeTraining.class, mappedBy = "employee", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    private Set<EmployeeTraining> employeeTrainings = new HashSet<>();
}