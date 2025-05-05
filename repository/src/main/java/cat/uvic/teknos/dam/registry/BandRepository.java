package cat.uvic.teknos.dam.registry;

public interface BandRepository {
    void insert(Band band);
    void update(Band band);
    void delete(Band band);
}
// i això no es pot fer perquè es inconsistent,
// creem Repository


