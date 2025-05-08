package cat.uvic.teknos.dam.registry;

public interface EmployeeShiftRepository extends Repository<String, EmployeeShift> {
    // També clau composta "employeeId-shiftId"
}

