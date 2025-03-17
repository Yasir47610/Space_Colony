package main;

import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * Implementation of the RaceResult interface for rally races.
 */
public class RallyRaceResult implements RaceResult {
    private String raceName;
    private String location;
    private Map<Driver, Integer> results;

    /**
     * Creates a new RallyRaceResult with the specified name and location.
     *
     * @param raceName The race name
     * @param location The race location
     */
    public RallyRaceResult(String raceName, String location) {
        this.raceName = raceName;
        this.location = location;
        this.results = new HashMap<>();
    }

    /**
     * Gets the race name.
     *
     * @return The race name
     */
    @Override
    public String getRaceName() {
        return raceName;
    }

    /**
     * Gets the race location.
     *
     * @return The race location
     */
    @Override
    public String getLocation() {
        return location;
    }

    /**
     * Records the driver's position in the race.
     *
     * @param driver   The driver
     * @param position The position (1st, 2nd, etc.)
     */
    @Override
    public void recordResult(Driver driver, int position) {
        results.put(driver, position);
        int points = calculatePoints(position);
        driver.addPoints(points);
    }

    /**
     * Gets the race results as a map of drivers to positions.
     *
     * @return The race results
     */
    @Override
    public Map<Driver, Integer> getResults() {
        return results;
    }

    /**
     * Calculates the points earned by a driver based on their position.
     * Points distribution follows WRC-like structure.
     *
     * @param position The driver's position
     * @return The points earned
     */
    @Override
    public int calculatePoints(int position) {
        return switch (position) {
            case 1 -> 25;  // 1st place
            case 2 -> 18;  // 2nd place
            case 3 -> 15;  // 3rd place
            case 4 -> 12;  // 4th place
            case 5 -> 10;  // 5th place
            case 6 -> 8;   // 6th place
            case 7 -> 6;   // 7th place
            case 8 -> 4;   // 8th place
            case 9 -> 2;   // 9th place
            case 10 -> 1;  // 10th place
            default -> 0;  // No points for positions beyond 10th
        };
    }

    /**
     * Gets the results sorted by position.
     *
     * @return A map of drivers sorted by their position in the race
     */
    public SortedMap<Integer, Driver> getSortedResults() {
        SortedMap<Integer, Driver> sortedResults = new TreeMap<>();
        for (Map.Entry<Driver, Integer> entry : results.entrySet()) {
            sortedResults.put(entry.getValue(), entry.getKey());
        }
        return sortedResults;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Race: ").append(raceName).append(" (").append(location).append(")\n");
        
        SortedMap<Integer, Driver> sortedResults = getSortedResults();
        for (Map.Entry<Integer, Driver> entry : sortedResults.entrySet()) {
            int position = entry.getKey();
            Driver driver = entry.getValue();
            int points = calculatePoints(position);
            sb.append("Position ").append(position).append(": ")
              .append(driver.getName()).append(" - ").append(points).append(" points\n");
        }
        
        return sb.toString();
    }
}