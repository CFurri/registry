package cat.uvic.teknos.registry.server;

import rawhttp.core.server.TcpRawHttpServer;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Initiates the server socket, manages client connections, and starts listening for requests.
 */
public class Server {

    private final int port;
    private final RawHttpService router;
    private final ExecutorService executor; // Encara el gestionem nosaltres

    public Server(int port, RawHttpService router) {
        this.port = port;
        this.router = router;
        this.executor = Executors.newFixedThreadPool(4);
    }

    public void start() {
        System.out.println("Server core starting on port " + port);

        // CANVI CLAU: Passem el port al constructor
        TcpRawHttpServer server = new TcpRawHttpServer(port);

        try {
            // CANVI CLAU: El mètode start() només necessita el router.
            // L'executor de fils el gestionarà internament el servidor.
            server.start(router);

            System.out.println("Server is now listening. Press ENTER to stop.");

            // Bloqueja el fil principal.
            System.in.read();

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // Atura el servidor i tanca el nostre executor.
            System.out.println("Stopping server...");
            server.stop();
            executor.shutdown(); // Important tancar-lo encara que el servidor no l'usi directament
        }
    }
}