package com.example.spacecolony;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

/**
 * StatsAdapter - shows individual crew member statistics in the Stats screen.
 * Each row shows the crew member's name, missions played, wins, and training sessions.
 * Uses a simple TextView-based layout instead of the full crew card.
 */
public class StatsAdapter extends RecyclerView.Adapter<StatsAdapter.StatsViewHolder> {

    private List<CrewMember> crewList;
    private Context context;

    public StatsAdapter(Context context, List<CrewMember> crewList) {
        this.context = context;
        this.crewList = crewList;
    }

    @NonNull
    @Override
    public StatsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // We reuse the item_crew_member layout but only fill the text fields
        View view = LayoutInflater.from(context).inflate(R.layout.item_stats_row, parent, false);
        return new StatsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StatsViewHolder holder, int position) {
        CrewMember cm = crewList.get(position);

        // Show the crew member's name and specialization
        holder.tvStatName.setText(cm.getSpecialization() + ": " + cm.getName());

        // Show their performance numbers
        holder.tvStatDetails.setText(
                "Missions: " + cm.getMissionsCompleted() +
                        " | Wins: " + cm.getMissionsWon() +
                        " | Training: " + cm.getTrainingSessions() +
                        " | XP: " + cm.getExperience()
        );

        // Color the name based on specialization for visual clarity
        holder.tvStatName.setTextColor(getSpecializationColor(cm.getSpecialization()));
    }

    @Override
    public int getItemCount() {
        return crewList.size();
    }

    /**
     * getSpecializationColor() - matches each class to its color theme.
     */
    private int getSpecializationColor(String specialization) {
        switch (specialization) {
            case "Pilot":     return 0xFF4FC3F7; // Blue
            case "Engineer":  return 0xFFFFD54F; // Yellow
            case "Medic":     return 0xFF81C784; // Green
            case "Scientist": return 0xFFCE93D8; // Purple
            case "Soldier":   return 0xFFEF9A9A; // Red
            default:          return 0xFFFFFFFF;
        }
    }

    public static class StatsViewHolder extends RecyclerView.ViewHolder {
        TextView tvStatName, tvStatDetails;

        public StatsViewHolder(@NonNull View itemView) {
            super(itemView);
            tvStatName = itemView.findViewById(R.id.tvStatName);
            tvStatDetails = itemView.findViewById(R.id.tvStatDetails);
        }
    }
}