package cat.uvic.teknos.registry.server;

import rawhttp.core.RawHttp;
import rawhttp.core.RawHttpRequest;
import rawhttp.core.RawHttpResponse;
import rawhttp.core.server.Router;

import java.io.IOException;
import java.net.Socket;

public class ClientHandler implements Runnable {

    private final Socket clientSocket;
    private final Router router;
    private final RawHttp http;

    public ClientHandler(Socket socket, Router router) {
        this.clientSocket = socket;
        this.router = router;
        this.http = new RawHttp();
    }

    @Override
    public void run() {
        try (clientSocket) {
            // Pas 1: Llegir la petició del client utilitzant el Socket's InputStream
            RawHttpRequest request = http.parseRequest(clientSocket.getInputStream()).eagerly();

            // Pas 2: Utilitzar el router per obtenir la resposta
            RawHttpResponse<?> response = router.route(request)
                    .orElse(http.parseResponse("HTTP/1.1 404 Not Found"));

            // Pas 3: Escriure la resposta al client utilitzant el Socket's OutputStream
            response.writeTo(clientSocket.getOutputStream());

        } catch (IOException e) {
            System.err.println("Error de comunicació amb el client: " + e.getMessage());
        }
    }
}