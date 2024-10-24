package com.example.sportease;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class CoachLogin extends AppCompatActivity {

    private EditText etEmail, etPassword;
    private Button btnLogin;
    private ProgressBar progressBar;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coach_login);  // Ensure XML matches your layout file name

        // Initialize FirebaseAuth instance
        mAuth = FirebaseAuth.getInstance();

        // Link XML elements to Java code
        etEmail = findViewById(R.id.et_Email);
        etPassword = findViewById(R.id.edit_pass);
        btnLogin = findViewById(R.id.button);
        progressBar = new ProgressBar(this);  // Optional: You can also use a ProgressBar widget in XML.

        // Set click listener for Login button
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUser();
            }
        });
    }

    private void loginUser() {
        // Retrieve input from EditTexts
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        // Validate email and password inputs
        if (TextUtils.isEmpty(email)) {
            etEmail.setError("Email is required");
            etEmail.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(password)) {
            etPassword.setError("Password is required");
            etPassword.requestFocus();
            return;
        }

        if (password.length() < 6) {
            etPassword.setError("Password must be at least 6 characters");
            etPassword.requestFocus();
            return;
        }

        // Show progress bar while logging in
        progressBar.setVisibility(View.VISIBLE);

        // Authenticate user using Firebase Authentication
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<com.google.firebase.auth.AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<com.google.firebase.auth.AuthResult> task) {
                        // Hide progress bar after completion
                        progressBar.setVisibility(View.GONE);

                        if (task.isSuccessful()) {
                            // Login successful
                            Toast.makeText(CoachLogin.this, "Login successful!", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(CoachLogin.this, CoachView.class));
                            finish();
                        } else {
                            // Login failed
                            Toast.makeText(CoachLogin.this, "Login failed: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }
}
