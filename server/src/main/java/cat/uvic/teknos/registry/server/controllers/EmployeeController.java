package cat.uvic.teknos.registry.server.controllers;

import cat.uvic.teknos.registry.app.DIManager;
import cat.uvic.teknos.registry.app.exceptions.DIException;
import cat.uvic.teknos.registry.models.Employee;
import cat.uvic.teknos.registry.models.ModelFactory;
import cat.uvic.teknos.registry.repositories.EmployeeRepository;
import cat.uvic.teknos.registry.repositories.RepositoryFactory;
import cat.uvic.teknos.registry.server.RawHttpService;
import cat.uvic.teknos.registry.server.dto.EmployeeDTO;
import cat.uvic.teknos.registry.server.exceptions.BadRequestException;
import cat.uvic.teknos.registry.server.exceptions.ConflictException;
import cat.uvic.teknos.registry.server.exceptions.NotFoundException;
import com.fasterxml.jackson.databind.ObjectMapper;
import rawhttp.core.RawHttp; // Import necessari per al patró de resposta
import rawhttp.core.RawHttpRequest;
import rawhttp.core.RawHttpResponse;
import rawhttp.core.body.StringBody;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class EmployeeController implements RawHttpService {

    private final EmployeeRepository employeeRepository;
    private final ModelFactory modelFactory;
    private final ObjectMapper objectMapper;
    private final RawHttp http; // Afegim una instància per reutilitzar-la

    public EmployeeController(DIManager diManager) throws DIException {
        RepositoryFactory repositoryFactory = diManager.get("repository_factory");
        this.employeeRepository = repositoryFactory.getEmployeeRepository();
        this.modelFactory = diManager.get("model_factory");
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new com.fasterxml.jackson.datatype.jsr310.JavaTimeModule());
        this.http = new RawHttp(); // Inicialitzem l'instància
    }

    @Override
    public Optional<RawHttpResponse<?>> route(RawHttpRequest request) {
        String path = request.getUri().getPath();
        String method = request.getMethod();

        if (path.equals("/api/employees")) {
            if (method.equals("GET")) {
                return handleGetAllEmployees();
            }
            if (method.equals("POST")) {
                return handlePostEmployee(request);
            }
        } else if (path.startsWith("/api/employees/")) {
            try {
                String idStr = path.substring("/api/employees/".length());
                int id = Integer.parseInt(idStr);

                switch (method) {
                    case "GET":
                        return handleGetEmployeeById(id);
                    case "DELETE":
                        return handleDeleteEmployee(id);
                    case "PUT":
                        return handlePutEmployee(request, id);
                }
            } catch (NumberFormatException e) {
                throw new BadRequestException("Invalid employee ID format.", e);
            }
        }

        return Optional.empty();
    }

    private EmployeeDTO toDTO(Employee employee) {
        if (employee == null) return null;

        EmployeeDTO dto = new EmployeeDTO();

        dto.setId(employee.getId());
        dto.setFirstName(employee.getFirstName());
        dto.setLastName(employee.getLastName());
        dto.setEmail(employee.getEmail());
        dto.setPhoneNumber(employee.getPhoneNumber());
        // dto.setHireDate(employee.getHireDate()); // Assegura't que els tipus coincideixin
        return dto;
    }

    // CORRECCIÓ: Mètode per a respostes JSON adaptat a la versió 2.6.0
    private RawHttpResponse<?> jsonResponse(int status, String reason, Object data) {
        try {
            String jsonBody = objectMapper.writeValueAsString(data);

            // Aquesta és la manera correcta de construir la resposta a la v2.6.0
            return http.parseResponse("HTTP/1.1 " + status + " " + reason)
                    .withBody(new StringBody(jsonBody, "application/json; charset=utf-8"));
            // La llibreria afegirà les capçaleres Content-Type i Content-Length automàticament
        } catch (IOException e) {
            throw new RuntimeException("Failed to serialize response to JSON", e);
        }
    }

    // CORRECCIÓ: Mètode per a respostes 204 adaptat a la versió 2.6.0
    private RawHttpResponse<?> noContentResponse() {
        // Un 204 no ha de tenir cos ni capçaleres de contingut
        return http.parseResponse("HTTP/1.1 204 No Content");
    }

    private Optional<RawHttpResponse<?>> handleGetAllEmployees() {
        Set<Employee> employees = employeeRepository.getAll();

        // TRADUÏM la llista de Models a una llista de DTOs
        List<EmployeeDTO> employeeDTOs = employees.stream()
                .map(this::toDTO) // Utilitzem el nostre nou mètode
                .collect(Collectors.toList());

        // Enviem la llista de DTOs, que sí que és segura per serialitzar
        return Optional.of(jsonResponse(200, "OK", employeeDTOs));
    }

    private Optional<RawHttpResponse<?>> handleGetEmployeeById(int id) {
        Employee employee = employeeRepository.get(id);

        if (employee == null) {
            throw new NotFoundException("Employee with ID " + id + " not found.");
        }

        // TRADUÏM el model a DTO abans d'enviar-lo
        EmployeeDTO dto = toDTO(employee);

        return Optional.of(jsonResponse(200, "OK", dto));
    }


    private Optional<RawHttpResponse<?>> handlePostEmployee(RawHttpRequest request) {
        if (request.getBody().isEmpty()) {
            throw new BadRequestException("Request body is required.");
        }

        try {
            String jsonPayload = request.getBody().get().toString();
            EmployeeDTO dto = objectMapper.readValue(jsonPayload, EmployeeDTO.class);

            Employee employeeToSave = modelFactory.newEmployee();
            employeeToSave.setFirstName(dto.getFirstName());
            employeeToSave.setLastName(dto.getLastName());
            employeeToSave.setEmail(dto.getEmail());
            employeeToSave.setPhoneNumber(dto.getPhoneNumber());
            //employeeToSave.setHireDate(dto.getHireDate());

            employeeRepository.save(employeeToSave);

            EmployeeDTO createdDTO = toDTO(employeeToSave); // Convertim a DTO abans d'enviar
            return Optional.of(jsonResponse(201, "Created", createdDTO));
        } catch (IOException e) {
            throw new BadRequestException("Invalid JSON format or missing fields.", e);
        } catch (RuntimeException e) {
            if (e.getMessage() != null && e.getMessage().toLowerCase().contains("duplicate entry")) {
                throw new ConflictException("An employee with that email or phone number already exists.", e);
            }
            throw e;
        }
    }

    private Optional<RawHttpResponse<?>> handleDeleteEmployee(int id) {
        Employee employee = employeeRepository.get(id);
        if (employee == null) {
            throw new NotFoundException("Employee with ID " + id + " not found.");
        }
        employeeRepository.delete(employee);
        return Optional.of(noContentResponse());
    }

    private Optional<RawHttpResponse<?>> handlePutEmployee(RawHttpRequest request, int id) {
        if (request.getBody().isEmpty()) {
            throw new BadRequestException("Request body is required.");
        }

        Employee employeeToUpdate = employeeRepository.get(id);
        if (employeeToUpdate == null) {
            throw new NotFoundException("Employee with ID " + id + " not found for update.");
        }

        try {
            String jsonPayload = request.getBody().get().toString();
            EmployeeDTO dto = objectMapper.readValue(jsonPayload, EmployeeDTO.class);

            employeeToUpdate.setFirstName(dto.getFirstName());
            employeeToUpdate.setLastName(dto.getLastName());
            employeeToUpdate.setEmail(dto.getEmail());
            employeeToUpdate.setPhoneNumber(dto.getPhoneNumber());
            //employeeToUpdate.setHireDate(dto.getHireDate());

            employeeRepository.save(employeeToUpdate);

            EmployeeDTO updatedDTO = toDTO(employeeToUpdate); // Convertim a DTO abans d'enviar
            return Optional.of(jsonResponse(200, "OK", updatedDTO));
        } catch (IOException e) {
            throw new BadRequestException("Invalid JSON or data: " + e.getMessage(), e);
        }
    }
}