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

import com.srinivas.Printer.Print_Receipt;

import java.util.ArrayList;

public class Transaction_Adapter extends RecyclerView.Adapter<Transaction_Adapter.Hospital> {

    ArrayList<Transactions> Transactionss;
    int Rowlayout;
    Context context;

    public Transaction_Adapter(ArrayList<Transactions> Transactionss, int check_single, Context applicationContext) {
        this.context = applicationContext;
        this.Rowlayout = check_single;
        this.Transactionss = Transactionss;
    }

    @NonNull
    @Override
    public Transaction_Adapter.Hospital onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(Rowlayout, viewGroup, false);
        return new Hospital(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Transaction_Adapter.Hospital hospital, final int i) {
        hospital.trans_code.setText("  "+Transactionss.get(i).getTransaction_code());
        hospital.facilityname.setText("  " +Transactionss.get(i).getFacility_name());

        hospital.hostpital_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent biowaxform = new Intent(context, Print_Receipt.class);
                biowaxform.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                biowaxform.putExtra("transaction_code",Transactionss.get(i).getTransaction_code());
                context.startActivity(biowaxform);
            }
        });
    }

    @Override
    public int getItemCount() {
        return Transactionss.size();
    }

    public class Hospital extends RecyclerView.ViewHolder {
        TextView trans_code, facilityname;
        LinearLayout hostpital_ll;

        public Hospital(View itemView) {
            super(itemView);
            hostpital_ll = (LinearLayout) itemView.findViewById(R.id.hostpital_ll);
            trans_code = (TextView) itemView.findViewById(R.id.trans_code);
            facilityname = (TextView) itemView.findViewById(R.id.facilityname);

        }
    }
}
