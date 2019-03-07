package com.srinivas.Models;

public class Invoice {

    String hcf_master_id,invoice_date,billing_month,no_of_bedsbeds_amount,no_of_labs,cost_per_lab,lab_amount,paid_amount,total_invoice_amount;

    public Invoice(String hcf_master_id, String invoice_date, String billing_month, String no_of_bedsbeds_amount, String no_of_labs,
                   String cost_per_lab, String lab_amount, String paid_amount, String total_invoice_amount) {
        this.hcf_master_id = hcf_master_id;
        this.invoice_date = invoice_date;
        this.billing_month = billing_month;
        this.no_of_bedsbeds_amount = no_of_bedsbeds_amount;
        this.no_of_labs = no_of_labs;
        this.cost_per_lab = cost_per_lab;
        this.lab_amount = lab_amount;
        this.paid_amount = paid_amount;
        this.total_invoice_amount = total_invoice_amount;
    }

    public String getHcf_master_id() {
        return hcf_master_id;
    }

    public void setHcf_master_id(String hcf_master_id) {
        this.hcf_master_id = hcf_master_id;
    }

    public String getInvoice_date() {
        return invoice_date;
    }

    public void setInvoice_date(String invoice_date) {
        this.invoice_date = invoice_date;
    }

    public String getBilling_month() {
        return billing_month;
    }

    public void setBilling_month(String billing_month) {
        this.billing_month = billing_month;
    }

    public String getNo_of_bedsbeds_amount() {
        return no_of_bedsbeds_amount;
    }

    public void setNo_of_bedsbeds_amount(String no_of_bedsbeds_amount) {
        this.no_of_bedsbeds_amount = no_of_bedsbeds_amount;
    }

    public String getNo_of_labs() {
        return no_of_labs;
    }

    public void setNo_of_labs(String no_of_labs) {
        this.no_of_labs = no_of_labs;
    }

    public String getCost_per_lab() {
        return cost_per_lab;
    }

    public void setCost_per_lab(String cost_per_lab) {
        this.cost_per_lab = cost_per_lab;
    }

    public String getLab_amount() {
        return lab_amount;
    }

    public void setLab_amount(String lab_amount) {
        this.lab_amount = lab_amount;
    }

    public String getPaid_amount() {
        return paid_amount;
    }

    public void setPaid_amount(String paid_amount) {
        this.paid_amount = paid_amount;
    }

    public String getTotal_invoice_amount() {
        return total_invoice_amount;
    }

    public void setTotal_invoice_amount(String total_invoice_amount) {
        this.total_invoice_amount = total_invoice_amount;
    }
}
