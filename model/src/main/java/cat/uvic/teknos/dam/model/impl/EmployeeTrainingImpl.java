package cat.uvic.teknos.dam.model.impl;

import cat.uvic.teknos.dam.model.Employee;
import cat.uvic.teknos.dam.model.EmployeeTraining;
import cat.uvic.teknos.dam.model.Training;

import java.time.LocalDate;

public class EmployeeTrainingImpl implements EmployeeTraining {
    @Override
    public int getPersonId() {
        return 0;
    }

    @Override
    public void setPersonId(int personId) {

    }

    @Override
    public Employee getEmployee() {
        return null;
    }

    @Override
    public void setEmployee(Employee employee) {

    }

    @Override
    public int getCourseId() {
        return 0;
    }

    @Override
    public void setCourseId(int courseId) {

    }

    @Override
    public Training getTraining() {
        return null;
    }

    @Override
    public void setTraining(Training training) {

    }

    @Override
    public LocalDate getCompletionDate() {
        return null;
    }

    @Override
    public void setCompletionDate(LocalDate completionDate) {

    }

    @Override
    public boolean isPassed() {
        return false;
    }

    @Override
    public void setPassed(boolean passed) {

    }

    @Override
    public double getScore() {
        return 0;
    }

    @Override
    public void setScore(double score) {

    }
}
