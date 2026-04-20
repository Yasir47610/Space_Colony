package com.example.spacecolony;

import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

/**
 * QuartersActivity - shows crew members currently resting at home.
 * Users can select crew members and move them to the Simulator or Mission Control.
 * Energy is fully restored when a crew member is in Quarters.
 */
public class QuartersActivity extends AppCompatActivity {

    private RecyclerView rvQuarters;
    private Button btnMoveToSimulator, btnMoveToMission;
    private CrewMemberAdapter adapter;
    private Storage storage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quarters);

        storage = Storage.getInstance();

        // Hook up UI
        rvQuarters = findViewById(R.id.rvQuarters);
        btnMoveToSimulator = findViewById(R.id.btnMoveToSimulator);
        btnMoveToMission = findViewById(R.id.btnMoveToMission);

        // Set up RecyclerView with a vertical list layout
        rvQuarters.setLayoutManager(new LinearLayoutManager(this));

        // Load crew members currently in Quarters
        List<CrewMember> quartersCreW = storage.getCrewByLocation("Quarters");
        adapter = new CrewMemberAdapter(this, quartersCreW);
        rvQuarters.setAdapter(adapter);

        // Move selected crew to Simulator for training
        btnMoveToSimulator.setOnClickListener(v -> moveSelected("Simulator"));

        // Move selected crew to Mission Control for combat
        btnMoveToMission.setOnClickListener(v -> moveSelected("MissionControl"));
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Refresh the list whenever we come back to this screen
        adapter.updateList(storage.getCrewByLocation("Quarters"));
    }

    /**
     * moveSelected() - moves all checked crew members to the given location.
     * Shows a toast confirming how many were moved.
     */
    private void moveSelected(String destination) {
        List<CrewMember> selected = adapter.getSelectedCrew();

        if (selected.isEmpty()) {
            Toast.makeText(this, "Select at least one crew member!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Update location for each selected crew member
        for (CrewMember cm : selected) {
            cm.setLocation(destination);
        }

        // Save the updated locations to file
        storage.saveToFile(this);

        String destName = destination.equals("MissionControl") ? "Mission Control" : destination;
        Toast.makeText(this, selected.size() + " crew member(s) moved to " + destName,
                Toast.LENGTH_SHORT).show();

        // Refresh the list - moved crew should disappear from Quarters view
        adapter.updateList(storage.getCrewByLocation("Quarters"));
    }
}