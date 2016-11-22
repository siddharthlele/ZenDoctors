package com.zenpets.doctors.utils.models.doctors;

public class EducationData {

    private String qualificationName;
    private String collegeName;
    private String qualificationYear;

    public EducationData() {
    }

    public EducationData(String qualificationName, String collegeName, String qualificationYear) {
        this.qualificationName = qualificationName;
        this.collegeName = collegeName;
        this.qualificationYear = qualificationYear;
    }

    public String getQualificationName() {
        return qualificationName;
    }

    public String getCollegeName() {
        return collegeName;
    }

    public String getQualificationYear() {
        return qualificationYear;
    }
}