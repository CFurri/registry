package cat.uvic.teknos.registry.server;


import cat.uvic.teknos.registry.server.controllers.EmployeeController;
import rawhttp.core.RawHttp;
import rawhttp.core.RawHttpRequest;
import rawhttp.core.RawHttpResponse;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLOutput;

//Gestiona el ServerSocket, accepta connexions i crea un ClientHandler o passa la petició al RequestRouter.
public class Server {

    public void start() {
        try (ServerSocket server = new ServerSocket(8083)) {
            RawHttp http = new RawHttp();
            System.out.println("Server started on port 8083");

            while(true){
                Socket client = server.accept();
                System.out.println("Client connected: "+ client);

                try {
                    RawHttpRequest request = http.parseRequest(client.getInputStream());
                    String path = request.getUri().getPath();

                    RequestRouter router = new RequestRouter(new EmployeeController());
                    RawHttpResponse<?> response = router.route(request);
                    response.writeTo(client.getOutputStream());

                    client.close();
                }catch (IOException e) {
                    e.printStackTrace();
                }
            }

        } catch (IOException e){
            e.printStackTrace();
        }
    }
}