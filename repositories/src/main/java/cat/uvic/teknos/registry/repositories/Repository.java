package cat.uvic.teknos.registry.repositories;

import java.util.Set;

public interface Repository<K, V> {
    void save(V value);       // Inserta o actualitza
    void delete(V value);     // Elimina
    V get(K id);              // Obté un registre pel seu id
    Set<V> getAll();          // Obté tots els registres
}

