package com.soccerapp.iterator;

import com.soccerapp.model.Team;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * Custom iterator for a list of Team objects.
 * Implements CustomIterator<Team> as per the UML diagram.
 *
 * Usage example:
 *   TeamIterator it = new TeamIterator(teams);
 *   while (it.hasNext()) { Team t = it.next(); ... }
 */
public class TeamIterator implements CustomIterator<Team> {

    private final List<Team> teams;
    private int cursor;

    public TeamIterator(List<Team> teams) {
        if (teams == null) throw new IllegalArgumentException("Team list cannot be null");
        this.teams = teams;
        this.cursor = 0;
    }

    @Override
    public boolean hasNext() {
        return cursor < teams.size();
    }

    @Override
    public Team next() {
        if (!hasNext()) {
            throw new NoSuchElementException("No more teams to iterate over");
        }
        return teams.get(cursor++);
    }

    /** Resets the iterator back to the beginning of the list. */
    public void reset() {
        cursor = 0;
    }

    /** Returns the current position of the iterator (0-based). */
    public int getPosition() {
        return cursor;
    }
}
