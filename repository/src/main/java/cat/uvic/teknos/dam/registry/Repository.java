package cat.uvic.teknos.dam.registry;

import java.util.Set;

public interface Repository<K, V> {
    void save(V value);       // Inserta o actualitza
    void delete(V value);     // Elimina
    V get(K id);              // Obté un registre pel seu id
    Set<V> getAll();          // Obté tots els registres
}
//defineixo una interfície amb genèrics (K, V)
// en classe principal = repositori, sinó no
