package com.zenpets.doctors.utils.models;

import android.graphics.Bitmap;

public class CountryData {

    private String countryName;
    private Bitmap countryFlag;
    private String currencySymbol;

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public Bitmap getCountryFlag() {
        return countryFlag;
    }

    public void setCountryFlag(Bitmap countryFlag) {
        this.countryFlag = countryFlag;
    }

    public String getCurrencySymbol() {
        return currencySymbol;
    }

    public void setCurrencySymbol(String currencySymbol) {
        this.currencySymbol = currencySymbol;
    }
}