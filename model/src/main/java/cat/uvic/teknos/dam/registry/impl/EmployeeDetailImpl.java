package cat.uvic.teknos.dam.registry.impl;

import cat.uvic.teknos.dam.registry.EmployeeDetail;

public class EmployeeDetailImpl implements EmployeeDetail {
    private int employeeId;
    private String address;
    private String nationalId;
    private String emergencyContactName;
    private String emergencyContactPhone;

    @Override
    public int getEmployeeId() {
        return employeeId;
    }

    @Override
    public void setEmployeeId(int employeeId) {
        this.employeeId = employeeId;
    }

    @Override
    public String getAddress() {
        return address;
    }

    @Override
    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public String getNationalId() {
        return nationalId;
    }

    @Override
    public void setNationalId(String nationalId) {
        this.nationalId = nationalId;
    }

    @Override
    public String getEmergencyContactName() {
        return emergencyContactName;
    }

    @Override
    public void setEmergencyContactName(String emergencyContactName) {
        this.emergencyContactName = emergencyContactName;
    }

    @Override
    public String getEmergencyContactPhone() {
        return emergencyContactPhone;
    }

    @Override
    public void setEmergencyContactPhone(String emergencyContactPhone) {
        this.emergencyContactPhone = emergencyContactPhone;
    }
}

