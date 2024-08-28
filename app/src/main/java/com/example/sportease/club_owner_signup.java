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
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class club_owner_signup extends AppCompatActivity {

    private static final int SELECT_IMAGE_REQUEST = 1;
    private EditText clubNameEditText, addressEditText, emailEditText, passwordEditText;
    private TextInputEditText openTimeEditText, closeTimeEditText;
    private Button signupButton, selectImageButton;
    private String openTime, closeTime;
    private LinearLayout imagesContainer;
    private ArrayList<Uri> imageUriList;
    private FirebaseFirestore db;
    private FirebaseStorage storage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_club_owner_signup);

        db = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Initialize EditText fields
        clubNameEditText = findViewById(R.id.et_clubname);
        addressEditText = findViewById(R.id.et_address);
        emailEditText = findViewById(R.id.et_email);
        passwordEditText = findViewById(R.id.et_Pass);

        // Initialize TextInputEditText fields for time pickers
        openTimeEditText = findViewById(R.id.open_time_edit_text);
        closeTimeEditText = findViewById(R.id.close_time_edit_text);

        // Initialize Buttons
        selectImageButton = findViewById(R.id.upload_button);
        signupButton = findViewById(R.id.sign_button);

        // Initialize list to hold image URIs
        imageUriList = new ArrayList<>();

        // Set listeners for time pickers
        openTimeEditText.setOnClickListener(view -> showTimePickerDialog(true));
        closeTimeEditText.setOnClickListener(view -> showTimePickerDialog(false));

        // Set listener for image selection button
        selectImageButton.setOnClickListener(view -> selectImages());

        // Set listener for signup button
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
                true // Use 24-hour format
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
                for (int i = 0; i < count; i++) {
                    Uri imageUri = data.getClipData().getItemAt(i).getUri();
                    imageUriList.add(imageUri);
                }
            } else if (data.getData() != null) {
                Uri imageUri = data.getData();
                imageUriList.add(imageUri);
            }
        }
    }

    private void saveClubOwnerInfo() {
        String clubName = clubNameEditText.getText().toString().trim();
        String address = addressEditText.getText().toString().trim();
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        StringBuilder errors = new StringBuilder();

        if (clubName.isEmpty()) {
            errors.append("Club name is required.\n");
        }
        if (address.isEmpty()) {
            errors.append("Address is required.\n");
        }
        if (email.isEmpty()) {
            errors.append("Email is required.\n");
        }
        if (password.isEmpty()) {
            errors.append("Password is required.\n");
        }
        if (openTime == null || openTime.isEmpty()) {
            errors.append("Opening time is required.\n");
        }
        if (closeTime == null || closeTime.isEmpty()) {
            errors.append("Closing time is required.\n");
        }
        if (imageUriList.isEmpty()) {
            errors.append("At least one club image is required.\n");
        }

        if (errors.length() > 0) {
            Toast.makeText(this, errors.toString().trim(), Toast.LENGTH_LONG).show();
        } else {
            // Generate unique ID
            String uniqueId = UUID.randomUUID().toString();
            uploadImagesAndSaveInfo(uniqueId, clubName, address, email, password);
        }
    }

    private void uploadImagesAndSaveInfo(String uniqueId, String clubName, String address, String email, String password) {
        final ArrayList<String> imageUrls = new ArrayList<>();
        final int totalImages = imageUriList.size();
        int[] uploadedImagesCount = {0};  // Use array to modify count inside inner class

        for (Uri imageUri : imageUriList) {
            final String imageId = UUID.randomUUID().toString();
            StorageReference storageRef = storage.getReference().child("club_images/" + uniqueId + "/" + imageId);
            UploadTask uploadTask = storageRef.putFile(imageUri);

            uploadTask.addOnSuccessListener(taskSnapshot -> {
                storageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                    String imageUrl = uri.toString();
                    imageUrls.add(imageUrl);
                    uploadedImagesCount[0]++;
                    if (uploadedImagesCount[0] == totalImages) {
                        saveClubOwnerData(uniqueId, clubName, address, email, password, imageUrls);
                    }
                }).addOnFailureListener(e -> {
                    Toast.makeText(club_owner_signup.this, "Failed to get image URL!", Toast.LENGTH_SHORT).show();
                    Log.e("ClubOwnerSignup", "Failed to get image URL", e);
                });
            }).addOnFailureListener(e -> {
                Toast.makeText(club_owner_signup.this, "Image upload failed!", Toast.LENGTH_SHORT).show();
                Log.e("ClubOwnerSignup", "Image upload failed", e);
            });
        }
    }

    private void saveClubOwnerData(String uniqueId, String clubName, String address, String email, String password, ArrayList<String> imageUrls) {
        Map<String, Object> clubOwner = new HashMap<>();
        clubOwner.put("id", uniqueId); // Save unique ID
        clubOwner.put("clubName", clubName);
        clubOwner.put("address", address);
        clubOwner.put("email", email);
        clubOwner.put("password", password);  // Consider encrypting it
        clubOwner.put("openTime", openTime);
        clubOwner.put("closeTime", closeTime);
        clubOwner.put("imageUrls", imageUrls);

        db.collection("clubOwners")
                .document(uniqueId) // Use unique ID as document ID
                .set(clubOwner)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(club_owner_signup.this, "Signup successful!", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(club_owner_signup.this, "Signup failed! Please try again.", Toast.LENGTH_SHORT).show();
                        Log.e("ClubOwnerSignup", "Error inserting club owner data", task.getException());
                    }
                });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
