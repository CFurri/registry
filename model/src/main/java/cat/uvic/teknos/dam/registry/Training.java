package cat.uvic.teknos.dam.registry;

public interface Training {
    // Mètodes per a la columna 'training_id' (PK autoincremental)
    int getId();
    void setId(int id);

    //Mètodes per a la columna 'title'
    String getTitle();
    void setTitle(String title);

}
// https://chat.deepseek.com/a/chat/s/b999c978-4c3b-48be-8ec6-ae43ef8a8068