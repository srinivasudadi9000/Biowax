package com.srinivas.utils;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.BatteryManager;
import android.os.Build;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;


import com.srinivas.biowax.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

/**
 * Created by RajeshT on 21-11-2017.
 */

public class Utils {

    private static SharedPreferences sharedPreferences;

    public static void nwDialog(final Activity activity) {
        AlertDialog alertDialog;
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setCancelable(false);
        builder.setTitle(activity.getString(R.string.no_network_title));
        builder.setMessage(activity.getString(R.string.no_network_msg));
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                //activity.finish();
                dialog.dismiss();
            }
        });
        alertDialog = builder.create();
        if (alertDialog == null || !alertDialog.isShowing())
            alertDialog.show();
    }

    public static void startStopTrackerDialog(final Activity activity, String message) {
        AlertDialog alertDialog;
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setCancelable(false);
        builder.setTitle(activity.getString(R.string.app_name));
        builder.setMessage(message);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                //activity.finish();
                dialog.dismiss();
            }
        });
        alertDialog = builder.create();
        if (alertDialog == null || !alertDialog.isShowing())
            alertDialog.show();
    }


    static AlertDialog alertDialog = null;

    public static String latLongtoAddress(Context activity, double latitude, double longitude) {
        Geocoder geocoder;
        List<Address> addresses = null;
        String address = "";
        geocoder = new Geocoder(activity, Locale.getDefault());

        try {
            addresses = geocoder.getFromLocation(latitude, longitude, 1);
            System.out.println("dadi dadi .. " + addresses.toString());
            // Here 1 represent max location result to returned, by documents it recommended 1 to 5
            if (addresses != null && addresses.size() > 0) {
                // address = addresses.get(0).getAddressLine(0) + "dadione " + addresses.get(0).getAddressLine(1);
                // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                // address = addresses.toString();


                String[] addressList = new String[addresses.size()];
                int j = 0;
                for (Address address1 : addresses) {
                    ArrayList<String> addressInfo = new ArrayList<>();
                    for (int i = 0; i <= address1.getMaxAddressLineIndex(); i++) {
                        System.out.println("srinivasu " + address1.getAddressLine(i));
                        addressInfo.add(address1.getAddressLine(i));
                    }
                    addressList[j] = TextUtils.join(System.getProperty("line.separator"),
                            addressInfo);
                    Log.d("IDENTIFIER", addressList[j]);
                    address = address + "\n" + addressList[j].toString();
                    j++;
                }
                System.out.println("max addressw dadi " + address);
            } else {
              //  address = "Unknown";
                address = "";
                System.out.println("max address dadi " + address);
            }
        } catch (IOException e) {
            e.printStackTrace();
           // address = "Unknown";
            address = "";
        }
        Log.d("Geocoder", address);
        return address;
    }

    public static List<Address> latLongtoAddress_list(Context activity, double latitude, double longitude) {
        Geocoder geocoder;
        List<Address> addresses = null;
        String address;
        geocoder = new Geocoder(activity, Locale.getDefault());

        try {
            addresses = geocoder.getFromLocation(latitude, longitude, 5);
            System.out.println("dadi dadi .. " + addresses.toString());
            // Here 1 represent max location result to returned, by documents it recommended 1 to 5
            if (addresses != null && addresses.size() > 0) {
                address = addresses.get(0).getAddressLine(0) + "dadione " + addresses.get(0).getAddressLine(1);
                // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                // address = addresses.toString();

                String[] addressList = new String[addresses.size()];
                int j = 0;
                for (Address address1 : addresses) {
                    ArrayList<String> addressInfo = new ArrayList<>();
                    for (int i = 0; i <= address1.getMaxAddressLineIndex(); i++) {
                        addressInfo.add(address1.getAddressLine(i));
                    }
                    addressList[j] = TextUtils.join(System.getProperty("line.separator"),
                            addressInfo);
                    Log.d("IDENTIFIER", addressList[j]);
                    j++;
                }
                System.out.println("max addressw dadi " + address);
            } else {
                address = "Unknown";
                System.out.println("max address dadi " + address);
            }
        } catch (IOException e) {
            e.printStackTrace();
            address = "Unknown";
        }
        Log.d("Geocoder", address);
        return addresses;
    }

    public static class GeoCoderAsync extends AsyncTask<Void, Void, String> {

        Geocoder geocoder;
        List<Address> addresses = null;
        String address;
        //geocoder = new Geocoder(activity, Locale.getDefault());
        Context activity;
        Double latitude, longitude;

        public GeoCoderAsync(Context _activity, double _latitude, double _longitude) {
            this.activity = _activity;
            this.latitude = _latitude;
            this.longitude = _longitude;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            geocoder = new Geocoder(activity, Locale.getDefault());
        }

        @Override
        protected String doInBackground(Void... voids) {
            try {
                addresses = geocoder.getFromLocation(latitude, longitude, 5);
                // Here 1 represent max location result to returned, by documents it recommended 1 to 5
                if (addresses != null && addresses.size() > 0)
                    address = addresses.get(0).getAddressLine(0) + addresses.get(0).getAddressLine(1); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                else
                    address = "Unknown";
            } catch (IOException e) {
                e.printStackTrace();
                address = "Unknown";
            }
            Log.d("Geocoder", address);
            return address;
        }

        @Override
        protected void onPostExecute(String address) {
            super.onPostExecute(address);
            AddressFromLatLong(address);
        }
    }

    public static String AddressFromLatLong(String geoAddress) {
        return geoAddress;
    }


    public static String getCurrentDate() {
        Calendar c = Calendar.getInstance();
        System.out.println("Current time => " + c.getTime());
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        String formattedDate = df.format(c.getTime());
        Log.d("Current Date", formattedDate);
        return formattedDate;
    }

    public static String getCurrentDateTime(Date date) {
        //Calendar c = Calendar.getInstance();
        //System.out.println("Current time => " + c.getTime());
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        String formattedDate = df.format(date);
        Log.d("Current DateTime", formattedDate);
        return formattedDate;
    }


    //IgnoreCase and get values from response parameters
    public static String getIgnoreCase(JSONObject jobj, String key) throws JSONException {
        Iterator<String> iter = jobj.keys();
        while (iter.hasNext()) {
            String key1 = iter.next();
            if (key1.equalsIgnoreCase(key)) {
                try {

                    return (String) jobj.get(key1);
                } catch (Exception e) {

                    System.out.println(jobj.get(key1));
                    System.out.println(e.toString());
                }
                return (String) jobj.get(key1);
            }
        }
        return null;
    }

    //IgnoreCase and get values from response parameters
    public static int getInt(JSONObject jobj, String key) throws JSONException {
        Iterator<String> iter = jobj.keys();
        while (iter.hasNext()) {
            String key1 = iter.next();
            if (key1.equalsIgnoreCase(key)) {
                try {


                    return (Integer) jobj.get(key1);
                } catch (Exception e) {

                    System.out.println(jobj.get(key1));
                    System.out.println(e.toString());
                }
                return (Integer) jobj.get(key1);
            }
        }
        return -1;
    }

    @TargetApi(Build.VERSION_CODES.ECLAIR)
    private double getBatteryLevel(Context context) {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.ECLAIR) {
            Intent batteryIntent = context.registerReceiver(null, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
            int level = batteryIntent.getIntExtra(BatteryManager.EXTRA_LEVEL, 0);
            int scale = batteryIntent.getIntExtra(BatteryManager.EXTRA_SCALE, 1);
            return (level * 100.0) / scale;
        } else {
            return 0;
        }
    }

    public static String getDeviceName() {
        String deviceName = Build.MANUFACTURER
                + " " + Build.MODEL + " " + Build.VERSION.RELEASE
                + " " + Build.VERSION_CODES.class.getFields()[Build.VERSION.SDK_INT].getName();
        return deviceName;
    }

    public static String getAndroidID(Context context) {
        String androidId = Settings.Secure.getString(context.getContentResolver(),
                Settings.Secure.ANDROID_ID);
        return androidId;
    }
    public static String getVersion(Context context) {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPreferences.getString("version", "0");
    }

 }
