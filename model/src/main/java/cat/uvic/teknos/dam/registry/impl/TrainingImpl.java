package cat.uvic.teknos.dam.registry.impl;

import cat.uvic.teknos.dam.registry.Training;

public class TrainingImpl implements Training {
    private int trainingId;
    private String title;
    private String description;
    private int durationHours;
    private boolean mandatory;

    @Override
    public int getTrainingId() {
        return trainingId;
    }

    @Override
    public void setTrainingId(int trainingId) {
        this.trainingId = trainingId;
    }

    @Override
    public int getId() {
        return 0;
    }

    @Override
    public void setId(int id) {

    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public int getDurationHours() {
        return durationHours;
    }

    @Override
    public void setDurationHours(int durationHours) {
        this.durationHours = durationHours;
    }

    @Override
    public boolean isMandatory() {
        return mandatory;
    }

    @Override
    public void setMandatory(boolean mandatory) {
        this.mandatory = mandatory;
    }
}
