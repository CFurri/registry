package cat.uvic.teknos.dam.registry.impl;

import cat.uvic.teknos.dam.registry.Employee;
import cat.uvic.teknos.dam.registry.Shift;

import java.util.List;

public class ShiftImpl implements Shift {
    private int shiftId;
    private String name;
    private String startTime;
    private String endTime;
    private String location;

    @Override
    public int getShiftId() {
        return shiftId;
    }

    @Override
    public void setShiftId(int shiftId) {
        this.shiftId = shiftId;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getStartTime() {
        return startTime;
    }

    @Override
    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    @Override
    public String getEndTime() {
        return endTime;
    }

    @Override
    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    @Override
    public String getLocation() {
        return location;
    }

    @Override
    public void setLocation(String location) {
        this.location = location;
    }

    @Override
    public List<Employee> getEmployees() {
        return List.of();
    }

    @Override
    public void setEmployees(List<Employee> employees) {

    }
}

