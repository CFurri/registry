package cat.uvic.teknos.dam.registry;

import java.util.Set;

public interface EmployeeRepository extends Repository<Integer, Employee> {
    // Aquí pots afegir mètodes específics si vols, per exemple:
    Set<Employee> findByLastName(String lastName);

    void save(cat.uvic.teknos.dam.registry.Employee value);
}