package com.example.sportease;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;

public class ViewGround extends AppCompatActivity {

    private static final String TAG = "ViewGround"; // Tag for logging
    private RecyclerView recyclerView;
    private GroundAdapter groundAdapter;
    private List<Ground> groundList;
    private CollectionReference groundRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_ground);
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();

        if (user != null) {
            Log.d(TAG, "User ID: " + user.getUid());
        } else {
            Log.d(TAG, "No authenticated user found.");
            Toast.makeText(ViewGround.this, "User not authenticated. Please log in.", Toast.LENGTH_SHORT).show();
            return; // Exit if user is not authenticated
        }


        // Initialize RecyclerView and adapter
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2)); // 2 is the span count

        groundList = new ArrayList<>();
        groundAdapter = new GroundAdapter(this, groundList);
        recyclerView.setAdapter(groundAdapter);

        // Reference to the 'clubOwners' collection in Firestore
        groundRef = FirebaseFirestore.getInstance().collection("clubOwners");
        Log.d(TAG, "Firestore reference: " + groundRef.getPath());

        // Fetch data from Firestore
        groundRef.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.e(TAG, "Failed to load data.", e);
                    Toast.makeText(ViewGround.this, "Failed to load data. Please try again.", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (queryDocumentSnapshots != null && !queryDocumentSnapshots.isEmpty()) {
                    Log.d(TAG, "DocumentSnapshot count: " + queryDocumentSnapshots.size());
                    groundList.clear();
                    for (DocumentSnapshot snapshot : queryDocumentSnapshots) {
                        Ground ground = snapshot.toObject(Ground.class);
                        if (ground != null) {
                            Log.d(TAG, "Ground loaded: " + ground.getFormattedDescription());
                            groundList.add(ground);
                        } else {
                            Log.d(TAG, "Ground is null for document: " + snapshot.getId());
                        }
                    }
                    groundAdapter.notifyDataSetChanged();
                } else {
                    Log.d(TAG, "No data available");
                }
            }
        });
    }
}
