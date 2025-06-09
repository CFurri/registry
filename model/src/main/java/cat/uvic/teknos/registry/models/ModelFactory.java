package cat.uvic.teknos.registry.models;

public interface ModelFactory {
    Employee newEmployee();
    EmployeeDetail newEmployeeDetail();
    EmployeeTraining newEmployeeTraining();
    Shift newShift();
    TimeLog newTimeLog();
    Training newTraining();
}
