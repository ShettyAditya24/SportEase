package com.example.sportease;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

public class GroundDetailFragment extends Fragment {

    private ImageView groundImageView;
    private TextView groundNameTextView, addressTextView, openTimeTextView, closeTimeTextView;
    private LinearLayout slotsContainer;

    private static final String ARG_GROUND = "ground";

    private Ground ground; // Your Ground class object

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
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ground_detail, container, false);

        // Initialize UI elements
        groundImageView = view.findViewById(R.id.groundImageView);
        groundNameTextView = view.findViewById(R.id.groundNameTextView);
        addressTextView = view.findViewById(R.id.addressTextView);
        openTimeTextView = view.findViewById(R.id.openTimeTextView);
        closeTimeTextView = view.findViewById(R.id.closeTimeTextView);
        slotsContainer = view.findViewById(R.id.slotsContainer);

        displayGroundDetails();

        return view;
    }

    private void displayGroundDetails() {
        if (ground != null) {
            // Load the ground image using Glide
            if (ground.getImageUrls() != null && !ground.getImageUrls().isEmpty()) {
                Glide.with(requireContext())
                        .load(ground.getImageUrls().get(0)) // Load the first image from the list
                        .placeholder(R.drawable.ic_placeholder)
                        .into(groundImageView);
            } else {
                groundImageView.setImageResource(R.drawable.ic_placeholder); // Set a placeholder if no image URL
            }

            // Set other ground details
            groundNameTextView.setText(ground.getClubName());
            addressTextView.setText(ground.getAddress());
            openTimeTextView.setText("Open Time: " + ground.getOpenTime());
            closeTimeTextView.setText("Close Time: " + ground.getCloseTime());

            // Display time slots
//            if (ground.getTimeSlots() != null) {
//                for (String slot : ground.getTimeSlots()) {
//                    addSlotView(slot);
//                }
//            }
        } else {
            Toast.makeText(requireContext(), "Failed to load ground details", Toast.LENGTH_SHORT).show();
        }
    }

    private void addSlotView(String slot) {
        // Inflate slot item layout
        View slotItem = LayoutInflater.from(getContext()).inflate(R.layout.slot_item, slotsContainer, false);

        TextView slotTextView = slotItem.findViewById(R.id.slotTextView);
        slotTextView.setText(slot);

        slotsContainer.addView(slotItem);
    }
}
