package cat.uvic.teknos.registry.server.dto;

import java.sql.Date;
import java.time.LocalDate;

/**
 * DTO (Data Transfer Object) per a les dades de l'empleat.
 * S'utilitza per rebre i enviar dades a través de l'API en format JSON.
 */
public class EmployeeDTO {
    private int id;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private LocalDate hireDate;

    // Constructors (un de buit és necessari per a la deserialització de Jackson)
    public EmployeeDTO() {
    }

    // Getters i Setters (essencials perquè la llibreria Jackson pugui llegir i escriure les propietats)
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public LocalDate getHireDate() {
        return hireDate;
    }

    public void setHireDate(LocalDate hireDate) {
        this.hireDate = hireDate;
    }
}