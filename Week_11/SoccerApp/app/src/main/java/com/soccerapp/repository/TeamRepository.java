package com.soccerapp.repository;

import com.soccerapp.model.Team;
import java.util.Comparator;
import java.util.List;

/**
 * Specialized repository for {@link Team} entities.
 * Extends the generic {@link Repository} and adds team-specific query methods.
 */
public class TeamRepository extends Repository<Team> {

    public TeamRepository() {
        super();
    }

    public TeamRepository(List<Team> initialTeams) {
        super(initialTeams);
    }

    // -----------------------------------------------------------------------
    // Domain-specific filter (Lambda use #2 in the architecture)
    // -----------------------------------------------------------------------

    /**
     * Returns all teams that belong to the given league.
     * Delegates to the parent's {@link #filter(java.util.function.Predicate)} with a lambda.
     */
    public List<Team> filterByLeague(String league) {
        if (league == null || league.trim().isEmpty()) return getAll();
        // Lambda #2 – Predicate lambda for league filtering
        return filter(team -> team.getLeague().equalsIgnoreCase(league.trim()));
    }

    /**
     * Returns teams sorted by name using a Comparator lambda.
     * Lambda #3 – Comparator lambda for sorting.
     */
    public List<Team> sortedByName() {
        List<Team> sorted = getAll();
        sorted.sort((a, b) -> a.getName().compareToIgnoreCase(b.getName()));
        return sorted;
    }

    /**
     * Returns teams sorted by founding year (ascending) using a lambda.
     */
    public List<Team> sortedByFounded() {
        List<Team> sorted = getAll();
        sorted.sort(Comparator.comparingInt(Team::getFounded));
        return sorted;
    }

    /**
     * Returns teams from a specific country.
     */
    public List<Team> filterByCountry(String country) {
        return filter(team -> team.getCountry().equalsIgnoreCase(country));
    }
}
