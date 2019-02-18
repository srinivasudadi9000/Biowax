package com.srinivas.biowax;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import me.relex.circleindicator.CircleIndicator;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class Home extends Activity {

    ViewPager viewPager;
    int images[] = {R.drawable.image4, R.drawable.image2, R.drawable.image3, R.drawable.image4,R.drawable.image5,R.drawable.image6};
    MyCustomPagerAdapter myCustomPagerAdapter;
    int currentPage = 0;
    TextView skip_tv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);

        viewPager = (ViewPager)findViewById(R.id.my_viewpager);

        myCustomPagerAdapter = new MyCustomPagerAdapter(Home.this, images);
        viewPager.setAdapter(myCustomPagerAdapter);

        CircleIndicator indicator = (CircleIndicator) findViewById(R.id.indicator);
        indicator.setViewPager(viewPager);

        // Auto start of viewpager
        final Handler handler = new Handler();
        final Runnable Update = new Runnable() {
            public void run() {
                if (currentPage == images.length) {
                    currentPage = 0;
                }
                viewPager.setCurrentItem(currentPage++, true);
            }
        };

        skip_tv = findViewById(R.id.skip_tv);
        skip_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent subhome = new Intent(Home.this,Login.class);
                startActivity(subhome);
            }
        });
       /* Timer swipeTimer = new Timer();
        swipeTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(Update);
            }
        }, 2500, 2500);*/



    }





}
