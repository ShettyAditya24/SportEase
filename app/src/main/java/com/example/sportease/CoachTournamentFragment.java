package com.example.sportease;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sportease.R;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import adapters.TournamentAdapter;
import models.Tournament;

public class CoachTournamentFragment extends Fragment {

    private RecyclerView recyclerView;
    private TournamentAdapter tournamentAdapter;
    private List<Tournament> tournamentList = new ArrayList<>();
    private FirebaseFirestore db;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_coach_tournament, container, false);

        recyclerView = view.findViewById(R.id.recyclerViewTournaments);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        tournamentAdapter = new TournamentAdapter(tournamentList);
        recyclerView.setAdapter(tournamentAdapter);

        db = FirebaseFirestore.getInstance();
        loadTournaments();

        return view;
    }

    private void loadTournaments() {
        db.collection("tournaments")
                .orderBy("date", Query.Direction.ASCENDING)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        QuerySnapshot snapshots = task.getResult();
                        if (snapshots != null) {
                            tournamentList.clear();
                            tournamentList.addAll(snapshots.toObjects(Tournament.class));
                            tournamentAdapter.notifyDataSetChanged();
                        }
                    } else {
                        // Handle the error (e.g., show a message to the user)
                    }
                });
    }
}
