package cat.uvic.teknos.dam.registry;

public interface ModelFactory {
    Employee newEmployee();
    EmployeeDetail newEmployeeDetail();
    EmployeeShift newEmployeeShift();
    EmployeeTraining newEmployeeTraining();
    Shift newShift();
    TimeLog newTimeLog();
    Training newTraining();

}
