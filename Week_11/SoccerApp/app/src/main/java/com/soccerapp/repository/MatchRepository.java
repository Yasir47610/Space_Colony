package com.soccerapp.repository;

import com.soccerapp.model.Match;
import java.util.List;

/**
 * Specialized repository for {@link Match} entities.
 * The UML diagram shows MatchRepository also extends CustomIterator<T>;
 * iteration is delegated to {@link com.soccerapp.iterator.MatchIterator}.
 */
public class MatchRepository extends Repository<Match> {

    public MatchRepository() {
        super();
    }

    public MatchRepository(List<Match> initialMatches) {
        super(initialMatches);
    }

    /**
     * Returns all matches that involve the given team (home or away).
     */
    public List<Match> filterByTeam(String team) {
        if (team == null || team.trim().isEmpty()) return getAll();
        // Lambda Predicate – team can appear as home or away
        return filter(match -> match.involvesTeam(team.trim()));
    }

    /**
     * Returns all matches for a specific league.
     */
    public List<Match> filterByLeague(String league) {
        return filter(match -> match.getLeague().equalsIgnoreCase(league));
    }

    /**
     * Returns matches sorted by date string (lexicographic works for ISO yyyy-MM-dd).
     */
    public List<Match> sortedByDate() {
        List<Match> sorted = getAll();
        sorted.sort((a, b) -> a.getDate().compareTo(b.getDate()));
        return sorted;
    }
}
