package adapters;



import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sportease.R;

import java.util.List;

import models.BookedSlot;
import models.BookedSlots;

public class CoachBookingsAdapter extends RecyclerView.Adapter<CoachBookingsAdapter.ViewHolder> {

    private List<BookedSlots> bookedSlots;

    public CoachBookingsAdapter(List<BookedSlots> bookedSlots) {
        this.bookedSlots = bookedSlots;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_coach, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        BookedSlots slot = bookedSlots.get(position);
        holder.coachNameTextView.setText(slot.getCoachName());
        holder.clubNameTextView.setText(slot.getClubName());
    }

    @Override
    public int getItemCount() {
        return bookedSlots.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView coachNameTextView, clubNameTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            coachNameTextView = itemView.findViewById(R.id.textViewCoachName);
            clubNameTextView = itemView.findViewById(R.id.textViewClubName);
        }
    }
}
