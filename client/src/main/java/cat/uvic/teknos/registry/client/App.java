package cat.uvic.teknos.registry.client;

import cat.uvic.teknos.registry.client.dto.EmployeeDTO;

import java.util.List;
import java.util.Scanner;

public class App {

    public static void main(String[] args) {
        // Creem una instància del nostre client, que gestiona la comunicació
        Client client = new Client();
        Scanner scanner = new Scanner(System.in);

        // Bucle infinit per mostrar el menú fins que l'usuari decideixi sortir
        while (true) {
            System.out.println("\n--- MENÚ PRINCIPAL ---");
            System.out.println("1. Llistar tots els empleats");
            // Aquí afegirem més opcions (obtenir per ID, afegir, etc.)
            System.out.println("0. Sortir");
            System.out.print("Tria una opció: ");

            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    try {
                        System.out.println("\n--- Llista d'Empleats ---");
                        // Ara cridem al mètode que retorna objectes, no text!
                        List<EmployeeDTO> employees = client.getAllEmployees();

                        if (employees != null && !employees.isEmpty()) {
                            for (EmployeeDTO employee : employees) {
                                // Mostrem les dades de manera molt més clara
                                System.out.printf("Nom: %s, Cognom: %s, Email: %s\n",
                                        employee.getFirstName(),
                                        employee.getLastName(),
                                        employee.getEmail());
                            }
                        } else {
                            System.out.println("No s'han trobat empleats.");
                        }
                    } catch (Exception e) {
                        System.err.println("Error en connectar amb el servidor: " + e.getMessage());
                    }
                    break;
                case "0":
                    System.out.println("Fins aviat!");
                    scanner.close();
                    return; // Surt del programa
                default:
                    System.out.println("Opció no vàlida. Torna a intentar-ho.");
                    break;
            }
        }
    }
}
