package com.zenpets.doctors.utils.models.doctors;

public class SpecializationsData {

    private String doctorID;
    private String specializationName;

    public SpecializationsData() {
    }

    public SpecializationsData(String doctorID, String specializationName) {
        this.doctorID = doctorID;
        this.specializationName = specializationName;
    }

    public String getDoctorID() {
        return doctorID;
    }

    public String getSpecializationName() {
        return specializationName;
    }
}