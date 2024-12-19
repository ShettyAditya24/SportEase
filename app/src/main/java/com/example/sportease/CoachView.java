package com.example.sportease;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import Fragments.CoachProfileFragment;
import Fragments.CoachTournamentFragment;
import adapters.GroundAdapter;
import models.Ground;

public class CoachView extends AppCompatActivity {

    private RecyclerView recyclerView;
    private GroundAdapter groundAdapter;
    private List<Ground> groundList = new ArrayList<>();
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coach_view);

        // Initialize RecyclerView and adapter
        recyclerView = findViewById(R.id.recyclerViewGrounds);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        groundAdapter = new GroundAdapter(this, groundList);
        recyclerView.setAdapter(groundAdapter);

        // Initialize Firestore
        db = FirebaseFirestore.getInstance();
        loadGrounds(); // Load the list of grounds

        groundAdapter.setOnItemClickListener(ground -> showGroundDetailFragment(ground));
        // Setup bottom navigation view
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomCoachNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment selectedFragment = null;

//                 Fragment selection using if-else
//                if (item.getItemId() == R.id.nav_coach_tourna) {
//                    selectedFragment = new CoachTournamentFragment();
//            }
                  if (item.getItemId() == R.id.nav_coach_profile) {
                    selectedFragment = new CoachProfileFragment();
                }

                // Replace the fragment if one was selected
                if (selectedFragment != null) {
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragmentContainer, selectedFragment)
                            .addToBackStack(null) // Add transaction to back stack
                            .commit();
                }
                return true;
            }
        });
    }

    // Method to load grounds from Firestore
    private void loadGrounds() {
        db.collection("clubOwners")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        QuerySnapshot snapshots = task.getResult();
                        if (snapshots != null) {
                            groundList.clear();
                            groundList.addAll(snapshots.toObjects(Ground.class));
                            groundAdapter.notifyDataSetChanged(); // Refresh RecyclerView
                        }
                    } else {
                        // Handle the error (e.g., log the issue)
                    }
                });
    }

    @Override
    public void onBackPressed() {
        // Check if there are fragments in the back stack
        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            // If there are, pop the fragment from the back stack
            getSupportFragmentManager().popBackStack();
        } else {
            // If there are no fragments, call the super method to exit the activity
            super.onBackPressed();
        }
    }
    private void showGroundDetailFragment(Ground ground) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = CoachGroundDetail.newInstance(ground); // Ensure GroundDetailFragment is implemented
        fragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, fragment) // Ensure fragment_container exists in XML
                .addToBackStack(null)
                .commit();
    }
}
