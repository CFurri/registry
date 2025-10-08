package cat.uvic.teknos.registry.server;


import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

//Punt d'entrada del servidor (main()), crea dependències i arrenca el Server.
public class App {
    public static void main(String[] args) throws IOException {
        new Server().start();
    }
}
