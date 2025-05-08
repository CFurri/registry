package cat.uvic.teknos.dam.registry;

public interface EmployeeTrainingRepository extends Repository<String, EmployeeTraining> {
    // Clau composta → pots codificar-la com un String "employeeId-trainingId" o fer servir una classe auxiliar si vols més netedat
}
