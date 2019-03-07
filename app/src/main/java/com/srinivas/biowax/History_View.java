package com.srinivas.biowax;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class History_View extends Activity implements View.OnClickListener {
    TextView hcfc_master_id_tv, waste_collection_date_tv, barcodeNumber_tv, transaction_code_tv, cover_color_id_tv, is_approval_required_tv,
            approved_by_tv, bag_weight_in_hcf_tv, is_manual_input_tv, hcf_authorized_person_name_tv, is_sagregation_completed_tv;
    ImageView myimage_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.history__view);
        myimage_back = findViewById(R.id.myimage_back);
        myimage_back.setOnClickListener(this);
        hcfc_master_id_tv = findViewById(R.id.hcfc_master_id_tv);
        waste_collection_date_tv = findViewById(R.id.waste_collection_date_tv);
        barcodeNumber_tv = findViewById(R.id.barcodeNumber_tv);
        transaction_code_tv = findViewById(R.id.transaction_code_tv);
        cover_color_id_tv = findViewById(R.id.cover_color_id_tv);
        is_approval_required_tv = findViewById(R.id.is_approval_required_tv);
        approved_by_tv = findViewById(R.id.approved_by_tv);
        bag_weight_in_hcf_tv = findViewById(R.id.bag_weight_in_hcf_tv);
        is_manual_input_tv = findViewById(R.id.is_manual_input_tv);
        hcf_authorized_person_name_tv = findViewById(R.id.hcf_authorized_person_name_tv);
        is_sagregation_completed_tv = findViewById(R.id.is_sagregation_completed_tv);


        hcfc_master_id_tv.setText(getIntent().getStringExtra("hcfc_master_id_tv"));
        waste_collection_date_tv.setText(getIntent().getStringExtra("waste_collection_date_tv"));
        barcodeNumber_tv.setText(getIntent().getStringExtra("barcodeNumber_tv"));
        transaction_code_tv.setText(getIntent().getStringExtra("transaction_code_tv"));
        cover_color_id_tv.setText(getIntent().getStringExtra("cover_color_id_tv"));
        is_approval_required_tv.setText(getIntent().getStringExtra("is_approval_required_tv"));
        approved_by_tv.setText(getIntent().getStringExtra("approved_by_tv"));
        is_manual_input_tv.setText(getIntent().getStringExtra("is_manual_input_tv"));
        hcf_authorized_person_name_tv.setText(getIntent().getStringExtra("hcf_authorized_person_name_tv"));
        is_sagregation_completed_tv.setText(getIntent().getStringExtra("is_sagregation_completed_tv"));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.myimage_back:
                finish();
                break;
        }
    }
}
