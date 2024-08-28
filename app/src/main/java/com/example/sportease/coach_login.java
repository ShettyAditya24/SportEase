//package com.example.sportease;
//
//import android.content.Intent;
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
//public class coach_login extends AppCompatActivity {
//
//    EditText et_email,et_Pass;
//    Button loginbutton;
////    DbHelper db;
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        EdgeToEdge.enable(this);
//        setContentView(R.layout.activity_coach_login);
//
////        db = new DbHelper(this);
//         et_email = findViewById(R.id.et_Email);
//         et_Pass = findViewById(R.id.edit_pass);
//         loginbutton = findViewById(R.id.button);
//
////
////         loginbutton.setOnClickListener(new View.OnClickListener() {
////             @Override
////             public void onClick(View view) {
////                 String email = et_email.getText().toString();
////                 String password = et_Pass.getText().toString();
////
////
//////                 if(db.checkCoach(email,password)){
////                     Toast.makeText(coach_login.this, "Login Successful", Toast.LENGTH_SHORT).show();
////                     Intent intent =  new Intent(coach_login.this,coach_View.class);
////                     startActivity(intent);
////                 }
//////                 else {
////                     Toast.makeText(coach_login.this, "In valid Credential", Toast.LENGTH_SHORT).show();
////                 }
////             }
////         });
////
////    }
////}