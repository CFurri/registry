package cat.uvic.teknos.dam.registry;

public interface Repository <K, V> {
    void save(V value);
    void delete(V Value);
    Object get(K id);
    Set <V> getAll();

}
//defineixo una interfície amb genèrics (K, V)
// en classe principal = repositori, sinó no
