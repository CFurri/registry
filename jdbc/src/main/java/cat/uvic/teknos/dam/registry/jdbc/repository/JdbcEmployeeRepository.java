package cat.uvic.teknos.dam.registry.jdbc.repository;

import cat.uvic.teknos.dam.registry.EmployeeRepository;
import cat.uvic.teknos.dam.registry.jdbc.repository.datasource.DataSource;

import java.util.Set;

public class JdbcEmployeeRepository implements EmployeeRepository {
    private final DataSource dataSource;

    public JdbcEmployeeRepository(DataSource dataSource) {
       this.dataSource = dataSource;
    }

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
