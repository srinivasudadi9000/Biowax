package com.srinivas.biowax;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class History_View extends AppCompatActivity {
   TextView mytext_history;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.history__view);
        mytext_history = findViewById(R.id.mytext_history);
        mytext_history.setText("Status : true");
    }
}
