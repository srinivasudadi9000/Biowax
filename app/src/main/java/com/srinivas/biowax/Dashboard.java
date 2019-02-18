package com.srinivas.biowax;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

public class Dashboard extends Activity implements View.OnClickListener {
    private StarAnimationView mAnimationView;
    LinearLayout garbage_ll, history_ll, mapview_ll, driverinfo_ll, receipt_ll, logout_ll;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard);
        garbage_ll = findViewById(R.id.garbage_ll);
        history_ll = findViewById(R.id.history_ll);
        mapview_ll = findViewById(R.id.mapview_ll);
        driverinfo_ll = findViewById(R.id.driverinfo_ll);
        receipt_ll = findViewById(R.id.receipt_ll);
        logout_ll = findViewById(R.id.logout_ll);

        garbage_ll.setOnClickListener(this);
        history_ll.setOnClickListener(this);
        mapview_ll.setOnClickListener(this);
        driverinfo_ll.setOnClickListener(this);
        receipt_ll.setOnClickListener(this);
        logout_ll.setOnClickListener(this);

    }

    public void vibrate() {
        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        // Vibrate for 500 milliseconds
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            v.vibrate(VibrationEffect.createOneShot(50, VibrationEffect.DEFAULT_AMPLITUDE));
        } else {
            //deprecated in API 26
            v.vibrate(50);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.garbage_ll:
                Intent garbagecollection = new Intent(Dashboard.this,GarbageCollection.class);
                startActivity(garbagecollection);
                break;
            case R.id.history_ll:
                break;
            case R.id.mapview_ll:
                break;
            case R.id.driverinfo_ll:
                break;
            case R.id.receipt_ll:
                break;
            case R.id.logout_ll:
                break;
        }
    }


}
