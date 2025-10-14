package cat.uvic.teknos.registry.server;

import cat.uvic.teknos.registry.app.DIManager;

public class App {

    private static final int PORT = 9000;

    public static void main(String[] args) {
        DIManager diManager = null;

        try {
            // 1. Inicialització del sistema DI
            diManager = new DIManager();

            // 2. Creació del Router
            RawHttpService router = new RequestRouter(diManager);

            // 3. Creació de la Classe Server (el socket manager)
            Server server = new Server(PORT, router);

            String repoType = diManager.get("repository_factory").getClass().getSimpleName();
            System.out.println("Registry Back Office Server initialized (Repo: " + repoType + ")");

            // 4. Llançament del Servidor
            server.start();

        } catch (Exception e) {
            System.err.println("FATAL ERROR: Server failed to start.");
            e.printStackTrace();
        } finally {
            // 5. Tancar recursos (encara responsabilitat de l'App)
            if (diManager != null) {
                diManager.close();
            }
            System.out.println("Application shut down.");
        }
    }
}