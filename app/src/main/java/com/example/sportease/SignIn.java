package com.example.sportease;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.Map;

public class SignIn extends AppCompatActivity {

    private EditText etUsername, etLastname, etEmail, etPassword;
    private Button btnSignup;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_sign_up);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        etUsername = findViewById(R.id.et_username);
        etEmail = findViewById(R.id.et_email);
        etPassword = findViewById(R.id.et_Pass);
        etLastname = findViewById(R.id.et_rpass);
        btnSignup = findViewById(R.id.sign_button);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateAndSignup();
            }
        });
    }

    private void validateAndSignup() {
        String username = etUsername.getText().toString().trim();
        String lastname = etLastname.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if (TextUtils.isEmpty(username)) {
            etUsername.setError("Username is required");
            return;
        }

        if (TextUtils.isEmpty(lastname)) {
            etLastname.setError("Lastname is required");
            return;
        }

        if (TextUtils.isEmpty(email) || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etEmail.setError("Valid email is required");
            return;
        }

        if (TextUtils.isEmpty(password) || password.length() < 6) {
            etPassword.setError("Password must be at least 6 characters");
            return;
        }

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            saveUserData(username, lastname, email);
                        } else {
                            handleAuthError(task.getException());
                        }
                    }
                });
    }

    private void saveUserData(String username, String lastname, String email) {
        String userId = mAuth.getCurrentUser().getUid();

        DocumentReference userRef = db.collection("users").document(userId);

        // Prepare user data
        Map<String, Object> userData = new HashMap<>();
        userData.put("userId", userId);
        userData.put("username", username);
        userData.put("lastname", lastname);
        userData.put("email", email);

        // Merge data without overwriting the document
        userRef.set(userData, SetOptions.merge())
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(SignIn.this, "Registration Successful", Toast.LENGTH_SHORT).show();
                            navigateToLogin();
                        } else {
                            handleFirestoreError(task.getException());
                        }
                    }
                });
    }

    private void navigateToLogin() {
        Intent intent = new Intent(SignIn.this, loginActivity.class);
        startActivity(intent);
        finish();
    }

    private void handleAuthError(Exception e) {
        if (e != null) {
            Toast.makeText(SignIn.this, "Registration Failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            Log.e("Auth Error", "Registration failed", e);
        } else {
            Toast.makeText(SignIn.this, "Registration Failed", Toast.LENGTH_SHORT).show();
        }
    }

    private void handleFirestoreError(Exception e) {
        if (e != null) {
            Toast.makeText(SignIn.this, "Failed to store user data: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            Log.e("Firestore Error", "Failed to store user data", e);
        } else {
            Toast.makeText(SignIn.this, "Failed to store user data", Toast.LENGTH_SHORT).show();
        }
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
