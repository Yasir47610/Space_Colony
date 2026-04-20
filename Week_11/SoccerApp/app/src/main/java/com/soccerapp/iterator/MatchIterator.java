package com.soccerapp.iterator;

import com.soccerapp.model.Match;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * Custom iterator for a list of Match objects.
 * MatchRepository extends CustomIterator<T> per the UML diagram,
 * so we provide this standalone iterator as a companion.
 */
public class MatchIterator implements CustomIterator<Match> {

    private final List<Match> matches;
    private int cursor;

    public MatchIterator(List<Match> matches) {
        if (matches == null) throw new IllegalArgumentException("Match list cannot be null");
        this.matches = matches;
        this.cursor = 0;
    }

    @Override
    public boolean hasNext() {
        return cursor < matches.size();
    }

    @Override
    public Match next() {
        if (!hasNext()) {
            throw new NoSuchElementException("No more matches to iterate over");
        }
        return matches.get(cursor++);
    }

    public void reset() { cursor = 0; }

    public int getPosition() { return cursor; }
}
