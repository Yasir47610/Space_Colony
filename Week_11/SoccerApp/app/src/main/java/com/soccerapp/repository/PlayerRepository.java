package com.soccerapp.repository;

import com.soccerapp.model.Player;
import java.util.List;

/**
 * Specialized repository for {@link Player} entities.
 */
public class PlayerRepository extends Repository<Player> {

    public PlayerRepository() {
        super();
    }

    public PlayerRepository(List<Player> initialPlayers) {
        super(initialPlayers);
    }

    /**
     * Returns all players that play for the given team.
     * Uses a lambda Predicate delegated to the parent filter method.
     */
    public List<Player> filterByTeam(String team) {
        if (team == null || team.trim().isEmpty()) return getAll();
        return filter(player -> player.getTeam().equalsIgnoreCase(team.trim()));
    }

    /**
     * Returns all players in a given position (e.g. "Forward").
     */
    public List<Player> filterByPosition(String position) {
        return filter(player -> player.getPosition().equalsIgnoreCase(position));
    }

    /**
     * Returns players sorted by name using a lambda Comparator.
     */
    public List<Player> sortedByName() {
        List<Player> sorted = getAll();
        sorted.sort((a, b) -> a.getName().compareToIgnoreCase(b.getName()));
        return sorted;
    }

    /**
     * Returns players sorted by age ascending.
     */
    public List<Player> sortedByAge() {
        List<Player> sorted = getAll();
        sorted.sort((a, b) -> Integer.compare(a.getAge(), b.getAge()));
        return sorted;
    }
}
