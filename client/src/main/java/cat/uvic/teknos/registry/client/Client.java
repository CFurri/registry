package cat.uvic.teknos.registry.client;

import cat.uvic.teknos.registry.client.dto.EmployeeDTO;
import com.fasterxml.jackson.core.type.TypeReference; // Import important!
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule; // Per a LocalDate
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

public class Client {

    private final HttpClient httpClient;
    private final ObjectMapper objectMapper; // El nostre traductor de JSON
    private final String baseUrl = "http://localhost:8080";

    public Client() {
        this.httpClient = HttpClient.newHttpClient();
        this.objectMapper = new ObjectMapper();
        // Registrem el mòdul per a poder treballar amb dates com LocalDate
        this.objectMapper.registerModule(new JavaTimeModule());
    }

    // Aquest mètode retorna el JSON cru (pot ser útil per depurar)
    public String getAllEmployeesAsString() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + "/api/employees"))
                .GET()
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            return response.body();
        } else {
            System.err.println("Error: " + response.statusCode());
            return null;
        }
    }

    // NOU MÈTODE: Aquest retorna la llista d'objectes DTO ja traduïda!
    public List<EmployeeDTO> getAllEmployees() throws IOException, InterruptedException {
        String jsonBody = getAllEmployeesAsString(); // Reutilitzem el mètode anterior
        if (jsonBody != null) {
            // La màgia de Jackson: convertim el String JSON a una Llista d'EmployeeDTO
            return objectMapper.readValue(jsonBody, new TypeReference<List<EmployeeDTO>>() {});
        }
        return null;
    }
}
