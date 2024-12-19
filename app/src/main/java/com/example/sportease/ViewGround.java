package com.example.sportease;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import Fragments.GroundDetailFragment;
import Fragments.UserCoachFragment;
import Fragments.UserProfileFragment;
import Fragments.UserTournamentFragment;
import adapters.GroundAdapter;
import models.Ground;

public class ViewGround extends AppCompatActivity {

    private static final String TAG = "ViewGround";
    private RecyclerView recyclerView;
    private GroundAdapter groundAdapter;
    private List<Ground> groundList;
    private List<Ground> fullGroundList; // For storing the unfiltered list
    private CollectionReference groundRef;
    private ProgressBar progressBar;
    private BottomNavigationView bottomNavigationView;
    private SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_ground);

        // Initialize Views
        recyclerView = findViewById(R.id.groundRecyclerView);
        progressBar = findViewById(R.id.progressBar);
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        searchView = findViewById(R.id.searchView);

        // Initialize Firestore reference
        groundRef = FirebaseFirestore.getInstance().collection("clubOwners");

        // Set up RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        progressBar.setVisibility(View.VISIBLE);

        groundList = new ArrayList<>();
        fullGroundList = new ArrayList<>();
        groundAdapter = new GroundAdapter(this, groundList);
        recyclerView.setAdapter(groundAdapter);

        // Fetch data from Firestore
        fetchGroundData();

        // Set click listener for RecyclerView items
        groundAdapter.setOnItemClickListener(ground -> showGroundDetailFragment(ground));

        // Bottom Navigation Listener
        bottomNavigationView.setOnItemSelectedListener(item -> {
            Fragment selectedFragment = null;
            int itemId = item.getItemId();

            // Use if-else to determine which fragment to display
            if (itemId == R.id.nav_tournament) {
                selectedFragment = new UserTournamentFragment();
            } else if (itemId == R.id.nav_profile) {
                selectedFragment = new UserProfileFragment();
            } else if (itemId == R.id.nav_coach) {
                selectedFragment = new UserCoachFragment(); // Ensure this is correct
            }

            if (selectedFragment != null) {
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container, selectedFragment) // Ensure fragment_container exists in XML
                        .addToBackStack(null)
                        .commit();
                return true;
            }
            return false;
        });

        // Search functionality
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                filterGrounds(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterGrounds(newText);
                return true;
            }
        });
    }

    // Fetch Ground Data from Firestore
    private void fetchGroundData() {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() == null) {
            Log.e(TAG, "User is not authenticated");
            Toast.makeText(this, "Please log in to view grounds.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Start fetching data
        groundRef.addSnapshotListener((queryDocumentSnapshots, e) -> {
            progressBar.setVisibility(View.GONE); // Hide the progress bar

            if (e != null) {
                Log.e(TAG, "Failed to load data.", e);
                Toast.makeText(ViewGround.this, "Failed to load data. Please try again.", Toast.LENGTH_SHORT).show();
                return;
            }

            if (queryDocumentSnapshots != null && !queryDocumentSnapshots.isEmpty()) {
                groundList.clear();
                fullGroundList.clear();

                // Loop through documents and add to the list
                for (DocumentSnapshot snapshot : queryDocumentSnapshots) {
                    Ground ground = snapshot.toObject(Ground.class);
                    if (ground != null) {
                        groundList.add(ground);
                        fullGroundList.add(ground); // Keep a full copy for filtering
                    } else {
                        Log.d(TAG, "Ground is null for document: " + snapshot.getId());
                    }
                }
                groundAdapter.notifyDataSetChanged();
            } else {
                Toast.makeText(ViewGround.this, "No grounds available.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Filter grounds based on search query
    private void filterGrounds(String query) {
        if (query == null || query.isEmpty()) {
            groundList.clear();
            groundList.addAll(fullGroundList); // Restore full list when query is empty
        } else {
            String lowerCaseQuery = query.toLowerCase();
            groundList.clear();
            groundList.addAll(fullGroundList.stream()
                    .filter(ground -> ground.getClubName().toLowerCase().contains(lowerCaseQuery))
                    .collect(Collectors.toList()));
        }
        groundAdapter.notifyDataSetChanged();
    }

    // Show Ground Detail Fragment
    private void showGroundDetailFragment(Ground ground) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = GroundDetailFragment.newInstance(ground); // Ensure GroundDetailFragment is implemented
        fragmentManager.beginTransaction()
                .replace(R.id.fragment_container, fragment) // Ensure fragment_container exists in XML
                .addToBackStack(null)
                .commit();
    }
}
