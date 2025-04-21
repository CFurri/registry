package cat.uvic.teknos.dam.registry;

public interface Training {
    // Mètodes per a la columna 'training_id' (PK autoincremental)
    int getId();
    void setId(int id);

    //Mètodes per a la columna 'title'
    String getTitle();
    void setTitle(String title);

    // Mètodes per a la columna 'description'
    String getDescription();
    void setDescription(String description);

    // Mètodes per a 'duration_hours'
    int getDurationHours();
    void setDurationHours(int durationHours);

    // Mètodes per a 'mandatory'
    boolean isMandatory();
    void setMandatory(boolean mandatory);

}
// https://chat.deepseek.com/a/chat/s/b999c978-4c3b-48be-8ec6-ae43ef8a8068