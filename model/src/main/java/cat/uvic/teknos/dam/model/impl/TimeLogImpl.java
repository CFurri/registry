package cat.uvic.teknos.dam.model.impl;

import cat.uvic.teknos.dam.model.Employee;
import cat.uvic.teknos.dam.model.TimeLog;

import java.time.LocalDate;
import java.time.LocalTime;

public class TimeLogImpl implements TimeLog {
    @Override
    public int getId() {
        return 0;
    }

    @Override
    public void setId(int id) {

    }

    @Override
    public Employee getEmployee() {
        return null;
    }

    @Override
    public void setEmployee(Employee employee) {

    }

    @Override
    public LocalDate getLogDate() {
        return null;
    }

    @Override
    public void setLogDate(LocalDate date) {

    }

    @Override
    public LocalTime getCheckInTime() {
        return null;
    }

    @Override
    public void setCheckInTime(LocalTime time) {

    }

    @Override
    public LocalTime getCheckOutTime() {
        return null;
    }

    @Override
    public void setCheckOutTime(LocalTime time) {

    }

    @Override
    public double getTotalHours() {
        return 0;
    }

    @Override
    public void setTotalHours(double hours) {

    }

    @Override
    public boolean isLate() {
        return false;
    }

    @Override
    public void setLate(boolean isLate) {

    }

    @Override
    public boolean isRemote() {
        return false;
    }

    @Override
    public void setRemote(boolean isRemote) {

    }

    @Override
    public int getEmployeeId() {
        return 0;
    }

    @Override
    public double getHoursWorked() {
        return 0;
    }

    @Override
    public String getDescription() {
        return "";
    }
}
