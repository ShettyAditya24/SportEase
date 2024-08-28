//package com.example.sportease;
//
//import android.os.Bundle;
//import android.view.View;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.Toast;
//
//import androidx.activity.EdgeToEdge;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.core.graphics.Insets;
//import androidx.core.view.ViewCompat;
//import androidx.core.view.WindowInsetsCompat;
//
//public class coach_signup extends AppCompatActivity {
//
//    EditText etFullName, etEmail, etPhoneNumber, etPassword, etSpecialization, etExperience, etCertifications, etBio, etAvailability;
//    Button btnRegister;
//    DbHelper dbHelper;
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        EdgeToEdge.enable(this);
//        setContentView(R.layout.activity_coach_signup);
//                // Initialize views
//                etFullName = findViewById(R.id.et_coach_name); // Assuming 'et_clubname' is for coach full name
//                etEmail = findViewById(R.id.etEmail);
//                etPhoneNumber = findViewById(R.id.et_phone); // Assuming 'et_address' is for phone number
//                etPassword = findViewById(R.id.et_Pass);
//                etSpecialization = findViewById(R.id.et_specialization); // Add this to your XML layout
//                etExperience = findViewById(R.id.et_experience); // Add this to your XML layout
//                etCertifications = findViewById(R.id.et_certifications); // Add this to your XML layout
//                etBio = findViewById(R.id.et_bio); // Add this to your XML layout
//                etAvailability = findViewById(R.id.et_availability); // Add this to your XML layout
//                btnRegister = findViewById(R.id.sign_button);
//
//                // Initialize database helper
//                dbHelper = new DbHelper(this);
//
//                // Set click listener for the register button
//                btnRegister.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        String fullName = etFullName.getText().toString();
//                        String email = etEmail.getText().toString();
//                        String phoneNumber = etPhoneNumber.getText().toString();
//                        String password = etPassword.getText().toString();
//                        String specialization = etSpecialization.getText().toString();
//                        String experience = etExperience.getText().toString();
//                        String certifications = etCertifications.getText().toString();
//                        String bio = etBio.getText().toString();
//                        String availability = etAvailability.getText().toString();
//
//                        if (fullName.isEmpty() || email.isEmpty() || phoneNumber.isEmpty() || password.isEmpty() || specialization.isEmpty() || experience.isEmpty() || certifications.isEmpty() || bio.isEmpty() || availability.isEmpty()) {
//                            Toast.makeText(coach_signup.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
//                        } else {
//                            boolean isInserted = dbHelper.insertCoach(fullName, email, phoneNumber, password, specialization, experience, certifications, bio, availability);
//                            if (isInserted) {
//                                Toast.makeText(coach_signup.this, "Coach Registered Successfully", Toast.LENGTH_SHORT).show();
//                                // Redirect to another activity or clear fields
//                            } else {
//                                Toast.makeText(coach_signup.this, "Registration Failed", Toast.LENGTH_SHORT).show();
//                            }
//                        }
//                    }
//                });
//            }
//        }
//
//
