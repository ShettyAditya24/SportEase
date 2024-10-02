package com.example.sportease;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import models.BookedSlot;

public class GroundDetailFragment extends Fragment {
    // UI elements
    private ImageView groundImageView;
    private TextView groundNameTextView, addressTextView, openTimeTextView, closeTimeTextView;
    private LinearLayout slotsContainer;
    private static final String ARG_GROUND = "ground";
    private Ground ground;
    private List<BookedSlot> bookedSlots = new ArrayList<>();
    private static final String TAG = "GroundDetailFragment"; // Log tag

    public static GroundDetailFragment newInstance(Ground ground) {
        GroundDetailFragment fragment = new GroundDetailFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_GROUND, ground);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            ground = getArguments().getParcelable(ARG_GROUND);
            Log.d(TAG, "Ground received: " + ground.getClubName());
            loadBookedSlotsFromFirestore(); // Load booked slots from Firestore
        } else {
            Log.e(TAG, "No ground data received!");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ground_detail, container, false);
        groundImageView = view.findViewById(R.id.groundImageView);
        groundNameTextView = view.findViewById(R.id.groundNameTextView);
        addressTextView = view.findViewById(R.id.addressTextView);
        openTimeTextView = view.findViewById(R.id.openTimeTextView);
        closeTimeTextView = view.findViewById(R.id.closeTimeTextView);
        slotsContainer = view.findViewById(R.id.slotsContainer);

        Log.d(TAG, "Ground detail fragment view created.");
        displayGroundDetails();
        return view;
    }

    private void displayGroundDetails() {
        if (ground != null) {
            Log.d(TAG, "Displaying ground details for: " + ground.getClubName());

            // Load the ground image using Glide
            if (ground.getImageUrls() != null && !ground.getImageUrls().isEmpty()) {
                Log.d(TAG, "Loading ground image.");
                Glide.with(requireContext())
                        .load(ground.getImageUrls().get(0))
                        .placeholder(R.drawable.ic_placeholder)
                        .into(groundImageView);
            } else {
                Log.d(TAG, "No image URLs found, using placeholder.");
                groundImageView.setImageResource(R.drawable.ic_placeholder);
            }

            groundNameTextView.setText(ground.getClubName());
            addressTextView.setText(ground.getAddress());
            openTimeTextView.setText("Open Time: " + ground.getOpenTime());
            closeTimeTextView.setText("Close Time: " + ground.getCloseTime());

            // Display available slots
            List<String> availableSlots = generateTimeSlots(ground.getOpenTime(), ground.getCloseTime());
            Log.d(TAG, "Generated available slots: " + availableSlots);
            showAvailableSlots(availableSlots);
        } else {
            Log.e(TAG, "Ground is null, cannot display details.");
            Toast.makeText(requireContext(), "Failed to load ground details", Toast.LENGTH_SHORT).show();
        }
    }

    private List<String> generateTimeSlots(String openTime, String closeTime) {
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
        SimpleDateFormat displayFormat = new SimpleDateFormat("hh:mm a", Locale.getDefault()); // For 12-hour display
        List<String> slots = new ArrayList<>();

        try {
            Date openDate = timeFormat.parse(openTime);
            Date closeDate = timeFormat.parse(closeTime);

            if (openDate != null && closeDate != null) {
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(openDate);

                while (calendar.getTime().before(closeDate)) {
                    Date slotStart = calendar.getTime();
                    calendar.add(Calendar.HOUR, 1); // 1-hour slots
                    Date slotEnd = calendar.getTime();

                    if (slotEnd.after(closeDate)) break;

                    slots.add(displayFormat.format(slotStart) + " - " + displayFormat.format(slotEnd));
                }
                Log.d(TAG, "Time slots generated: " + slots);
            } else {
                Log.e(TAG, "Failed to parse open or close time.");
            }
        } catch (ParseException e) {
            Log.e(TAG, "Error parsing time: ", e);
        }

        return slots;
    }

    private void showAvailableSlots(List<String> availableSlots) {
        Log.d(TAG, "Displaying available slots.");
        for (String slot : availableSlots) {
            boolean isBooked = false;
            for (BookedSlot bookedSlot : bookedSlots) {
                if (bookedSlot.getTimeSlot().equals(slot)) {
                    isBooked = true;
                    break;
                }
            }

            if (!isBooked) {
                Log.d(TAG, "Slot available: " + slot);
                addSlotView(slot);
            } else {
                Log.d(TAG, "Slot already booked: " + slot);
            }
        }
    }

    private void addSlotView(String slot) {
        View slotItem = LayoutInflater.from(getContext()).inflate(R.layout.slot_item, slotsContainer, false);
        TextView slotTextView = slotItem.findViewById(R.id.slotTextView);
        slotTextView.setText(slot);
        Button bookButton = slotItem.findViewById(R.id.bookButton);

        // Set the click listener to book the slot
        bookButton.setOnClickListener(v -> bookSlot(slot));

        slotsContainer.addView(slotItem);
    }

    private void bookSlot(String slot) {
        for (BookedSlot bookedSlot : bookedSlots) {
            if (bookedSlot.getTimeSlot().equals(slot)) {
                Log.d(TAG, "Attempt to book an already booked slot: " + slot);
                Toast.makeText(requireContext(), "Slot already booked!", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser == null) {
            Log.e(TAG, "User not authenticated!");
            Toast.makeText(requireContext(), "Please log in to book a slot.", Toast.LENGTH_SHORT).show();
            return;
        }

        String userId = currentUser.getUid(); // Use actual user ID
        BookedSlot newBookedSlot = new BookedSlot(userId, ground.getGroundId(), slot); // Pass groundId
        bookedSlots.add(newBookedSlot);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("bookedSlots") // This will create the collection if it doesn't exist
                .add(newBookedSlot)
                .addOnSuccessListener(documentReference -> {
                    Log.d(TAG, "Slot booked successfully: " + slot);
                    Toast.makeText(requireContext(), "Slot booked successfully!", Toast.LENGTH_SHORT).show();
                    refreshSlotsUI(); // Call this to refresh available slots in the UI
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Failed to book slot: " + slot, e);
                    Toast.makeText(requireContext(), "Failed to book slot.", Toast.LENGTH_SHORT).show();
                });
    }

    private void refreshSlotsUI() {
        slotsContainer.removeAllViews(); // Clear existing views
        List<String> availableSlots = generateTimeSlots(ground.getOpenTime(), ground.getCloseTime());
        showAvailableSlots(availableSlots); // Redisplay available slots
    }

    private void loadBookedSlotsFromFirestore() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("bookedSlots")
                .whereEqualTo("groundId", ground.getGroundId()) // Ensure groundId matches
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Log.d(TAG, "Booked slots loaded successfully for ground: " + ground.getGroundId());
                        bookedSlots.clear(); // Clear previous booked slots
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            BookedSlot slot = document.toObject(BookedSlot.class);
                            bookedSlots.add(slot);
                        }
                        refreshSlotsUI(); // Refresh UI with booked slots
                    } else {
                        Log.e(TAG, "Failed to load booked slots.");
                        Toast.makeText(requireContext(), "Failed to load booked slots.", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
