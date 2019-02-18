package com.srinivas.biowax;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

public class Biowastageform extends Activity implements View.OnClickListener {
   ImageView scanning_qrcode;
    public static EditText waste_collection_date;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.biowastageform);

        scanning_qrcode = findViewById(R.id.scanning_qrcode);
        scanning_qrcode.setOnClickListener(this);

        waste_collection_date = findViewById(R.id.waste_collection_date);

    }

    @Override
    public void onClick(View v) {
       switch (v.getId()){
           case R.id.scanning_qrcode:
               Intent barcodescanner = new Intent(Biowastageform.this,Barcodescanner.class);
               startActivity(barcodescanner);
               break;
       }
    }
}
