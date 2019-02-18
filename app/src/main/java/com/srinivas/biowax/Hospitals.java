package com.srinivas.biowax;

public class Hospitals {
    String h_name,h_code,h_address;
    Double h_lat,h_long;

    public Hospitals(String h_name, String h_code, Double h_lat, Double h_long, String h_address) {
        this.h_name = h_name;
        this.h_code = h_code;
        this.h_lat = h_lat;
        this.h_long = h_long;
        this.h_address = h_address;
    }

    public String getH_name() {
        return h_name;
    }

    public void setH_name(String h_name) {
        this.h_name = h_name;
    }

    public String getH_code() {
        return h_code;
    }

    public void setH_code(String h_code) {
        this.h_code = h_code;
    }

    public Double getH_lat() {
        return h_lat;
    }

    public void setH_lat(Double h_lat) {
        this.h_lat = h_lat;
    }

    public Double getH_long() {
        return h_long;
    }

    public void setH_long(Double h_long) {
        this.h_long = h_long;
    }

    public String getH_address() {
        return h_address;
    }

    public void setH_address(String h_address) {
        this.h_address = h_address;
    }
}
