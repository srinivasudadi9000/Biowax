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

    ArrayList<Hospitals> checkins;
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

        checkins = new ArrayList<Hospitals>();
        checkins.add(new Hospitals("BARCODE-84", "20", 17.701001, 82.998878, "visakhapatnam"));
        checkins.add(new Hospitals("BARCODE-85", "21", 17.704581, 82.997954, "visakhapatnam"));
        checkins.add(new Hospitals("BARCODE-86", "22", 17.702920, 82.998180, "visakhapatnam"));
        checkins.add(new Hospitals("BARCODE-87", "23", 17.700445, 82.997803, "visakhapatnam"));
        checkins.add(new Hospitals("BARCODE-88", "24", 17.6998393, 82.9986091, "visakhapatnam"));

        hospitals_adapter = new Histroy_Adapter(checkins, R.layout.history_single, getApplicationContext());
        hospitalb_rv.setAdapter(hospitals_adapter);
      /*  try {
            getRoutes();
        } catch (IOException e) {
            e.printStackTrace();
        }*/

    }


    public void getRoutes() throws IOException {

        SharedPreferences ss = getSharedPreferences("Login", MODE_PRIVATE);
        // avoid creating several instances, should be singleon
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer" + ss.getString("access_token", ""))
                .url("http://175.101.151.121:8001/api/routesscheduleformobile")
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
                            System.out.println("JONDDDd " + obj.toString());

                            JSONArray jsonArray = obj.getJSONArray("data");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject res = jsonArray.getJSONObject(i);
                                JSONObject hos = res.getJSONObject("hcf_master");
                                checkins.add(new Hospitals(hos.getString("facility_name"),
                                        hos.getString("hcf_unique_code"),
                                        Double.valueOf(hos.getString("hcf_longitude")),
                                        Double.valueOf(hos.getString("hcf_lattitudue"))
                                        , hos.getString("hcf_address")));
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

                        hospitals_adapter = new Histroy_Adapter(checkins, R.layout.history_single, getApplicationContext());
                        hospitalb_rv.setAdapter(hospitals_adapter);
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
