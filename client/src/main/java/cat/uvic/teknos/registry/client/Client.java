package cat.uvic.teknos.registry.client;

import cat.uvic.teknos.registry.client.dto.EmployeeDTO;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import rawhttp.core.RawHttp;
import rawhttp.core.RawHttpRequest;
import rawhttp.core.RawHttpResponse;

import java.io.IOException;
import java.net.Socket;
import java.util.List;
import java.util.Scanner;

public class Client {

    private final String host = "localhost";
    private final int port = 9000;
    private final ObjectMapper objectMapper;
    private final RawHttp http;
    private final Scanner scanner;

    // AQUESTA ÉS LA PART NOVA: guardem el socket com un membre de la classe
    private Socket socket;

    public Client() {
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
        this.http = new RawHttp();
        this.scanner = new Scanner(System.in);
    }

    /**
     * Mètode principal que gestiona el cicle de vida complet del client.
     */
    public void start() {
        try {
            // 1. Connectem UN SOL COP a l'inici
            connect();

            // 2. Comença el bucle del menú
            runMenu();

        } catch (IOException e) {
            System.err.println("No s'ha pogut connectar al servidor: " + e.getMessage());
        } finally {
            // 3. Desconnectem UN SOL COP al final, passi el que passi
            disconnect();
        }
    }

    private void runMenu() {
        while (true) {
            printMenu();
            String choice = scanner.nextLine();
            if ("0".equals(choice)) {
                break; // Surt del bucle per finalitzar el programa
            }

            switch (choice) {
                case "1":
                    handleListAllEmployees();
                    break;
                case "2":
                    handleAddEmployee();
                    break;
                case "3":
                    handleDeleteEmployee();
                    break;
                default:
                    System.out.println("Opció no vàlida.");
                    break;
            }
        }
    }

    // --- MÈTODES DE GESTIÓ AMB EL SERVIDOR ---

    private void handleListAllEmployees() {
        System.out.println("\n--- Llista d'Empleats ---");
        try {
            List<EmployeeDTO> employees = getAllEmployees();
            if (employees != null && !employees.isEmpty()) {
                for (EmployeeDTO employee : employees) {
                    System.out.printf("Id: %d, Nom: %s %s, Email: %s, Antiguitat: %s\n",
                             employee.getId(), employee.getFirstName(), employee.getLastName(), employee.getEmail(), employee.getHireDate());

                }
            } else {
                System.out.println("No s'han trobat empleats.");
            }
        } catch (IOException e) {
            System.err.println("Error durant la comunicació amb el servidor: " + e.getMessage());
        }
    }

    private void handleDeleteEmployee() {
        System.out.println("\n--- Esborrar Empleat ---");
        System.out.print("Introdueix l'ID de l'empleat que vols esborrar: ");
        try {
            int id = Integer.parseInt(scanner.nextLine());

            // Cridem al mètode de comunicació
            if (deleteEmployee(id)) {
                System.out.println("Empleat esborrat amb èxit!");
            } else {
                System.out.println("No s'ha pogut esborrar l'empleat. Pot ser que l'ID no existeixi.");
            }
        } catch (NumberFormatException e) {
            System.err.println("Error: L'ID ha de ser un número.");
        } catch (IOException e) {
            System.err.println("Error de connexió amb el servidor: " + e.getMessage());
        }
    }

    private void handleAddEmployee() {
        System.out.println("\n--- Afegir un Nou Empleat ---");
        try {
            // Demanem les dades a l'usuari
            System.out.print("Introdueix el nom: ");
            String firstName = scanner.nextLine();

            System.out.print("Introdueix el cognom: ");
            String lastName = scanner.nextLine();

            System.out.print("Introdueix l'email: ");
            String email = scanner.nextLine();

            System.out.print("Introdueix el telèfon: ");
            String phone = scanner.nextLine();

            // Creem l'objecte DTO amb les dades
            EmployeeDTO newEmployee = new EmployeeDTO();
            newEmployee.setFirstName(firstName);
            newEmployee.setLastName(lastName);
            newEmployee.setEmail(email);
            newEmployee.setPhoneNumber(phone);

            // Cridem al mètode de comunicació
            if (addEmployee(newEmployee)) {
                System.out.println("Empleat afegit amb èxit!");
            } else {
                System.out.println("No s'ha pogut afegir l'empleat. Revisa les dades o l'error del servidor.");
            }
        } catch (IOException e) {
            System.err.println("Error de connexió amb el servidor: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Ha ocorregut un error inesperat: " + e.getMessage());
        }
    }


    // --- MÈTODES DE COMUNICACIÓ AMB EL SERVIDOR ---

    private List<EmployeeDTO> getAllEmployees() throws IOException {
        if (socket == null || socket.isClosed()) {
            throw new IOException("No estàs connectat al servidor.");
        }

        System.out.println("Enviant petició GET /api/employees...");
        RawHttpRequest request = http.parseRequest(
                "GET /api/employees HTTP/1.1\r\n" +
                        "Host: " + host + "\r\n" +
                        "Connection: keep-alive\r\n" + // Important: mantenim la connexió oberta
                        "\r\n");

        request.writeTo(socket.getOutputStream());

        // AFEGIM .eagerly() com a l'exemple del professor
        RawHttpResponse<?> response = http.parseResponse(socket.getInputStream()).eagerly();

        if (response.getStatusCode() == 200) {
            String jsonBody = response.getBody().orElseThrow().toString();
            return objectMapper.readValue(jsonBody, new TypeReference<>() {});
        } else {
            System.err.println("Error del servidor: " + response.getStatusCode());
            return null;
        }
    }

    /**
     * Envia una petició DELETE al servidor per esborrar un empleat.
     * Retorna 'true' si l'operació ha tingut èxit (el servidor respon 204),
     * 'false' en cas contrari.
     */

    private boolean deleteEmployee(int id) throws IOException {
        // Creem la petició DELETE amb l'ID a la URL
        RawHttpRequest request = http.parseRequest(
                String.format("DELETE /api/employees/%d HTTP/1.1\r\n", id) +
                        "Host: " + host + "\r\n" +
                        "Connection: close\r\n" +
                        "\r\n");

        try (Socket socket = new Socket(host, port)) {
            System.out.println("Enviant petició DELETE per a l'ID " + id + "...");
            request.writeTo(socket.getOutputStream());

            RawHttpResponse<?> response = http.parseResponse(socket.getInputStream()).eagerly();

            // L'estàndard per a un DELETE amb èxit és 204 No Content
            if (response.getStatusCode() == 204) {
                return true;
            } else {
                // Podria ser un 404 Not Found si l'ID no existeix
                System.err.println("Error del servidor: " + response.getStatusCode() + " " + response.getStartLine().getReason());
                return false;
            }
        }
    }

    /**
     * Envia una petició POST al servidor per crear un nou empleat.
     * Retorna 'true' si l'operació ha tingut èxit (el servidor respon 201),
     * 'false' en cas contrari.
     */
    private boolean addEmployee(EmployeeDTO newEmployee) throws IOException {
        // 1. Convertim l'objecte DTO a una cadena de text JSON
        String jsonBody = objectMapper.writeValueAsString(newEmployee);

        // 2. Creem la petició POST, incloent capçaleres importants per al cos JSON
        RawHttpRequest request = http.parseRequest(
                "POST /api/employees HTTP/1.1\r\n" +
                        "Host: " + host + "\r\n" +
                        "Content-Type: application/json\r\n" + // Crucial: li diem al servidor que enviem JSON
                        "Content-Length: " + jsonBody.getBytes().length + "\r\n" + // Crucial: li diem la mida del cos
                        "Connection: close\r\n" +
                        "\r\n" + // Línia en blanc que separa capçaleres i cos
                        jsonBody); // El cos de la petició

        try (Socket socket = new Socket(host, port)) {
            System.out.println("Enviant petició POST per crear un nou empleat...");
            request.writeTo(socket.getOutputStream());

            RawHttpResponse<?> response = http.parseResponse(socket.getInputStream()).eagerly();

            // L'estàndard per a una creació amb èxit és 201 Created
            if (response.getStatusCode() == 201) {
                return true;
            } else {
                // Pot ser un 400 Bad Request si el JSON és invàlid, o 409 Conflict si l'email ja existeix
                System.err.println("Error del servidor: " + response.getStatusCode() + " " + response.getStartLine().getReason());
                response.getBody().ifPresent(body -> System.err.println("Cos de l'error: " + body.toString()));
                return false;
            }
        }
    }


    // --- MÈTODES DE GESTIÓ DE LA CONNEXIÓ ---

    private void connect() throws IOException {
        System.out.println("Connectant al servidor a " + host + ":" + port + "...");
        this.socket = new Socket(host, port);
        System.out.println("Connectat!");
    }

    private void disconnect() {
        if (socket != null && !socket.isClosed()) {
            try {
                System.out.println("Desconnectant del servidor...");
                socket.close();
            } catch (IOException e) {
                System.err.println("Error en tancar el socket: " + e.getMessage());
            }
        }
        if(scanner != null) scanner.close();
        System.out.println("Fins aviat!");
    }

    private void printMenu() {
        System.out.println("\n--- MENÚ PRINCIPAL ---");
        System.out.println("1. Llistar tots els empleats");
        System.out.println("2. Afegir un nou empleat");
        System.out.println("3. Esborrar un empleat");
        System.out.println("0. Sortir");
        System.out.print("Tria una opció: ");
    }
}