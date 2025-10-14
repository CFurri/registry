package cat.uvic.teknos.registry.client;

import cat.uvic.teknos.registry.client.dto.EmployeeDTO;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import rawhttp.core.RawHttp;
import rawhttp.core.RawHttpResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class Client {

    private final String host = "localhost";
    private final int port = 8080;
    private final ObjectMapper objectMapper;
    private final RawHttp http;

    public Client() {
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
        this.http = new RawHttp();
    }

    public List<EmployeeDTO> getAllEmployees() {
        // Creem un nou Socket per a cada petició
        try (Socket socket = new Socket(host, port)) {
            PrintWriter writer = new PrintWriter(socket.getOutputStream(), true, StandardCharsets.UTF_8);

            // Pas 1: Escrivim la petició HTTP a mà a l'OutputStream del Socket
            writer.println("GET /api/employees HTTP/1.1");
            writer.println("Host: " + host);
            writer.println("Connection: close");
            writer.println(); // Línia final en blanc, crucial!

            // Pas 2: rawhttp llegeix la resposta des de l'InputStream del Socket
            RawHttpResponse<?> response = http.parseResponse(socket.getInputStream());

            // Pas 3: Processem la resposta com abans
            if (response.getStatusCode() == 200) {
                String jsonBody = response.getBody().get().toString(); // rawhttp ens dona el cos
                return objectMapper.readValue(jsonBody, new TypeReference<List<EmployeeDTO>>() {});
            } else {
                System.err.println("Error del servidor: " + response.getStatusCode());
                return null;
            }
        } catch (IOException e) {
            System.err.println("Error de connexió amb el servidor: " + e.getMessage());
            return null;
        }
    }

    // Aquí implementaries els mètodes per a POST, PUT, DELETE, etc.
}