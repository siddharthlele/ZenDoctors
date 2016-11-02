package com.zenpets.doctors.utils.models.doctors;

public class ExperiencesData {

    private String doctorID;
    private String experienceTitle;

    public ExperiencesData() {
    }

    public ExperiencesData(String doctorID, String experienceTitle) {
        this.doctorID = doctorID;
        this.experienceTitle = experienceTitle;
    }

    public String getDoctorID() {
        return doctorID;
    }

    public String getExperienceTitle() {
        return experienceTitle;
    }
}