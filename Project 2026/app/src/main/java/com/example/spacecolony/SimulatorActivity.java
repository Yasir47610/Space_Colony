package com.example.spacecolony;

import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

/**
 * SimulatorActivity - training area for crew members.
 * Users can select crew members and train them to gain +1 XP each session.
 * XP permanently increases their effective skill (skill + experience).
 * Crew can also be sent back to Quarters from here (restores energy).
 */
public class SimulatorActivity extends AppCompatActivity {

    private RecyclerView rvSimulator;
    private Button btnTrainSelected, btnMoveToQuarters;
    private CrewMemberAdapter adapter;
    private Storage storage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simulator);

        storage = Storage.getInstance();

        // Hook up UI
        rvSimulator = findViewById(R.id.rvSimulator);
        btnTrainSelected = findViewById(R.id.btnTrainSelected);
        btnMoveToQuarters = findViewById(R.id.btnMoveToQuarters);

        // Set up RecyclerView
        rvSimulator.setLayoutManager(new LinearLayoutManager(this));

        List<CrewMember> simulatorCrew = storage.getCrewByLocation("Simulator");
        adapter = new CrewMemberAdapter(this, simulatorCrew);
        rvSimulator.setAdapter(adapter);

        // Train selected crew members - each gets +1 XP
        btnTrainSelected.setOnClickListener(v -> trainSelected());

        // Send selected crew back to Quarters - restores their energy
        btnMoveToQuarters.setOnClickListener(v -> sendToQuarters());
    }

    @Override
    protected void onResume() {
        super.onResume();
        adapter.updateList(storage.getCrewByLocation("Simulator"));
    }

    /**
     * trainSelected() - gives +1 XP to all checked crew members.
     * XP increases their effective skill in combat.
     */
    private void trainSelected() {
        List<CrewMember> selected = adapter.getSelectedCrew();

        if (selected.isEmpty()) {
            Toast.makeText(this, "Select crew members to train!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Call train() on each selected crew member
        for (CrewMember cm : selected) {
            cm.train(); // adds 1 XP and increments training session counter
        }

        storage.saveToFile(this);

        Toast.makeText(this, selected.size() + " crew member(s) trained! +1 XP each",
                Toast.LENGTH_SHORT).show();

        // Refresh list to show updated XP values
        adapter.updateList(storage.getCrewByLocation("Simulator"));
    }

    /**
     * sendToQuarters() - moves selected crew back home and restores their energy.
     */
    private void sendToQuarters() {
        List<CrewMember> selected = adapter.getSelectedCrew();

        if (selected.isEmpty()) {
            Toast.makeText(this, "Select crew members to send home!", Toast.LENGTH_SHORT).show();
            return;
        }

        for (CrewMember cm : selected) {
            cm.setLocation("Quarters");
            cm.restoreEnergy(); // energy fully restored when returning to Quarters
        }

        storage.saveToFile(this);

        Toast.makeText(this, selected.size() + " crew member(s) sent to Quarters. Energy restored!",
                Toast.LENGTH_SHORT).show();

        adapter.updateList(storage.getCrewByLocation("Simulator"));
    }
}