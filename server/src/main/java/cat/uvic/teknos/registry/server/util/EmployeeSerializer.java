package cat.uvic.teknos.registry.server.util;

import cat.uvic.teknos.registry.models.Employee;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.util.List;

public class EmployeeSerializer {

    private final ObjectMapper objectMapper;

    public EmployeeSerializer() {
        objectMapper = new ObjectMapper();
        // Important per si els models tenen dates (LocalDate, etc.)
        objectMapper.registerModule(new JavaTimeModule());
    }

    public String serialize(Employee employee) {
        try {
            return objectMapper.writeValueAsString(employee);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return "{}"; // Retorna un JSON buit en cas d'error
        }
    }

    public String serialize(List<Employee> employees) {
        try {
            return objectMapper.writeValueAsString(employees);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return "[]"; // Retorna un JSON de llista buida en cas d'error
        }
    }
}
