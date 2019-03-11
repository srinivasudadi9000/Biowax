package com.srinivas.biowax;

public class Locationshistory {

     String latitude,longitude,hospitalid;

    public Locationshistory(String latitude, String longitude, String hospitalid) {
        this.hospitalid = hospitalid;
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getHospitalid() {
        return hospitalid;
    }

    public void setHospitalid(String hospitalid) {
        this.hospitalid = hospitalid;
    }
}
