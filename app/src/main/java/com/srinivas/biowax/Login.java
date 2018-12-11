package com.srinivas.biowax;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;

public class Login extends Activity implements View.OnClickListener {
Button login_btn;
     @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        login_btn = findViewById(R.id.login_btn);
        login_btn.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.login_btn:

                Intent dashboard = new Intent(Login.this,Dashboard.class);
                startActivity(dashboard);
                break;
        }
    }
}
