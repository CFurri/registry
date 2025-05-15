package cat.uvic.teknos.dam.registry.repository.jdbc;

import cat.uvic.teknos.dam.registry.EmployeeRepository;

import java.util.Set;

public class JdbcEmployeeRepository implements EmployeeRepository {
    @Override
    public Set<cat.uvic.teknos.dam.registry.Employee> findByLastName(String lastName) {
        return Set.of();
    }

    @Override
    public void save(cat.uvic.teknos.dam.registry.Employee value) {

    }

    @Override
    public void delete(cat.uvic.teknos.dam.registry.Employee value) {

    }

    @Override
    public cat.uvic.teknos.dam.registry.Employee get(Integer id) {
        return null;
    }

    @Override
    public Set<cat.uvic.teknos.dam.registry.Employee> getAll() {
        return Set.of();
    }
}
