package com.example.sportease;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
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
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.Map;

public class loginActivity extends AppCompatActivity {

    private EditText etPassword, etEmail;
    private Button btnLogin;
    private FirebaseAuth mAuth;
    public FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_login);

        // Initialize Firebase Auth and Firestore
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        etEmail = findViewById(R.id.et_Email);
        etPassword = findViewById(R.id.editTextTextPersonName2);
        btnLogin = findViewById(R.id.button);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateAndLogin();
            }
        });
    }

    private void validateAndLogin() {
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if (TextUtils.isEmpty(email)) {
            etEmail.setError("Email is required");
            return;
        }

        if (TextUtils.isEmpty(password)) {
            etPassword.setError("Password is required");
            return;
        }

        loginUser(email, password);
    }

    private void loginUser(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(loginActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                            FirebaseUser currentUser = mAuth.getCurrentUser();
                            if (currentUser != null) {
                                updateUserInFirestore(currentUser.getUid(), email);
                            }

                            // Navigate to the club owner view
                            Intent intent = new Intent(loginActivity.this, ViewGround.class);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(loginActivity.this, "Invalid Credentials", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void updateUserInFirestore(String uid, String email) {
        // Fetch the existing user data to avoid overwriting important fields
        db.collection("users").document(uid).get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            // Merge only the email without overwriting other fields
                            Map<String, Object> updatedData = new HashMap<>();
                            updatedData.put("email", email); // Only add/merge the email field

                            db.collection("users").document(uid)
                                    .set(updatedData, SetOptions.merge()) // Use merge to retain other fields
                                    .addOnSuccessListener(aVoid -> {
                                        // Data updated successfully
                                        Toast.makeText(loginActivity.this, "User data updated", Toast.LENGTH_SHORT).show();
                                    })
                                    .addOnFailureListener(e -> {
                                        Toast.makeText(loginActivity.this, "Error updating user data: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                    });
                        } else {
                            // Document doesn't exist, create a new one with necessary fields
                            createUserInFirestore(uid, email);
                        }
                    } else {
                        Toast.makeText(loginActivity.this, "Error fetching user data", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void createUserInFirestore(String uid, String email) {
        // Create a new user with all necessary fields
        Map<String, Object> newUser = new HashMap<>();
        newUser.put("email", email);
        newUser.put("username", "");  // Initialize with empty or default values
        newUser.put("lastname", "");
        newUser.put("userid", uid);

        db.collection("users").document(uid)
                .set(newUser)
                .addOnSuccessListener(aVoid -> {
                    // User created successfully
                    Toast.makeText(loginActivity.this, "New user created", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(loginActivity.this, "Error creating user: " + e.getMessage(), Toast.LENGTH_SHORT).show();
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
