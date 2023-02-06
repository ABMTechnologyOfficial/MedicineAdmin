package com.abmtech.medicalapplication.ui.home;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.abmtech.medicalapplication.R;
import com.abmtech.medicalapplication.databinding.FragmentDashboardBinding;


public class DashboardFragment extends Fragment {
    private FragmentDashboardBinding binding;
    private Activity activity;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        activity = requireActivity();



        return binding.getRoot();
    }
}