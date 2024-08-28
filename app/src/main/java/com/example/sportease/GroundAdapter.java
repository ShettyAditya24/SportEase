package com.example.sportease;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class GroundAdapter extends RecyclerView.Adapter<GroundAdapter.GroundViewHolder> {

    private static final String TAG = "GroundAdapter";
    private Context context;
    private List<Ground> groundList;

    public GroundAdapter(Context context, List<Ground> groundList) {
        this.context = context;
        this.groundList = groundList;
    }

    @NonNull
    @Override
    public GroundViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_ground, parent, false);
        return new GroundViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GroundViewHolder holder, int position) {
        Ground ground = groundList.get(position);

        if (ground != null) {
            String description = ground.getFormattedDescription();
            holder.groundDescriptionTextView.setText(description);
            Log.d(TAG, "Binding ground at position " + position + ": " + description);

            // Check if imageUrl is valid before loading with Glide
            String imageUrl = ground.getImageUrl();
            if (imageUrl != null && !imageUrl.isEmpty()) {
                Glide.with(context)
                        .load(imageUrl)
                        .placeholder(R.drawable.ic_placeholder) // Placeholder image
                        .error(R.drawable.ic_error) // Error image
                        .into(holder.groundImageView);
            } else {
                // Optionally set a fallback image or do nothing if the URL is invalid
                holder.groundImageView.setImageResource(R.drawable.ic_placeholder);
                Log.d(TAG, "No valid image URL for ground at position: " + position);
            }
        } else {
            Log.d(TAG, "Ground is null at position: " + position);
        }
    }

    @Override
    public int getItemCount() {
        return groundList.size();
    }

    class GroundViewHolder extends RecyclerView.ViewHolder {

        ImageView groundImageView;
        TextView groundDescriptionTextView;

        public GroundViewHolder(@NonNull View itemView) {
            super(itemView);
            groundImageView = itemView.findViewById(R.id.groundImageView);
            groundDescriptionTextView = itemView.findViewById(R.id.groundDescriptionTextView);

            // Set click listener on the image to show/hide the description
            groundImageView.setOnClickListener(v -> {
                groundDescriptionTextView.setVisibility(
                        groundDescriptionTextView.getVisibility() == View.GONE ? View.VISIBLE : View.GONE
                );
                Log.d(TAG, "Toggled description visibility for ground at position: " + getAdapterPosition());
            });
        }
    }
}
