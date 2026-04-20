package com.soccerapp;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.soccerapp.ui.matches.MatchesFragment;
import com.soccerapp.ui.players.PlayersFragment;
import com.soccerapp.ui.teams.TeamsFragment;

/**
 * Entry-point activity. Sets up:
 *  - TabLayout with Teams / Players / Matches fragments
 *  - Global search bar that delegates to the active fragment via lambda TextWatcher
 */
public class MainActivity extends AppCompatActivity {

    private TeamsFragment  teamsFragment;
    private PlayersFragment playersFragment;
    private MatchesFragment matchesFragment;

    private ViewPager2 viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Create fragment instances
        teamsFragment   = new TeamsFragment();
        playersFragment = new PlayersFragment();
        matchesFragment = new MatchesFragment();

        // ViewPager2 + adapter
        viewPager = findViewById(R.id.viewPager);
        viewPager.setAdapter(new FragmentStateAdapter(this) {
            @Override public int getItemCount() { return 3; }

            @Override
            public Fragment createFragment(int position) {
                switch (position) {
                    case 1: return playersFragment;
                    case 2: return matchesFragment;
                    default: return teamsFragment;
                }
            }
        });

        // TabLayout
        TabLayout tabLayout = findViewById(R.id.tabLayout);
        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {
            // Lambda #6: tab title mapping
            switch (position) {
                case 1: tab.setText("Players"); break;
                case 2: tab.setText("Matches"); break;
                default: tab.setText("Teams");  break;
            }
        }).attach();

        // Search bar with lambda TextWatcher
        EditText searchBar = findViewById(R.id.searchBar);
        searchBar.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override public void afterTextChanged(Editable s) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Lambda #7: delegate search to the correct fragment based on current tab
                String query = s.toString().trim();
                int currentTab = viewPager.getCurrentItem();
                switch (currentTab) {
                    case 0: teamsFragment.applySearch(query);   break;
                    case 1: playersFragment.applySearch(query); break;
                    case 2: matchesFragment.applySearch(query); break;
                }
            }
        });
    }
}
