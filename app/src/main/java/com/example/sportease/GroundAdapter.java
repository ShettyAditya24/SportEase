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
    private final Context context;
    private final List<Ground> groundList;
    private OnItemClickListener onItemClickListener;

    public GroundAdapter(Context context, List<Ground> groundList) {
        this.context = context;
        this.groundList = groundList;
    }

    public interface OnItemClickListener {
        void onItemClick(Ground ground);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
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

        // Set the club name text safely
        String clubName = ground.getClubName();
        if (clubName != null && !clubName.isEmpty()) {
            holder.clubNameTextView.setText(clubName);
            Log.d(TAG, "Setting club name: " + clubName);
        } else {
            Log.e(TAG, "Club name is null or empty at position " + position);
            holder.clubNameTextView.setText("Unknown Club");
        }

        // Load the image using Glide
        List<String> imageUrls = ground.getImageUrls();
        if (imageUrls != null && !imageUrls.isEmpty()) {
            String imageUrl = imageUrls.get(0);
            if (imageUrl != null && !imageUrl.isEmpty()) {
                Log.d(TAG, "Loading image URL: " + imageUrl);
                Glide.with(context)
                        .load(imageUrl)
                        .placeholder(R.drawable.ic_placeholder)
                        .error(R.drawable.ic_error)
                        .into(holder.groundImageView);
            } else {
                Log.d(TAG, "Image URL is null or empty, setting placeholder.");
                holder.groundImageView.setImageResource(R.drawable.ic_placeholder);
            }
        } else {
            Log.d(TAG, "No image URL available, setting placeholder.");
            holder.groundImageView.setImageResource(R.drawable.ic_placeholder);
        }

        // Set the click listener for the item
        holder.itemView.setOnClickListener(v -> {
            if (onItemClickListener != null) {
                onItemClickListener.onItemClick(ground);
            }
        });
    }

    @Override
    public int getItemCount() {
        return groundList != null ? groundList.size() : 0;
    }

    static class GroundViewHolder extends RecyclerView.ViewHolder {
        final TextView clubNameTextView;
        final ImageView groundImageView;

        public GroundViewHolder(@NonNull View itemView) {
            super(itemView);

            // Initialize the views from the layout
            clubNameTextView = itemView.findViewById(R.id.clubNameTextView);
            groundImageView = itemView.findViewById(R.id.groundImageView);
        }
    }
}
