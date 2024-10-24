package com.example.sportease;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class ClubTournamentFragment extends Fragment {

    private static final String TAG = "ClubTournamentFragment"; // Tag for logging

    private EditText etTournamentName;
    private Button btnSelectDate, btnSelectTime, btnAddTournament;
    private String selectedDate, selectedTime;
    private FirebaseFirestore firestore;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_club_tournament, container, false);

        // Initialize views
        etTournamentName = view.findViewById(R.id.etTournamentName);
        btnSelectDate = view.findViewById(R.id.btnSelectDate);
        btnSelectTime = view.findViewById(R.id.btnSelectTime);
        btnAddTournament = view.findViewById(R.id.btnAddTournament);

        // Initialize Firestore
        firestore = FirebaseFirestore.getInstance();

        // Date picker
        btnSelectDate.setOnClickListener(v -> showDatePicker());

        // Time picker
        btnSelectTime.setOnClickListener(v -> showTimePicker());

        // Add tournament to Firestore
        btnAddTournament.setOnClickListener(v -> saveTournamentData());

        return view;
    }

    // Display DatePickerDialog to select date
    private void showDatePicker() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(requireContext(),
                (view, year1, month1, dayOfMonth) -> {
                    selectedDate = dayOfMonth + "/" + (month1 + 1) + "/" + year1;
                    btnSelectDate.setText(selectedDate);
                    Log.d(TAG, "Selected Date: " + selectedDate);
                }, year, month, day);
        datePickerDialog.show();
    }

    // Display TimePickerDialog to select time
    private void showTimePicker() {
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(requireContext(),
                (view, hourOfDay, minute1) -> {
                    selectedTime = String.format("%02d:%02d", hourOfDay, minute1);
                    btnSelectTime.setText(selectedTime);
                    Log.d(TAG, "Selected Time: " + selectedTime);
                }, hour, minute, true);
        timePickerDialog.show();
    }

    // Save tournament data to Firestore
    private void saveTournamentData() {
        String tournamentName = etTournamentName.getText().toString().trim();

        // Validate input
        if (TextUtils.isEmpty(tournamentName) || selectedDate == null || selectedTime == null) {
            Toast.makeText(getContext(), "Please fill all fields", Toast.LENGTH_SHORT).show();
            Log.e(TAG, "Validation failed: Empty fields.");
            return;
        }

        // Create a map to store tournament data
        Map<String, Object> tournament = new HashMap<>();
        tournament.put("name", tournamentName);
        tournament.put("date", selectedDate);
        tournament.put("time", selectedTime);

        Log.d(TAG, "Saving tournament data: " + tournament);

        // Save data to Firestore under "tournaments" collection
        firestore.collection("tournaments")
                .add(tournament)
                .addOnSuccessListener(documentReference -> {
                    Toast.makeText(getContext(), "Tournament added successfully", Toast.LENGTH_SHORT).show();
                    Log.i(TAG, "Tournament added with ID: " + documentReference.getId());
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(getContext(), "Failed to add tournament", Toast.LENGTH_SHORT).show();
                    Log.e(TAG, "Error adding tournament", e);
                });
    }
}
