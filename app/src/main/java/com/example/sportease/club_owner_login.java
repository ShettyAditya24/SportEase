package com.example.sportease;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseUser;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

public class club_owner_login extends AppCompatActivity {

    EditText email, password;
    Button loginButton, registerButton;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_club_owner_login);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mAuth = FirebaseAuth.getInstance();
        email = findViewById(R.id.et_Email);
        password = findViewById(R.id.edit_pass);
        loginButton = findViewById(R.id.button);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String emailText = email.getText().toString();
                String passwordText = password.getText().toString();

                // Firebase authentication login
                mAuth.signInWithEmailAndPassword(emailText, passwordText)
                        .addOnCompleteListener(club_owner_login.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Login success
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    Toast.makeText(club_owner_login.this, "Login successful!", Toast.LENGTH_SHORT).show();

                                    // Navigate to next activity
                                    Intent intent = new Intent(club_owner_login.this, club_owner_view.class);
                                    startActivity(intent);
                                } else {
                                    // If sign in fails, display a message to the user.
                                    Toast.makeText(club_owner_login.this, "Invalid email or password", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            // Handle the back button press
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
