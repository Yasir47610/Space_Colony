package com.example.spacecolony;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

/**
 * MainActivity - the home screen of the app.
 * Shows colony status summary and navigation buttons to all other screens.
 * Also handles loading saved data when the app starts.
 */
public class MainActivity extends AppCompatActivity {

    // UI elements
    private TextView tvQuartersCount, tvSimulatorCount, tvMissionCount, tvTotalMissions;
    private Button btnRecruit, btnQuarters, btnSimulator, btnMissionControl, btnStats;

    // Our central data store
    private Storage storage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Get the singleton storage instance
        storage = Storage.getInstance();

        // Try to load any previously saved crew data
        boolean loaded = storage.loadFromFile(this);

        // Hook up all the UI elements to their IDs in activity_main.xml
        tvQuartersCount = findViewById(R.id.tvQuartersCount);
        tvSimulatorCount = findViewById(R.id.tvSimulatorCount);
        tvMissionCount = findViewById(R.id.tvMissionCount);
        tvTotalMissions = findViewById(R.id.tvTotalMissions);
        btnRecruit = findViewById(R.id.btnRecruit);
        btnQuarters = findViewById(R.id.btnQuarters);
        btnSimulator = findViewById(R.id.btnSimulator);
        btnMissionControl = findViewById(R.id.btnMissionControl);
        btnStats = findViewById(R.id.btnStats);

        // Set up button click listeners - each button opens a different screen
        btnRecruit.setOnClickListener(v ->
                startActivity(new Intent(this, RecruitActivity.class)));

        btnQuarters.setOnClickListener(v ->
                startActivity(new Intent(this, QuartersActivity.class)));

        btnSimulator.setOnClickListener(v ->
                startActivity(new Intent(this, SimulatorActivity.class)));

        btnMissionControl.setOnClickListener(v ->
                startActivity(new Intent(this, MissionControlActivity.class)));

        btnStats.setOnClickListener(v ->
                startActivity(new Intent(this, StatsActivity.class)));
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Refresh the colony status counts every time we return to this screen
        updateStatusCounts();
    }

    /**
     * updateStatusCounts() - refreshes the crew count display for each location.
     * Called every time MainActivity becomes visible (onResume).
     */
    private void updateStatusCounts() {
        int quartersCount = storage.getCrewByLocation("Quarters").size();
        int simulatorCount = storage.getCrewByLocation("Simulator").size();
        int missionCount = storage.getCrewByLocation("MissionControl").size();

        tvQuartersCount.setText(String.valueOf(quartersCount));
        tvSimulatorCount.setText(String.valueOf(simulatorCount));
        tvMissionCount.setText(String.valueOf(missionCount));
        tvTotalMissions.setText("Total missions completed: " + storage.getMissionCount());
    }
}