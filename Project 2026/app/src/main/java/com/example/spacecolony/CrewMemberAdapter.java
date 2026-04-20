package com.example.spacecolony;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

/**
 * CrewMemberAdapter, this connects our crew member data to the RecyclerView UI.
 * Each row shows a crew member card with their stats, energy bar and a checkbox.
 * The checkbox lets users select multiple crew members for training or moving.
 */
public class CrewMemberAdapter extends RecyclerView.Adapter<CrewMemberAdapter.CrewViewHolder> {

    private List<CrewMember> crewList;
    private final Context context;
    private final List<CrewMember> selectedCrew = new ArrayList<>();
    private OnCrewClickListener clickListener;

    public interface OnCrewClickListener {
        void onCrewClick(CrewMember crewMember);
    }

    public CrewMemberAdapter(Context context, List<CrewMember> crewList) {
        this.context = context;
        this.crewList = crewList;
    }

    public void setOnCrewClickListener(OnCrewClickListener listener) {
        this.clickListener = listener;
    }

    @NonNull
    @Override
    public CrewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_crew_member, parent, false);
        return new CrewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CrewViewHolder holder, int position) {
        CrewMember cm = crewList.get(position);

        // Fills in the crew member info
        holder.tvCrewName.setText(cm.getName());
        holder.tvSpecialization.setText(cm.getSpecialization());
        holder.tvStats.setText("Skill:" + (cm.getSkill() + cm.getExperience())
                + " | Res:" + cm.getResilience()
                + " | XP:" + cm.getExperience());

        // Energy bar percentage
        int energyPercent = (int) ((cm.getEnergy() / (float) cm.getMaxEnergy()) * 100);
        holder.pbEnergy.setProgress(energyPercent);
        holder.tvEnergy.setText("Energy: " + cm.getEnergy() + "/" + cm.getMaxEnergy());

        // It Colors the energy bar based on how full it is
        if (energyPercent > 60) {
            holder.pbEnergy.setProgressTintList(
                    ColorStateList.valueOf(Color.parseColor("#4CAF50")));
        } else if (energyPercent > 30) {
            holder.pbEnergy.setProgressTintList(
                    ColorStateList.valueOf(Color.parseColor("#FF9800")));
        } else {
            holder.pbEnergy.setProgressTintList(
                    ColorStateList.valueOf(Color.parseColor("#F44336")));
        }

        // Sets crew image and specialization color
        holder.ivCrewImage.setImageResource(getCrewImage(cm.getSpecialization()));
        holder.tvSpecialization.setTextColor(getSpecializationColor(cm.getSpecialization()));

        // It Syncs checkbox without triggering listener
        holder.cbSelect.setOnCheckedChangeListener(null);
        holder.cbSelect.setChecked(selectedCrew.contains(cm));

        // Checkbox toggles the handler
        holder.cbSelect.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                if (!selectedCrew.contains(cm)) selectedCrew.add(cm);
            } else {
                selectedCrew.remove(cm);
            }
        });

        // Row click handler for MissionControl
        holder.itemView.setOnClickListener(v -> {
            if (clickListener != null) {
                clickListener.onCrewClick(cm);
            }
        });
    }

    @Override
    public int getItemCount() {
        return crewList.size();
    }

    public List<CrewMember> getSelectedCrew() {
        return new ArrayList<>(selectedCrew);
    }

    public void updateList(List<CrewMember> newList) {
        crewList = newList;
        selectedCrew.clear();
        notifyDataSetChanged();
    }

    private int getCrewImage(String specialization) {
        switch (specialization) {
            case "Pilot":     return R.drawable.ic_pilot;
            case "Engineer":  return R.drawable.ic_engineer;
            case "Medic":     return R.drawable.ic_medic;
            case "Scientist": return R.drawable.ic_scientist;
            case "Soldier":   return R.drawable.ic_soldier;
            default:          return android.R.drawable.ic_menu_myplaces;
        }
    }

    private int getSpecializationColor(String specialization) {
        switch (specialization) {
            case "Pilot":     return 0xFF4FC3F7;
            case "Engineer":  return 0xFFFFD54F;
            case "Medic":     return 0xFF81C784;
            case "Scientist": return 0xFFCE93D8;
            case "Soldier":   return 0xFFEF9A9A;
            default:          return 0xFFFFFFFF;
        }
    }

    public static class CrewViewHolder extends RecyclerView.ViewHolder {
        ImageView ivCrewImage;
        TextView tvCrewName, tvSpecialization, tvStats, tvEnergy;
        ProgressBar pbEnergy;
        CheckBox cbSelect;

        public CrewViewHolder(@NonNull View itemView) {
            super(itemView);
            ivCrewImage = itemView.findViewById(R.id.ivCrewImage);
            tvCrewName = itemView.findViewById(R.id.tvCrewName);
            tvSpecialization = itemView.findViewById(R.id.tvSpecialization);
            tvStats = itemView.findViewById(R.id.tvStats);
            tvEnergy = itemView.findViewById(R.id.tvEnergy);
            pbEnergy = itemView.findViewById(R.id.pbEnergy);
            cbSelect = itemView.findViewById(R.id.cbSelect);
        }
    }
}