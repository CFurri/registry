package cat.uvic.teknos.dam.registry.impl;

import cat.uvic.teknos.dam.registry.Employee;
import cat.uvic.teknos.dam.registry.Shift;

import java.time.LocalTime;
import java.util.Set;

public class ShiftImpl implements Shift {
    @Override
    public int getId() {
        return 0;
    }

    @Override
    public void setId(int id) {

    }

    @Override
    public String getName() {
        return "";
    }

    @Override
    public void setName(String name) {

    }

    @Override
    public LocalTime getStartTime() {
        return null;
    }

    @Override
    public void setStartTime(LocalTime startTime) {

    }

    @Override
    public LocalTime getEndTime() {
        return null;
    }

    @Override
    public void setEndTime(LocalTime endTime) {

    }

    @Override
    public String getLocation() {
        return "";
    }

    @Override
    public void setLocation(String location) {

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

