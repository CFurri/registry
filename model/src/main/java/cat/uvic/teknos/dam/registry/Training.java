package cat.uvic.teknos.dam.registry;

public interface Training {
    int getTrainingId();

    void setTrainingId(int trainingId);

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
