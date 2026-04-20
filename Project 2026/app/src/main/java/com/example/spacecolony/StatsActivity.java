package com.example.spacecolony;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;

import java.util.ArrayList;
import java.util.List;

/**
 * StatsActivity - shows colony-wide and per-crew statistics.
 * Uses MPAndroidChart to display a bar chart of crew performance (bonus feature).
 * Also uses a RecyclerView to list individual crew member stats.
 */
public class StatsActivity extends AppCompatActivity {

    private TextView tvTotalCrew, tvTotalMissions;
    private BarChart barChart;
    private RecyclerView rvStats;
    private Storage storage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats);

        storage = Storage.getInstance();

        // Hook up UI
        tvTotalCrew = findViewById(R.id.tvTotalCrew);
        tvTotalMissions = findViewById(R.id.tvTotalMissions);
        barChart = findViewById(R.id.barChart);
        rvStats = findViewById(R.id.rvStats);

        // Set up the individual stats RecyclerView
        rvStats.setLayoutManager(new LinearLayoutManager(this));
        rvStats.setNestedScrollingEnabled(false);

        // Load and display everything
        loadColonyStats();
        loadBarChart();
        loadCrewStats();
    }

    /**
     * loadColonyStats() - fills in the colony-wide summary numbers.
     */
    private void loadColonyStats() {
        tvTotalCrew.setText("Total Crew: " + storage.getTotalCrew());
        tvTotalMissions.setText("Total Missions: " + storage.getMissionCount());
    }

    /**
     * loadBarChart() - builds and displays the MPAndroidChart bar chart.
     * Shows missions won per crew member as colored bars.
     */
    private void loadBarChart() {
        List<CrewMember> allCrew = storage.listCrewMembers();

        if (allCrew.isEmpty()) {
            barChart.setNoDataText("No crew members yet!");
            barChart.setNoDataTextColor(Color.WHITE);
            return;
        }

        // Build bar entries - one bar per crew member showing missions won
        ArrayList<BarEntry> entries = new ArrayList<>();
        ArrayList<String> labels = new ArrayList<>();

        for (int i = 0; i < allCrew.size(); i++) {
            CrewMember cm = allCrew.get(i);
            entries.add(new BarEntry(i, cm.getMissionsWon()));
            labels.add(cm.getName());
        }

        BarDataSet dataSet = new BarDataSet(entries, "Missions Won");

        // Color each bar based on the crew member's specialization
        ArrayList<Integer> colors = new ArrayList<>();
        for (CrewMember cm : allCrew) {
            colors.add(getSpecializationColor(cm.getSpecialization()));
        }
        dataSet.setColors(colors);
        dataSet.setValueTextColor(Color.WHITE);
        dataSet.setValueTextSize(11f);

        BarData barData = new BarData(dataSet);
        barChart.setData(barData);

        // Style the chart to match our dark space theme
        barChart.setBackgroundColor(Color.parseColor("#1A1A3E"));
        barChart.getDescription().setEnabled(false);
        barChart.getLegend().setTextColor(Color.WHITE);
        barChart.getAxisLeft().setTextColor(Color.WHITE);
        barChart.getAxisRight().setEnabled(false);
        barChart.getAxisLeft().setAxisMinimum(0f);

        // Show crew names on the X axis instead of numbers
        XAxis xAxis = barChart.getXAxis();
        xAxis.setTextColor(Color.WHITE);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setValueFormatter(new IndexAxisValueFormatter(labels));
        xAxis.setGranularity(1f);
        xAxis.setGranularityEnabled(true);

        barChart.animateY(800); // animate bars growing upward
        barChart.invalidate();
    }

    /**
     * loadCrewStats() - populates the RecyclerView with per-crew stat cards.
     * Uses a simple StatsAdapter instead of the regular CrewMemberAdapter.
     */
    private void loadCrewStats() {
        List<CrewMember> allCrew = storage.listCrewMembers();
        StatsAdapter statsAdapter = new StatsAdapter(this, allCrew);
        rvStats.setAdapter(statsAdapter);
    }

    /**
     * getSpecializationColor() - returns color int matching each specialization's theme.
     */
    private int getSpecializationColor(String specialization) {
        switch (specialization) {
            case "Pilot":     return Color.parseColor("#4FC3F7"); // Blue
            case "Engineer":  return Color.parseColor("#FFD54F"); // Yellow
            case "Medic":     return Color.parseColor("#81C784"); // Green
            case "Scientist": return Color.parseColor("#CE93D8"); // Purple
            case "Soldier":   return Color.parseColor("#EF9A9A"); // Red
            default:          return Color.WHITE;
        }
    }
}