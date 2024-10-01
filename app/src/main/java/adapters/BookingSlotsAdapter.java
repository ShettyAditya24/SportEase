package adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sportease.R;

import java.util.ArrayList;
import java.util.List;

import models.BookingSlot;

public class BookingSlotsAdapter extends RecyclerView.Adapter<BookingSlotsAdapter.SlotViewHolder> {

    private final Context context;
    private List<BookingSlot> bookingSlotList;

    public BookingSlotsAdapter(Context context, List<BookingSlot> bookingSlotList) {
        this.context = context;
        this.bookingSlotList = bookingSlotList != null ? bookingSlotList : new ArrayList<>(); // Ensures a non-null list
    }

    @NonNull
    @Override
    public SlotViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_booking_slot, parent, false);
        return new SlotViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SlotViewHolder holder, int position) {
        BookingSlot slot = bookingSlotList.get(position);
        if (slot != null && slot.getTimeSlot() != null) {
            holder.tvSlotTime.setText(slot.getTimeSlot());
        } else {
            holder.tvSlotTime.setText(R.string.edit_slot); // Display a default message if slot is null
        }
    }

    @Override
    public int getItemCount() {
        return bookingSlotList != null ? bookingSlotList.size() : 0;
    }

    public void updateSlots(List<BookingSlot> newSlotList) {
        if (newSlotList != null) {
            bookingSlotList.clear();
            bookingSlotList.addAll(newSlotList); // Add all new slots to the existing list
        } else {
            bookingSlotList.clear(); // Clear the list if new data is null
        }
        notifyDataSetChanged(); // Notify adapter to refresh data
    }

    static class SlotViewHolder extends RecyclerView.ViewHolder {
        TextView tvSlotTime;

        public SlotViewHolder(@NonNull View itemView) {
            super(itemView);
            tvSlotTime = itemView.findViewById(R.id.tvSlotTime);
        }
    }
}
