package com.example.sportease;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import adapters.TournamentAdapter;
import models.Tournament;

public class UserTournamentFragment extends Fragment {

    private RecyclerView tournamentRecyclerView;
    private TournamentAdapter tournamentAdapter;
    private List<models.Tournament> tournamentList;
    private FirebaseFirestore firestore;
    private static final String TAG = "TournamentFragment";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Inflate the layout
        View view = inflater.inflate(R.layout.fragment_tournament_user, container, false);

        // Initialize views and Firestore
        tournamentRecyclerView = view.findViewById(R.id.tournamentRecyclerView);
        firestore = FirebaseFirestore.getInstance();
        tournamentList = new ArrayList<>();
        tournamentAdapter = new TournamentAdapter(tournamentList);

        // Set up RecyclerView
        tournamentRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        tournamentRecyclerView.setAdapter(tournamentAdapter);

        // Fetch tournament data from Firestore
        fetchTournamentData();

        return view;
    }

    private void fetchTournamentData() {
        CollectionReference tournamentRef = firestore.collection("tournaments");

        tournamentRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult() != null) {
                tournamentList.clear(); // Clear the list to avoid duplication
                QuerySnapshot snapshots = task.getResult();

                for (DocumentSnapshot snapshot : snapshots) {
                    Tournament tournament = snapshot.toObject(Tournament.class);
                    if (tournament != null) {
                        tournamentList.add(tournament);
                    }
                }

                tournamentAdapter.notifyDataSetChanged(); // Notify adapter about data changes

            } else {
                Log.e(TAG, "Error fetching tournaments: ", task.getException());
                Toast.makeText(getContext(), "Failed to load tournaments.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
