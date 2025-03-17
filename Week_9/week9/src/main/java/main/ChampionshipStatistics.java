package main;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Static utility class for calculating championship statistics.
 */
public class ChampionshipStatistics {
    /**
     * Private constructor to prevent instantiation of utility class.
     */
    private ChampionshipStatistics() {
        // This prevents instantiation
    }

    /**
     * Calculates the average points per driver.
     *
     * @param manager The championship manager
     * @return The average points per driver
     */
    public static double calculateAveragePointsPerDriver(ChampionshipManager manager) {
        List<Driver> drivers = manager.getDrivers();
        if (drivers.isEmpty()) {
            return 0;
        }
        
        int totalPoints = manager.calculateTotalChampionshipPoints();
        return (double) totalPoints / drivers.size();
    }

    /**
     * Finds the most successful country based on total points.
     *
     * @param manager The championship manager
     * @return The most successful country, or "None" if no drivers
     */
    public static String findMostSuccessfulCountry(ChampionshipManager manager) {
        List<Driver> drivers = manager.getDrivers();
        if (drivers.isEmpty()) {
            return "None";
        }
        
        Map<String, Integer> countryPoints = new HashMap<>();
        
        // Sum points for each country
        for (Driver driver : drivers) {
            String country = driver.getCountry();
            countryPoints.put(country, countryPoints.getOrDefault(country, 0) + driver.getTotalPoints());
        }
        
        // Find country with most points
        String mostSuccessfulCountry = "None";
        int highestPoints = 0;
        
        for (Map.Entry<String, Integer> entry : countryPoints.entrySet()) {
            if (entry.getValue() > highestPoints) {
                highestPoints = entry.getValue();
                mostSuccessfulCountry = entry.getKey();
            }
        }
        
        return mostSuccessfulCountry;
    }

    /**
     * Counts the total number of races held.
     *
     * @param manager The championship manager
     * @return The total number of races
     */
    public static int countTotalRacesHeld(ChampionshipManager manager) {
        return manager.getRaceResults().size();
    }
}