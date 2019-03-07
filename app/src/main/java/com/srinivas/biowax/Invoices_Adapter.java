package com.srinivas.biowax;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.srinivas.Models.Invoice;

import java.util.ArrayList;

public class Invoices_Adapter extends RecyclerView.Adapter<Invoices_Adapter.Hospital> {

    ArrayList<Invoice> invoices;
    int Rowlayout;
    Context context;

    public Invoices_Adapter(ArrayList<Invoice> invoices, int check_single, Context applicationContext) {
        this.context = applicationContext;
        this.Rowlayout = check_single;
        this.invoices = invoices;
    }

    @NonNull
    @Override
    public Invoices_Adapter.Hospital onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(Rowlayout, viewGroup, false);
        return new Hospital(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Invoices_Adapter.Hospital hospital, int i) {
        hospital.hcfc_master_id_i.setText(invoices.get(i).getHcf_master_id());
        hospital.invoice_date.setText(invoices.get(i).getInvoice_date());
        hospital.billing_month.setText(invoices.get(i).getBilling_month());
        hospital.no_of_labs.setText(invoices.get(i).getNo_of_labs());
        hospital.cost_per_lab.setText(invoices.get(i).getCost_per_lab());
        hospital.lab_amount.setText(invoices.get(i).getLab_amount());
        hospital.paid_amount.setText(invoices.get(i).getPaid_amount());
        hospital.total_invoice_amount.setText(invoices.get(i).getTotal_invoice_amount());

    }

    @Override
    public int getItemCount() {
        return invoices.size();
    }

    public class Hospital extends RecyclerView.ViewHolder {
        TextView hcfc_master_id_i, invoice_date, billing_month, no_of_labs, cost_per_lab, lab_amount, paid_amount, total_invoice_amount;
        LinearLayout hostpital_ll;

        public Hospital(View itemView) {
            super(itemView);
            hcfc_master_id_i = (TextView) itemView.findViewById(R.id.hcfc_master_id_i);
            invoice_date = (TextView) itemView.findViewById(R.id.invoice_date);
            billing_month = (TextView) itemView.findViewById(R.id.billing_month);
            no_of_labs = (TextView) itemView.findViewById(R.id.no_of_labs);
            cost_per_lab = (TextView) itemView.findViewById(R.id.cost_per_lab);
            lab_amount = (TextView) itemView.findViewById(R.id.lab_amount);
            paid_amount = (TextView) itemView.findViewById(R.id.paid_amount);
            total_invoice_amount = (TextView) itemView.findViewById(R.id.total_invoice_amount);

            hostpital_ll = (LinearLayout) itemView.findViewById(R.id.hostpital_ll);
        }
    }
}
