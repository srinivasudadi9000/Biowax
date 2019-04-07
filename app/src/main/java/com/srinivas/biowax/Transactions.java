package com.srinivas.biowax;

public class Transactions {

    String transaction_code,transaction_ref,transaction_num,facility_name,color_name;

    public Transactions(String transaction_code, String transaction_ref, String transaction_num, String facility_name, String color_name) {
        this.transaction_code = transaction_code;
        this.transaction_ref = transaction_ref;
        this.transaction_num = transaction_num;
        this.facility_name = facility_name;
        this.color_name = color_name;
    }

    public String getTransaction_code() {
        return transaction_code;
    }

    public void setTransaction_code(String transaction_code) {
        this.transaction_code = transaction_code;
    }

    public String getTransaction_ref() {
        return transaction_ref;
    }

    public void setTransaction_ref(String transaction_ref) {
        this.transaction_ref = transaction_ref;
    }

    public String getTransaction_num() {
        return transaction_num;
    }

    public void setTransaction_num(String transaction_num) {
        this.transaction_num = transaction_num;
    }

    public String getFacility_name() {
        return facility_name;
    }

    public void setFacility_name(String facility_name) {
        this.facility_name = facility_name;
    }

    public String getColor_name() {
        return color_name;
    }

    public void setColor_name(String color_name) {
        this.color_name = color_name;
    }
}
