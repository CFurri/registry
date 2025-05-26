package cat.uvic.teknos.dam.registry.model.jpa;

import cat.uvic.teknos.dam.model.Employee;
import jakarta.persistence.*;

import lombok.Data;
import java.util.HashSet;
import java.util.Set;

@Entity //obligatori que una annotation Entity tingui una ID
@Table(name = "EMPLOYEE")
@Data
public class JpaEmployee implements Employee {
    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Com es gestiona l'anotation GeneratedValue
    private int id;
    private String firstName;
    @Transient
    //falta de l'estil private bla<bla>
    private Set<BandMusician> bandMusicians = new HasSet<BandMusician>();
    @Transient
    //falta de l'estil private bla<bla>

}
