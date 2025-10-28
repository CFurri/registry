package cat.uvic.teknos.registry.server;

import cat.uvic.teknos.registry.app.DIManager;

public class App {

    private static final int PORT = 9000;

    public static void main(String[] args) {
        try {
            // 1. Creem el DIManager. Ell carregarà automàticament "di.properties".
            DIManager diManager = new DIManager();

            RawHttpService router = new RequestRouter(diManager);

            // 3. Creació del Servidor
            Server server = new Server(PORT, router);

            // 4. Comprovació: Demanem al DIManager que fabriqui el que digui
            //    el fitxer de propietats. Ell ho farà tot sol.
            String repoType = diManager.get("repository_factory").getClass().getSimpleName();
            System.out.println("Registry Back Office Server initialized (Repo: " + repoType + ")");

            // 5. Llançament del Servidor
            server.start();

        } catch (Exception e) {
            System.err.println("FATAL ERROR: Could not start the server.");
            e.printStackTrace();
        }
    }
}