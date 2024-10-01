package com.example.sportease;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class ViewGround extends AppCompatActivity {

    private static final String TAG = "ViewGround";
    private RecyclerView recyclerView;
    private GroundAdapter groundAdapter;
    private List<Ground> groundList;
    private CollectionReference groundRef;
    private ProgressBar progressBar;
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_ground); // Ensure this XML has RecyclerView, ProgressBar, and BottomNavigationView

        // Initialize Views
        recyclerView = findViewById(R.id.groundRecyclerView);
        progressBar = findViewById(R.id.progressBar);
        bottomNavigationView = findViewById(R.id.bottomNavigationView);

        // Initialize Firestore reference
        groundRef = FirebaseFirestore.getInstance().collection("grounds");

        // Set up RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        progressBar.setVisibility(View.VISIBLE);

        groundList = new ArrayList<>();
        groundAdapter = new GroundAdapter(this, groundList);
        recyclerView.setAdapter(groundAdapter);

        // Fetch data from Firestore
        fetchGroundData();

        // Set click listener for RecyclerView items
        groundAdapter.setOnItemClickListener(new GroundAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Ground ground) {
                showGroundDetailFragment(ground);
            }
        });

        // Bottom Navigation Listener
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.nav_tournament) {
                Toast.makeText(ViewGround.this, "Tournaments selected", Toast.LENGTH_SHORT).show();
                return true;
            } else if (itemId == R.id.nav_profile) {
                Toast.makeText(ViewGround.this, "Profile selected", Toast.LENGTH_SHORT).show();
                return true;
            }
            return false;
        });
    }

    // Fetch Ground Data from Firestore
    private void fetchGroundData() {
        // Ensure the user is authenticated before fetching data
        FirebaseAuth auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() == null) {
            Log.e(TAG, "User is not authenticated");
            Toast.makeText(this, "Please log in to view grounds.", Toast.LENGTH_SHORT).show();
            return; // Exit if not authenticated
        }

        // Initialize Firestore reference
        CollectionReference groundRef = FirebaseFirestore.getInstance().collection("clubOwners");

        // Start fetching data
        groundRef.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                progressBar.setVisibility(View.GONE); // Hide the progress bar after fetching data

                if (e != null) {
                    Log.e(TAG, "Failed to load data.", e); // Log error if there's an issue
                    Toast.makeText(ViewGround.this, "Failed to load data. Please try again.", Toast.LENGTH_SHORT).show();
                    return; // Exit if there was an error
                }

                if (queryDocumentSnapshots != null && !queryDocumentSnapshots.isEmpty()) {
                    Log.d(TAG, "DocumentSnapshot count: " + queryDocumentSnapshots.size());
                    groundList.clear(); // Clear existing data in the list

                    // Loop through each document in the query result
                    for (DocumentSnapshot snapshot : queryDocumentSnapshots) {
                        Ground ground = snapshot.toObject(Ground.class); // Convert document to Ground object
                        if (ground != null) {
                            Log.d(TAG, "Ground loaded: " + ground.getClubName());
                            groundList.add(ground); // Add the ground to the list
                        } else {
                            Log.d(TAG, "Ground is null for document: " + snapshot.getId());
                        }
                    }
                    groundAdapter.notifyDataSetChanged(); // Notify adapter of data changes
                } else {
                    Log.d(TAG, "No data available");
                    Toast.makeText(ViewGround.this, "No grounds available.", Toast.LENGTH_SHORT).show(); // Inform user
                }
            }
        });
    }


    private void showGroundDetailFragment(Ground ground) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = GroundDetailFragment.newInstance(ground); // Pass necessary parameters
        fragmentManager.beginTransaction()
                .replace(R.id.fragment_container, fragment) // Ensure fragment_container exists in your layout
                .addToBackStack(null) // Add this transaction to the back stack
                .commit();
    }
}
