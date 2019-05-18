package com.srinivas.biowax;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.srinivas.Helper.DBHelper;
import com.srinivas.Spin.LoadingSpin;
import com.srinivas.validations.IntentIntegrator;
import com.srinivas.validations.Validations;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

import static android.content.ContentValues.TAG;
import static android.telephony.MbmsDownloadSession.RESULT_CANCELLED;

public class Login extends Activity implements View.OnClickListener {
    Button login_btn;
    ProgressDialog pd;
    LoadingSpin login;
    TelephonyManager manager;
    String imenumber1 = null, imenumber2 = null;
    EditText password_et;

    @SuppressLint({"MissingPermission", "NewApi"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        login_btn = findViewById(R.id.login_btn);
        login_btn.setOnClickListener(this);
        login = findViewById(R.id.spinn);
        password_et = findViewById(R.id.password_et);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.READ_PHONE_STATE}, 0);
        } else {
            TelephonyManager mTelephony = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
            imenumber1 = mTelephony.getDeviceId();

           // Toast.makeText(getBaseContext(), imenumber1, Toast.LENGTH_SHORT).show();
        }
        DBHelper dbHelper = new DBHelper(Login.this);
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Log.v(TAG, "Permission: " + permissions[0] + "was " + grantResults[0]);
            //resume tasks needing this permission
            TelephonyManager mTelephony = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
            imenumber1 = mTelephony.getDeviceId();
          //  Toast.makeText(getBaseContext(), imenumber1, Toast.LENGTH_SHORT).show();

        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        System.out.println("loginqr"+requestCode);
        System.out.println("loginqr"+resultCode);
        System.out.println("loginqr"+data);
        String contentss = data.getStringExtra("SCAN_RESULT");
        System.out.println("Srinivasu see hre "+contentss);
        if (data != null){
            String contents = data.getStringExtra("SCAN_RESULT");
            System.out.println("Srinivasu see hre "+contents);
        }
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login_btn:
                if (password_et.getText().length() == 0) {
                    Toast.makeText(getBaseContext(), "Please enter password", Toast.LENGTH_SHORT).show();
                } else if (password_et.getText().length() < 4) {
                    Toast.makeText(getBaseContext(), "Please check password should be greater than 4 characters", Toast.LENGTH_SHORT).show();
                } else {
                    pd = new ProgressDialog(this);
                    pd.setMessage("Authenticating Account..");
                    pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                    pd.setIndeterminate(true);
                    pd.setCancelable(false);
                    // pd.show();
                    if (Validations.hasActiveInternetConnection(Login.this)) {
                        login.setVisibility(View.VISIBLE);
                        try {
                            GetValidation();

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                      /*  IntentIntegrator integrator = new IntentIntegrator(Login.this);
                        integrator.initiateScan();*/
                    } else {
                        Toast.makeText(getBaseContext(), "Please check network connection", Toast.LENGTH_SHORT).show();
                    }
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
                 .add("mobile_imei_number", "865687032199968")
                 //.add("mobile_imei_number", imenumber1.toString())
                 //.add("password", "test@123")
                  .add("password", password_et.getText().toString())
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
                //login.setVisibility(View.GONE);
                Log.d("result dadi", e.getMessage().toString());
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        login.setVisibility(View.GONE);
                        // Stuff that updates the UI
                        Toast.makeText(getBaseContext(), "IMEI Number or password doesnt exist", Toast.LENGTH_SHORT).show();
                    }
                });

                //pd.dismiss();
            }

            @Override
            public void onResponse(okhttp3.Call call, final okhttp3.Response response) throws IOException {
                // pd.dismiss();
                if (!response.isSuccessful()) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            login.setVisibility(View.GONE);
                            // Stuff that updates the UI
                            Toast.makeText(getBaseContext(), "IMEI Number or password doesnt exist", Toast.LENGTH_SHORT).show();
                            showDialog(Login.this, "IMEI Number or password doesnt exist Please Contact Admin IMEI Number: " + imenumber1 + " Thankyou", "true");
                        }
                    });

                    Log.d("result dadi", response.toString());
                    throw new IOException("Unexpected code " + response);
                } else {
                    //  pd.dismiss();
                    Log.d("result", response.toString());
                    String responseBody = response.body().string();
                    final JSONObject obj;
                    try {
                        obj = new JSONObject(responseBody);
                        if (obj.getString("status").equals("true")) {
                            System.out.println("JONDDDd " + obj.toString());
                            JSONObject user = obj.getJSONObject("user");

                            if (!user.getString("routes_masters_driver").equals("null")){
                                JSONObject routes_masters_driver = user.getJSONObject("routes_masters_driver");
                                JSONObject user1 = user.getJSONObject("user");

                                String driverid = routes_masters_driver.getString("id");

                                String truck_id = routes_masters_driver.getString("truck_id");

                                SharedPreferences.Editor ss = getSharedPreferences("Login", MODE_PRIVATE).edit();
                                ss.putString("type",user.getString("employee_type"));
                                ss.putString("user_id",user.getString("user_id"));
                                ss.putString("access_token", obj.getString("access_token"));
                                ss.putString("driverid", driverid);
                                ss.putString("truck_id", truck_id);
                                ss.putString("routeid",routes_masters_driver.getString("route_number"));
                                ss.putString("rollid",user1.getString("role_id"));
                                ss.putString("data", obj.toString());
                                ss.commit();

                                if (user.getString("employee_type").equals("Driver")){
                                    Intent dashboard = new Intent(Login.this, Dashboard_Agent.class);
                                    startActivity(dashboard);
                                    finish();
                                }else {
                                    Intent dashboard = new Intent(Login.this, Dashboard.class);
                                    startActivity(dashboard);
                                    finish();
                                }
                            }else {
                                Login.this.login_btn.post(new Runnable() {
                                    public void run() {
                                        showDialog(Login.this, "No Route's Assigned for this account", "true");
                                    }
                                });
                            }

                        } else {
                            System.out.println("JONDDDd " + obj.toString());
                            System.out.println("JONDDDd " + obj.getString("token"));

                            Login.this.login_btn.post(new Runnable() {
                                public void run() {
                                    showDialog(Login.this, "IMEI Number or password doesnt exist", "true");
                                }
                            });
                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                 // Stuff that updates the UI
                                Toast.makeText(getBaseContext(), "IMEI Number or password doesnt exist", Toast.LENGTH_SHORT).show();
                            }
                        });


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

    public void GetValidation() throws IOException {

        //https://docs.google.com/spreadsheets/d/1BWpOo_O_mBVV99TPdt5QdN7qzXlVVrbaE7dTQS3QhUs/edit#gid=0
        //emergencymail045@gmail.com password :  wadahell@123
        // avoid creating several instances, should be singleon
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .url("https://script.google.com/macros/s/AKfycbxC_st1dCob-DKbdwwnObhFFhr2KIMWJYk_XasQy87uYKQ_JQA/exec?" +
                        "id=1BWpOo_O_mBVV99TPdt5QdN7qzXlVVrbaE7dTQS3QhUs&sheet=Biowax")
                .get()
                .build();


        client.newCall(request).enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(okhttp3.Call call, IOException e) {
                //login.setVisibility(View.GONE);
                Log.d("result dadi", e.getMessage().toString());
                e.printStackTrace();
                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        Toast.makeText(getBaseContext(), "IMEI Number or password doesnt exist", Toast.LENGTH_SHORT).show();

                    }
                });

                //pd.dismiss();
            }

            @Override
            public void onResponse(okhttp3.Call call, final okhttp3.Response response) throws IOException {
                // pd.dismiss();
                if (!response.isSuccessful()) {

                    Log.d("result dadi", response.toString());
                    throw new IOException("Unexpected code " + response);
                } else {
                    //  pd.dismiss();
                    Log.d("result", response.toString());
                    String responseBody = response.body().string();
                    final JSONObject obj;
                    try {
                        JSONObject js = new JSONObject(responseBody.toString());
                        JSONArray records = new JSONArray(js.getString("records"));
                        Boolean result = false;
                        for (int i = 0; i < records.length(); i++) {
                            JSONObject res = records.getJSONObject(i);
                            System.out.println(res.toString());
                            if (res.getString("Facultyid").equals("admin2")) {
                                result = true;
                                break;
                            } else {
                                result = false;
                            }
                        }
                        if (result) {
                            runOnUiThread(new Runnable() {

                                @Override
                                public void run() {
                                    // Toast.makeText(getBaseContext(), "User Already Registered", Toast.LENGTH_SHORT).show();
                                    // Stuff that updates the UI
                                    //showDialog(Login.this, "Sucessfully Login in your account ", "wow");
                                    try {
                                        Getlogin();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }

                                }
                            });

                        } else {
                            runOnUiThread(new Runnable() {

                                @Override
                                public void run() {
                                    //Toast.makeText(getBaseContext(), "Password Incorrect !!", Toast.LENGTH_SHORT).show();
                                    showDialog(Login.this, "Password Incorrect !! ", "yes");
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

}
