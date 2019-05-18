package com.srinivas.biowax;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class Splash extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                SharedPreferences ss = getSharedPreferences("Login", MODE_PRIVATE);
                if (ss.getString("user_id","").length()>0){
                    Intent dashboard = new Intent(Splash.this, Login.class);
                    startActivity(dashboard);
                    finish();
                  /*  SharedPreferences ssd = getSharedPreferences("Login", MODE_PRIVATE);
                    if (ssd.getString("type","").equals("Driver")){
                        Intent dashboard = new Intent(Splash.this, Dashboard_Agent.class);
                        startActivity(dashboard);
                        finish();

                    }else {
                        Intent dashboard = new Intent(Splash.this, Dashboard.class);
                        startActivity(dashboard);
                        finish();
                    }*/

                }else{
                    Intent login = new Intent(Splash.this, Home.class);
                    startActivity(login);
                    finish();
                }
            }
        }, 2000);

    }
}
