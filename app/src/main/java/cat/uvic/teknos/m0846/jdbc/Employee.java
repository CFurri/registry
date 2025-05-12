package cat.uvic.teknos.m0846.jdbc;

import cat.uvic.teknos.dam.registry.EmployeeRepository;
import cat.uvic.teknos.dam.registry.impl.EmployeeImpl;

import java.sql.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

public class Employee implements EmployeeRepository {

    private final String jdbcUrl = "jdbc:mysql://localhost:8080/registry";
    private final String jdbcUsername = "root";
    private final String jdbcPassword = "rootpassword"; // Canvia-ho si cal

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(jdbcUrl, jdbcUsername, jdbcPassword);
    }

    @Override
    public void save(cat.uvic.teknos.dam.registry.Employee employee) {
        String sql;

        if (employee.getId() == null || get(employee.getId()) == null) {
            sql = "INSERT INTO EMPLOYEE (first_name, last_name, email, phone_number, hire_date) VALUES (?, ?, ?, ?, ?)";
        } else {
            sql = "UPDATE EMPLOYEE SET first_name = ?, last_name = ?, email = ?, phone_number = ?, hire_date = ? WHERE employee_id = ?";
        }

        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, employee.getFirstName());
            stmt.setString(2, employee.getLastName());
            stmt.setString(3, employee.getEmail());
            stmt.setString(4, employee.getPhoneNumber());
            stmt.setDate(5, Date.valueOf(employee.getHireDate()));

            if (employee.getId() != null && get(employee.getId()) != null) {
                stmt.setInt(6, employee.getId());
            }

            stmt.executeUpdate();

            // Assignar ID generat automàticament (si s'ha fet INSERT)
            if (employee.getId() == null) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        employee.setId(generatedKeys.getInt(1));
                    }
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(cat.uvic.teknos.dam.registry.Employee employee) {
        String sql = "DELETE FROM EMPLOYEE WHERE employee_id = ?";

        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, employee.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public cat.uvic.teknos.dam.registry.Employee get(Integer id) {
        String sql = "SELECT * FROM EMPLOYEE WHERE employee_id = ?";
        cat.uvic.teknos.dam.registry.Employee employee = null;

        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                employee = mapRowToEmployee(rs);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return employee;
    }

    @Override
    public Set<cat.uvic.teknos.dam.registry.Employee> getAll() {
        String sql = "SELECT * FROM EMPLOYEE";
        Set<cat.uvic.teknos.dam.registry.Employee> employees = new HashSet<>();

        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                employees.add(mapRowToEmployee(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return employees;
    }

    private cat.uvic.teknos.dam.registry.Employee mapRowToEmployee(ResultSet rs) throws SQLException {
        cat.uvic.teknos.dam.registry.Employee employee = new EmployeeImpl();
        employee.setId(rs.getInt("employee_id"));
        employee.setFirstName(rs.getString("first_name"));
        employee.setLastName(rs.getString("last_name"));
        employee.setEmail(rs.getString("email"));
        employee.setPhoneNumber(rs.getString("phone_number"));

        Date hireDate = rs.getDate("hire_date");
        if (hireDate != null) {
            employee.setHireDate(hireDate.toLocalDate());
        } else {
            employee.setHireDate(LocalDate.now());
        }

        return employee;
    }
}
