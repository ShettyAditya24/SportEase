package com.example.sportease;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class welcome_club_owner extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_welcome_club_owner);
        Button logbutton = findViewById(R.id.button1);
        Button sigbutton = findViewById(R.id.button2);

        logbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(welcome_club_owner.this, club_owner_login.class);
                startActivity(intent);
            }
        });
        sigbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(welcome_club_owner.this, club_owner_signup.class);
                startActivity(intent);
            }
        });



    }
}