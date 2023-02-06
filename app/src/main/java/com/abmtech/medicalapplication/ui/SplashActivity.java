package com.abmtech.medicalapplication.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.abmtech.medicalapplication.R;

public class SplashActivity extends AppCompatActivity {

    private final int SplashTimer = 2000;
    private Activity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        activity = SplashActivity.this;

        new Handler().postDelayed(() -> {
            startActivity(new Intent(activity, LoginActivity.class));
            finish();
        }, SplashTimer);
    }
}