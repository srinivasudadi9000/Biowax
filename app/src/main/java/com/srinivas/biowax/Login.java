package com.srinivas.biowax;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class Login extends Activity implements View.OnClickListener {
    Button login_btn;
    ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        login_btn = findViewById(R.id.login_btn);
        login_btn.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login_btn:
                pd = new ProgressDialog(this);
                pd.setMessage("Authenticating Account..");
                pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                pd.setIndeterminate(true);
                pd.setCancelable(false);
                pd.show();
                try {
                    Getlogin();
                } catch (IOException e) {
                    e.printStackTrace();
                }
               /* Intent dashboard = new Intent(Login.this, Dashboard.class);
                startActivity(dashboard);*/
                //    "message": "Unauthenticated."
                break;
        }
    }


    public void Getlogin() throws IOException {

        // avoid creating several instances, should be singleon
        OkHttpClient client = new OkHttpClient();


        RequestBody formBody = new FormBody.Builder()
                .add("mobile_imei_number", "911637752174844")
                .add("password", "demo@biowax.com")
                /*.add("UserEmail", email_et.getText().toString())
                .add("password", password_tv.getText().toString())*/
                .build();
        Request request = new Request.Builder()
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .url("http://175.101.151.121:8001/api/mobilelogin?")
                .post(formBody)
                .build();


        client.newCall(request).enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(okhttp3.Call call, IOException e) {
                Log.d("result", e.getMessage().toString());
                e.printStackTrace();
                pd.dismiss();
            }

            @Override
            public void onResponse(okhttp3.Call call, final okhttp3.Response response) throws IOException {
                pd.dismiss();
                if (!response.isSuccessful()) {
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
                            System.out.println("JONDDDd " + obj.toString());
                         /*   Login.this.login_btn.post(new Runnable() {
                                public void run() {
                                    try {
                                        showDialog(Login.this, obj.getString("user").toString(), "true");
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            });*/
                            SharedPreferences.Editor ss = getSharedPreferences("Login",MODE_PRIVATE).edit();
                            ss.putString("access_token",obj.getString("access_token"));
                            ss.putString("data",obj.toString());
                            ss.commit();
                            Intent dashboard = new Intent(Login.this, Dashboard.class);
                            startActivity(dashboard);
                        } else {
                            System.out.println("JONDDDd " + obj.toString());
                            System.out.println("JONDDDd " + obj.getString("token"));

                            Login.this.login_btn.post(new Runnable() {
                                public void run() {
                                    showDialog(Login.this, "Unauthorized user", "true");
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


}
