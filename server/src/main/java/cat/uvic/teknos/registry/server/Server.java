package cat.uvic.teknos.registry.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Server {

    private final int port;
    private final RawHttpService router;
    private final ExecutorService executor;

    //Pràctica 2 (nous camps)
    //Un comptador per als clients actius
    private int activeClients = 0;
    private final Lock clientCountLock = new ReentrantLock();

    public Server(int port, RawHttpService router) {
        this.port = port;
        this.router = router;
        this.executor = Executors.newFixedThreadPool(10); // Gestiona fins a 10 clients alhora
    }

    public void start() {
        //Engeguem el fil monitor ABANS del bucle principal
        startMonitorThread();

        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Servidor basat en Sockets escoltant al port " + port);

            // Bucle infinit per acceptar connexions de clients
            while (true) {
                // El mètode accept() espera que un client es connecti
                Socket clientSocket = serverSocket.accept();
                System.out.println("Client connectat des de " + clientSocket.getInetAddress());

                //-->Antic
                // Creem un ClientHandler, li passem el Socket i el Router
                //ClientHandler clientHandler = new ClientHandler(clientSocket, router);

                // --> Nou
                // Passem 'this' (el propi servidor) al handler, perquè pugui accedir als mètodes inc/dec
                ClientHandler clientHandler = new ClientHandler(clientSocket, router, this);

                // El pool de fils s'encarrega d'executar la lògica del handler
                executor.submit(clientHandler);
            }
        } catch (IOException e) {
            System.err.println("Error en iniciar el servidor al port " + port);
            e.printStackTrace();
        }
    }
    // --- NOUS MÈTODES ---(P2)
    // Mètode per engegar el fil monitor
    private void startMonitorThread() {
        Thread monitorThread = new Thread(() -> {
            try {
                while (true) {
                    // Espera 1 minut
                    Thread.sleep(60000);

                    // Bloqueja, llegeix el valor, desbloqueja
                    clientCountLock.lock();
                    try {
                        System.out.println("[MONITOR] Clients actius: " + activeClients);
                    } finally {
                        clientCountLock.unlock();
                    }
                }
            } catch (InterruptedException e) {
                System.out.println("[MONITOR] Fil monitor interromput.");
            }
        });

        monitorThread.setDaemon(true); // IMPRESCINDIBLE
        monitorThread.setName("ServerMonitor");
        monitorThread.start();
    }

    // Mètodes 'thread-safe' que el ClientHandler cridarà
    public void incrementClientCount() {
        clientCountLock.lock();
        try {
            activeClients++;
        } finally {
            clientCountLock.unlock();
        }
    }

    public void decrementClientCount() {
        clientCountLock.lock();
        try {
            activeClients--;
        } finally {
            clientCountLock.unlock();
        }
    }
    // --- FI NOUS MÈTODES ---
}