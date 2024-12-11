package com.example.sportease;

import android.os.Bundle;
import android.util.Log; // Import Log
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

import adapters.CoachBookingsAdapter;
import models.BookedSlots;

public class ClubCoachFragment extends Fragment {

    private static final String TAG = "UserCoachFragment"; // Add a TAG for logging
    private FirebaseFirestore db;
    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private List<BookedSlots> bookedSlots;
    private CoachBookingsAdapter adapter;

    public ClubCoachFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_club_coach, container, false);

        // Initialize Firestore and views
        db = FirebaseFirestore.getInstance();
        recyclerView = view.findViewById(R.id.recyclerClubViewCoachBookings);
        progressBar = view.findViewById(R.id.progressBarClub);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        bookedSlots = new ArrayList<>();
        adapter = new CoachBookingsAdapter(bookedSlots);
        recyclerView.setAdapter(adapter);

        // Load booked slots from Firestore
        loadBookedSlots();

        return view;
    }

    private void loadBookedSlots() {
        progressBar.setVisibility(View.VISIBLE);
        Log.d(TAG, "Loading booked slots..."); // Log the loading action

        CollectionReference bookedByRef = db.collection("bookedSlots");

        bookedByRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Log.d(TAG, "Successfully fetched bookedBy documents"); // Log success
                for (QueryDocumentSnapshot document : task.getResult()) {
                    String id = document.getString("id");  // Coach or user ID
                    String bookedBy = document.getString("bookedBy");  // "user" or "coach"
                    String groundId = document.getString("groundId");
                    String timeSlot = document.getString("timeSlot");

                    Log.d(TAG, "Document ID: " + document.getId() + " - ID: " + id + ", Booked By: " + bookedBy + ", Ground ID: " + groundId + ", Time Slot: " + timeSlot);

                    fetchCoachAndClubDetails(id, bookedBy, groundId, timeSlot);
                }
            } else {
                Log.e(TAG, "Failed to load data", task.getException()); // Log error
                Toast.makeText(getContext(), "Failed to load data", Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    private void fetchCoachAndClubDetails(String id, String bookedBy, String groundId, String timeSlot) {
        // Fetch coach name from 'coaches' collection
        Log.d(TAG, "Fetching coach details for ID: " + id);
        db.collection("coaches").document(id).get()
                .addOnSuccessListener(coachDoc -> {
                    if (coachDoc.exists()) {
                        String coachName = coachDoc.getString("name");
                        Log.d(TAG, "Fetched coach details: " + coachName);

                        // Fetch club name from 'clubOwners' collection
                        Log.d(TAG, "Fetching club details for Ground ID: " + groundId);
                        db.collection("clubOwners").document(groundId).get()
                                .addOnSuccessListener(clubDoc -> {
                                    if (clubDoc.exists()) {
                                        String clubName = clubDoc.getString("clubName");
                                        Log.d(TAG, "Fetched club details: " + clubName);

                                        // Create BookedSlots object and add it to the list
                                        BookedSlots slot = new BookedSlots(id, bookedBy, groundId, timeSlot, coachName, clubName);
                                        bookedSlots.add(slot);

                                        // Notify the adapter and hide the progress bar
                                        adapter.notifyDataSetChanged();
                                        progressBar.setVisibility(View.GONE);
                                        Log.d(TAG, "Added booked slot for coach: " + coachName + " at club: " + clubName);
                                    }
                                })
                                .addOnFailureListener(e -> {
                                    Log.e(TAG, "Failed to fetch club details", e); // Log error
                                    Toast.makeText(getContext(), "Failed to fetch club details", Toast.LENGTH_SHORT).show();
                                    progressBar.setVisibility(View.GONE);
                                });
                    } else {
                        Log.e(TAG, "Coach document does not exist for ID: " + id); // Log if the document does not exist
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Failed to fetch coach details", e); // Log error
                    Toast.makeText(getContext(), "Failed to fetch coach details", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                });
    }
}
