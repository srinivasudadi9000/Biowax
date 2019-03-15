package com.srinivas.biowax;

public class Hospitalshistory {

     String hcf_master_id,waste_collection_date,barcode_number,transaction_code,cover_color_id,is_approval_required,approved_by,
             bag_weight_in_hcf,is_manual_input,hcf_authorized_person_name,is_sagregation_completed,sagregation_image,transactionid,weight;

    public Hospitalshistory(String hcf_master_id, String waste_collection_date, String barcode_number, String transaction_code,
                            String cover_color_id, String is_approval_required, String approved_by, String bag_weight_in_hcf,
                            String is_manual_input,
                            String hcf_authorized_person_name, String is_sagregation_completed, String sagregation_image,
    String transactionid,String weight) {
        this.hcf_master_id = hcf_master_id;
        this.waste_collection_date = waste_collection_date;
        this.barcode_number = barcode_number;
        this.transaction_code = transaction_code;
        this.cover_color_id = cover_color_id;
        this.is_approval_required = is_approval_required;
        this.approved_by = approved_by;
        this.bag_weight_in_hcf = bag_weight_in_hcf;
        this.is_manual_input = is_manual_input;
        this.hcf_authorized_person_name = hcf_authorized_person_name;
        this.is_sagregation_completed = is_sagregation_completed;
        this.sagregation_image = sagregation_image;
        this.transactionid= transactionid;
        this.weight = weight;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getTransactionid() {
        return transactionid;
    }

    public void setTransactionid(String transactionid) {
        this.transactionid = transactionid;
    }

    public String getHcf_master_id() {
        return hcf_master_id;
    }

    public void setHcf_master_id(String hcf_master_id) {
        this.hcf_master_id = hcf_master_id;
    }

    public String getWaste_collection_date() {
        return waste_collection_date;
    }

    public void setWaste_collection_date(String waste_collection_date) {
        this.waste_collection_date = waste_collection_date;
    }

    public String getBarcode_number() {
        return barcode_number;
    }

    public void setBarcode_number(String barcode_number) {
        this.barcode_number = barcode_number;
    }

    public String getTransaction_code() {
        return transaction_code;
    }

    public void setTransaction_code(String transaction_code) {
        this.transaction_code = transaction_code;
    }

    public String getCover_color_id() {
        return cover_color_id;
    }

    public void setCover_color_id(String cover_color_id) {
        this.cover_color_id = cover_color_id;
    }

    public String getIs_approval_required() {
        return is_approval_required;
    }

    public void setIs_approval_required(String is_approval_required) {
        this.is_approval_required = is_approval_required;
    }

    public String getApproved_by() {
        return approved_by;
    }

    public void setApproved_by(String approved_by) {
        this.approved_by = approved_by;
    }

    public String getBag_weight_in_hcf() {
        return bag_weight_in_hcf;
    }

    public void setBag_weight_in_hcf(String bag_weight_in_hcf) {
        this.bag_weight_in_hcf = bag_weight_in_hcf;
    }

    public String getIs_manual_input() {
        return is_manual_input;
    }

    public void setIs_manual_input(String is_manual_input) {
        this.is_manual_input = is_manual_input;
    }

    public String getHcf_authorized_person_name() {
        return hcf_authorized_person_name;
    }

    public void setHcf_authorized_person_name(String hcf_authorized_person_name) {
        this.hcf_authorized_person_name = hcf_authorized_person_name;
    }

    public String getIs_sagregation_completed() {
        return is_sagregation_completed;
    }

    public void setIs_sagregation_completed(String is_sagregation_completed) {
        this.is_sagregation_completed = is_sagregation_completed;
    }

    public String getSagregation_image() {
        return sagregation_image;
    }

    public void setSagregation_image(String sagregation_image) {
        this.sagregation_image = sagregation_image;
    }
}
