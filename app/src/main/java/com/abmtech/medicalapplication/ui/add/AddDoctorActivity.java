package com.abmtech.medicalapplication.ui.add;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.abmtech.medicalapplication.databinding.ActivityAddDoctorBinding;
import com.abmtech.medicalapplication.utils.Constants;
import com.abmtech.medicalapplication.utils.Session;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.makeramen.roundedimageview.RoundedImageView;
import com.sangcomz.fishbun.FishBun;
import com.sangcomz.fishbun.adapter.image.impl.GlideAdapter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class AddDoctorActivity extends AppCompatActivity {
    private Activity activity;
    private ActivityAddDoctorBinding binding;
    private FirebaseDatabase firebaseDatabase;
    private Session session;
    private ArrayList<Uri> imageArray = new ArrayList<>();
    private final ArrayList<Uri> urlArray = new ArrayList<>();
    private StorageReference storageReference;
    private int count = 0;
    private String name = "", speciality = "", price = "", timing = "", about = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddDoctorBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        activity = AddDoctorActivity.this;

        firebaseDatabase = FirebaseDatabase.getInstance();
        session = new Session(activity);

        FirebaseStorage storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        binding.imageFirst.setOnClickListener(view -> selectImage());
        binding.imageSecond.setOnClickListener(view -> selectImage());
        binding.imageThird.setOnClickListener(view -> selectImage());
        binding.imageFourth.setOnClickListener(view -> selectImage());
        binding.imageFifth.setOnClickListener(view -> selectImage());

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.MANAGE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            binding.textPublish.setOnClickListener(view -> publish());
            Log.e(Constants.TAG, "onCreate() called with: if = [" + savedInstanceState + "]");
        } else {
            Log.e(Constants.TAG, "onCreate() called with: else = [" + savedInstanceState + "]");
            Dexter.withContext(this)
                    .withPermissions(
                            Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.MANAGE_EXTERNAL_STORAGE,
                            Manifest.permission.CAMERA
                    )
                    .withListener(new MultiplePermissionsListener() {
                        @Override
                        public void onPermissionsChecked(MultiplePermissionsReport multiplePermissionsReport) {
                            binding.textPublish.setOnClickListener(view -> publish());
                            Log.e(Constants.TAG, "onPermissionsChecked() called with: multiplePermissionsReport = [" + multiplePermissionsReport + "]");
                        }

                        @Override
                        public void onPermissionRationaleShouldBeShown(List<PermissionRequest> list, PermissionToken permissionToken) {

                        }
                    }).check();
        }

    }

    private void publish() {
        if (binding.edtDoctorName.getText() != null && binding.edtDoctorName.getText().length() != 0) {
            if (binding.edtPrice.getText() != null && binding.edtPrice.getText().length() != 0) {
                if (binding.edtTiming.getText() != null && binding.edtTiming.getText().length() != 0) {
                    if (binding.edtDescription.getText() != null && binding.edtDescription.getText().length() != 0) {
                        if (binding.edtSpeciality.getText() != null && binding.edtSpeciality.getText().length() != 0 && !binding.edtAmount.getText().toString().equalsIgnoreCase("0")) {
                            if (imageArray != null && imageArray.size() != 0) {
                                name = binding.edtDoctorName.getText().toString();
                                timing = binding.edtTiming.getText().toString();
                                price = binding.edtPrice.getText().toString();
                                about = binding.edtDescription.getText().toString();
                                speciality = binding.edtSpeciality.getText().toString();

                                processImage();
                            } else {
                                Snackbar.make(binding.getRoot(), "Select At least One Image!", Snackbar.LENGTH_LONG).show();
                                binding.body.fullScroll(View.FOCUS_DOWN);
                            }
                        } else {
                            binding.edtSpeciality.setError("Speciality cannot be Empty!");
                            binding.edtSpeciality.requestFocus();
                        }
                    } else {
                        binding.edtDescription.setError("About cannot be Empty!");
                        binding.edtDescription.requestFocus();
                    }
                } else {
                    binding.edtTiming.setError("Timing cannot be Empty!");
                    binding.edtTiming.requestFocus();
                }
            } else {
                binding.edtPrice.setError("Fees cannot be Empty!");
                binding.edtPrice.requestFocus();
            }
        } else {
            binding.edtDoctorName.setError("Name cannot be Empty!");
            binding.edtDoctorName.requestFocus();
        }
    }

    private void selectImage() {
        FishBun.with(activity)
                .setImageAdapter(new GlideAdapter())
                .setMaxCount(5).hasCameraInPickerPage(true)
                .textOnNothingSelected("Please select One to Five Images!")
                .textOnImagesSelectionLimitReached("You can't select any more.")
                .setRequestCode(100)
                .setSelectedImages(imageArray)
                .startAlbum();
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent imageData) {
        super.onActivityResult(requestCode, resultCode, imageData);
        Log.e(Constants.TAG, "onActivityResult: Is calling and Size - " + imageArray.size());
        if (requestCode == 100) {
            try {
                imageArray = imageData.getParcelableArrayListExtra(FishBun.INTENT_PATH);
            } catch (Exception e) {
                e.printStackTrace();
            }
            for (int i = 0; i < imageArray.size(); i++) {
                Log.e(Constants.TAG, "onActivityResult: ImagePicker" + imageArray.get(i));
                if (i == 0) binding.imageFirst.setImageURI(imageArray.get(i));
                if (i == 1) binding.imageSecond.setImageURI(imageArray.get(i));
                if (i == 2) binding.imageThird.setImageURI(imageArray.get(i));
                if (i == 3) binding.imageFourth.setImageURI(imageArray.get(i));
                if (i == 4) binding.imageFifth.setImageURI(imageArray.get(i));
            }
        }
    }

    private void processImage() {
        binding.textPublish.setOnClickListener(view -> {});
        count = 0;
        urlArray.clear();
        for (int i = 0; i < imageArray.size(); i++) {
            ProgressBar progressBar = null;
            LinearLayout ll = null;
            RoundedImageView imageView = null;
            if (i == 0) {
                progressBar = binding.progressFirst;
                ll = binding.progressFirstLl;
                imageView = binding.imageFirst;
                binding.progressFirstLl.setVisibility(View.VISIBLE);
                binding.imageFirst.setVisibility(View.GONE);
            } else if (i == 1) {
                progressBar = binding.progressFirst;
                ll = binding.progressSecondLl;
                imageView = binding.imageSecond;
                binding.progressSecondLl.setVisibility(View.VISIBLE);
                binding.imageSecond.setVisibility(View.GONE);
            } else if (i == 2) {
                progressBar = binding.progressFirst;
                ll = binding.progressThirdLl;
                imageView = binding.imageThird;
                binding.progressThirdLl.setVisibility(View.VISIBLE);
                binding.imageThird.setVisibility(View.GONE);
            } else if (i == 3) {
                progressBar = binding.progressFirst;
                ll = binding.progressFourthLl;
                imageView = binding.imageFourth;
                binding.progressFourthLl.setVisibility(View.VISIBLE);
                binding.imageFourth.setVisibility(View.GONE);
            } else if (i == 4) {
                progressBar = binding.progressFirst;
                ll = binding.progressFifthLl;
                imageView = binding.imageFifth;
                binding.progressFifthLl.setVisibility(View.VISIBLE);
                binding.imageFifth.setVisibility(View.GONE);
            }
            count++;
            uploadImage(imageArray.get(i), progressBar, ll, imageView);
        }
    }

    private void pushToStorage() {
        if (count == 0) {
            Map<String, Object> map = new HashMap<>();

            String currentTime = Calendar.getInstance().getTime().toString();

            Log.e(Constants.TAG, "addItem() called with: name = [" + name + "], speciality = [" + speciality + "]," +
                    " price = [" + price + "], currentTime = [" + currentTime + "]," +
                    " about = [" + about + "], timing = [" + timing + "]");

            map.put("name", name);
            map.put("speciality", speciality);
            map.put("price", price);
            map.put("about", about);
            map.put("timing", timing);
            map.put("currentTime", currentTime);
            map.put("user_id", session.getUserId());

            for (int i = 0; i < urlArray.size(); i++) {
                map.put("image" + (i + 1), urlArray.get(i).toString());
                Log.e(Constants.TAG, "pushToStorage: image" + (i + 1) + ": " + urlArray.get(i).toString());
            }

            String mGroupId = firebaseDatabase.getReference().push().getKey();

            map.put("doctor_id", mGroupId);

            if (mGroupId != null) {
                firebaseDatabase.getReference()
                        .child("doctors")
                        .child(session.getUserId())
                        .child(mGroupId)
                        .setValue(map)
                        .addOnSuccessListener(unused -> {
                            Toast.makeText(activity, "Data Saved", Toast.LENGTH_SHORT).show();
                            finish();
                        });
            }
        }
    }

    private void uploadImage(Uri filePath, ProgressBar progressBar, LinearLayout ll, RoundedImageView imageView) {
        if (filePath != null) {
            StorageReference ref = storageReference.child("images/" + UUID.randomUUID().toString());

            ref.putFile(filePath).addOnSuccessListener(taskSnapshot -> {
                        Task<Uri> url = ref.getDownloadUrl();
                        url.addOnSuccessListener(uri -> {
                            urlArray.add(uri);
                            count--;
                            pushToStorage();
                            Log.e(Constants.TAG, "onSuccess() called with: count = [" + count + "]");
                        });
                        ll.setVisibility(View.GONE);
                        imageView.setVisibility(View.VISIBLE);
                    })

                    .addOnFailureListener(e -> {
                        ll.setVisibility(View.GONE);
                        imageView.setVisibility(View.VISIBLE);
                        count--;
                        pushToStorage();
                        Toast.makeText(activity, "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    })

                    .addOnProgressListener(taskSnapshot -> {
                        double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                        progressBar.setProgress((int) progress);
                    });
        }

    }
}