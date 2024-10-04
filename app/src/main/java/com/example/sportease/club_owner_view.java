package com.example.sportease;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

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
    private CollectionReference bookedSlotsRef;
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
            Log.d(TAG, "Current user ID: " + clubOwnerId);
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

        // Reference to Firestore
        clubOwnerRef = FirebaseFirestore.getInstance().collection("clubOwners");
        bookedSlotsRef = FirebaseFirestore.getInstance().collection("bookedSlots"); // Reference to booked slots collection

        // Fetch uploaded images from Firestore
        fetchUploadedImages();

        // Fetch booked slots from Firestore
        fetchBookedSlots();

        fabAddSlot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addNewBookingSlot();
            }
        });
    }

    private void fetchUploadedImages() {
        Log.d(TAG, "Fetching uploaded images for club owner: " + clubOwnerId);
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
                                Log.d(TAG, "Fetched image URLs: " + imageUrls.toString());
                            } else {
                                Log.d(TAG, "No image URLs found in document.");
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

    private void fetchBookedSlots() {
        Log.d(TAG, "Fetching booked slots for club owner: " + clubOwnerId);
        // Fetch booked slots for this club owner
        bookedSlotsRef.whereEqualTo("groundId", clubOwnerId) // Assuming booked slots have clubOwnerId field
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (error != null) {
                            Log.e(TAG, "Error fetching booked slots", error);
                            Toast.makeText(club_owner_view.this, "Error loading booked slots", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        bookingSlotList.clear(); // Clear the current list
                        if (value != null) {
                            for (QueryDocumentSnapshot doc : value) {
                                BookingSlot slot = doc.toObject(BookingSlot.class); // Convert document to BookingSlot
                                if (slot != null) {
                                    bookingSlotList.add(slot); // Add each booked slot to the list
                                }
                            }
                        }

                        bookingSlotsAdapter.notifyDataSetChanged(); // Notify adapter of data change
                        Log.d(TAG, "Booked slots fetched: " + bookingSlotList.size() + " slots available.");
                    }
                });
    }

    private void addNewBookingSlot() {
        Toast.makeText(this, "Add new slot functionality", Toast.LENGTH_SHORT).show();
        Log.d(TAG, "Add new booking slot clicked.");
    }
}
