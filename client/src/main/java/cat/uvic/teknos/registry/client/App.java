package cat.uvic.teknos.registry.client;

public class App {

    public static void main(String[] args) {
        // 1. Creem l'objecte Client, que ara ho conté tot
        Client client = new Client();

        // 2. Engeguem el seu bucle principal
        client.start();
    }
}