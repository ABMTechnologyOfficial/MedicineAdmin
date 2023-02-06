package com.abmtech.medicalapplication.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.abmtech.medicalapplication.databinding.ActivityLoginBinding;
import com.abmtech.medicalapplication.ui.home.HomeActivity;
import com.abmtech.medicalapplication.utils.Session;

public class LoginActivity extends AppCompatActivity {
    private ActivityLoginBinding binding;
    private Activity activity;
    private Session session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        activity = LoginActivity.this;

        session = new Session(activity);

        session.setUserId("1");

        binding.textLogin.setOnClickListener(view -> startActivity(new Intent(activity, HomeActivity.class)));
    }
}