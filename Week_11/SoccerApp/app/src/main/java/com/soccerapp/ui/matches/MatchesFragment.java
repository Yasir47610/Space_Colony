package com.soccerapp.ui.matches;

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
import com.soccerapp.iterator.MatchIterator;
import com.soccerapp.model.Match;
import com.soccerapp.repository.MatchRepository;

import java.util.Arrays;
import java.util.List;

/**
 * Fragment showing the list of Matches.
 *
 * Demonstrates:
 *  - Generic MatchRepository usage
 *  - MatchIterator traversal
 *  - Lambda-based filtering and sorting
 */
public class MatchesFragment extends Fragment {

    private MatchRepository matchRepository;
    private SoccerAdapter<Match> adapter;

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

        DataProvider provider = new DataProvider();
        matchRepository = new MatchRepository(provider.createSampleMatches());

        // Demonstrate MatchIterator
        traverseWithIterator(matchRepository.getAll());

        // Generic adapter with lambda field mappers
        adapter = new SoccerAdapter<>(
                matchRepository.getAll(),
                m -> m.getHomeTeam() + "  " + m.getScore() + "  " + m.getAwayTeam(), // lambda primary
                m -> m.getLeague() + "  ·  " + m.getDate()                            // lambda secondary
        );

        // Lambda click handler
        adapter.setOnItemClickListener(match ->
                Toast.makeText(requireContext(),
                        "🏟 " + match.getVenue() + "\n" + match.getDate(),
                        Toast.LENGTH_SHORT).show()
        );

        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        recyclerView.setAdapter(adapter);

        setupSortSpinner(view);
    }

    private void traverseWithIterator(List<Match> matches) {
        MatchIterator iterator = new MatchIterator(matches);
        while (iterator.hasNext()) {
            Match m = iterator.next();
            android.util.Log.d("MatchIterator", "Match: " + m.getName() + " score=" + m.getScore());
        }
    }

    private void setupSortSpinner(View root) {
        Spinner spinner = root.findViewById(R.id.sortSpinner);
        if (spinner == null) return;

        List<String> options = Arrays.asList(
                "All matches", "Sort by Date",
                "La Liga", "Premier League", "Bundesliga", "Champions League"
        );
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(
                requireContext(), android.R.layout.simple_spinner_item, options);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerAdapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View v, int pos, long id) {
                List<Match> result;
                switch (pos) {
                    case 1: result = matchRepository.sortedByDate();                          break;
                    case 2: result = matchRepository.filterByLeague("La Liga");               break;
                    case 3: result = matchRepository.filterByLeague("Premier League");        break;
                    case 4: result = matchRepository.filterByLeague("Bundesliga");            break;
                    case 5: result = matchRepository.filterByLeague("Champions League");      break;
                    default: result = matchRepository.getAll();                               break;
                }
                adapter.updateItems(result);
            }
            @Override public void onNothingSelected(AdapterView<?> parent) { }
        });
    }

    public void applySearch(String query) {
        if (matchRepository == null || adapter == null) return;
        adapter.updateItems(matchRepository.searchByName(query));
    }
}
