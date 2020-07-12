package com.hexoncode.cryptit;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.widget.TextView;

public class SplashActivity extends AppCompatActivity {

    public TextView madeInIndia;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        madeInIndia = findViewById(R.id.madeInIndia);
        madeInIndia.setText(Html.fromHtml("<font color=#ffffff>Made in India</font><font color=#40ACFF> © </font><font color=#ffffff>2020</font>"));

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(SplashActivity.this, OnBoardingActivity.class));
                finish();
            }
        }, 1500);
    }
}
