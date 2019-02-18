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

public class Hospitals_Adapter extends RecyclerView.Adapter<Hospitals_Adapter.Hospital> {

    ArrayList<Hospitals> hospitals;
    int Rowlayout;
    Context context;

    public Hospitals_Adapter(ArrayList<Hospitals> hospitals, int check_single, Context applicationContext) {
        this.context = applicationContext;
        this.Rowlayout = check_single;
        this.hospitals = hospitals;
    }

    @NonNull
    @Override
    public Hospitals_Adapter.Hospital onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(Rowlayout, viewGroup, false);
        return new Hospital(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Hospitals_Adapter.Hospital hospital, int i) {
        hospital.hospitalname_tv.setText(hospitals.get(i).getH_name());
        hospital.hostpital_code_tv.setText(hospitals.get(i).getH_code());
        hospital.hostpital_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent biowaxform = new Intent(context,Biowastageform.class);
                context.startActivity(biowaxform);
            }
        });
    }

    @Override
    public int getItemCount() {
        return hospitals.size();
    }

    public class Hospital extends RecyclerView.ViewHolder {
        TextView hospitalname_tv, hostpital_code_tv;
        LinearLayout hostpital_ll;

        public Hospital(View itemView) {
            super(itemView);
            hospitalname_tv = (TextView) itemView.findViewById(R.id.hospitalname_tv);
            hostpital_code_tv = (TextView) itemView.findViewById(R.id.hostpital_code_tv);
            hostpital_ll = (LinearLayout) itemView.findViewById(R.id.hostpital_ll);
        }
    }
}
