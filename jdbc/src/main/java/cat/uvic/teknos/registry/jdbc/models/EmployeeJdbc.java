package cat.uvic.teknos.registry.jdbc.models;

import cat.uvic.teknos.registry.models.*;
import lombok.*;

import java.sql.Date;
import java.util.Set;

/**
 * JDBC implementation of the Employee interface.
 * The @Data annotation from Lombok generates all the required
 * getters and setters to fulfill the interface contract.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(exclude = {"employeeDetail", "timeLogs", "shifts", "employeeTrainings"}) // Exclude collections
public class EmployeeJdbc implements Employee {
    private int id;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private Date hireDate;

    // Fields use interface types for decoupling
    private EmployeeDetail employeeDetail;
    private Set<TimeLog> timeLogs;
    private Set<Shift> shifts;
    private Set<EmployeeTraining> employeeTrainings;
}