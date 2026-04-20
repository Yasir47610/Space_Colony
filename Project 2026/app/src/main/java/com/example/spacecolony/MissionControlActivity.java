package com.example.spacecolony;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

/**
 * MissionControlActivity - the battle preparation screen.
 * User selects exactly two crew members by tapping their cards.
 * Then launches the mission which opens MissionLogActivity.
 */
public class MissionControlActivity extends AppCompatActivity {

    private RecyclerView rvMissionCrew;
    private Button btnLaunchMission;
    private TextView tvCrewA, tvCrewB;
    private CrewMemberAdapter adapter;
    private Storage storage;

    // The two selected crew members for the mission
    private CrewMember selectedA = null;
    private CrewMember selectedB = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mission_control);

        storage = Storage.getInstance();

        // Hook up UI
        rvMissionCrew = findViewById(R.id.rvMissionCrew);
        btnLaunchMission = findViewById(R.id.btnLaunchMission);
        tvCrewA = findViewById(R.id.tvCrewA);
        tvCrewB = findViewById(R.id.tvCrewB);

        // Set up RecyclerView - tap a crew member to select them
        rvMissionCrew.setLayoutManager(new LinearLayoutManager(this));

        List<CrewMember> missionCrew = storage.getCrewByLocation("MissionControl");
        adapter = new CrewMemberAdapter(this, missionCrew);

        // Use click listener instead of checkboxes for this screen
        adapter.setOnCrewClickListener(cm -> selectCrewMember(cm));
        rvMissionCrew.setAdapter(adapter);

        // Launch the mission with the two selected crew members
        btnLaunchMission.setOnClickListener(v -> launchMission());
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Reset selections when returning to this screen
        selectedA = null;
        selectedB = null;
        updateSelectionDisplay();
        adapter.updateList(storage.getCrewByLocation("MissionControl"));
    }

    /**
     * selectCrewMember() - handles tapping a crew member card.
     * First tap = Crew A, second tap = Crew B, third tap resets.
     */
    private void selectCrewMember(CrewMember cm) {
        // Don't select the same crew member twice
        if (cm == selectedA) {
            selectedA = null;
        } else if (cm == selectedB) {
            selectedB = null;
        } else if (selectedA == null) {
            selectedA = cm;
        } else if (selectedB == null) {
            selectedB = cm;
        } else {
            // Both slots full - replace Crew A and shift B to A
            selectedA = selectedB;
            selectedB = cm;
        }
        updateSelectionDisplay();
    }

    /**
     * updateSelectionDisplay() - shows which crew members are currently selected.
     */
    private void updateSelectionDisplay() {
        tvCrewA.setText(selectedA != null ?
                selectedA.getSpecialization() + ": " + selectedA.getName() : "Not selected");
        tvCrewB.setText(selectedB != null ?
                selectedB.getSpecialization() + ": " + selectedB.getName() : "Not selected");
    }

    /**
     * launchMission() - validates selection and opens the MissionLogActivity.
     * Passes the IDs of the two selected crew members via Intent extras.
     */
    private void launchMission() {
        if (selectedA == null || selectedB == null) {
            Toast.makeText(this, "Select exactly 2 crew members!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Pass crew member IDs to MissionLogActivity via Intent
        Intent intent = new Intent(this, MissionLogActivity.class);
        intent.putExtra("crewAId", selectedA.getId());
        intent.putExtra("crewBId", selectedB.getId());
        startActivity(intent);
    }
}