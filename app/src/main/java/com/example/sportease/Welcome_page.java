package com.example.sportease;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

//import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;


public class Welcome_page extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_welcome_page);
        Button co_button = findViewById(R.id.club_owner_button);
        Button user_button = findViewById(R.id.user_button);
        Button coach_button = findViewById(R.id.coach_button);

        co_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =new Intent(Welcome_page.this, welcome_club_owner.class);
                startActivity(intent);
            }
        });
        user_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =new Intent(Welcome_page.this, MainActivity.class);
                startActivity(intent);
            }
        });
//        coach_button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent =new Intent(Welcome_page.this, welcome_coach.class);
//                startActivity(intent);
//            }
//        });


    }
}