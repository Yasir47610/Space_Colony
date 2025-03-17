package main;

import java.util.*;

/**
 * Singleton class for managing the rally championship.
 * This class follows the Singleton pattern to ensure only one instance exists.
 */
public class ChampionshipManager {
    // Static instance for Singleton pattern
    private static ChampionshipManager instance;
    
    // Static data members to track totals
    private static int totalDrivers = 0;
    private static int totalRaces = 0;
    
    // Lists for drivers and race results
    private List<Driver> drivers;
    private List<RaceResult> raceResults;

    /**
     * Private constructor to prevent direct instantiation (part of Singleton pattern).
     */
    private ChampionshipManager() {
        drivers = new ArrayList<>();
        raceResults = new ArrayList<>();
    }

    /**
     * Gets the singleton instance of ChampionshipManager.
     *
     * @return The ChampionshipManager instance
     */
    public static ChampionshipManager getInstance() {
        if (instance == null) {
            instance = new ChampionshipManager();
        }
        return instance;
    }

    /**
     * Registers a driver in the championship.
     *
     * @param driver The driver to register
     */
    public void registerDriver(Driver driver) {
        drivers.add(driver);
        totalDrivers++;
    }

    /**
     * Adds a race result to the championship.
     *
     * @param raceResult The race result to add
     */
    public void addRaceResult(RaceResult raceResult) {
        raceResults.add(raceResult);
        totalRaces++;
    }

    /**
     * Gets the list of registered drivers.
     *
     * @return The list of drivers
     */
    public List<Driver> getDrivers() {
        return drivers;
    }

    /**
     * Gets the list of race results.
     *
     * @return The list of race results
     */
    public List<RaceResult> getRaceResults() {
        return raceResults;
    }

    /**
     * Gets the total number of drivers in the championship.
     *
     * @return The total number of drivers
     */
    public static int getTotalDrivers() {
        return totalDrivers;
    }

    /**
     * Gets the total number of races in the championship.
     *
     * @return The total number of races
     */
    public static int getTotalRaces() {
        return totalRaces;
    }

    /**
     * Gets the championship standings as a sorted list of drivers.
     *
     * @return The sorted list of drivers
     */
    public List<Driver> getChampionshipStandings() {
        List<Driver> standings = new ArrayList<>(drivers);
        standings.sort((d1, d2) -> Integer.compare(d2.getTotalPoints(), d1.getTotalPoints()));
        return standings;
    }

    /**
     * Gets the leading driver in the championship.
     *
     * @return The leading driver, or null if no drivers
     */
    public Driver getLeadingDriver() {
        if (drivers.isEmpty()) {
            return null;
        }
        return getChampionshipStandings().get(0);
    }

    /**
     * Calculates the total championship points across all drivers.
     *
     * @return The total championship points
     */
    public int calculateTotalChampionshipPoints() {
        return drivers.stream().mapToInt(Driver::getTotalPoints).sum();
    }
}