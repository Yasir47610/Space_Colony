package com.example.spacecolony;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    // TextViews and buttons from the main layout
    private TextView tvQuartersCount, tvSimulatorCount, tvMissionCount, tvTotalMissions;
    private Button btnRecruit, btnQuarters, btnSimulator, btnMissionControl, btnStats;

    // Shared app storage
    private Storage storage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        storage = Storage.getInstance();

        // Load previously saved data
        storage.loadFromFile(this);

        tvQuartersCount = findViewById(R.id.tvQuartersCount);
        tvSimulatorCount = findViewById(R.id.tvSimulatorCount);
        tvMissionCount = findViewById(R.id.tvMissionCount);
        tvTotalMissions = findViewById(R.id.tvTotalMissions);
        btnRecruit = findViewById(R.id.btnRecruit);
        btnQuarters = findViewById(R.id.btnQuarters);
        btnSimulator = findViewById(R.id.btnSimulator);
        btnMissionControl = findViewById(R.id.btnMissionControl);
        btnStats = findViewById(R.id.btnStats);

        // Open each screen from its button
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
        updateStatusCounts();
    }

    // Update the numbers shown on the home screen
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