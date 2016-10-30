package com.zenpets.doctors.utils.models;

public class ClinicAlbumsData {

    private String clinicOwner;
    private String clinicID;
    private String clinicImage;

    public ClinicAlbumsData() {
    }

    public ClinicAlbumsData(String clinicOwner, String clinicID, String clinicImage) {
        this.clinicOwner = clinicOwner;
        this.clinicID = clinicID;
        this.clinicImage = clinicImage;
    }

    public String getClinicOwner() {
        return clinicOwner;
    }

    public String getClinicID() {
        return clinicID;
    }

    public String getClinicImage() {
        return clinicImage;
    }
}