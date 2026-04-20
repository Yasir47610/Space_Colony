package com.soccerapp.ui.players;

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
import com.soccerapp.model.Player;
import com.soccerapp.repository.PlayerRepository;

import java.util.Arrays;
import java.util.List;

/**
 * Fragment showing the list of Players.
 *
 * Demonstrates:
 *  - Generic PlayerRepository usage
 *  - Lambda-based filtering (by position) via Spinner
 *  - Lambda-based sorting (by name / by age)
 *  - Lambda click handler
 */
public class PlayersFragment extends Fragment {

    private PlayerRepository playerRepository;
    private SoccerAdapter<Player> adapter;

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
        playerRepository = new PlayerRepository(provider.createSamplePlayers());

        // Generic adapter with lambda mappers
        adapter = new SoccerAdapter<>(
                playerRepository.getAll(),
                p -> "#" + p.getJerseyNumber() + "  " + p.getName(),   // lambda primary
                p -> p.getPosition() + "  ·  " + p.getTeam()           // lambda secondary
        );

        // Lambda click handler
        adapter.setOnItemClickListener(player ->
                Toast.makeText(requireContext(),
                        player.getName() + " · Age " + player.getAge()
                                + " · " + player.getNationality(),
                        Toast.LENGTH_SHORT).show()
        );

        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        recyclerView.setAdapter(adapter);

        setupSortSpinner(view);
    }

    private void setupSortSpinner(View root) {
        Spinner spinner = root.findViewById(R.id.sortSpinner);
        if (spinner == null) return;

        List<String> options = Arrays.asList(
                "All players", "Sort A→Z", "Sort by Age",
                "Forwards", "Midfielders", "Defenders", "Goalkeepers"
        );
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(
                requireContext(), android.R.layout.simple_spinner_item, options);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerAdapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View v, int pos, long id) {
                List<Player> result;
                switch (pos) {
                    case 1: result = playerRepository.sortedByName();                break;
                    case 2: result = playerRepository.sortedByAge();                 break;
                    case 3: result = playerRepository.filterByPosition("Forward");   break;
                    case 4: result = playerRepository.filterByPosition("Midfielder");break;
                    case 5: result = playerRepository.filterByPosition("Defender");  break;
                    case 6: result = playerRepository.filterByPosition("Goalkeeper");break;
                    default: result = playerRepository.getAll();                     break;
                }
                adapter.updateItems(result);
            }
            @Override public void onNothingSelected(AdapterView<?> parent) { }
        });
    }

    public void applySearch(String query) {
        if (playerRepository == null || adapter == null) return;
        adapter.updateItems(playerRepository.searchByName(query));
    }
}
