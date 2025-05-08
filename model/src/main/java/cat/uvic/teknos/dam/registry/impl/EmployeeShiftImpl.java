package cat.uvic.teknos.dam.registry.impl;

import cat.uvic.teknos.dam.registry.EmployeeShift;

public class EmployeeShiftImpl implements EmployeeShift {
    private int employeeId;
    private int shiftId;

    @Override
    public int getEmployeeId() {
        return employeeId;
    }

    @Override
    public void setEmployeeId(int employeeId) {
        this.employeeId = employeeId;
    }

    @Override
    public int getShiftId() {
        return shiftId;
    }

    @Override
    public void setShiftId(int shiftId) {
        this.shiftId = shiftId;
    }
}

