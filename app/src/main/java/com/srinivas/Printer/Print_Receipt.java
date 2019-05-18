package com.srinivas.Printer;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.srinivas.biowax.GarbageHistory;
import com.srinivas.biowax.R;
import com.srinivas.biowax.Transaction_Adapter;
import com.srinivas.biowax.Transactions;
import com.srinivas.validations.Validations;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;

public class Print_Receipt extends Activity {
TextView trans_code_tv,weights_tv,netweight;
    ProgressDialog pd;
    String def;
    ImageView printer_img,history_back;
    Double sum=0.0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.print__receipt);
        weights_tv = findViewById(R.id.weights_tv);
        trans_code_tv = findViewById(R.id.trans_code_tv);
        printer_img = findViewById(R.id.printer_img);
        netweight = findViewById(R.id.netweight);
        def =getIntent().getStringExtra("transaction_code").toString();
        trans_code_tv.setText("Transaction Code \n "+def+"\n\n");
       // Toast.makeText(getBaseContext(),getIntent().getStringExtra("transaction_code"),Toast.LENGTH_SHORT).show();
        history_back = findViewById(R.id.history_back);
        history_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        printer_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        if (Validations.hasActiveInternetConnection(Print_Receipt.this)) {

                pd = new ProgressDialog(Print_Receipt.this);
                pd.setMessage("Generating Receipt..");
                pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                pd.setIndeterminate(true);
                pd.setCancelable(false);
                pd.show();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            getRoutes(getIntent().getStringExtra("transaction_code"));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                },1000);



        } else {
            Toast.makeText(getBaseContext(),"Please check your internet connection",Toast.LENGTH_SHORT).show();
            //  getcheckins_from_local();
        }
    }

    public void getRoutes(String xx) throws IOException {
       System.out.println("See here "+def);
        SharedPreferences ss = getSharedPreferences("Login", MODE_PRIVATE);
        // avoid creating several instances, should be singleon
        OkHttpClient client = new OkHttpClient();

          Request request = new Request.Builder()
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer" + ss.getString("access_token", ""))
                //.url("http://175.101.151.121:8001/api/hcfwastecollectionviewformobile/EVB/TRANSACTION/35.1")
                 .url("http://175.101.151.121:8001/api/hcfwastecollectionviewformobile/"+xx.toString())
                .get()
                .build();


        client.newCall(request).enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(okhttp3.Call call, IOException e) {
                Log.d("result", e.getMessage().toString());
                e.printStackTrace();
                pd.dismiss();
            }

            @Override
            public void onResponse(okhttp3.Call call,okhttp3.Response response) throws IOException {
                //  pd.dismiss();
                if (!response.isSuccessful()) {
                    pd.dismiss();
                    Log.d("result", response.toString());
                    throw new IOException("Unexpected code " + response);
                } else {
                    pd.dismiss();
                    Log.d("result", response.toString());
                    String responseBody = response.body().string();
                    final JSONObject obj;
                    try {
                        obj = new JSONObject(responseBody);
                        if (obj.getString("status").equals("true")) {
                            System.out.println("hcfwastecollectiondataformobile " + obj.toString());
                            String vall = "";

                            JSONArray jsonArray = obj.getJSONArray("data");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject res = jsonArray.getJSONObject(i);
                                String x = String.valueOf(i+1);
                                vall = vall + "\n "+"Weight_"+x+"\t\t : \t"+res.getString("bag_weight_in_hcf")+".00";
                                int val_t = (int) res.getDouble("bag_weight_in_hcf");
                                sum = sum+ val_t;
                             }
                            final String finalVall = vall;
                            runOnUiThread(new Runnable() {

                                @Override
                                public void run() {
                                 //  Toast.makeText(getBaseContext(),finalVall,Toast.LENGTH_SHORT).show();
                                    weights_tv.setText(finalVall);
                                    String xxx = String.valueOf(sum);
                                    netweight.setText("Total Net Weight :"+xxx);
                                }
                            });


                        } else {
                            System.out.println("JONDDDd " + obj.toString());
                            System.out.println("JONDDDd " + obj.getString("token"));

                         }


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

    }

}
