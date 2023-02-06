package com.abmtech.medicalapplication.ui.home;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.LinearLayout;

import com.abmtech.medicalapplication.R;
import com.abmtech.medicalapplication.databinding.ActivityHomeBinding;
import com.abmtech.medicalapplication.databinding.AddItemPopupBinding;
import com.abmtech.medicalapplication.ui.add.AddDoctorActivity;
import com.abmtech.medicalapplication.ui.add.AddItemActivity;
import com.google.android.material.navigation.NavigationBarView;

public class HomeActivity extends AppCompatActivity {
    private ActivityHomeBinding binding;
    private Activity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        activity = HomeActivity.this;

        binding.bottomNavHome.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_home) {
                replaceFragment(new DashboardFragment(), false);
                return true;
            } else if (id == R.id.nav_profile) {
                replaceFragment(new ProfileFragment(), false);
                return true;
            } else {
                showAddDialog();
                return false;
            }
        });
    }

    private void showAddDialog() {
        Dialog dialog = new Dialog(activity);

        AddItemPopupBinding binding = AddItemPopupBinding.inflate(getLayoutInflater());

        dialog.setContentView(binding.getRoot());
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);

        binding.llAddMedicine.setOnClickListener(view -> {
            startActivity(new Intent(activity, AddItemActivity.class));
            dialog.dismiss();
        });

        binding.llAddDoctor.setOnClickListener(view -> {
            startActivity(new Intent(activity, AddDoctorActivity.class));
            dialog.dismiss();
        });

        dialog.show();
    }

    private void replaceFragment(Fragment fragment, boolean addToBackstack) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction().replace(binding.container.getId(), fragment);

        if (addToBackstack)
            ft.addToBackStack("home");
        else ft.addToBackStack(null);

        ft.commit();
    }
}