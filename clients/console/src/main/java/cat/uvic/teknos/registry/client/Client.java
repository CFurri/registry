package cat.uvic.teknos.registry.client;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Scanner;

public class Client {
    public static void main(String[] args) throws IOException {
        Socket socket = new Socket("localhost", 5001);

        var inputStream = new Scanner(new InputStreamReader(socket.getInputStream()));
        var outputStream = new PrintWriter(socket.getOutputStream());

        System.out.println(inputStream.nextLine());

        var scanner = new Scanner(System.in);
        var request = "";
        while (! (request = scanner.nextLine()).equals("exit")) {
            outputStream.println(request);
            outputStream.flush();

            System.out.println(inputStream.nextLine());
        }

        socket.close();
    }
}
