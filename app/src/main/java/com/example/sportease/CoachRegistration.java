package com.example.sportease;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class CoachRegistration extends AppCompatActivity {

    private EditText etName, etEmail, etPhone, etPassword, etSpecialization, etExperience, etBio;
    private Button signButton;
    private FirebaseAuth mAuth;
    private FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coach_signup);  // Ensure XML layout is correctly referenced.

        // Initialize FirebaseAuth and Firestore
        mAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();

        // Link XML elements to Java code
        etName = findViewById(R.id.et_coach_name);
        etEmail = findViewById(R.id.etEmail);
        etPhone = findViewById(R.id.et_phone);
        etPassword = findViewById(R.id.et_Pass);
        etSpecialization = findViewById(R.id.et_specialization);
        etExperience = findViewById(R.id.et_experience);
        etBio = findViewById(R.id.et_bio);
        signButton = findViewById(R.id.sign_button);

        // Set click listener for Signup button
        signButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerCoach();
            }
        });
    }

    private void registerCoach() {
        // Retrieve user input from EditTexts
        String name = etName.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String phone = etPhone.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        String specialization = etSpecialization.getText().toString().trim();
        String experience = etExperience.getText().toString().trim();
        String bio = etBio.getText().toString().trim();

        // Validate input
        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(email) || TextUtils.isEmpty(phone) ||
                TextUtils.isEmpty(password) || TextUtils.isEmpty(specialization) ||
                TextUtils.isEmpty(experience) || TextUtils.isEmpty(bio)) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Validate email format
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this, "Please enter a valid email", Toast.LENGTH_SHORT).show();
            return;
        }

        // Parse experience to an integer and handle potential errors
        int yearsOfExperience;
        try {
            yearsOfExperience = Integer.parseInt(experience);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Invalid experience value", Toast.LENGTH_SHORT).show();
            return;
        }

        // Create user with Firebase Authentication
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Ensure the user is not null
                            FirebaseUser user = mAuth.getCurrentUser();
                            if (user != null) {
                                String coachId = user.getUid();

                                // Store additional coach data in Firestore
                                Map<String, Object> coach = new HashMap<>();
                                coach.put("name", name);
                                coach.put("email", email);
                                coach.put("phone", phone);
                                coach.put("specialization", specialization);
                                coach.put("experience", yearsOfExperience);
                                coach.put("bio", bio);
                                coach.put("coachId",coachId);

                                // Use UID as document ID for easy access
                                firestore.collection("coaches").document(coachId)
                                        .set(coach)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Toast.makeText(CoachRegistration.this,
                                                        "Registration successful!",
                                                        Toast.LENGTH_SHORT).show();
                                                clearFields();
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(CoachRegistration.this,
                                                        "Firestore error: " + e.getMessage(),
                                                        Toast.LENGTH_LONG).show();
                                            }
                                        });
                            }
                        } else {
                            Toast.makeText(CoachRegistration.this,
                                    "Authentication failed: " + task.getException().getMessage(),
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    // Clear input fields after successful registration
    private void clearFields() {
        etName.setText("");
        etEmail.setText("");
        etPhone.setText("");
        etPassword.setText("");
        etSpecialization.setText("");
        etExperience.setText("");
        etBio.setText("");
    }
}
