package cat.uvic.teknos.dam.registry.impl;

import cat.uvic.teknos.dam.registry.EmployeeTraining;

import java.time.LocalDate;

public class EmployeeTrainingImpl implements EmployeeTraining {
    private int employeeId;
    private int trainingId;
    private LocalDate completionDate;
    private boolean passed;
    private double score;

    @Override
    public int getEmployeeId() {
        return employeeId;
    }

    @Override
    public void setEmployeeId(int employeeId) {
        this.employeeId = employeeId;
    }

    @Override
    public int getTrainingId() {
        return trainingId;
    }

    @Override
    public void setTrainingId(int trainingId) {
        this.trainingId = trainingId;
    }

    @Override
    public LocalDate getCompletionDate() {
        return completionDate;
    }

    @Override
    public void setCompletionDate(LocalDate completionDate) {
        this.completionDate = completionDate;
    }

    @Override
    public boolean isPassed() {
        return passed;
    }

    @Override
    public void setPassed(boolean passed) {
        this.passed = passed;
    }

    @Override
    public double getScore() {
        return score;
    }

    @Override
    public void setScore(double score) {
        this.score = score;
    }
}

