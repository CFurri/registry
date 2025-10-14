package cat.uvic.teknos.registry.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {

    private final int port;
    private final RawHttpService router;
    private final ExecutorService executor;

    public Server(int port, RawHttpService router) {
        this.port = port;
        this.router = router;
        this.executor = Executors.newFixedThreadPool(10); // Gestiona fins a 10 clients alhora
    }

    public void start() {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Servidor basat en Sockets escoltant al port " + port);

            // Bucle infinit per acceptar connexions de clients
            while (true) {
                // El mètode accept() espera que un client es connecti
                Socket clientSocket = serverSocket.accept();
                System.out.println("Client connectat des de " + clientSocket.getInetAddress());

                // Creem un ClientHandler, li passem el Socket i el Router
                ClientHandler clientHandler = new ClientHandler(clientSocket, router);

                // El pool de fils s'encarrega d'executar la lògica del handler
                executor.submit(clientHandler);
            }
        } catch (IOException e) {
            System.err.println("Error en iniciar el servidor al port " + port);
            e.printStackTrace();
        }
    }
}