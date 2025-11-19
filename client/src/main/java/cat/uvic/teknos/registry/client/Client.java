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
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Scanner;

//Practica2 imports:
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.nio.charset.StandardCharsets;

//Practica3 imports:
import cat.uvic.teknos.registry.security.CryptoUtils;
import java.util.Optional;


public class Client {

    private final String host = "localhost";
    private final int port = 9000;
    private final ObjectMapper objectMapper;
    private final RawHttp http;
    private final Scanner scanner;
    private final CryptoUtils cryptoUtils; //P3

    // Nous camps per inactivitat
    private ScheduledExecutorService inactivityTimer;
    private ScheduledFuture<?> inactivityTask;
    private final long INACTIVITY_TIMEOUT_MINUTES = 2;


    private Socket socket;

    public Client() {
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
        this.http = new RawHttp();
        this.scanner = new Scanner(System.in);
        this.cryptoUtils = new CryptoUtils(); //P3
    }

    /**
     * Mètode principal que gestiona el cicle de vida complet del client.
     */
    public void start() {
        try {
            inactivityTimer = Executors.newSingleThreadScheduledExecutor();
            resetInactivityTimer(); //Programem la primera desconnexió

            // Comença el bucle del menú
            runMenu();

        } finally {
            // Tanquem recursos quan 'runMenu' acaba
            if (inactivityTimer != null){
                inactivityTimer.shutdownNow();
            }
            if (scanner != null){
                scanner.close();
            }
            System.out.println("Fins aviat!");
        }
    }


    private void runMenu() {
        while (true) {
            printMenu();
            String choice = scanner.nextLine();
            if ("0".equals(choice)) {
                sendDisconnectRequest(); //P2-Envia missatge de desconnexió
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
                case "4":
                    handleUpdateEmployee();
                    break;
                case "5":
                    handleGetEmployeeById();
                    break;
                default:
                    System.out.println("Opció no vàlida.");
                    break;
            }
            //P2
            // Després de cada acció de l'usuari, reiniciem el comptador
            System.out.println("[CLIENT] Acció registrada, reiniciant timer d'inactivitat (2 min).");
            resetInactivityTimer();
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
                System.out.println("No s'han trobat empleats o les dades estan corruptes.");
            }
        } catch (IOException e) {
            System.err.println("Error durant la comunicació amb el servidor (" + e.getClass().getSimpleName() + "): " + e.getMessage());
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

    private void handleUpdateEmployee() {
        System.out.println("\n--- Actualitzar Empleat ---");
        try {
            // 1. Demanem l'ID de l'empleat a modificar
            System.out.print("Introdueix l'ID de l'empleat que vols modificar: ");
            int id = Integer.parseInt(scanner.nextLine());

            // 2. Creem un DTO buit i li posem l'ID
            // (Seria ideal primer comprovar si l'ID existeix amb un 'getById',
            // però per ara ho fem directe)
            EmployeeDTO employeeToUpdate = new EmployeeDTO();
            employeeToUpdate.setId(id);

            // 3. Demanem la resta de dades
            System.out.print("Introdueix el nou nom (deixa en blanc per no canviar): ");
            String firstName = scanner.nextLine();
            if (!firstName.isEmpty()) {
                employeeToUpdate.setFirstName(firstName);
            }

            System.out.print("Introdueix el nou cognom (deixa en blanc per no canviar): ");
            String lastName = scanner.nextLine();
            if (!lastName.isEmpty()) {
                employeeToUpdate.setLastName(lastName);
            }

            System.out.print("Introdueix el nou email (deixa en blanc per no canviar): ");
            String email = scanner.nextLine();
            if (!email.isEmpty()) {
                employeeToUpdate.setEmail(email);
            }

            System.out.print("Introdueix el nou telèfon (deixa en blanc per no canviar): ");
            String phone = scanner.nextLine();
            if (!phone.isEmpty()) {
                employeeToUpdate.setPhoneNumber(phone);
            }

            // 4. Cridem al mètode de comunicació
            if (updateEmployee(employeeToUpdate)) {
                System.out.println("Empleat actualitzat amb èxit!");
            } else {
                System.out.println("No s'ha pogut actualitzar l'empleat. Pot ser que l'ID no existeixi.");
            }

        } catch (NumberFormatException e) {
            System.err.println("Error: L'ID ha de ser un número.");
        } catch (Exception e) {
            System.err.println("Ha ocorregut un error inesperat: " + e.getMessage());
        }
    }

    private void handleGetEmployeeById() {
        System.out.println("\n--- Veure Empleat per ID ---");
        System.out.print("Introdueix l'ID de l'empleat: ");
        try {
            int id = Integer.parseInt(scanner.nextLine());
            EmployeeDTO employee = getEmployeeById(id);
            if (employee != null) {
                System.out.printf("Id: %d, Nom: %s %s, Email: %s, Telèfon: %s\n",
                        employee.getId(), employee.getFirstName(), employee.getLastName(),
                        employee.getEmail(), employee.getPhoneNumber());
            }
        } catch (NumberFormatException e) {
            System.err.println("Error: L'ID ha de ser un número.");
        } catch (IOException e) {
            System.err.println("Error de connexió amb el servidor: " + e.getMessage());
        }
    }


    // --- MÈTODES DE COMUNICACIÓ AMB EL SERVIDOR ---

    private List<EmployeeDTO> getAllEmployees() throws IOException {
        System.out.println("Enviant petició GET /api/employees...");

        try (Socket socket = new Socket(host, port)) {  // <--- nou socket cada vegada
            RawHttpRequest request = http.parseRequest(
                    "GET /api/employees HTTP/1.1\r\n" +
                            "Host: " + host + "\r\n" +
                            "Connection: close\r\n" +
                            "\r\n");

            request.writeTo(socket.getOutputStream());
            RawHttpResponse<?> response = http.parseResponse(socket.getInputStream()).eagerly();

            if (response.getStatusCode() == 200) {
                // <-- P3: VERIFICAR HASH DE RESPOSTA -->
                if (!verifyResponseHash(response)) {
                    System.err.println("Error: Les dades rebudes del servidor estan corruptes (el hash no coincideix)");
                    return null; // Retornem null per indicar l'error
                }

                String jsonBody = response.getBody().orElseThrow().toString();
                return objectMapper.readValue(jsonBody, new TypeReference<>() {});
            } else {
                System.err.println("Error del servidor: " + response.getStatusCode());
                return null;
            }
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

        //P3: Calcular el Hash del cos enviat
        String hash = cryptoUtils.hash(jsonBody);

        // 2. Creem la petició POST, incloent capçaleres importants per al cos JSON
        RawHttpRequest request = http.parseRequest(
                "POST /api/employees HTTP/1.1\r\n" +
                        "Host: " + host + "\r\n" +
                        "Content-Type: application/json\r\n" + // Crucial: li diem al servidor que enviem JSON
                        "Content-Length: " + jsonBody.getBytes().length + "\r\n" + // Crucial: li diem la mida del cos
                        "X-Message-Hash: " + hash + "\r\n" + // <-- P3: CAPÇALERA DE HASH
                        "Connection: close\r\n" +
                        "\r\n" + // Línia en blanc que separa capçaleres i cos
                        jsonBody); // El cos de la petició

        try (Socket socket = new Socket(host, port)) {
            System.out.println("Enviant petició POST per crear un nou empleat...");
            request.writeTo(socket.getOutputStream());

            RawHttpResponse<?> response = http.parseResponse(socket.getInputStream()).eagerly();

            // L'estàndard per a una creació amb èxit és 201 Created
            if (response.getStatusCode() == 201) {
                if (!verifyResponseHash(response)) {
                    System.err.println("Error: Les dades rebudes del servidor estan corruptes (el hash no coincideix)");
                    return false;
                }
                return true;
            } else {
                // Pot ser un 400 Bad Request si el JSON és invàlid, o 409 Conflict si l'email ja existeix
                System.err.println("Error del servidor: " + response.getStatusCode() + " " + response.getStartLine().getReason());
                response.getBody().ifPresent(body -> System.err.println("Cos de l'error: " + body.toString()));
                return false;
            }
        }
    }


    /**
     * Envia una petició PUT al servidor per actualitzar un empleat existent.
     * Retorna 'true' si l'operació ha tingut èxit (el servidor respon 200 o 204),
     * 'false' en cas contrari.
     *
     * @param employee L'objecte DTO amb les dades a actualitzar (ha de contenir l'ID).
     * @return true si l'actualització ha tingut èxit, false altrament.
     * @throws IOException Si hi ha un error de serialització o de xarxa.
     */
    private boolean updateEmployee(EmployeeDTO employee) throws IOException {
        // 1. Validació bàsica
        if (employee == null || employee.getId() <= 0) {
            System.err.println("Error intern: L'employee a actualitzar és invàlid o no té ID.");
            return false;
        }

        // 2. Preparem el path i el cos de la petició
        String path = "/api/employees/" + employee.getId();
        System.out.println("Enviant petició PUT a " + path + "...");

        // Serialitzem l'objecte a JSON
        String jsonBody = objectMapper.writeValueAsString(employee);

        //P3: Calcular el Hash del cost enviat
        String hash = cryptoUtils.hash(jsonBody);

        // Calculem la mida del cos EN BYTES (importantíssim)
        byte[] jsonBodyBytes = jsonBody.getBytes(StandardCharsets.UTF_8);
        int contentLength = jsonBodyBytes.length;

        // 3. Construïm la petició HTTP completa (headers + cos)
        String requestString = "PUT " + path + " HTTP/1.1\r\n" +
                "Host: " + host + "\r\n" +
                "Connection: close\r\n" +
                "Content-Type: application/json\r\n" +
                "Content-Length: " + contentLength + "\r\n" +
                "X-Message-Hash: " + hash + "\r\n" + // <-- P3: CAPÇALERA DE HASH
                "\r\n" + // Línia en blanc VITAL
                jsonBody; // El cos

        // Parsejem la petició
        RawHttpRequest request = http.parseRequest(requestString);

        // 4. Obrim socket, enviem petició i rebem resposta
        // Fem servir el mateix patró robust "un socket per petició"
        try (Socket socket = new Socket(host, port)) {

            request.writeTo(socket.getOutputStream());

            RawHttpResponse<?> response = http.parseResponse(socket.getInputStream()).eagerly();

            // 5. Comprovem el codi de resposta
            // Un PUT exitós pot retornar 200 (OK) o 204 (No Content)
            if (response.getStatusCode() == 200 || response.getStatusCode() == 204) {
                if (response.getStatusCode() == 200) { // Un 204 no té cos
                    if (!verifyResponseHash(response)) {
                        System.err.println("Error: Les dades rebudes del servidor estan corruptes (el hash no coincideix)");
                        return false;
                    }
                }
                return true;
            } else {
                // Gestionem errors comuns com 404 (No trobat)
                System.err.println("Error del servidor: " + response.getStatusCode() + " " + response.getStartLine().getReason());
                return false;
            }
        }
        // Les IOException (com JsonProcessingException o de xarxa) es propagaran
        // i seran capturades pel 'handleUpdateEmployee'.
    }


    private EmployeeDTO getEmployeeById(int id) throws IOException {
        String path = "/api/employees/" + id;
        try (Socket socket = new Socket(host, port)) {
            RawHttpRequest request = http.parseRequest(
                    "GET " + path + " HTTP/1.1\r\n" +
                            "Host: " + host + "\r\n" +
                            "Connection: close\r\n" +
                            "\r\n"
            );

            request.writeTo(socket.getOutputStream());
            RawHttpResponse<?> response = http.parseResponse(socket.getInputStream()).eagerly();

            if (response.getStatusCode() == 200) {
                // <-- P3: VERIFICAR HASH DE RESPOSTA -->
                if (!verifyResponseHash(response)) {
                    System.err.println("Error: Les dades rebudes del servidor estan corruptes (el hash no coincideix)");
                    return null; // Retornem null per indicar l'error
                }

                String jsonBody = response.getBody().orElseThrow().toString();
                return objectMapper.readValue(jsonBody, EmployeeDTO.class);
            } else if (response.getStatusCode() == 404) {
                System.err.println("Empleat amb ID " + id + " no trobat.");
                return null;
            } else {
                System.err.println("Error del servidor: " + response.getStatusCode() + " " + response.getStartLine().getReason());
                return null;
            }
        }
    }


    // <-- P3: MÈTODE PER VERIFICAR HASHES REBUTS -->
    /**
     * Verifica la integritat d'una resposta HTTP comprovant la seva capçalera X-Message-Hash.
     * @param response La resposta HTTP rebuda.
     * @return true si el hash és correcte o si la resposta no té cos, false si hi ha un error d'integritat.
     */
    private boolean verifyResponseHash(RawHttpResponse<?> response) {
        // Obtenim la capçalera amb el hash que envia el servidor
        Optional<String> receivedHashOpt = response.getHeaders().getFirst("X-Message-Hash");

        // Obtenim el cos
        Optional<? extends rawhttp.core.body.BodyReader> bodyOpt = response.getBody();
        if (bodyOpt.isEmpty()) {
            // Si no hi ha cos (p.ex. un 204 No Content), no cal verificar res.
            return true;
        }

        // Si hi ha cos, HI HA D'HAVER hash.
        if (receivedHashOpt.isEmpty()) {
            System.err.println("Error d'integritat: El servidor ha enviat un cos sense la capçalera X-Message-Hash.");
            return false;
        }

        String receivedHash = receivedHashOpt.get();
        String jsonBody = bodyOpt.get().toString();

        // Calculem el nostre propi hash del cos
        String calculatedHash = cryptoUtils.hash(jsonBody);

        // Comparem
        if (!receivedHash.equals(calculatedHash)) {
            System.err.println("ALERTA DE SEGURETAT: El hash del missatge no coincideix!");
            System.err.println("  > Hash rebut:   " + receivedHash);
            System.err.println("  > Hash calculat: " + calculatedHash);
            return false;
        }

        // Tot correcte
        return true;
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
        System.out.println("4. Actualitzar un empleat");
        System.out.println("5. Veure empleat per ID");
        System.out.println("0. Sortir");
        System.out.print("Tria una opció: ");
    }

    // --- NOUS MÈTODES D'INACTIVITAT ---

    // Aquest mètode s'executarà si passen 2 minuts
    private void handleInactivity() {
        System.out.println("\n[CLIENT] Inactivitat detectada (2 min).");
        sendDisconnectRequest();

        // Forcem la sortida del client
        System.out.println("[CLIENT] Tancant per inactivitat.");
        System.exit(0);
    }

    // Reinicia el temporitzador
    private void resetInactivityTimer() {
        // Si hi ha una tasca programada, la cancelem
        if (inactivityTask != null) {
            inactivityTask.cancel(false);
        }
        // Programem la nova tasca de desconnexió
        inactivityTask = inactivityTimer.schedule(
                this::handleInactivity, // Mètode a executar
                INACTIVITY_TIMEOUT_MINUTES,
                TimeUnit.MINUTES
        );
    }

    // Lògica per enviar el missatge 'disconnect'
    private void sendDisconnectRequest() {
        System.out.println("[CLIENT] Notificant al servidor la desconnexió...");
        try (Socket socket = new Socket(host, port)) {
            // Creem la petició especial POST /api/disconnect
            RawHttpRequest request = http.parseRequest(
                    "POST /api/disconnect HTTP/1.1\r\n" +
                            "Host: " + host + "\r\n" +
                            "Connection: close\r\n" +
                            "\r\n");

            request.writeTo(socket.getOutputStream());

            // Esperem l'ACK
            RawHttpResponse<?> response = http.parseResponse(socket.getInputStream()).eagerly();
            if (response.getStatusCode() == 200) {
                System.out.println("[CLIENT] ACK rebut del servidor.");
            } else {
                System.err.println("[CLIENT] Resposta inesperada del servidor a la desconnexió: " + response.getStatusCode());
            }

        } catch (IOException e) {
            System.err.println("[CLIENT] No s'ha pogut notificar al servidor: " + e.getMessage());
        }
    }
    // --- FI NOUS MÈTODES ---
}