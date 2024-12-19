package com.example.sportease.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.sportease.ExpenseActivity;
import com.example.sportease.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class ClubProfileFragment extends Fragment {

    private static final String TAG = "ProfileFragment";
    private TextView tvClubOwnerName;
    private TextView tvClubOwnerEmail;
    private Button btnEditProfile;
    private Button btnLogout;
    private Button btnViewExpenseChart;

    private FirebaseAuth mAuth;
    private FirebaseFirestore firestore;

    private String clubOwnerId;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_club_owner_profile, container, false);

        // Initialize Firebase components
        mAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        clubOwnerId = mAuth.getCurrentUser().getUid(); // Get current user's ID

        // Initialize UI components
        tvClubOwnerName = view.findViewById(R.id.tvClubOwnerName);
        tvClubOwnerEmail = view.findViewById(R.id.tvClubOwnerEmail);
        btnEditProfile = view.findViewById(R.id.btnEditProfile);
        btnLogout = view.findViewById(R.id.btnLogout);
        btnViewExpenseChart=view.findViewById(R.id.btnViewExpenseChart);

        // Load club owner's information
        loadClubOwnerInfo();
        btnViewExpenseChart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity()  , ExpenseActivity.class);
                startActivity(intent);

            }
        });

        // Edit Profile button click listener
        btnEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showEditProfileDialog();
            }
        });

        // Logout button click listener
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
            }
        });

        return view;
    }

    private void loadClubOwnerInfo() {
        firestore.collection("clubOwners").document(clubOwnerId)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null) {
                        String name = task.getResult().getString("clubOwnerName");
                        String email = task.getResult().getString("email");

                        tvClubOwnerName.setText(name);
                        tvClubOwnerEmail.setText(email);
                    } else {
                        Log.e(TAG, "Error loading club owner info", task.getException());
                        Toast.makeText(getContext(), "Error loading information", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void showEditProfileDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Edit Profile");

        // Input field for new name
        final EditText input = new EditText(getActivity());
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        input.setHint("Enter your name");
        builder.setView(input);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String newName = input.getText().toString().trim();
                updateProfileName(newName);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    private void updateProfileName(String newName) {
        if (!newName.isEmpty()) {
            firestore.collection("clubOwners").document(clubOwnerId)
                    .update("clubOwnerName", newName)
                    .addOnSuccessListener(aVoid -> {
                        tvClubOwnerName.setText(newName);
                        Toast.makeText(getContext(), "Name updated successfully", Toast.LENGTH_SHORT).show();
                    })
                    .addOnFailureListener(e -> {
                        Log.e(TAG, "Error updating name", e);
                        Toast.makeText(getContext(), "Error updating name", Toast.LENGTH_SHORT).show();
                    });
        } else {
            Toast.makeText(getContext(), "Name cannot be empty", Toast.LENGTH_SHORT).show();
        }
    }

    private void logout() {
        mAuth.signOut();
        Toast.makeText(getContext(), "Logged out successfully", Toast.LENGTH_SHORT).show();
        getActivity().finish(); // Close the activity
    }
}
