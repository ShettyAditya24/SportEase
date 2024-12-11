package adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sportease.R;

import java.util.List;
import models.BookingSlot;

public class BookingSlotsAdapter extends RecyclerView.Adapter<BookingSlotsAdapter.ViewHolder> {

    private List<BookingSlot> bookingSlots;
    private Context context;

    public BookingSlotsAdapter(Context context, List<BookingSlot> bookingSlots) {
        this.context = context;
        this.bookingSlots = bookingSlots;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_booking_slot, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        BookingSlot slot = bookingSlots.get(position);

        // Log for debugging
        Log.d("BookingSlotsAdapter", "Binding slot: " + slot.getTimeSlot() + ", Coach: " + slot.getCoachName());

        holder.tvSlotTime.setText(slot.getTimeSlot());
        holder.textViewCoachName.setText(slot.getCoachName());

        // Optional: If you have a client name to display
        // holder.textViewClientName.setText(slot.getClientName());
    }

    @Override
    public int getItemCount() {
        return bookingSlots.size();
    }

    public void updateSlots(List<BookingSlot> newSlots) {
        this.bookingSlots = newSlots;
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvSlotTime;
        TextView textViewCoachName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvSlotTime = itemView.findViewById(R.id.tvSlotTime);
            textViewCoachName = itemView.findViewById(R.id.textViewCoachName);
        }
    }
}
