package com.example.sportease;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.sportease.loginActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class UserProfileFragment extends Fragment {

    private TextView tvUserName, tvLastName, tvGmail;
    private Button btnLogout;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firestore;

    @SuppressLint("MissingInflatedId")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        // Initialize Firebase instances
        firebaseAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();

        // Initialize UI elements
        tvUserName = view.findViewById(R.id.tvUserName);
        tvLastName = view.findViewById(R.id.tvLastName);
        tvGmail = view.findViewById(R.id.tvGmail);
        btnLogout = view.findViewById(R.id.btnLogout);

        // Fetch and display user data
        loadUserData();

        // Logout button click listener
        btnLogout.setOnClickListener(v -> logoutUser());

        return view;
    }

    private void loadUserData() {
        String userId = firebaseAuth.getCurrentUser().getUid();
        DocumentReference userRef = firestore.collection("users").document(userId);

        userRef.get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                // Retrieve data from Firestore and set it to TextViews
                String name = documentSnapshot.getString("username");
                String Lastname = documentSnapshot.getString("lastname");
                String email = documentSnapshot.getString("email");

                tvUserName.setText(name);
                tvLastName.setText(Lastname);
                tvGmail.setText(email);
            } else {
                Toast.makeText(getContext(), "User data not found", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(e ->
                Toast.makeText(getContext(), "Error fetching data: " + e.getMessage(), Toast.LENGTH_SHORT).show()
        );
    }

    private void logoutUser() {
        firebaseAuth.signOut();
        Toast.makeText(getContext(), "Logged out successfully", Toast.LENGTH_SHORT).show();

        // Redirect to Login Activity
        Intent intent = new Intent(getActivity(), loginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        getActivity().finish(); // Close the current activity
    }
}
