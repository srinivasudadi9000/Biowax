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

import java.util.ArrayList;

public class Histroy_Adapter extends RecyclerView.Adapter<Histroy_Adapter.Hospital> {

    ArrayList<Hospitalshistory> hospitals;
    int Rowlayout;
    Context context;

    public Histroy_Adapter(ArrayList<Hospitalshistory> hospitals, int check_single, Context applicationContext) {
        this.context = applicationContext;
        this.Rowlayout = check_single;
        this.hospitals = hospitals;
    }

    @NonNull
    @Override
    public Histroy_Adapter.Hospital onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(Rowlayout, viewGroup, false);
        return new Hospital(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Histroy_Adapter.Hospital hospital, final int i) {
        hospital.hcfc_master_id.setText("HospitalId: "+hospitals.get(i).getHcf_master_id());
        hospital.waste_collection_date.setText(hospitals.get(i).getWaste_collection_date());
        hospital.barcodenumber.setText(hospitals.get(i).getBarcode_number());
        hospital.cover_color_id.setText("CoverId: "+hospitals.get(i).getCover_color_id());
        hospital.bag_weight_in_hcf.setText("TransactionId : "+hospitals.get(i).getTransactionid());

        hospital.hostpital_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent biowaxform = new Intent(context, History_View.class);
                biowaxform.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                biowaxform.putExtra("hcfc_master_id_tv",hospitals.get(i).getHcf_master_id());
                biowaxform.putExtra("waste_collection_date_tv",hospitals.get(i).getWaste_collection_date());
                biowaxform.putExtra("barcodeNumber_tv",hospitals.get(i).getBarcode_number());
                biowaxform.putExtra("transaction_code_tv",hospitals.get(i).getTransaction_code());
                biowaxform.putExtra("cover_color_id_tv",hospitals.get(i).getCover_color_id());
                biowaxform.putExtra("is_approval_required_tv",hospitals.get(i).getIs_approval_required());
                biowaxform.putExtra("approved_by_tv",hospitals.get(i).getApproved_by());
                biowaxform.putExtra("bag_weight_in_hcf_tv",hospitals.get(i).getTransactionid());
                biowaxform.putExtra("is_manual_input_tv",hospitals.get(i).getIs_manual_input());
                biowaxform.putExtra("hcf_authorized_person_name_tv",hospitals.get(i).getHcf_authorized_person_name());
                biowaxform.putExtra("is_sagregation_completed_tv",hospitals.get(i).getIs_sagregation_completed());
                context.startActivity(biowaxform);
            }
        });
    }

    @Override
    public int getItemCount() {
        return hospitals.size();
    }

    public class Hospital extends RecyclerView.ViewHolder {
        TextView hcfc_master_id, waste_collection_date, barcodenumber, cover_color_id, bag_weight_in_hcf;
        LinearLayout hostpital_ll;

        public Hospital(View itemView) {
            super(itemView);
            hcfc_master_id = (TextView) itemView.findViewById(R.id.hcfc_master_id);
            waste_collection_date = (TextView) itemView.findViewById(R.id.waste_collection_date);
            barcodenumber = (TextView) itemView.findViewById(R.id.barcodenumber);
            cover_color_id = (TextView) itemView.findViewById(R.id.cover_color_id);
            bag_weight_in_hcf = (TextView) itemView.findViewById(R.id.bag_weight_in_hcf);
            hostpital_ll = (LinearLayout) itemView.findViewById(R.id.hostpital_ll);
        }
    }
}
