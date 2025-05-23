package cat.uvic.teknos.dam.registry.jdbc.repository;

import cat.uvic.teknos.dam.registry.*;
import cat.uvic.teknos.dam.registry.impl.EmployeeImpl;
import cat.uvic.teknos.dam.registry.jdbc.repository.datasource.DataSource;
import cat.uvic.teknos.dam.registry.jdbc.repository.exceptions.CrudException;
import cat.uvic.teknos.dam.registry.jdbc.repository.exceptions.DataSourceException;

import java.sql.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
//FET PER IA
public class JdbcEmployeeRepository implements EmployeeRepository {
    private final DataSource dataSource;

    public JdbcEmployeeRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    // Mètodes de la interfície Repository base

    @Override
    public void save(Employee employee) {
        if (employee.getId() == 0) {
            insertEmployee(employee);
        } else {
            updateEmployee(employee);
        }
    }

    @Override
    public void delete(Employee employee) {
        String sql = "DELETE FROM employees WHERE employee_id = ?";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, employee.getId());
            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected == 0) {
                throw new CrudException("No s'ha pogut eliminar l'employee amb ID: " + employee.getId());
            }

        } catch (SQLException e) {
            throw new CrudException("Error eliminant employee: " + e.getMessage());
        }
    }

    @Override
    public Employee get(Integer id) {
        String sql = "SELECT * FROM employees WHERE employee_id = ?";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToEmployee(rs);
                }
                return null;
            }

        } catch (SQLException e) {
            throw new CrudException("Error trobant employee per ID: " + e.getMessage());
        }
    }

    @Override
    public Set<Employee> getAll() {
        String sql = "SELECT * FROM employees ORDER BY last_name, first_name";
        Set<Employee> employees = new HashSet<>();

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                employees.add(mapResultSetToEmployee(rs));
            }

        } catch (SQLException e) {
            throw new CrudException("Error obtenint tots els employees: " + e.getMessage());
        }

        return employees;
    }

    // Mètodes específics de EmployeeRepository

    @Override
    public Set<Employee> findByLastName(String lastName) {
        String sql = "SELECT * FROM employees WHERE last_name = ? ORDER BY first_name";
        Set<Employee> employees = new HashSet<>();

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, lastName);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    employees.add(mapResultSetToEmployee(rs));
                }
            }

        } catch (SQLException e) {
            throw new CrudException("Error trobant employees per cognom: " + e.getMessage());
        }

        return employees;
    }

    @Override
    public Set<Employee> findByHireDateRange(LocalDate startDate, LocalDate endDate) {
        String sql = "SELECT * FROM employees WHERE hire_date BETWEEN ? AND ? ORDER BY hire_date";
        Set<Employee> employees = new HashSet<>();

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setDate(1, Date.valueOf(startDate));
            stmt.setDate(2, Date.valueOf(endDate));

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    employees.add(mapResultSetToEmployee(rs));
                }
            }

        } catch (SQLException e) {
            throw new CrudException("Error trobant employees per rang de dates: " + e.getMessage());
        }

        return employees;
    }

    @Override
    public double calculateTotalHoursWorked(Integer employeeId, LocalDate startDate, LocalDate endDate) {
        String sql = "SELECT COALESCE(SUM(hours_worked), 0) as total_hours FROM time_logs " +
                "WHERE employee_id = ? AND log_date BETWEEN ? AND ?";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, employeeId);
            stmt.setDate(2, Date.valueOf(startDate));
            stmt.setDate(3, Date.valueOf(endDate));

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getDouble("total_hours");
                }
                return 0.0;
            }

        } catch (SQLException e) {
            throw new CrudException("Error calculant hores treballades: " + e.getMessage());
        }
    }

    // Mètodes per EmployeeDetail (relació 1:1)

    @Override
    public EmployeeDetail getEmployeeDetail(Integer employeeId) {
        String sql = "SELECT * FROM employee_details WHERE employee_id = ?";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, employeeId);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToEmployeeDetail(rs);
                }
                return null;
            }

        } catch (SQLException e) {
            throw new CrudException("Error obtenint detalls d'employee: " + e.getMessage());
        }
    }

    @Override
    public void saveEmployeeDetail(EmployeeDetail employeeDetail) {
        String checkSql = "SELECT COUNT(*) FROM employee_details WHERE employee_id = ?";
        String insertSql = "INSERT INTO employee_details (employee_id, address, salary, department, position) VALUES (?, ?, ?, ?, ?)";
        String updateSql = "UPDATE employee_details SET address = ?, salary = ?, department = ?, position = ? WHERE employee_id = ?";

        try (Connection conn = dataSource.getConnection()) {

            // Comprovar si ja existeix
            boolean exists = false;
            try (PreparedStatement checkStmt = conn.prepareStatement(checkSql)) {
                checkStmt.setInt(1, employeeDetail.getEmployeeId());
                try (ResultSet rs = checkStmt.executeQuery()) {
                    if (rs.next()) {
                        exists = rs.getInt(1) > 0;
                    }
                }
            }

            // INSERT o UPDATE segons correspongui
            if (!exists) {
                try (PreparedStatement stmt = conn.prepareStatement(insertSql)) {
                    stmt.setInt(1, employeeDetail.getEmployeeId());
                    stmt.setString(2, employeeDetail.getAddress());
                    stmt.setDouble(3, employeeDetail.getSalary());
                    stmt.setString(4, employeeDetail.getDepartment());
                    stmt.setString(5, employeeDetail.getPosition());
                    stmt.executeUpdate();
                }
            } else {
                try (PreparedStatement stmt = conn.prepareStatement(updateSql)) {
                    stmt.setString(1, employeeDetail.getAddress());
                    stmt.setDouble(2, employeeDetail.getSalary());
                    stmt.setString(3, employeeDetail.getDepartment());
                    stmt.setString(4, employeeDetail.getPosition());
                    stmt.setInt(5, employeeDetail.getEmployeeId());
                    stmt.executeUpdate();
                }
            }

        } catch (SQLException e) {
            throw new CrudException("Error guardant detalls d'employee: " + e.getMessage());
        }
    }

    // Mètodes per TimeLogs (relació 1:N)

    @Override
    public Set<TimeLog> getEmployeeTimeLogs(Integer employeeId) {
        String sql = "SELECT * FROM time_logs WHERE employee_id = ? ORDER BY log_date DESC";
        Set<TimeLog> timeLogs = new HashSet<>();

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, employeeId);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    timeLogs.add(mapResultSetToTimeLog(rs));
                }
            }

        } catch (SQLException e) {
            throw new CrudException("Error obtenint time logs: " + e.getMessage());
        }

        return timeLogs;
    }

    @Override
    public void saveTimeLog(TimeLog timeLog) {
        if (timeLog.getId() == 0) {
            insertTimeLog(timeLog);
        } else {
            updateTimeLog(timeLog);
        }
    }

    @Override
    public void deleteTimeLog(Integer logId) {
        String sql = "DELETE FROM time_logs WHERE log_id = ?";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, logId);
            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected == 0) {
                throw new CrudException("No s'ha pogut eliminar el time log amb ID: " + logId);
            }

        } catch (SQLException e) {
            throw new CrudException("Error eliminant time log: " + e.getMessage());
        }
    }

    // Mètodes per Shifts (relació N:M)

    @Override
    public void assignShiftToEmployee(Integer employeeId, Integer shiftId) {
        String sql = "INSERT INTO employee_shifts (employee_id, shift_id) VALUES (?, ?)";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, employeeId);
            stmt.setInt(2, shiftId);
            stmt.executeUpdate();

        } catch (SQLException e) {
            if (e.getErrorCode() == 1062) { // Duplicate entry
                throw new CrudException("L'employee ja té assignat aquest torn");
            }
            throw new CrudException("Error assignant torn a employee: " + e.getMessage());
        }
    }

    @Override
    public void removeShiftFromEmployee(Integer employeeId, Integer shiftId) {
        String sql = "DELETE FROM employee_shifts WHERE employee_id = ? AND shift_id = ?";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, employeeId);
            stmt.setInt(2, shiftId);
            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected == 0) {
                throw new CrudException("No s'ha pogut eliminar la relació employee-shift");
            }

        } catch (SQLException e) {
            throw new CrudException("Error eliminant torn d'employee: " + e.getMessage());
        }
    }

    @Override
    public Set<Shift> getEmployeeShifts(Integer employeeId) {
        String sql = "SELECT s.* FROM shifts s " +
                "INNER JOIN employee_shifts es ON s.shift_id = es.shift_id " +
                "WHERE es.employee_id = ? ORDER BY s.start_time";
        Set<Shift> shifts = new HashSet<>();

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, employeeId);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    shifts.add(mapResultSetToShift(rs));
                }
            }

        } catch (SQLException e) {
            throw new CrudException("Error obtenint torns d'employee: " + e.getMessage());
        }

        return shifts;
    }

    // Mètodes per Trainings (relació N:M)

    @Override
    public void assignTrainingToEmployee(Integer employeeId, Integer trainingId, LocalDate completionDate, boolean passed, double score) {
        String sql = "INSERT INTO employee_trainings (employee_id, training_id, completion_date, passed, score) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, employeeId);
            stmt.setInt(2, trainingId);
            stmt.setDate(3, Date.valueOf(completionDate));
            stmt.setBoolean(4, passed);
            stmt.setDouble(5, score);
            stmt.executeUpdate();

        } catch (SQLException e) {
            if (e.getErrorCode() == 1062) { // Duplicate entry
                throw new CrudException("L'employee ja té aquesta formació assignada");
            }
            throw new CrudException("Error assignant formació a employee: " + e.getMessage());
        }
    }

    @Override
    public void updateEmployeeTraining(Integer employeeId, Integer trainingId, LocalDate completionDate, boolean passed, double score) {
        String sql = "UPDATE employee_trainings SET completion_date = ?, passed = ?, score = ? WHERE employee_id = ? AND training_id = ?";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setDate(1, Date.valueOf(completionDate));
            stmt.setBoolean(2, passed);
            stmt.setDouble(3, score);
            stmt.setInt(4, employeeId);
            stmt.setInt(5, trainingId);

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected == 0) {
                throw new CrudException("No s'ha pogut actualitzar la formació de l'employee");
            }

        } catch (SQLException e) {
            throw new CrudException("Error actualitzant formació d'employee: " + e.getMessage());
        }
    }

    @Override
    public void removeTrainingFromEmployee(Integer employeeId, Integer trainingId) {
        String sql = "DELETE FROM employee_trainings WHERE employee_id = ? AND training_id = ?";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, employeeId);
            stmt.setInt(2, trainingId);
            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected == 0) {
                throw new CrudException("No s'ha pogut eliminar la formació de l'employee");
            }

        } catch (SQLException e) {
            throw new CrudException("Error eliminant formació d'employee: " + e.getMessage());
        }
    }

    @Override
    public Set<Training> getEmployeeTrainings(Integer employeeId) {
        String sql = "SELECT t.*, et.completion_date, et.passed, et.score FROM trainings t " +
                "INNER JOIN employee_trainings et ON t.training_id = et.training_id " +
                "WHERE et.employee_id = ? ORDER BY et.completion_date DESC";
        Set<Training> trainings = new HashSet<>();

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, employeeId);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    trainings.add(mapResultSetToTraining(rs));
                }
            }

        } catch (SQLException e) {
            throw new CrudException("Error obtenint formacions d'employee: " + e.getMessage());
        }

        return trainings;
    }

    @Override
    public Set<Training> getPendingMandatoryTrainings(Integer employeeId) {
        String sql = "SELECT t.* FROM trainings t " +
                "WHERE t.mandatory = true " +
                "AND t.training_id NOT IN (" +
                "    SELECT et.training_id FROM employee_trainings et " +
                "    WHERE et.employee_id = ? AND et.passed = true" +
                ") ORDER BY t.title";
        Set<Training> trainings = new HashSet<>();

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, employeeId);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    trainings.add(mapResultSetToTraining(rs));
                }
            }

        } catch (SQLException e) {
            throw new CrudException("Error obtenint formacions pendents: " + e.getMessage());
        }

        return trainings;
    }

    // Mètodes auxiliars per INSERT/UPDATE

    private void insertEmployee(Employee employee) {
        String sql = "INSERT INTO employees (first_name, last_name, email, phone_number, hire_date) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, employee.getFirstName());
            stmt.setString(2, employee.getLastName());
            stmt.setString(3, employee.getEmail());
            stmt.setString(4, employee.getPhoneNumber());
            stmt.setDate(5, Date.valueOf(employee.getHireDate()));

            stmt.executeUpdate();

            // Obtenir l'ID generat automàticament
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    employee.setId(rs.getInt(1));
                }
            }

        } catch (SQLException e) {
            throw new CrudException("Error inserint employee: " + e.getMessage());
        }
    }

    private void updateEmployee(Employee employee) {
        String sql = "UPDATE employees SET first_name = ?, last_name = ?, email = ?, phone_number = ?, hire_date = ? WHERE employee_id = ?";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, employee.getFirstName());
            stmt.setString(2, employee.getLastName());
            stmt.setString(3, employee.getEmail());
            stmt.setString(4, employee.getPhoneNumber());
            stmt.setDate(5, Date.valueOf(employee.getHireDate()));
            stmt.setInt(6, employee.getId());

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected == 0) {
                throw new CrudException("No s'ha pogut actualitzar l'employee amb ID: " + employee.getId());
            }

        } catch (SQLException e) {
            throw new CrudException("Error actualitzant employee: " + e.getMessage());
        }
    }

    private void insertTimeLog(TimeLog timeLog) {
        String sql = "INSERT INTO time_logs (employee_id, log_date, hours_worked, description) VALUES (?, ?, ?, ?)";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, timeLog.getEmployeeId());
            stmt.setDate(2, Date.valueOf(timeLog.getLogDate()));
            stmt.setDouble(3, timeLog.getHoursWorked());
            stmt.setString(4, timeLog.getDescription());

            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    timeLog.setId(rs.getInt(1));
                }
            }

        } catch (SQLException e) {
            throw new CrudException("Error inserint time log: " + e.getMessage());
        }
    }

    private void updateTimeLog(TimeLog timeLog) {
        String sql = "UPDATE time_logs SET employee_id = ?, log_date = ?, hours_worked = ?, description = ? WHERE log_id = ?";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, timeLog.getEmployeeId());
            stmt.setDate(2, Date.valueOf(timeLog.getLogDate()));
            stmt.setDouble(3, timeLog.getHoursWorked());
            stmt.setString(4, timeLog.getDescription());
            stmt.setInt(5, timeLog.getId());

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected == 0) {
                throw new CrudException("No s'ha pogut actualitzar el time log amb ID: " + timeLog.getId());
            }

        } catch (SQLException e) {
            throw new CrudException("Error actualitzant time log: " + e.getMessage());
        }
    }

    // Mètodes de mappage ResultSet -> Object

    private Employee mapResultSetToEmployee(ResultSet rs) throws SQLException {
        Employee employee = new EmployeeImpl();
        employee.setId(rs.getInt("employee_id"));
        employee.setFirstName(rs.getString("first_name"));
        employee.setLastName(rs.getString("last_name"));
        employee.setEmail(rs.getString("email"));
        employee.setPhoneNumber(rs.getString("phone_number"));
        employee.setHireDate(rs.getDate("hire_date").toLocalDate());
        return employee;
    }

    private EmployeeDetail mapResultSetToEmployeeDetail(ResultSet rs) throws SQLException {
        // Assumint que tens una implementació EmployeeDetailImpl
        // Hauràs d'adaptar segons els camps reals de la teva taula
        return null; // Implementar segons el teu model
    }

    private TimeLog mapResultSetToTimeLog(ResultSet rs) throws SQLException {
        // Assumint que tens una implementació TimeLogImpl
        // Hauràs d'adaptar segons els camps reals de la teva taula
        return null; // Implementar segons el teu model
    }

    private Shift mapResultSetToShift(ResultSet rs) throws SQLException {
        // Assumint que tens una implementació ShiftImpl
        // Hauràs d'adaptar segons els camps reals de la teva taula
        return null; // Implementar segons el teu model
    }

    private Training mapResultSetToTraining(ResultSet rs) throws SQLException {
        // Assumint que tens una implementació TrainingImpl
        // Hauràs d'adaptar segons els camps reals de la teva taula
        return null; // Implementar segons el teu model
    }
}
//NO IA

//package cat.uvic.teknos.dam.registry.jdbc.repository;
//
//import cat.uvic.teknos.dam.registry.*;
//import cat.uvic.teknos.dam.registry.jdbc.repository.datasource.DataSource;
//
//import java.time.LocalDate;
//import java.util.Set;
//
//public class JdbcEmployeeRepository implements EmployeeRepository {
//    public JdbcEmployeeRepository(DataSource dataSource) {
//
//    }
//
//    @Override
//    public Set<Employee> findByLastName(String lastName) {
//        return Set.of();
//    }
//
//    @Override
//    public EmployeeDetail getEmployeeDetail(Integer employeeId) {
//        return null;
//    }
//
//    @Override
//    public void saveEmployeeDetail(EmployeeDetail employeeDetail) {
//
//    }
//
//    @Override
//    public Set<TimeLog> getEmployeeTimeLogs(Integer employeeId) {
//        return Set.of();
//    }
//
//    @Override
//    public void saveTimeLog(TimeLog timeLog) {
//
//    }
//
//    @Override
//    public void deleteTimeLog(Integer logId) {
//
//    }
//
//    @Override
//    public void assignShiftToEmployee(Integer employeeId, Integer shiftId) {
//
//    }
//
//    @Override
//    public void removeShiftFromEmployee(Integer employeeId, Integer shiftId) {
//
//    }
//
//    @Override
//    public Set<Shift> getEmployeeShifts(Integer employeeId) {
//        return Set.of();
//    }
//
//    @Override
//    public void assignTrainingToEmployee(Integer employeeId, Integer trainingId, LocalDate completionDate, boolean passed, double score) {
//
//    }
//
//    @Override
//    public void updateEmployeeTraining(Integer employeeId, Integer trainingId, LocalDate completionDate, boolean passed, double score) {
//
//    }
//
//    @Override
//    public void removeTrainingFromEmployee(Integer employeeId, Integer trainingId) {
//
//    }
//
//    @Override
//    public Set<Training> getEmployeeTrainings(Integer employeeId) {
//        return Set.of();
//    }
//
//    @Override
//    public Set<Employee> findByHireDateRange(LocalDate startDate, LocalDate endDate) {
//        return Set.of();
//    }
//
//    @Override
//    public double calculateTotalHoursWorked(Integer employeeId, LocalDate startDate, LocalDate endDate) {
//        return 0;
//    }
//
//    @Override
//    public Set<Training> getPendingMandatoryTrainings(Integer employeeId) {
//        return Set.of();
//    }
//
//    @Override
//    public void save(Employee value) {
//
//    }
//
//    @Override
//    public void delete(Employee value) {
//
//    }
//
//    @Override
//    public Employee get(Integer id) {
//        return null;
//    }
//
//    @Override
//    public Set<Employee> getAll() {
//        return Set.of();
//    }
//}
