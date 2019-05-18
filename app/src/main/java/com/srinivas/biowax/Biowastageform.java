package com.srinivas.biowax;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.srinivas.Helper.DBHelper;

import com.srinivas.Models.UploadInstall;
import com.srinivas.rest.ApiClient;
import com.srinivas.rest.ApiInterface;
import com.srinivas.utils.Utils;
import com.srinivas.validations.Validations;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Multipart;
import retrofit2.http.Part;
import retrofit2.http.Query;

public class Biowastageform extends Activity implements View.OnClickListener {
    ImageView scanning_qrcode, waste_image, myimage_back, done_img;
    public EditText waste_collection_date;
    public static EditText barcodeNumber;
    public EditText cover_color_id;
    public EditText Latitude;
    public EditText Longitude;
    public EditText driver_id;
    public EditText is_approval_required;
    public EditText approved_by;
    ProgressDialog pd;
    public EditText bag_weight_in_hcf;
    public EditText is_manual_input;
    public EditText hcf_authorized_person_name;
    String hcf_master_id, truckid, route_master_id, routes_masters_driver_id, cover_id, clicked = "not", pic = "null", confirm = "no";
    File otherImagefile2 = null;
    Uri iv_url2;
    int O_IMAGE2 = 2;
    CheckBox saveandcontinue;
    GPSTracker gps;
    String latitude, logiitude;
    TextView completed_tv;
    // private PeopleTrackerService service;
    ProgressDialog progress;
    int aNumber = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.biowastageform);
        saveandcontinue = findViewById(R.id.saveandcontinue);
        scanning_qrcode = findViewById(R.id.scanning_qrcode);
        scanning_qrcode.setOnClickListener(this);
        completed_tv = findViewById(R.id.completed_tv);
        completed_tv.setOnClickListener(this);
        waste_image = findViewById(R.id.waste_image);
        waste_image.setOnClickListener(this);
        is_manual_input = findViewById(R.id.is_manual_input);
        hcf_authorized_person_name = findViewById(R.id.hcf_authorized_person_name);
        approved_by = findViewById(R.id.approved_by);
        bag_weight_in_hcf = findViewById(R.id.bag_weight_in_hcf);
        is_approval_required = findViewById(R.id.is_approval_required);
        waste_collection_date = findViewById(R.id.waste_collection_date);
        myimage_back = findViewById(R.id.myimage_back);
        myimage_back.setOnClickListener(this);
        done_img = findViewById(R.id.done_img);
        done_img.setOnClickListener(this);
        barcodeNumber = findViewById(R.id.barcodeNumber);
        cover_color_id = findViewById(R.id.cover_color_id);
        Longitude = findViewById(R.id.longitude);
        Latitude = findViewById(R.id.Latitude);
        driver_id = findViewById(R.id.driver_id);

        SharedPreferences.Editor hcfid = getSharedPreferences("HCFID",MODE_PRIVATE).edit();
        hcfid.putString("hcfid",getIntent().getStringExtra("hcf_id"));
        hcfid.commit();
        gps = new GPSTracker(this);
        if (!gps.isGPSEnabled && !gps.isNetworkEnabled) {
            Log.d("networkd", "false");
            gps.showSettingsAlert();
        } else {
            latitude = String.valueOf(gps.getLatitude());
            logiitude = String.valueOf(gps.getLongitude());
            // Toast.makeText(getBaseContext(),latitude+" "+longitude  ,Toast.LENGTH_SHORT).show();
        }
        showcase_attendance("Confirmation For Wastage", "Did you found wastage " + " \n If YES click on OK");


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.scanning_qrcode:
                if (Validations.hasActiveInternetConnection(Biowastageform.this)) {
                    Intent barcodescanner = new Intent(Biowastageform.this, Barcodescanner.class);
                    startActivity(barcodescanner);
                } else {
                    cover_color_id.setText("");
                    ;
                    bag_weight_in_hcf.setText("");
                    hcf_authorized_person_name.setText("");

                    barcodeNumber.setText("");
                    barcodeNumber.getText().toString();

                    SharedPreferences barcodes = getSharedPreferences("Barcodes", MODE_PRIVATE);

                    String responseBody = barcodes.getString("barcodes", "");

                    System.out.println("DAd do it " + responseBody);
                    try {
                        SharedPreferences sstruck = getSharedPreferences("Login", MODE_PRIVATE);

                        truckid = sstruck.getString("truck_id", "");
                        JSONObject obj = new JSONObject(responseBody);
                        JSONArray jsonArray = obj.getJSONArray("data");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject res = jsonArray.getJSONObject(i);
                            JSONObject routes_master = res.getJSONObject("routes_master");
                            route_master_id = routes_master.getString("id");
                            JSONObject hos = res.getJSONObject("hcf_master");
                            hcf_master_id = hos.getString("id");
                            JSONArray wow = hos.getJSONArray("hcf_waste_barcodes_not_scanned");


                            System.out.println("wowow " + wow.toString());
                            Boolean allow = false;
                            for (int j = 0; j < wow.length(); j++) {

                                String sfd = wow.getString(j);
                                System.out.println("barcode_number  " + sfd);
                                JSONObject js = wow.getJSONObject(j);
                                if (js.getString("barcode_number").equals(barcodeNumber.getText().toString())) {
                                    barcodeNumber.setText(js.getString("barcode_number"));
                                    SharedPreferences ss = getSharedPreferences("Login", MODE_PRIVATE);

                                    driver_id.setText(ss.getString("driverid", ""));
                                    cover_color_id.setText(js.getString("color_name"));

                                    cover_id = js.getString("cover_color_id");
                                }

                            }

                        }
                        if (cover_color_id.getText().length() == 0) {
                            showcase2("Barcode Alert", "Barcode Already Scanned");
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                   /* barcodeNumber.setText(result.getString("barcode_number"));
                    driver_id.setText(result.getString("driver_id"));
                    cover_color_id.setText(result.getString("cover_color_id"));

                    hcf_master_id = result.getString("hcf_master_id");
                    route_master_id = result.getString("route_master_id");*/

                }

                break;

            case R.id.waste_image:

                clicked = "clicked";
                pic = "clicked";
                String root = Environment.getExternalStorageDirectory().toString();
                File myDir = new File(root + "/RecceImages/");
                myDir.mkdirs();
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                otherImagefile2 = new File(myDir,
                        String.valueOf(System.currentTimeMillis()) + ".jpg");
                iv_url2 = FileProvider.getUriForFile(getApplicationContext(),
                        getApplication().getPackageName() + ".provider", otherImagefile2);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, iv_url2);
                startActivityForResult(intent, O_IMAGE2);

               /* try {
                    uploadWasteform();
                } catch (IOException e) {
                    e.printStackTrace();
                }*/
                break;
            case R.id.myimage_back:
                finish();
                break;
            case R.id.done_img:

                if (bag_weight_in_hcf.getText().toString().length() == 0) {
                    showcase2("Form Alert", "Please Enter Weight should not be null ");
                } else {
                    done();
                  //  Toast.makeText(getBaseContext(), "Successfully Record inserted", Toast.LENGTH_SHORT).show();
                }

                break;
            case R.id.completed_tv:
                Intent garbagehistory = new Intent(Biowastageform.this, GarbageHistory.class);
                startActivity(garbagehistory);
                finish();
                break;
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();

        Biowastageform.this.runOnUiThread(new Runnable() {
            public void run() {
                if (barcodeNumber.getText().toString().length() == 0) {
                    showcase2("Form Validation ", "Scan Barcode with valid data");
                } else {
                    try {
                        pd = new ProgressDialog(Biowastageform.this);
                        pd.setMessage("Fetching Barcode Details..");
                        pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                        pd.setIndeterminate(true);
                        pd.setCancelable(false);
                        pd.show();
                        SharedPreferences hcfid = getSharedPreferences("HCFID",MODE_PRIVATE);
                        getBarcodeDetails(barcodeNumber.getText().toString()+hcfid.getString("hcfid",""));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

            }
        });

/*
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (cover_color_id.getText().toString().length() == 0) {
                    showDialog(Biowastageform.this, "Sorry This Barcode Already Scanned", "true");
                }
            }
        }, 1000);
*/

       /* barcodeNumber.setText("BARCODE-87");
        cover_color_id.setText("2");*/
      //  Toast.makeText(getBaseContext(), "Dadi Restart here ", Toast.LENGTH_SHORT).show();
    }

    public void done() {
        System.out.println("weight " + bag_weight_in_hcf.getText().toString());
        if (bag_weight_in_hcf.getText().equals("0")) {
            showcase2("Form Alert", "Weight should not be null value");
        } else if (cover_color_id.getText().toString().length() == 0) {
            showcase2("Form Alert", "Barcode not found in server");
        } else {
            try {
                uploadWasteform();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
/*        if (clicked.equals("not")) {
            String root = Environment.getExternalStorageDirectory().toString();
            File myDir = new File(root + "/RecceImages/");
            myDir.mkdirs();
            otherImagefile2 = new File(myDir,
                    String.valueOf(System.currentTimeMillis()) + ".jpg");
            FileOutputStream out = null;
            try {
                out = new FileOutputStream(otherImagefile2);
                Bitmap bmp = BitmapFactory.decodeResource(Biowastageform.this.getResources(), R.drawable.imgnoavailable);
                bmp.compress(Bitmap.CompressFormat.PNG, 100, out); // bmp is your Bitmap instance
                // PNG is a lossless format, the compression factor (100) is ignored
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    if (out != null) {
                        out.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyy HH:mm:ss");
        String millisInString = dateFormat.format(new Date());
        if (Validations.hasActiveInternetConnection(Biowastageform.this)) {
            done_img.setClickable(false);

        }else {

            done_img.setClickable(false);
            DBHelper dbHelper = new DBHelper(Biowastageform.this);
            dbHelper.insertReport(latitude, logiitude, "", millisInString,
                    millisInString, otherImagefile2.getAbsolutePath(), "local",
                    "", Biowastageform.this);
            showDialog(Biowastageform.this, "Report saved to offline, will synch automatically when internet is available", "yes");
        }*/

    }

    /*
        public PeopleTrackerService getTrackerService() {
            if (service == null)
                //Log.d("Main activity1 2389", "service.toString()");
                service = Utils.createService();
            return service;
        }
    */
/*
    public void updateInstall(String Token,
                              String DeviceID, final String MessageDescription,
                              final String Long, final String Lat,
                              String ReportedFrom, final String ReportedDateTime,
                              String DR, String MobileDeviceID,
                              final String imagepath, final String CenterID) {

        progress = new ProgressDialog(Biowastageform.this);
        progress.setMessage("Uploading Q & A To Server..");
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.setIndeterminate(true);
        progress.setCancelable(false);
        progress.show();
        service = getTrackerService();

        Double lati = Double.valueOf(Lat);
        Double longi = Double.valueOf(Long);
        retrofit2.Call<String> call = service.sendMessage("1.4", Token, DeviceID, MessageDescription, lati, longi
                , ReportedFrom, ReportedDateTime, DR, MobileDeviceID,
                getStringImage(imagepath), CenterID);

        call.enqueue(new retrofit2.Callback<String>() {
            @Override
            public void onResponse(retrofit2.Call<String> call, retrofit2.Response<String> response) {
                progress.dismiss();
                //  Log.d("response fromserver" + response.isSuccessful(), response.toString());
                if (response.isSuccessful()) {
                    try {
                        JSONObject obj = new JSONObject(response.body());
                        System.out.println("HIIIIII output see here " + obj.toString());
                        if (obj.getString("Message").equals("SUCCESS")) {
                            System.out.println("Dadi srinivasu " + obj.toString());
                            Biowastageform.this.runOnUiThread(new Runnable() {
                                public void run() {
                                    showDialog(Biowastageform.this, "Report Sent Successfully", "yes");
                                }
                            });
                        } else {
                            Toast.makeText(Biowastageform.this, "Sorry Dad", Toast.LENGTH_SHORT).show();
                            //startLocationUpdates();
                            DBHelper dbHelper = new DBHelper(Biowastageform.this);
                            dbHelper.insertReport(Lat, Long, MessageDescription.replace("'", ""), ReportedDateTime,
                                    ReportedDateTime, imagepath, "local",
                                    CenterID, Biowastageform.this);
                            Biowastageform.this.runOnUiThread(new Runnable() {
                                public void run() {
                                    showDialog(Biowastageform.this, "Internal server occurred", "yes");
                                }
                            });

                        }
                    } catch (JSONException e) {
                        DBHelper dbHelper = new DBHelper(Biowastageform.this);
                        dbHelper.insertReport(Lat, Long, MessageDescription.replace("'", ""), ReportedDateTime, ReportedDateTime, imagepath, "local",
                                CenterID, Biowastageform.this);
                        progress.dismiss();
                        Biowastageform.this.runOnUiThread(new Runnable() {
                            public void run() {
                                showDialog(Biowastageform.this, "Internal server occurred", "yes");
                            }
                        });
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(retrofit2.Call<String> call, Throwable t) {
                DBHelper dbHelper = new DBHelper(Biowastageform.this);
                dbHelper.insertReport(Lat, Long, MessageDescription.replace("'", ""), ReportedDateTime, ReportedDateTime, imagepath, "local",
                        CenterID, Biowastageform.this);
                progress.dismiss();
                showDialog(Biowastageform.this, "Internal server occurred", "yes");
                System.out.println("Dadi srinivasu error " + call.toString());
            }
        });


    }
*/
    public void uploadWasteform() throws IOException {

        final SharedPreferences ss = getSharedPreferences("Login", MODE_PRIVATE);
        // avoid creating several instances, should be singleon
        OkHttpClient client = new OkHttpClient();
/*
        MultipartBody.Part imageFilePart3 = MultipartBody.Part.createFormData("sagregation_image", otherImagefile2.getName(),
                RequestBody.create(MediaType.parse("image"), getStringImage(otherImagefile2.getAbsolutePath())));
*/

/*
        RequestBody body = new MultipartBody.Builder().setType(MultipartBody.FORM)

                .addFormDataPart("hcf_master_id", "")
                .addFormDataPart("waste_collection_date", "2018-01-29")
                .addFormDataPart("truck_id", "1")
                .addFormDataPart("route_master_id", "14")
                .addFormDataPart("barcode_number", "BARCODE-1")
                .addFormDataPart("cover_color_id", "1")
                .addFormDataPart("is_approval_required", "Yes")
                .addFormDataPart("approved_by", "1")
                .addFormDataPart("bag_weight_in_hcf", "100")
                .addFormDataPart("longitude", "13.333")
                .addFormDataPart("latitude", "83.333")
                .addFormDataPart("is_manual_input", "No")
                .addFormDataPart("hcf_authorized_person_name", "Suresh")
                .addFormDataPart("driver_id", "1")
                .addFormDataPart("driver_imei_number", "123456789")
                .addFormDataPart("is_sagregation_completed", "no")
                .addFormDataPart("sagregation_image",  "")
                .build();
*/
        Date c = Calendar.getInstance().getTime();
        System.out.println("Current time => " + c);

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        final String formattedDate = df.format(c);


        RequestBody formBody = new FormBody.Builder()
                .add("hcf_master_id", hcf_master_id)
                .add("waste_collection_date", formattedDate)
                .add("truck_id", truckid)
                .add("route_master_id", route_master_id)
                .add("barcode_number", barcodeNumber.getText().toString())
                .add("cover_color_id", cover_id)
                .add("is_approval_required", is_approval_required.getText().toString())
                .add("approved_by", "1")
                .add("bag_weight_in_hcf", bag_weight_in_hcf.getText().toString())
                .add("longitude", latitude)
                .add("latitude", logiitude)
                .add("is_manual_input", is_manual_input.getText().toString())
                .add("hcf_authorized_person_name", hcf_authorized_person_name.getText().toString())
                .add("driver_id", "1")
                .add("driver_imei_number", "123456789")
                .add("is_sagregation_completed", "no")
                .add("sagregation_image", "")
                .build();

        System.out.println("Dadi hcf_master_id " + hcf_master_id + " waste_collection_date " + formattedDate + " truck_id " + truckid +
                " routemaster_id " + route_master_id + " barcode no " + barcodeNumber.getText().toString() + " Coverid " + cover_color_id.getText().toString()
                + " isapproved required " +
                is_approval_required.getText().toString() + " approvedby " + approved_by + " bagweight " + bag_weight_in_hcf.getText().toString()
                + " latitude " + latitude + " longi " + logiitude
                + " Ismanual " + is_manual_input.getText().toString() + " hcfauthorized " + hcf_authorized_person_name.getText().toString() + " driverid = 1");
        Request request = new Request.Builder()
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer" + ss.getString("access_token", ""))
                .url("http://175.101.151.121:8001/api/addhcfwastecollectionfrommobile")
                .post(formBody)
                .build();

        if (Validations.hasActiveInternetConnection(Biowastageform.this)) {
            client.newCall(request).enqueue(new okhttp3.Callback() {
                @Override
                public void onFailure(okhttp3.Call call, IOException e) {
                    Log.d("result", e.getMessage().toString());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            // Stuff that updates the UI
                            showDialog(Biowastageform.this, "Please try again server busy at this moment", "true");
                        }
                    });
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
                            System.out.println("dadi out put here " + obj);
                            if (obj.getString("status").equals("true")) {
                                System.out.println("JONDDDd " + obj.toString());
                                Biowastageform.this.scanning_qrcode.post(new Runnable() {
                                    public void run() {
                                        aNumber = (int) ((Math.random() * 9000000) + 1000000);
                                        SharedPreferences.Editor trans = getSharedPreferences("Transaction", MODE_PRIVATE).edit();
                                        SharedPreferences ss = getSharedPreferences("Transaction", MODE_PRIVATE);
                                        System.out.println("Hoooo " + ss.getString("trans", ""));
                                        if (saveandcontinue.isChecked()) {

                                            if (!ss.getString("trans", "").equals("")) {

                                            } else {
                                                trans.putString("trans", String.valueOf(aNumber));
                                                trans.commit();
                                            }
                                        } else {
                                            trans.putString("trans", String.valueOf(aNumber));
                                            trans.commit();
                                        }
                                        DBHelper dbHelper = new DBHelper(Biowastageform.this);
                                        dbHelper.insertProject(latitude, logiitude, hcf_master_id, formattedDate, truckid, route_master_id, barcodeNumber.getText().toString()
                                                , cover_color_id.getText().toString(),
                                                is_approval_required.getText().toString()
                                                , approved_by.getText().toString(), bag_weight_in_hcf.getText().toString()
                                                , is_manual_input.getText().toString(), hcf_authorized_person_name.getText().toString()
                                                , driver_id.getText().toString(),
                                                "asdf", "asdf",
                                                ss.getString("trans", ""), "online", Biowastageform.this);

                                        runOnUiThread(new Runnable() {

                                            @Override
                                            public void run() {

                                                cover_color_id.setText("");
                                                bag_weight_in_hcf.setText("");
                                                hcf_authorized_person_name.setText("");
                                                // Stuff that updates the UI

                                            }
                                        });
                                        showDialog(Biowastageform.this, "Sucessfully uploaded..", "true");
                                    }
                                });

                                runOnUiThread(new Runnable() {

                                    @Override
                                    public void run() {

                                        cover_color_id.setText("");
                                        bag_weight_in_hcf.setText("");
                                        hcf_authorized_person_name.setText("");
                                        // Stuff that updates the UI

                                    }
                                });


                            } else {
                                DBHelper dbHelper = new DBHelper(Biowastageform.this);
                                dbHelper.insertProject(latitude, logiitude, hcf_master_id, formattedDate, truckid, route_master_id, barcodeNumber.getText().toString()
                                        , cover_color_id.getText().toString(),
                                        is_approval_required.getText().toString()
                                        , approved_by.getText().toString(), bag_weight_in_hcf.getText().toString()
                                        , is_manual_input.getText().toString(), hcf_authorized_person_name.getText().toString()
                                        , driver_id.getText().toString(),
                                        "asdf", "asdf",
                                        ss.getString("trans", ""), "local", Biowastageform.this);


                                /*System.out.println("JONDDDd " + obj.toString());
                                System.out.println("JONDDDd " + obj.getString("token"));
*/
                                Biowastageform.this.scanning_qrcode.post(new Runnable() {
                                    public void run() {
                                        showDialog(Biowastageform.this, "Record saved sucessfull in locally.", "true");
                                    }
                                });

                                runOnUiThread(new Runnable() {

                                    @Override
                                    public void run() {

                                        cover_color_id.setText("");
                                        bag_weight_in_hcf.setText("");
                                        hcf_authorized_person_name.setText("");
                                        // Stuff that updates the UI

                                    }
                                });

                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });

        } else {
            DBHelper dbHelper = new DBHelper(Biowastageform.this);
            dbHelper.insertProject(latitude, logiitude, hcf_master_id, formattedDate, truckid, route_master_id, barcodeNumber.getText().toString()
                    , cover_color_id.getText().toString(),
                    is_approval_required.getText().toString()
                    , approved_by.getText().toString(), bag_weight_in_hcf.getText().toString()
                    , is_manual_input.getText().toString(), hcf_authorized_person_name.getText().toString()
                    , driver_id.getText().toString(),
                    "asdf", "asdf",
                    ss.getString("trans", ""), "local", Biowastageform.this);
            showcase2("Form Alert", "Record saved in local Database sucessfully Thankyou !!");
        }

    }


    /*
        public void updateInstall(@Query("installation_date") final String installation_date,
                                  @Query("installation_remarks") final String installation_remarks,
                                  @Part("key") RequestBody key,
                                  @Part("user_id") RequestBody user_id, @Part("crew_person_id") RequestBody crew_person_id,
                                  @Part("recce_id") final RequestBody recce_id, @Part("project_id") final RequestBody project_id,
                                  @Part final MultipartBody.Part installation_image,
                                  @Part final MultipartBody.Part installation_image_1,
                                  @Part final MultipartBody.Part installation_image_2) {
            ApiInterface apiService = ApiClient.getSams().create(ApiInterface.class);
            Call<UploadInstall> call = apiService.getUploadInstall(installation_date, installation_remarks, key,
                    user_id, crew_person_id, recce_id, project_id, installation_image,installation_image_1,installation_image_2);
            call.enqueue(new Callback<UploadInstall>() {
                @Override
                public void onResponse(Call<UploadInstall> call, Response<UploadInstall> response) {
                    String result = String.valueOf(response.code());
                    Log.d("goodma",result+" "+offlineimgpath1);
                    Log.d("goodma",offlineimgpath2);
                    if (result.equals("200")) {
                          Toast.makeText(getBaseContext(),"successfull ",Toast.LENGTH_SHORT).show();
                        // finish();
                    } else {

                        Toast.makeText(getBaseContext(), "Please wait we are processing !!", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<UploadInstall> call, Throwable throwable) {
                      Toast.makeText(getBaseContext(), throwable.toString(), Toast.LENGTH_SHORT).show();

                    Log.d("message_image", throwable.toString());
                }
            });
        }
    */
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


    public void getBarcodeDetails(String barcodeno) throws IOException {

        final SharedPreferences ss = getSharedPreferences("Login", MODE_PRIVATE);
        // avoid creating several instances, should be singleon
        OkHttpClient client = new OkHttpClient();

        RequestBody formBody = new FormBody.Builder()
                .build();

        Request request = new Request.Builder()
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer" + ss.getString("access_token", ""))
                .url("http://175.101.151.121:8001/api/barcodedetails/" + barcodeno)
                .get()
                .build();


        client.newCall(request).enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(okhttp3.Call call, IOException e) {
                Log.d("result", e.getMessage().toString());
                e.printStackTrace();
                pd.dismiss();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // Stuff that updates the UI
                        showDialog(Biowastageform.this, "Please try again server busy at this moment", "true");

                    }
                });
            }

            @Override
            public void onResponse(okhttp3.Call call, final okhttp3.Response response) throws IOException {
                //  pd.dismiss();
                if (!response.isSuccessful()) {
                    pd.dismiss();
                    Log.d("result", response.toString());
                    throw new IOException("Unexpected code " + response);
                } else {
                    pd.dismiss();

                    Log.d("result", response.toString());
                    String responseBody = response.body().string();
                    System.out.println("Dadi " + responseBody.toString());
                    final JSONObject obj;
                    try {
                        obj = new JSONObject(responseBody);
                        if (obj.getString("status").equals("true")) {
                            Biowastageform.this.runOnUiThread(new Runnable() {
                                public void run() {
                                    System.out.println("JONDDDd " + obj.toString());
                                    JSONObject result = null;
                                    try {
                                        result = obj.getJSONObject("barcode_data");
                                        JSONObject hcf_master = result.getJSONObject("hcf_master");
                                        SharedPreferences ss = getSharedPreferences("Login", MODE_PRIVATE);

                                        truckid = ss.getString("truck_id", "");

                                        driver_id.setText(ss.getString("driverid", ""));

                                        route_master_id = ss.getString("driverid", "");
                                        hcf_master_id = result.getString("hcf_master_id");
                                        barcodeNumber.setText(result.getString("barcode_number"));


                                        JSONObject covers_color_master = result.getJSONObject("covers_color_master");
                                        cover_id = result.getString("cover_color_id");

                                        cover_color_id.setText(covers_color_master.getString("color_name"));

                                        JSONObject jsonObject = new JSONObject(ss.getString("data", "").toString());
                                        System.out.println("DADi srinivasu " + jsonObject.toString());
                                        JSONObject res = jsonObject.getJSONObject("user");

                                        JSONObject truck = res.getJSONObject("routes_masters_driver");
                                        //routes_masters_driver_id = truck.getString("id");
                                        /*if (result.getString("route_master_id") == null) {
                                            Toast.makeText(getBaseContext(), "dadi route null ", Toast.LENGTH_SHORT).show();
                                        } else {
                                            route_master_id = result.getString("route_master_id");
                                        }*/


                                        Date c = Calendar.getInstance().getTime();
                                        System.out.println("Current time => " + c);

                                        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                                        String formattedDate = df.format(c);
                                        waste_collection_date.setText(formattedDate);
                                        GPSTracker gpsTracker = new GPSTracker(Biowastageform.this);
                                        if (gpsTracker.canGetLocation) {
                                            System.out.println("loacotin update " + gpsTracker.getLatitude() + " longitude " + gpsTracker.getLongitude());
                                            String x = String.valueOf(gpsTracker.getLatitude());
                                            String xy = String.valueOf(gpsTracker.getLongitude());
                                            Latitude.setText(x);
                                            Longitude.setText(xy);
                                        }

                                        //hcf_master.getString(" facility_name");
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                }
                            });


                        } else {
                            Biowastageform.this.runOnUiThread(new Runnable() {
                                public void run() {
                                    showcase2("Barcode Alert", "Already Scanned Barcode");
                                }
                            });


                            System.out.println("else part JONDDDd " + obj.toString());

                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

    }

    private String getStringImage(String path) {
        String encodedImage = null;
        try {

            if (path != null) {
                Bitmap mBitmap = BitmapFactory.decodeFile(path);
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos); //bm is the bitmap object
                byte[] byteArrayImage = baos.toByteArray();
                encodedImage = Base64.encodeToString(byteArrayImage, Base64.DEFAULT);
            }
        } catch (Exception e) {
            Log.d("unable to read image", e.toString());
//            Toast.makeText(MainActivity1.this,"Retake picture",Toast.LENGTH_SHORT).show();
        }
        return encodedImage;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        System.out.println("Camera request code......................" + resultCode);
        if (requestCode == O_IMAGE2 && resultCode == RESULT_OK) {
            try {
                BitmapFactory.Options opt = new BitmapFactory.Options();
                opt.inSampleSize = 8;
                opt.inMutable = true;
                Bitmap bmImage = BitmapFactory.decodeFile(otherImagefile2.getPath().toString(), opt);
                waste_image.setScaleType(ImageView.ScaleType.FIT_XY);
                waste_image.setImageBitmap(bmImage);
                compressImage(otherImagefile2.getAbsolutePath().toString());
            } catch (Exception e) {
                Log.e("msg", e.getMessage());
            }
        } else {
            try {
                BitmapFactory.Options opt = new BitmapFactory.Options();
                opt.inSampleSize = 8;
                opt.inMutable = true;
                Bitmap bmImage = BitmapFactory.decodeResource(Biowastageform.this.getResources(), R.drawable.imgnoavailable);
                FileOutputStream fos = new FileOutputStream(otherImagefile2);
                bmImage.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                fos.flush();
                fos.close();
                //  Bitmap bmImage = BitmapFactory.decodeFile(otherImagefile2.getPath().toString(), opt);
                waste_image.setScaleType(ImageView.ScaleType.FIT_XY);
                waste_image.setImageBitmap(bmImage);
                compressImage(otherImagefile2.getAbsolutePath().toString());
            } catch (Exception e) {
                Log.e("msg", e.getMessage());
            }
        }
    }

    private String getRealPathFromURI(String contentURI) {
        Uri contentUri = Uri.parse(contentURI);
        Cursor cursor = getContentResolver().query(contentUri, null, null, null, null);
        if (cursor == null) {
            return contentUri.getPath();
        } else {
            cursor.moveToFirst();
            int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            return cursor.getString(index);
        }
    }

    public int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        final float totalPixels = width * height;
        final float totalReqPixelsCap = reqWidth * reqHeight * 2;
        while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
            inSampleSize++;
        }

        return inSampleSize;
    }

    public String compressImage(String imageUri) {

        String filePath = getRealPathFromURI(imageUri);
        Bitmap scaledBitmap = null;

        BitmapFactory.Options options = new BitmapFactory.Options();

//      by setting this field as true, the actual bitmap pixels are not loaded in the memory. Just the bounds are loaded. If
//      you try the use the bitmap here, you will get null.
        options.inJustDecodeBounds = true;
        Bitmap bmp = BitmapFactory.decodeFile(filePath, options);

        int actualHeight = options.outHeight;
        int actualWidth = options.outWidth;

//      max Height and width values of the compressed image is taken as 816x612

        float maxHeight = 816.0f;
        float maxWidth = 612.0f;
        float imgRatio = actualWidth / actualHeight;
        float maxRatio = maxWidth / maxHeight;

//      width and height values are set maintaining the aspect ratio of the image

        if (actualHeight > maxHeight || actualWidth > maxWidth) {
            if (imgRatio < maxRatio) {
                imgRatio = maxHeight / actualHeight;
                actualWidth = (int) (imgRatio * actualWidth);
                actualHeight = (int) maxHeight;
            } else if (imgRatio > maxRatio) {
                imgRatio = maxWidth / actualWidth;
                actualHeight = (int) (imgRatio * actualHeight);
                actualWidth = (int) maxWidth;
            } else {
                actualHeight = (int) maxHeight;
                actualWidth = (int) maxWidth;

            }
        }

//      setting inSampleSize value allows to load a scaled down version of the original image

        options.inSampleSize = calculateInSampleSize(options, actualWidth, actualHeight);

//      inJustDecodeBounds set to false to load the actual bitmap
        options.inJustDecodeBounds = false;

//      this options allow android to claim the bitmap memory if it runs low on memory
        options.inPurgeable = true;
        options.inInputShareable = true;
        options.inTempStorage = new byte[16 * 1024];

        try {
//          load the bitmap from its path
            bmp = BitmapFactory.decodeFile(filePath, options);
        } catch (OutOfMemoryError exception) {
            exception.printStackTrace();

        }
        try {
            scaledBitmap = Bitmap.createBitmap(actualWidth, actualHeight, Bitmap.Config.ARGB_8888);
        } catch (OutOfMemoryError exception) {
            exception.printStackTrace();
        }

        float ratioX = actualWidth / (float) options.outWidth;
        float ratioY = actualHeight / (float) options.outHeight;
        float middleX = actualWidth / 2.0f;
        float middleY = actualHeight / 2.0f;

        Matrix scaleMatrix = new Matrix();
        scaleMatrix.setScale(ratioX, ratioY, middleX, middleY);

        Canvas canvas = new Canvas(scaledBitmap);
        canvas.setMatrix(scaleMatrix);
        canvas.drawBitmap(bmp, middleX - bmp.getWidth() / 2, middleY - bmp.getHeight() / 2, new Paint(Paint.FILTER_BITMAP_FLAG));

//      check the rotation of the image and display it properly
        ExifInterface exif;
        try {
            exif = new ExifInterface(filePath);

            int orientation = exif.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION, 0);
            Log.d("EXIF", "Exif: " + orientation);
            Matrix matrix = new Matrix();
            if (orientation == 6) {
                matrix.postRotate(90);
                Log.d("EXIF", "Exif: " + orientation);
            } else if (orientation == 3) {
                matrix.postRotate(180);
                Log.d("EXIF", "Exif: " + orientation);
            } else if (orientation == 8) {
                matrix.postRotate(270);
                Log.d("EXIF", "Exif: " + orientation);
            }
            scaledBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0,
                    scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix,
                    true);
        } catch (IOException e) {
            e.printStackTrace();
        }

        FileOutputStream out = null;
        // String filename = getFilename();
        try {
            out = new FileOutputStream(imageUri);

//          write the compressed bitmap at the destination specified by filename.
            scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 25, out);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return imageUri;

    }

    public void showcase2(String title, String msg) {

        final AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(Biowastageform.this, android.R.style.Theme_Material_Dialog_Alert);
        } else {
            builder = new AlertDialog.Builder(Biowastageform.this);
        }
        builder.setTitle(title)
                .setMessage(msg)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // continue with delete

                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // continue with delete
                        builder.setCancelable(true);
                        finish();
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();

    }

    public void showcase_attendance(String title, String msg) {

        final AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(Biowastageform.this, android.R.style.Theme_Material_Dialog_Alert);
        } else {
            builder = new AlertDialog.Builder(Biowastageform.this);
        }
        builder.setTitle(title)
                .setMessage(msg)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // continue with delete
                        try {
                            addhcfvisitlog("yes");
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // continue with delete
                        try {
                            addhcfvisitlog("no");
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();

    }

    public void addhcfvisitlog(String intime) throws IOException {

        final SharedPreferences ss = getSharedPreferences("Login", MODE_PRIVATE);
        // avoid creating several instances, should be singleon
        OkHttpClient client = new OkHttpClient();

        Date c = Calendar.getInstance().getTime();
        System.out.println("Current time => " + c);

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String formattedDate = df.format(c);

         RequestBody formBody = new FormBody.Builder()
                .add("route_id", ss.getString("routeid",""))
                .add("hcf_id", getIntent().getStringExtra("hcfcode"))
                .add("role_type", ss.getString("rollid",""))
                .add("user_id", ss.getString("user_id", ""))
                .add("visited_time", formattedDate)
                .add("wastage_available", intime)
                .build();

        Request request = new Request.Builder()
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer" + ss.getString("access_token", ""))
                .url("http://175.101.151.121:8001/api/addhcfvisitlog")
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
                    System.out.println("Dadi this is attendance " + responseBody.toString());
                    final JSONObject obj;
                    try {
                        obj = new JSONObject(responseBody);
                        if (obj.getString("status").equals("true")) {


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
