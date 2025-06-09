package cat.uvic.teknos.registry.jdbc.models;

import cat.uvic.teknos.registry.models.Employee;
import cat.uvic.teknos.registry.models.EmployeeDetail;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeDetailJdbc implements EmployeeDetail {
    private int id;
    private String address;
    private String nationalId;
    private String emergencyContactName;
    private String emergencyContactPhone;

    private Employee employee;
}