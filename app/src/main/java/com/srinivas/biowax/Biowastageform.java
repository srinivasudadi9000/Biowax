package com.srinivas.biowax;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class Biowastageform extends Activity implements View.OnClickListener {
    ImageView scanning_qrcode, waste_image, myimage_back;
    public static EditText waste_collection_date, barcodeNumber, cover_color_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.biowastageform);

        scanning_qrcode = findViewById(R.id.scanning_qrcode);
        scanning_qrcode.setOnClickListener(this);
        waste_image = findViewById(R.id.waste_image);
        waste_image.setOnClickListener(this);
        waste_collection_date = findViewById(R.id.waste_collection_date);
        myimage_back = findViewById(R.id.myimage_back);
        myimage_back.setOnClickListener(this);
        barcodeNumber = findViewById(R.id.barcodeNumber);
        cover_color_id = findViewById(R.id.cover_color_id);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.scanning_qrcode:
                Intent barcodescanner = new Intent(Biowastageform.this, Barcodescanner.class);
                startActivity(barcodescanner);
                break;

            case R.id.waste_image:
                try {
                    uploadWasteform();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.myimage_back:
                finish();
                break;
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
       /* try {
            getBarcodeDetails();
        } catch (IOException e) {
            e.printStackTrace();
        }*/
        barcodeNumber.setText("BARCODE-87");
        cover_color_id.setText("2");
        Toast.makeText(getBaseContext(), "Dadi Restart here ", Toast.LENGTH_SHORT).show();
    }

    public void uploadWasteform() throws IOException {

        SharedPreferences ss = getSharedPreferences("Login", MODE_PRIVATE);
        // avoid creating several instances, should be singleon
        OkHttpClient client = new OkHttpClient();

        RequestBody formBody = new FormBody.Builder()
                .add("hcf_master_id", "5")
                .add("waste_collection_date", "2018-01-29")
                .add("truck_id", "1")
                .add("route_master_id", "14")
                .add("barcode_number", "BARCODE-88")
                .add("cover_color_id", "1")
                .add("is_approval_required", "Yes")
                .add("approved_by", "1")
                .add("bag_weight_in_hcf", "100")
                .add("longitude", "13.333")
                .add("latitude", "83.333")
                .add("is_manual_input", "No")
                .add("hcf_authorized_person_name", "Suresh")
                .add("driver_id", "1")
                .add("driver_imei_number", "123456789")
                .add("is_sagregation_completed", "no")
                .add("sagregation_image", "")
                .build();

        Request request = new Request.Builder()
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer" + ss.getString("access_token", ""))
                .url("http://175.101.151.121:8001/api/addhcfwastecollectionfrommobile")
                .post(formBody)
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
                            Biowastageform.this.scanning_qrcode.post(new Runnable() {
                                public void run() {
                                    showDialog(Biowastageform.this, "Invalid Response..", "true");
                                }
                            });
                        } else {
                            System.out.println("JONDDDd " + obj.toString());
                            System.out.println("JONDDDd " + obj.getString("token"));

                            Biowastageform.this.scanning_qrcode.post(new Runnable() {
                                public void run() {
                                    showDialog(Biowastageform.this, "Invalid Response..", "true");
                                }
                            });
                        }


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


    public void getBarcodeDetails() throws IOException {

        SharedPreferences ss = getSharedPreferences("Login", MODE_PRIVATE);
        // avoid creating several instances, should be singleon
        OkHttpClient client = new OkHttpClient();

        RequestBody formBody = new FormBody.Builder()
                .build();

        Request request = new Request.Builder()
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer" + ss.getString("access_token", ""))
                .url("http://175.101.151.121:8001/api/barcodedetails/barcode-" + 87)
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
                    System.out.println("Dadi " + responseBody.toString());
                    JSONObject obj;
                    try {
                        obj = new JSONObject(responseBody);
                        if (obj.getString("status").equals("true")) {
                            System.out.println("JONDDDd " + obj.toString());
                            JSONObject result = obj.getJSONObject("barcode_data");
                            barcodeNumber.setText(result.getString("barcode_number"));
                            cover_color_id.setText(result.getString("cover_color_id"));
                            // waste_collection_date.setText(obj.toString());
                        } else {
                            System.out.println("else part JONDDDd " + obj.toString());

                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

    }

}
