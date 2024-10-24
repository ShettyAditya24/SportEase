package adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sportease.R;

import java.util.List;

import models.Tournament;

public class TournamentAdapter extends RecyclerView.Adapter<TournamentAdapter.TournamentViewHolder> {

    private final List<Tournament> tournamentList;

    public TournamentAdapter(List<Tournament> tournamentList) {
        this.tournamentList = tournamentList;
    }

    @NonNull
    @Override
    public TournamentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_tournament, parent, false);
        return new TournamentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TournamentViewHolder holder, int position) {
        Tournament tournament = tournamentList.get(position);
        holder.tournamentNameTextView.setText(tournament.getName());
        holder.tournamentDateTextView.setText(tournament.getDate());
    }

    @Override
    public int getItemCount() {
        return tournamentList.size();
    }

    static class TournamentViewHolder extends RecyclerView.ViewHolder {

        TextView tournamentNameTextView;
        TextView tournamentDateTextView;

        public TournamentViewHolder(@NonNull View itemView) {
            super(itemView);
            tournamentNameTextView = itemView.findViewById(R.id.tournamentName);
            tournamentDateTextView = itemView.findViewById(R.id.tournamentDate);
        }
    }
}
