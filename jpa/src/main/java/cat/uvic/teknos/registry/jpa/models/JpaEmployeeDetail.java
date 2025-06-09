package cat.uvic.teknos.registry.jpa.models;

import cat.uvic.teknos.registry.models.Employee;
import cat.uvic.teknos.registry.models.EmployeeDetail;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Entity
@Table(name = "EMPLOYEE_DETAIL")
@Getter
@Setter
@EqualsAndHashCode(of = "id")
public class JpaEmployeeDetail implements EmployeeDetail {

    @Id
    @Column(name = "employee_id")
    private int id;

    private String address;

    @Column(name = "national_id")
    private String nationalId;

    @Column(name = "emergency_contact_name")
    private String emergencyContactName;

    @Column(name = "emergency_contact_phone")
    private String emergencyContactPhone;

    @OneToOne(targetEntity = JpaEmployee.class, fetch = FetchType.EAGER)
    @MapsId // This field maps to the primary key and is also the foreign key
    @JoinColumn(name = "employee_id")
    @ToString.Exclude
    private Employee employee;
}