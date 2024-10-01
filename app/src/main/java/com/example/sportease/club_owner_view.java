package com.example.sportease;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import adapters.BookingSlotsAdapter;
import adapters.UploadedImagesAdapter;
import models.BookingSlot;

public class club_owner_view extends AppCompatActivity {

    private static final String TAG = "ClubOwnerView";
    private RecyclerView recyclerViewUploadedImages;
    private RecyclerView recyclerViewBookingSlots;
    private UploadedImagesAdapter uploadedImagesAdapter;
    private BookingSlotsAdapter bookingSlotsAdapter;
    private List<String> uploadedImagesList;
    private List<BookingSlot> bookingSlotList;
    private CollectionReference clubOwnerRef;
    private FloatingActionButton fabAddSlot;
    private String clubOwnerId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_club_owner_view);

        // Initialize Firebase Auth
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser != null) {
            clubOwnerId = currentUser.getUid();
        } else {
            Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        recyclerViewUploadedImages = findViewById(R.id.recyclerViewUploadedImages);
        recyclerViewBookingSlots = findViewById(R.id.recyclerViewBookingSlots);
        fabAddSlot = findViewById(R.id.fabAddSlot);

        recyclerViewUploadedImages.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recyclerViewBookingSlots.setLayoutManager(new LinearLayoutManager(this));

        uploadedImagesList = new ArrayList<>();
        bookingSlotList = new ArrayList<>();

        uploadedImagesAdapter = new UploadedImagesAdapter(this, uploadedImagesList);
        bookingSlotsAdapter = new BookingSlotsAdapter(this, bookingSlotList);

        recyclerViewUploadedImages.setAdapter(uploadedImagesAdapter);
        recyclerViewBookingSlots.setAdapter(bookingSlotsAdapter);

        // Reference to Firestore (Assuming club owner data is stored in "clubOwners" collection)
        clubOwnerRef = FirebaseFirestore.getInstance().collection("clubOwners");

        // Fetch uploaded images from Firestore
        fetchUploadedImages();

        // Fetch booking slots from Firestore
        fetchBookingSlots();

        fabAddSlot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addNewBookingSlot();
            }
        });
    }

    private void fetchUploadedImages() {
        clubOwnerRef.document(clubOwnerId)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document != null && document.exists()) {
                            // Fetch the 'imageUrls' array from Firestore
                            List<String> imageUrls = (List<String>) document.get("imageUrls");
                            if (imageUrls != null) {
                                uploadedImagesList.clear();
                                uploadedImagesList.addAll(imageUrls);
                                uploadedImagesAdapter.notifyDataSetChanged();
                                Log.d(TAG, "Fetched image URLs: " + imageUrls.toString()); // Log the fetched URLs
                            }
                        } else {
                            Log.d(TAG, "No such document");
                            Toast.makeText(club_owner_view.this, "No images found", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Log.e(TAG, "Error fetching images", task.getException());
                        Toast.makeText(club_owner_view.this, "Error loading uploaded images", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void fetchBookingSlots() {
        clubOwnerRef.document(clubOwnerId).get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                String openTime = documentSnapshot.getString("openTime");
                String closeTime = documentSnapshot.getString("closeTime");

                if (openTime != null && closeTime != null) {
                    generateTwoHourSlots(openTime, closeTime);
                }
            }
        }).addOnFailureListener(e -> {
            Log.e(TAG, "Error loading club owner data", e);
            Toast.makeText(club_owner_view.this, "Error loading club owner data", Toast.LENGTH_SHORT).show();
        });
    }

    private void generateTwoHourSlots(String openTime, String closeTime) {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.getDefault());
        try {
            Date startTime = sdf.parse(openTime);
            Date endTime = sdf.parse(closeTime);

            if (startTime != null && endTime != null) {
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(startTime);

                bookingSlotList.clear();

                while (calendar.getTime().before(endTime)) {
                    Date slotStart = calendar.getTime();
                    calendar.add(Calendar.HOUR_OF_DAY, 2);
                    Date slotEnd = calendar.getTime();

                    if (slotEnd.after(endTime)) {
                        slotEnd = endTime;
                    }

                    String slotText = sdf.format(slotStart) + " - " + sdf.format(slotEnd);
                    bookingSlotList.add(new BookingSlot(slotText));
                }

                bookingSlotsAdapter.updateSlots(bookingSlotList); // Update adapter with new slots
            }
        } catch (ParseException e) {
            Log.e(TAG, "Error parsing time", e);
            Toast.makeText(this, "Error generating booking slots", Toast.LENGTH_SHORT).show();
        }
    }

    private void addNewBookingSlot() {
        Toast.makeText(this, "Add new slot functionality", Toast.LENGTH_SHORT).show();
    }
}
