package cat.uvic.teknos.registry.server;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Scanner;

public class Server {
    public static void main(String[] args) throws IOException {

        var server = new ServerSocket(5001);
        System.out.println("Servidor iniciat al port 5001. Esperant connexions...");

        var client = server.accept();
        System.out.println("Client connectat!");

        var inputStream = new Scanner(new InputStreamReader(client.getInputStream()));
        var outputStream = new PrintWriter(client.getOutputStream());

        var request = "";
        while(!(request = inputStream.nextLine().toLowerCase()).equals("exit")){
            var response = "";
            if (request.equals("time")){
                LocalTime localTime = LocalDateTime.now().toLocalTime();
                response = localTime.toString();
            }
            else if (request.equals("date")){
                LocalDate localDate = LocalDate.now();
                response = localDate.toString();

            }
            else {
                response = "Invalid request";
            }

            outputStream.println(response);
            outputStream.flush();
        }
        outputStream.println("Bye bye");

        client.close();
        server.close();
    }
}
