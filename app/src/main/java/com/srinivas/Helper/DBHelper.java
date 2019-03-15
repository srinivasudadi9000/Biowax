package com.srinivas.Helper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class DBHelper {
    static SQLiteDatabase db;
    static Context context;

    public DBHelper(Context context) {

        db = context.openOrCreateDatabase("RMAT", Context.MODE_PRIVATE, null);
        db.execSQL("CREATE TABLE IF NOT EXISTS checktimes(latitude VARCHAR,longitude VARCHAR,cdt VARCHAR,address VARCHAR," +
                "deviceid VARCHAR,deviceno VARCHAR,status VARCHAR,run VARCHAR);");
        db.execSQL("CREATE TABLE IF NOT EXISTS messagess(latitude VARCHAR,longitude VARCHAR,msg VARCHAR,cdt VARCHAR,status VARCHAR);");
        db.execSQL("CREATE TABLE IF NOT EXISTS dailyreportss(latitude VARCHAR,longitude VARCHAR,hcf_master_id VARCHAR,waste_collection_date VARCHAR," +
                "truck_id VARCHAR," +
                "route_master_id VARCHAR,barcode_number VARCHAR,cover_color_id VARCHAR,is_approval_required VARCHAR," +
                "approved_by VARCHAR,bag_weight_in_hcf VARCHAR,is_manual_input VARCHAR,hcf_authorized_person_name VARCHAR," +
                "driver_id VARCHAR,is_sagregation_completed VARCHAR,sagregation_image VARCHAR,transno VARCHAR);");
        //db.execSQL("CREATE TABLE IF NOT EXISTS questions(centers VARCHAR,params VARCHAR);");
        if (db.isOpen()) {
            db.close();
        }
    }

    public DBHelper() {

    }

    public void insertquestions(String centers, String params, Context context) {

        db = context.openOrCreateDatabase("RMAT", Context.MODE_PRIVATE, null);
        DBHelper.context = context;
        db.execSQL("INSERT INTO questions VALUES('" + centers + "','" + params + "');");
        db.close();
        Log.d("dbcreated_insert", "insertedsucess");
    }

    public void insertReport(String latitude, String longitude, String message, String cdt,
                             String rep_date, String imagepath, String status, String CenterID, Context context) {
        db = context.openOrCreateDatabase("RMAT", Context.MODE_PRIVATE, null);
        DBHelper.context = context;
        db.execSQL("INSERT INTO dailyreportss VALUES('" + latitude + "','" + longitude + "','" +
                message + "','" + cdt + "','" + rep_date + "','" + imagepath + "','" + status + "','" + CenterID + "');");
        db.close();
        Log.d("dbcreated_insert", "insertedsucess");
    }

    public void updateDailyReport(String status, String cdt, Context context) {
        db = context.openOrCreateDatabase("RMAT", Context.MODE_PRIVATE, null);
        String strSQL = "UPDATE dailyreportss SET status = '" + status + "' WHERE cdt = '" + cdt + "'";
        db.execSQL(strSQL);

        db.close();
        Log.d("checkupdated", "checkupdatedsuccessfully");
    }

    public void insertProject(String latitude,String longitude,String hcf_master_id ,String waste_collection_date ,
            String truck_id ,String route_master_id, String barcode_number,String cover_color_id,String is_approval_required ,
                              String approved_by ,String bag_weight_in_hcf ,String is_manual_input ,String hcf_authorized_person_name ,
                              String driver_id ,String is_sagregation_completed ,String sagregation_image,String transno , Context context) {
        db = context.openOrCreateDatabase("RMAT", Context.MODE_PRIVATE, null);
        DBHelper.context = context;
        db.execSQL("INSERT INTO dailyreportss VALUES('" + latitude + "','" + longitude + "','" + hcf_master_id + "','" + waste_collection_date
                + "','" + truck_id + "','" + route_master_id + "','" + barcode_number+ "','" + cover_color_id
                + "','" + is_approval_required + "','"  + approved_by +  "','" + bag_weight_in_hcf  +
                 "','" + is_manual_input + "','" + hcf_authorized_person_name + "','" +driver_id + "','"
                 + is_sagregation_completed + "','" + sagregation_image+ "','"+transno+"');");
        if (db.isOpen()) {
            db.close();
        }
        Log.d("dbcreated_insert", "insertedsucess");
    }

    public void updateProject(String status, String cdt, Context context) {

        DBHelper.context = context;
        db = context.openOrCreateDatabase("RMAT", Context.MODE_PRIVATE, null);
        String strSQL = "UPDATE checktimes SET status = '" + status + "' WHERE cdt = '" + cdt + "'";
        db.execSQL(strSQL);
        if (db.isOpen()) {
            db.close();
        }
        //db.close();
        Log.d("checkupdated", "checkupdatedsuccessfully");
    }


    public void insertMessage(String latitude, String longitude, String message, String cdt, String status, Context context) {
        DBHelper.context = context;
        db = context.openOrCreateDatabase("RMAT", Context.MODE_PRIVATE, null);
        db.execSQL("INSERT INTO messagess VALUES('" + latitude + "','" + longitude + "','" + message + "','" + cdt + "','" + status + "');");
        db.close();
        Log.d("dbcreated_insert", "insertedsucess");
    }

    public void updateMessage(String status, String cdt, Context context) {
        db = context.openOrCreateDatabase("RMAT", Context.MODE_PRIVATE, null);
        String strSQL = "UPDATE messagess SET status = '" + status + "' WHERE cdt = '" + cdt + "'";
        db.execSQL(strSQL);

        db.close();
        Log.d("checkupdated", "checkupdatedsuccessfully");
    }


    public static void deletetables(Context context) {
        DBHelper.context = context;
        db = context.openOrCreateDatabase("RMAT", Context.MODE_PRIVATE, null);

        db.execSQL("delete from checktimes");
        db.execSQL("delete from messagess");
        db.execSQL("delete from dailyreportss");
        db.close();
    }


}