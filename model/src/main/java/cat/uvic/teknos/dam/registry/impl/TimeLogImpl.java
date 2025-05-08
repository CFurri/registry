package cat.uvic.teknos.dam.registry.impl;

import cat.uvic.teknos.dam.registry.Employee;
import cat.uvic.teknos.dam.registry.TimeLog;

import java.time.LocalDate;
import java.time.LocalTime;

public class TimeLogImpl implements TimeLog {
    private int logId;
    private int employeeId;
    private LocalDate logDate;
    private LocalTime checkInTime;
    private LocalTime checkOutTime;
    private double totalHours;
    private boolean isLate;
    private boolean isRemote;

    @Override
    public int getLogId() {
        return logId;
    }

    @Override
    public void setLogId(int logId) {
        this.logId = logId;
    }

    @Override
    public int getEmployeeId() {
        return employeeId;
    }

    @Override
    public void setEmployeeId(int employeeId) {
        this.employeeId = employeeId;
    }

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
    public void setEmployeeId(Employee employee) {

    }

    @Override
    public LocalDate getLogDate() {
        return logDate;
    }

    @Override
    public void setLogDate(LocalDate logDate) {
        this.logDate = logDate;
    }

    @Override
    public LocalTime getCheckInTime() {
        return checkInTime;
    }

    @Override
    public void setCheckInTime(LocalTime checkInTime) {
        this.checkInTime = checkInTime;
    }

    @Override
    public LocalTime getCheckOutTime() {
        return checkOutTime;
    }

    @Override
    public void setCheckOutTime(LocalTime checkOutTime) {
        this.checkOutTime = checkOutTime;
    }

    @Override
    public double getTotalHours() {
        return totalHours;
    }

    @Override
    public void setTotalHours(double totalHours) {
        this.totalHours = totalHours;
    }

    @Override
    public boolean isLate() {
        return isLate;
    }

    @Override
    public void setLate(boolean isLate) {
        this.isLate = isLate;
    }

    @Override
    public boolean isRemote() {
        return isRemote;
    }

    @Override
    public void setRemote(boolean isRemote) {
        this.isRemote = isRemote;
    }
}

