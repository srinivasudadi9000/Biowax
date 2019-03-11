package com.srinivas.biowax;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class Location_Adapter extends RecyclerView.Adapter<Location_Adapter.Location> {

    ArrayList<Locationshistory> locations;
    int Rowlayout;
    Context context;

    public Location_Adapter(ArrayList<Locationshistory> locations, int check_single, Context applicationContext) {
        this.context = applicationContext;
        this.Rowlayout = check_single;
        this.locations = locations;
    }

    @NonNull
    @Override
    public Location_Adapter.Location onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(Rowlayout, viewGroup, false);
        return new Location(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Location_Adapter.Location location, final int i) {
        location.address.setText(locations.get(i).getHospitalid());
        location.latitude.setText(locations.get(i).getLongitude());
        location.longitude.setText(locations.get(i).getLatitude());

    }

    @Override
    public int getItemCount() {
        return locations.size();
    }

    public class Location extends RecyclerView.ViewHolder {
        TextView latitude, longitude, address;
        LinearLayout hostpital_ll;

        public Location(View itemView) {
            super(itemView);
            latitude = (TextView) itemView.findViewById(R.id.latitude_tv);
            longitude = (TextView) itemView.findViewById(R.id.Longitude_tv);
            address = (TextView) itemView.findViewById(R.id.address_tv);
        }
    }
}
