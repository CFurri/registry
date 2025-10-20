package cat.uvic.teknos.registry.server;

import cat.uvic.teknos.registry.app.DIManager;
import cat.uvic.teknos.registry.models.*;
import cat.uvic.teknos.registry.repositories.EmployeeRepository;
import cat.uvic.teknos.registry.repositories.RepositoryFactory;
import cat.uvic.teknos.registry.server.controllers.EmployeeController;
import cat.uvic.teknos.registry.server.dto.EmployeeDTO;
import cat.uvic.teknos.registry.server.exceptions.BadRequestException;
import cat.uvic.teknos.registry.server.exceptions.NotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import rawhttp.core.RawHttp;
import rawhttp.core.RawHttpRequest;
import rawhttp.core.RawHttpResponse;

import java.io.IOException;
import java.sql.Date;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class EmployeeControllerTest {

    private EmployeeController controller;
    private EmployeeRepository mockRepo;
    private ModelFactory mockModelFactory;
    private RawHttp http;

    @BeforeEach
    public void setUp() throws Exception {
        mockRepo = mock(EmployeeRepository.class);
        mockModelFactory = mock(ModelFactory.class);

        RepositoryFactory mockFactory = mock(RepositoryFactory.class);
        when(mockFactory.getEmployeeRepository()).thenReturn(mockRepo);

        DIManager mockDI = mock(DIManager.class);
        when(mockDI.get("repository_factory")).thenReturn(mockFactory);
        when(mockDI.get("model_factory")).thenReturn(mockModelFactory);

        controller = new EmployeeController(mockDI);
        http = new RawHttp();
    }

    // -------------------- GET /api/employees --------------------
    @Test
    public void testGetAllEmployeesReturns200() throws IOException {
        Employee e1 = new Employee() {
            @Override
            public int getId() {
                return 0;
            }

            @Override
            public void setId(int id) {

            }

            @Override
            public String getFirstName() {
                return "";
            }

            @Override
            public void setFirstName(String firstName) {

            }

            @Override
            public String getLastName() {
                return "";
            }

            @Override
            public void setLastName(String lastName) {

            }

            @Override
            public String getEmail() {
                return "";
            }

            @Override
            public void setEmail(String email) {

            }

            @Override
            public String getPhoneNumber() {
                return "";
            }

            @Override
            public void setPhoneNumber(String phoneNumber) {

            }

            @Override
            public Date getHireDate() {
                return null;
            }

            @Override
            public void setHireDate(Date hireDate) {

            }

            @Override
            public EmployeeDetail getEmployeeDetail() {
                return null;
            }

            @Override
            public void setEmployeeDetail(EmployeeDetail employeeDetail) {

            }

            @Override
            public Set<TimeLog> getTimeLogs() {
                return Set.of();
            }

            @Override
            public void setTimeLogs(Set<TimeLog> timeLogs) {

            }

            @Override
            public Set<Shift> getShifts() {
                return Set.of();
            }

            @Override
            public void setShifts(Set<Shift> shifts) {

            }

            @Override
            public Set<EmployeeTraining> getEmployeeTrainings() {
                return Set.of();
            }

            @Override
            public void setEmployeeTrainings(Set<EmployeeTraining> trainings) {

            }
        }; e1.setId(1); e1.setFirstName("John");
        Employee e2 = new Employee() {
            @Override
            public int getId() {
                return 0;
            }

            @Override
            public void setId(int id) {

            }

            @Override
            public String getFirstName() {
                return "";
            }

            @Override
            public void setFirstName(String firstName) {

            }

            @Override
            public String getLastName() {
                return "";
            }

            @Override
            public void setLastName(String lastName) {

            }

            @Override
            public String getEmail() {
                return "";
            }

            @Override
            public void setEmail(String email) {

            }

            @Override
            public String getPhoneNumber() {
                return "";
            }

            @Override
            public void setPhoneNumber(String phoneNumber) {

            }

            @Override
            public Date getHireDate() {
                return null;
            }

            @Override
            public void setHireDate(Date hireDate) {

            }

            @Override
            public EmployeeDetail getEmployeeDetail() {
                return null;
            }

            @Override
            public void setEmployeeDetail(EmployeeDetail employeeDetail) {

            }

            @Override
            public Set<TimeLog> getTimeLogs() {
                return Set.of();
            }

            @Override
            public void setTimeLogs(Set<TimeLog> timeLogs) {

            }

            @Override
            public Set<Shift> getShifts() {
                return Set.of();
            }

            @Override
            public void setShifts(Set<Shift> shifts) {

            }

            @Override
            public Set<EmployeeTraining> getEmployeeTrainings() {
                return Set.of();
            }

            @Override
            public void setEmployeeTrainings(Set<EmployeeTraining> trainings) {

            }
        }; e2.setId(2); e2.setFirstName("Jane");
        when(mockRepo.getAll()).thenReturn(Set.of(e1, e2));

        RawHttpRequest request = http.parseRequest(
                "GET /api/employees HTTP/1.1\r\n" +
                        "Host: localhost\r\n" +
                        "\r\n"
        ).eagerly();

        Optional<RawHttpResponse<?>> responseOpt = controller.route(request);
        assertTrue(responseOpt.isPresent());
        RawHttpResponse<?> response = responseOpt.get();
        assertEquals(200, response.getStatusCode());
    }

    // -------------------- GET /api/employees/{id} --------------------
    @Test
    public void testGetEmployeeByIdReturns200() throws IOException {
        Employee e = new Employee() {
            @Override
            public int getId() {
                return 0;
            }

            @Override
            public void setId(int id) {

            }

            @Override
            public String getFirstName() {
                return "";
            }

            @Override
            public void setFirstName(String firstName) {

            }

            @Override
            public String getLastName() {
                return "";
            }

            @Override
            public void setLastName(String lastName) {

            }

            @Override
            public String getEmail() {
                return "";
            }

            @Override
            public void setEmail(String email) {

            }

            @Override
            public String getPhoneNumber() {
                return "";
            }

            @Override
            public void setPhoneNumber(String phoneNumber) {

            }

            @Override
            public Date getHireDate() {
                return null;
            }

            @Override
            public void setHireDate(Date hireDate) {

            }

            @Override
            public EmployeeDetail getEmployeeDetail() {
                return null;
            }

            @Override
            public void setEmployeeDetail(EmployeeDetail employeeDetail) {

            }

            @Override
            public Set<TimeLog> getTimeLogs() {
                return Set.of();
            }

            @Override
            public void setTimeLogs(Set<TimeLog> timeLogs) {

            }

            @Override
            public Set<Shift> getShifts() {
                return Set.of();
            }

            @Override
            public void setShifts(Set<Shift> shifts) {

            }

            @Override
            public Set<EmployeeTraining> getEmployeeTrainings() {
                return Set.of();
            }

            @Override
            public void setEmployeeTrainings(Set<EmployeeTraining> trainings) {

            }
        }; e.setId(1); e.setFirstName("John");
        when(mockRepo.get(1)).thenReturn(e);

        RawHttpRequest request = http.parseRequest(
                "GET /api/employees/1 HTTP/1.1\r\n" +
                        "Host: localhost\r\n" +
                        "\r\n"
        ).eagerly();

        Optional<RawHttpResponse<?>> responseOpt = controller.route(request);
        assertTrue(responseOpt.isPresent());
        RawHttpResponse<?> response = responseOpt.get();
        assertEquals(200, response.getStatusCode());
    }

    @Test
    public void testGetEmployeeByIdReturns404() throws IOException {
        when(mockRepo.get(1)).thenReturn(null);

        RawHttpRequest request = http.parseRequest(
                "GET /api/employees/1 HTTP/1.1\r\n" +
                        "Host: localhost\r\n" +
                        "\r\n"
        ).eagerly();

        NotFoundException exception = assertThrows(NotFoundException.class, () -> {
            controller.route(request);
        });
        assertTrue(exception.getMessage().contains("not found"));
    }

    // -------------------- POST /api/employees --------------------
    @Test
    public void testPostEmployeeReturns201() throws Exception {
        EmployeeDTO dto = new EmployeeDTO();
        dto.setFirstName("Alice");

        Employee e = new Employee() {
            @Override
            public int getId() {
                return 0;
            }

            @Override
            public void setId(int id) {

            }

            @Override
            public String getFirstName() {
                return "";
            }

            @Override
            public void setFirstName(String firstName) {

            }

            @Override
            public String getLastName() {
                return "";
            }

            @Override
            public void setLastName(String lastName) {

            }

            @Override
            public String getEmail() {
                return "";
            }

            @Override
            public void setEmail(String email) {

            }

            @Override
            public String getPhoneNumber() {
                return "";
            }

            @Override
            public void setPhoneNumber(String phoneNumber) {

            }

            @Override
            public Date getHireDate() {
                return null;
            }

            @Override
            public void setHireDate(Date hireDate) {

            }

            @Override
            public EmployeeDetail getEmployeeDetail() {
                return null;
            }

            @Override
            public void setEmployeeDetail(EmployeeDetail employeeDetail) {

            }

            @Override
            public Set<TimeLog> getTimeLogs() {
                return Set.of();
            }

            @Override
            public void setTimeLogs(Set<TimeLog> timeLogs) {

            }

            @Override
            public Set<Shift> getShifts() {
                return Set.of();
            }

            @Override
            public void setShifts(Set<Shift> shifts) {

            }

            @Override
            public Set<EmployeeTraining> getEmployeeTrainings() {
                return Set.of();
            }

            @Override
            public void setEmployeeTrainings(Set<EmployeeTraining> trainings) {

            }
        };
        when(mockModelFactory.newEmployee()).thenReturn(e);

        String jsonPayload = "{\"firstName\":\"Alice\"}";
        RawHttpRequest request = http.parseRequest(
                "POST /api/employees HTTP/1.1\r\n" +
                        "Host: localhost\r\n" +
                        "Content-Type: application/json\r\n" +
                        "Content-Length: " + jsonPayload.length() + "\r\n" +
                        "\r\n" +
                        jsonPayload
        ).eagerly();

        Optional<RawHttpResponse<?>> responseOpt = controller.route(request);
        assertTrue(responseOpt.isPresent());
        RawHttpResponse<?> response = responseOpt.get();
        assertEquals(201, response.getStatusCode());
        verify(mockRepo).save(any(Employee.class));
    }

    @Test
    public void testPostEmployeeReturns400ForEmptyBody() throws IOException {
        RawHttpRequest request = http.parseRequest(
                "POST /api/employees HTTP/1.1\r\n" +
                        "Host: localhost\r\n" +
                        "\r\n"
        ).eagerly();

        BadRequestException exception = assertThrows(BadRequestException.class, () -> {
            controller.route(request);
        });
        assertTrue(exception.getMessage().contains("Request body is required"));
    }

    // -------------------- PUT /api/employees/{id} --------------------
    @Test
    public void testPutEmployeeReturns200() throws Exception {
        Employee e = new Employee() {
            @Override
            public int getId() {
                return 0;
            }

            @Override
            public void setId(int id) {

            }

            @Override
            public String getFirstName() {
                return "";
            }

            @Override
            public void setFirstName(String firstName) {

            }

            @Override
            public String getLastName() {
                return "";
            }

            @Override
            public void setLastName(String lastName) {

            }

            @Override
            public String getEmail() {
                return "";
            }

            @Override
            public void setEmail(String email) {

            }

            @Override
            public String getPhoneNumber() {
                return "";
            }

            @Override
            public void setPhoneNumber(String phoneNumber) {

            }

            @Override
            public Date getHireDate() {
                return null;
            }

            @Override
            public void setHireDate(Date hireDate) {

            }

            @Override
            public EmployeeDetail getEmployeeDetail() {
                return null;
            }

            @Override
            public void setEmployeeDetail(EmployeeDetail employeeDetail) {

            }

            @Override
            public Set<TimeLog> getTimeLogs() {
                return Set.of();
            }

            @Override
            public void setTimeLogs(Set<TimeLog> timeLogs) {

            }

            @Override
            public Set<Shift> getShifts() {
                return Set.of();
            }

            @Override
            public void setShifts(Set<Shift> shifts) {

            }

            @Override
            public Set<EmployeeTraining> getEmployeeTrainings() {
                return Set.of();
            }

            @Override
            public void setEmployeeTrainings(Set<EmployeeTraining> trainings) {

            }
        }; e.setId(1); e.setFirstName("Old");
        when(mockRepo.get(1)).thenReturn(e);

        String jsonPayload = "{\"firstName\":\"Updated\"}";
        RawHttpRequest request = http.parseRequest(
                "PUT /api/employees/1 HTTP/1.1\r\n" +
                        "Host: localhost\r\n" +
                        "Content-Type: application/json\r\n" +
                        "Content-Length: " + jsonPayload.length() + "\r\n" +
                        "\r\n" +
                        jsonPayload
        ).eagerly();

        Optional<RawHttpResponse<?>> responseOpt = controller.route(request);
        assertTrue(responseOpt.isPresent());
        RawHttpResponse<?> response = responseOpt.get();
        assertEquals(200, response.getStatusCode());
        verify(mockRepo).save(any(Employee.class));
    }

    @Test
    public void testPutEmployeeReturns404() throws IOException {
        when(mockRepo.get(1)).thenReturn(null);

        String jsonPayload = "{\"firstName\":\"Updated\"}";
        RawHttpRequest request = http.parseRequest(
                "PUT /api/employees/1 HTTP/1.1\r\n" +
                        "Host: localhost\r\n" +
                        "Content-Type: application/json\r\n" +
                        "Content-Length: " + jsonPayload.length() + "\r\n" +
                        "\r\n" +
                        jsonPayload
        ).eagerly();

        NotFoundException exception = assertThrows(NotFoundException.class, () -> {
            controller.route(request);
        });
        assertTrue(exception.getMessage().contains("not found"));
    }

    // -------------------- DELETE /api/employees/{id} --------------------
    @Test
    public void testDeleteEmployeeReturns204() throws IOException {
        Employee e = new Employee() {
            @Override
            public int getId() {
                return 0;
            }

            @Override
            public void setId(int id) {

            }

            @Override
            public String getFirstName() {
                return "";
            }

            @Override
            public void setFirstName(String firstName) {

            }

            @Override
            public String getLastName() {
                return "";
            }

            @Override
            public void setLastName(String lastName) {

            }

            @Override
            public String getEmail() {
                return "";
            }

            @Override
            public void setEmail(String email) {

            }

            @Override
            public String getPhoneNumber() {
                return "";
            }

            @Override
            public void setPhoneNumber(String phoneNumber) {

            }

            @Override
            public Date getHireDate() {
                return null;
            }

            @Override
            public void setHireDate(Date hireDate) {

            }

            @Override
            public EmployeeDetail getEmployeeDetail() {
                return null;
            }

            @Override
            public void setEmployeeDetail(EmployeeDetail employeeDetail) {

            }

            @Override
            public Set<TimeLog> getTimeLogs() {
                return Set.of();
            }

            @Override
            public void setTimeLogs(Set<TimeLog> timeLogs) {

            }

            @Override
            public Set<Shift> getShifts() {
                return Set.of();
            }

            @Override
            public void setShifts(Set<Shift> shifts) {

            }

            @Override
            public Set<EmployeeTraining> getEmployeeTrainings() {
                return Set.of();
            }

            @Override
            public void setEmployeeTrainings(Set<EmployeeTraining> trainings) {

            }
        }; e.setId(1);
        when(mockRepo.get(1)).thenReturn(e);

        RawHttpRequest request = http.parseRequest(
                "DELETE /api/employees/1 HTTP/1.1\r\n" +
                        "Host: localhost\r\n" +
                        "\r\n"
        ).eagerly();

        Optional<RawHttpResponse<?>> responseOpt = controller.route(request);
        assertTrue(responseOpt.isPresent());
        RawHttpResponse<?> response = responseOpt.get();
        assertEquals(204, response.getStatusCode());
        verify(mockRepo).delete(e);
    }

    @Test
    public void testDeleteEmployeeReturns404() throws IOException {
        when(mockRepo.get(1)).thenReturn(null);

        RawHttpRequest request = http.parseRequest(
                "DELETE /api/employees/1 HTTP/1.1\r\n" +
                        "Host: localhost\r\n" +
                        "\r\n"
        ).eagerly();

        NotFoundException exception = assertThrows(NotFoundException.class, () -> {
            controller.route(request);
        });
        assertTrue(exception.getMessage().contains("not found"));
    }
}
