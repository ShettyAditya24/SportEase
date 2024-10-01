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
import com.google.firebase.firestore.FirebaseFirestore;

public class club_owner_login extends AppCompatActivity {

    private EditText etEmail, etPassword;
    private Button btnLogin;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_club_owner_login);

        // Initialize Firebase Auth and Firestore
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        etEmail = findViewById(R.id.et_Email);
        etPassword = findViewById(R.id.edit_pass);
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
                            Toast.makeText(club_owner_login.this, "Login Successful", Toast.LENGTH_SHORT).show();
                            // Store additional club owner data in Firestore if needed
                            storeClubOwnerData(email);

                            // Navigate to the club owner view
                            Intent intent = new Intent(club_owner_login.this, club_owner_view.class);
                            startActivity(intent);
                            finish(); // Close the login activity
                        } else {
                            Toast.makeText(club_owner_login.this, "Invalid Credentials", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void storeClubOwnerData(String email) {
        // Storing club owner login data in Firestore (optional)
        db.collection("clubOwners").document(email)
                .set(new ClubOwner(email))
                .addOnSuccessListener(aVoid -> {
                    // Data stored successfully
                })
                .addOnFailureListener(e -> {
                    // Failed to store data
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

    // ClubOwner class to define the club owner object
    public static class ClubOwner {
        public String email;

        public ClubOwner(String email) {
            this.email = email;
        }
    }
}
