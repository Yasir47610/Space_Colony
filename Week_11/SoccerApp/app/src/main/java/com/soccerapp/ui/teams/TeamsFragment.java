package com.soccerapp.ui.teams;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.soccerapp.R;
import com.soccerapp.adapter.SoccerAdapter;
import com.soccerapp.data.DataProvider;
import com.soccerapp.iterator.TeamIterator;
import com.soccerapp.model.Team;
import com.soccerapp.repository.TeamRepository;

import java.util.Arrays;
import java.util.List;

/**
 * Fragment that shows the list of Teams.
 *
 * Demonstrates:
 *  - Generic TeamRepository usage
 *  - TeamIterator traversal (logged to logcat)
 *  - Lambda-based filtering via Spinner selection
 *  - Lambda-based sorting
 *  - Lambda click handler via the generic SoccerAdapter
 */
public class TeamsFragment extends Fragment {

    private TeamRepository teamRepository;
    private SoccerAdapter<Team> adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // 1. Build the repository with sample data
        DataProvider provider = new DataProvider();
        teamRepository = new TeamRepository(provider.createSampleTeams());

        // 2. Demonstrate custom iterator (Iterator pattern)
        traverseWithIterator(teamRepository.getAll());

        // 3. Set up RecyclerView with the generic adapter
        //    Lambda #1: Function<Team, String> for primary text mapping
        //    Lambda #2: Function<Team, String> for secondary text mapping
        adapter = new SoccerAdapter<>(
                teamRepository.getAll(),
                team -> team.getName() + "  ·  " + team.getLeague(),           // lambda primary mapper
                team -> team.getCountry() + "  |  Founded: " + team.getFounded() // lambda secondary mapper
        );

        // Lambda #3: click-handler callback
        adapter.setOnItemClickListener(team ->
                Toast.makeText(requireContext(),
                        "🏟  " + team.getStadium(), Toast.LENGTH_SHORT).show()
        );

        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        recyclerView.setAdapter(adapter);

        // 4. Set up sort/filter Spinner
        setupSortSpinner(view);
    }

    // -----------------------------------------------------------------------
    // Iterator demonstration
    // -----------------------------------------------------------------------
    private void traverseWithIterator(List<Team> teams) {
        TeamIterator iterator = new TeamIterator(teams);
        while (iterator.hasNext()) {
            Team t = iterator.next();
            android.util.Log.d("TeamIterator", "Visited: " + t.getName());
        }
    }

    // -----------------------------------------------------------------------
    // Sort / Filter Spinner
    // -----------------------------------------------------------------------
    private void setupSortSpinner(View root) {
        Spinner spinner = root.findViewById(R.id.sortSpinner);
        if (spinner == null) return;

        List<String> options = Arrays.asList(
                "All teams", "Sort A→Z", "Sort by Founded",
                "La Liga", "Premier League", "Bundesliga", "Champions League"
        );
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(
                requireContext(), android.R.layout.simple_spinner_item, options);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerAdapter);

        // Lambda #4: Spinner selection listener as lambda via AdapterView.OnItemSelectedListener
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View v, int pos, long id) {
                List<Team> result;
                switch (pos) {
                    case 1:  result = teamRepository.sortedByName();       break;
                    case 2:  result = teamRepository.sortedByFounded();    break;
                    case 3:  result = teamRepository.filterByLeague("La Liga");       break;
                    case 4:  result = teamRepository.filterByLeague("Premier League"); break;
                    case 5:  result = teamRepository.filterByLeague("Bundesliga");    break;
                    case 6:  result = teamRepository.filterByLeague("Champions League"); break;
                    default: result = teamRepository.getAll();             break;
                }
                adapter.updateItems(result);
            }

            @Override public void onNothingSelected(AdapterView<?> parent) { }
        });
    }

    // -----------------------------------------------------------------------
    // Called by MainActivity when the search bar text changes
    // -----------------------------------------------------------------------
    public void applySearch(String query) {
        if (teamRepository == null || adapter == null) return;
        // Lambda #5: searchByName internally uses filter(item -> item.getName()…)
        adapter.updateItems(teamRepository.searchByName(query));
    }
}
