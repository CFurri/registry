package cat.uvic.teknos.dam.model.impl;

import cat.uvic.teknos.dam.model.Employee;
import cat.uvic.teknos.dam.model.Training;

import java.util.Set;

public class TrainingImpl implements Training {

    @Override
    public int getId() {
        return 0;
    }

    @Override
    public void setId(int id) {

    }

    @Override
    public String getTitle() {
        return "";
    }

    @Override
    public void setTitle(String title) {

    }

    @Override
    public String getDescription() {
        return "";
    }

    @Override
    public void setDescription(String description) {

    }

    @Override
    public int getDurationHours() {
        return 0;
    }

    @Override
    public void setDurationHours(int durationHours) {

    }

    @Override
    public boolean isMandatory() {
        return false;
    }

    @Override
    public void setMandatory(boolean mandatory) {

    }

    @Override
    public Set<Employee> getEmployees() {
        return Set.of();
    }

    @Override
    public void setEmployees(Set<Employee> employees) {

    }

    @Override
    public void addEmployee(Employee employee) {

    }

    @Override
    public void removeEmployee(Employee employee) {

    }
}
