package cat.uvic.teknos.dam.registry;
import java.util.Set;
public interface Repository<K, V>{
    V get (K id);
    void save(V item );
    void delete(K id);
    Set<V> getAll();

    //gestiona un objecte i li hem d'especificar la clau, l'id
    //tots els repositoris que fem servir nosaltres, han d'implementar aquesta interfície
    //hola
}
