package com.srinivas.biowax;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.OkHttpClient;
import okhttp3.Request;

import static android.content.ContentValues.TAG;

public class GarbageHistory extends Activity {
    RecyclerView hospitalb_rv;

    ArrayList<Hospitalshistory> hospitalshistories;
    Histroy_Adapter hospitals_adapter, hospitals_adapter2;
    Handler handler;
    private Runnable mRunnable;
    TextView latitude_tv, longitude_tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.garbage_history);
        hospitalb_rv = findViewById(R.id.hospitalb_rv);
        hospitalb_rv.setLayoutManager(new LinearLayoutManager(this));
        longitude_tv = findViewById(R.id.longitude_tv);
        latitude_tv = findViewById(R.id.latitude_tv);

        hospitalshistories = new ArrayList<Hospitalshistory>();
        hospitals_adapter = new Histroy_Adapter(hospitalshistories, R.layout.history_single, getApplicationContext());
        hospitalb_rv.setAdapter(hospitals_adapter);
        try {
            getRoutes();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    public void getRoutes() throws IOException {

        SharedPreferences ss = getSharedPreferences("Login", MODE_PRIVATE);
        // avoid creating several instances, should be singleon
        OkHttpClient client = new OkHttpClient();

        final Request request = new Request.Builder()
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer" + ss.getString("access_token", ""))
                .url("http://175.101.151.121:8001/api/hcfwastecollectiondataformobile")
                .get()
                .build();


        client.newCall(request).enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(okhttp3.Call call, IOException e) {
                Log.d("result", e.getMessage().toString());
                e.printStackTrace();
            }

            @Override
            public void onResponse(okhttp3.Call call, final okhttp3.Response response) throws IOException {
                //  pd.dismiss();
                if (!response.isSuccessful()) {
                    Log.d("result", response.toString());
                    throw new IOException("Unexpected code " + response);
                } else {
                    Log.d("result", response.toString());
                    String responseBody = response.body().string();
                    final JSONObject obj;
                    try {
                        obj = new JSONObject(responseBody);
                        if (obj.getString("status").equals("true")) {
                            System.out.println("hcfwastecollectiondataformobile " + obj.toString());

                            JSONArray jsonArray = obj.getJSONArray("data");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject res = jsonArray.getJSONObject(i);
                                hospitalshistories.add(new Hospitalshistory(res.getString("hcf_master_id"),
                                        res.getString("waste_collection_date"),
                                        res.getString("barcode_number"),
                                        res.getString("transaction_code")
                                        , res.getString("cover_color_id"),
                                        res.getString("is_approval_required"),
                                        res.getString("approved_by"),
                                        res.getString("bag_weight_in_hcf"),
                                        res.getString("is_manual_input"),
                                        res.getString("hcf_authorized_person_name"),
                                        res.getString("is_sagregation_completed"),
                                        res.getString("sagregation_image")));
                            }


                        } else {
                            System.out.println("JONDDDd " + obj.toString());
                            System.out.println("JONDDDd " + obj.getString("token"));

                            GarbageHistory.this.longitude_tv.post(new Runnable() {
                                public void run() {
                                    showDialog(GarbageHistory.this, "Invalid Response..", "true");
                                }
                            });
                        }

                        runOnUiThread(new Runnable() {

                            @Override
                            public void run() {

                                // Stuff that updates the UI
                                hospitals_adapter = new Histroy_Adapter(hospitalshistories, R.layout.history_single, getApplicationContext());
                                hospitalb_rv.setAdapter(hospitals_adapter);
                            }
                        });

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

    }

    public void showDialog(Activity activity, String msg, final String status) {
        final Dialog dialog = new Dialog(activity, R.style.PauseDialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.custom_dialog);

        TextView text = dialog.findViewById(R.id.text_dialog);
        text.setText(msg);

        ImageView b = dialog.findViewById(R.id.b);

        Button dialogButton = dialog.findViewById(R.id.btn_dialog);
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
            }
        });
        dialog.show();
    }


}
