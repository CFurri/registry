package cat.uvic.teknos.registry.server;

import rawhttp.core.RawHttp;
import rawhttp.core.RawHttpRequest;
import rawhttp.core.RawHttpResponse;
import rawhttp.core.body.StringBody;
import rawhttp.core.server.Router;

import java.io.IOException;
import java.net.Socket;

public class ClientHandler implements Runnable {

    private final Socket clientSocket;
    private final Router router;
    private final RawHttp http;

    //Pràctica 2 - Nou camp
    private final Server server; //Fa referència al servidor principal

    // P2 (canvi al constructor +argument)
    public ClientHandler(Socket socket, Router router, Server server) {
        this.clientSocket = socket;
        this.router = router;
        this.server = server; //Aquest és el +1
        this.http = new RawHttp();
    }

    @Override
    public void run() {

        //P2 Incrementar comptador
        server.incrementClientCount();

        try (clientSocket) {

            // Pas 1: Llegir la petició del client utilitzant el Socket's InputStream
            RawHttpRequest request = http.parseRequest(clientSocket.getInputStream()).eagerly();

            System.out.println("Petició rebuda: " + request.getStartLine());

            // P2.part3--- NOU: Interceptar /api/disconnect ---
            if (request.getUri().getPath().equals("/api/disconnect")) {
                System.out.println("Client " + clientSocket.getInetAddress() + " ha sol·licitat desconnexió per inactivitat.");

                // 1. Enviar ACK
                RawHttpResponse<?> ack = http.parseResponse("HTTP/1.1 200 OK").withBody(new StringBody("ACK"));
                ack.writeTo(clientSocket.getOutputStream());

                // 2. Esperar 1 segon
                Thread.sleep(1000);

                // 3. Tancar connexió (el 'try-with-resources' ho farà sol)
                System.out.println("ACK enviat, tancant connexió amb " + clientSocket.getInetAddress());

                // Sortim del mètode 'run' per evitar anar al router
                return;
            }
            // --- FI INTERCEPCIÓ ---

            // Si no és 'disconnect', seguim com sempre
            RawHttpResponse<?> response = router.route(request)
                    .orElse(http.parseResponse("HTTP/1.1 404 Not Found"));

            response.writeTo(clientSocket.getOutputStream());


        } catch (IOException e) { // <-- Variable 'e' (tipus IOException)
            System.err.println("Error de comunicació amb el client: " + e.getMessage());
            try {
                http.parseResponse("HTTP/1.1 500 Internal Server Error\r\n\r\n").writeTo(clientSocket.getOutputStream());
            } catch (IOException ignored) {}


        } catch (InterruptedException ie) { // <-- Canviem 'e' per 'ie' (tipus InterruptedException)

            System.err.println("Interromput durant l'espera de desconnexió: " + ie.getMessage());

        } finally {
            // Decrementem el comptador SEMPRE, quan el fil acaba
            server.decrementClientCount();
        }
    }
}