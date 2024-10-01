package com.example.sportease;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class club_owner_signup extends AppCompatActivity {

    private static final int SELECT_IMAGE_REQUEST = 1;
    private static final int MAX_IMAGES = 4;

    private EditText clubNameEditText, addressEditText, emailEditText, passwordEditText;
    private TextInputEditText openTimeEditText, closeTimeEditText;
    private Button signupButton, selectImageButton;
    private String openTime, closeTime;
    private ArrayList<Uri> imageUriList = new ArrayList<>();
    private FirebaseFirestore db;
    private FirebaseStorage storage;
    private FirebaseAuth auth;
    private ProgressBar progressBar;
    private LinearLayout imageContainer;
    private int uploadCounter = 0; // Track how many images have been uploaded
    private ArrayList<String> uploadedImageUrls = new ArrayList<>(); // Store uploaded image URLs

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_club_owner_signup);

        db = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();
        auth = FirebaseAuth.getInstance();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        clubNameEditText = findViewById(R.id.et_clubname);
        addressEditText = findViewById(R.id.et_address);
        emailEditText = findViewById(R.id.et_email);
        passwordEditText = findViewById(R.id.et_Pass);
        openTimeEditText = findViewById(R.id.open_time_edit_text);
        closeTimeEditText = findViewById(R.id.close_time_edit_text);
        selectImageButton = findViewById(R.id.upload_button);
        signupButton = findViewById(R.id.sign_button);
        imageContainer = findViewById(R.id.image_container);
        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(ProgressBar.GONE);

        openTimeEditText.setOnClickListener(view -> showTimePickerDialog(true));
        closeTimeEditText.setOnClickListener(view -> showTimePickerDialog(false));
        selectImageButton.setOnClickListener(view -> selectImages());
        signupButton.setOnClickListener(view -> saveClubOwnerInfo());
    }

    private void showTimePickerDialog(boolean isOpenTime) {
        int currentHour = 12;
        int currentMinute = 0;
        TimePickerDialog timePickerDialog = new TimePickerDialog(
                this,
                (view, hourOfDay, minute) -> {
                    String time = String.format("%02d:%02d", hourOfDay, minute);
                    if (isOpenTime) {
                        openTime = time;
                        openTimeEditText.setText(openTime);
                    } else {
                        closeTime = time;
                        closeTimeEditText.setText(closeTime);
                    }
                },
                currentHour,
                currentMinute,
                true
        );
        timePickerDialog.show();
    }

    private void selectImages() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        startActivityForResult(intent, SELECT_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SELECT_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            if (data.getClipData() != null) {
                int count = data.getClipData().getItemCount();
                if (count + imageUriList.size() <= MAX_IMAGES) {
                    for (int i = 0; i < count; i++) {
                        Uri imageUri = data.getClipData().getItemAt(i).getUri();
                        imageUriList.add(imageUri);
                        displayImage(imageUri);
                    }
                } else {
                    Toast.makeText(this, "You can upload a maximum of " + MAX_IMAGES + " images.", Toast.LENGTH_SHORT).show();
                }
            } else if (data.getData() != null) {
                if (imageUriList.size() < MAX_IMAGES) {
                    Uri imageUri = data.getData();
                    imageUriList.add(imageUri);
                    displayImage(imageUri);
                } else {
                    Toast.makeText(this, "You can upload a maximum of " + MAX_IMAGES + " images.", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private void displayImage(Uri imageUri) {
        LinearLayout imageItem = new LinearLayout(this);
        imageItem.setOrientation(LinearLayout.VERTICAL);
        imageItem.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));

        ImageView imageView = new ImageView(this);
        imageView.setLayoutParams(new LinearLayout.LayoutParams(200, 200));
        imageView.setImageURI(imageUri);
        imageItem.addView(imageView);

        Button cancelButton = new Button(this);
        cancelButton.setText("X");
        cancelButton.setBackgroundColor(getResources().getColor(android.R.color.holo_red_dark));
        cancelButton.setTextColor(getResources().getColor(android.R.color.white));
        cancelButton.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));

        cancelButton.setOnClickListener(v -> {
            imageContainer.removeView(imageItem);
            imageUriList.remove(imageUri);
        });

        imageItem.addView(cancelButton);
        imageContainer.addView(imageItem);
    }

    private void saveClubOwnerInfo() {
        String clubName = clubNameEditText.getText().toString().trim();
        String address = addressEditText.getText().toString().trim();
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        StringBuilder errors = new StringBuilder();

        if (clubName.isEmpty()) errors.append("Club name is required.\n");
        if (address.isEmpty()) errors.append("Address is required.\n");
        if (email.isEmpty()) errors.append("Email is required.\n");
        if (password.isEmpty()) errors.append("Password is required.\n");
        if (openTime == null || openTime.isEmpty()) errors.append("Opening time is required.\n");
        if (closeTime == null || closeTime.isEmpty()) errors.append("Closing time is required.\n");
        if (imageUriList.isEmpty()) errors.append("At least one club image is required.\n");

        if (errors.length() > 0) {
            Toast.makeText(this, errors.toString().trim(), Toast.LENGTH_LONG).show();
        } else {
            progressBar.setVisibility(ProgressBar.VISIBLE);
            auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            String uniqueId = UUID.randomUUID().toString();
                            for (Uri imageUri : imageUriList) {
                                uploadImage(imageUri, uniqueId, clubName, address);
                            }
                        } else {
                            progressBar.setVisibility(ProgressBar.GONE);
                            Toast.makeText(this, "Signup failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

    private void uploadImage(Uri imageUri, String uniqueId, String clubName, String address) {
        StorageReference storageRef = storage.getReference("club_images/" + uniqueId + "/" + UUID.randomUUID().toString());
        storageRef.putFile(imageUri)
                .addOnSuccessListener(taskSnapshot -> storageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                    uploadedImageUrls.add(uri.toString());
                    uploadCounter++;

                    if (uploadCounter == imageUriList.size()) {
                        saveClubOwnerToFirestore(uniqueId, clubName, address);
                    }
                }))
                .addOnFailureListener(e -> {
                    progressBar.setVisibility(ProgressBar.GONE);
                    Toast.makeText(club_owner_signup.this, "Image upload failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.e("ImageUpload", "Upload failed: " + e.getMessage());
                });
    }

    private void saveClubOwnerToFirestore(String uniqueId, String clubName, String address) {
        Map<String, Object> clubOwner = new HashMap<>();
        clubOwner.put("clubName", clubName);
        clubOwner.put("address", address);
        clubOwner.put("openTime", openTime);
        clubOwner.put("closeTime", closeTime);
        clubOwner.put("imageUrls", uploadedImageUrls);

        db.collection("clubOwners").document(uniqueId).set(clubOwner)
                .addOnSuccessListener(aVoid -> {
                    progressBar.setVisibility(ProgressBar.GONE);
                    Toast.makeText(this, "Signup successful!", Toast.LENGTH_SHORT).show();
                    finish();
                })
                .addOnFailureListener(e -> {
                    progressBar.setVisibility(ProgressBar.GONE);
                    Log.e("FirestoreError", "Error saving data: " + e.getMessage());
                });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
