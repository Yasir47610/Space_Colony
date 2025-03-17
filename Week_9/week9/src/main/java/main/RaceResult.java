package main;
import java.util.Map;

/**
 * Interface for race results, following the Interface Segregation Principle.
 * This interface provides methods for recording and retrieving race results.
 */
public interface RaceResult {
    /**
     * Gets the race name.
     *
     * @return The race name
     */
    String getRaceName();
    
    /**
     * Gets the race location.
     *
     * @return The race location
     */
    String getLocation();
    
    /**
     * Records the driver's position in the race.
     *
     * @param driver   The driver
     * @param position The position (1st, 2nd, etc.)
     */
    void recordResult(Driver driver, int position);
    
    /**
     * Gets the race results as a map of drivers to positions.
     *
     * @return The race results
     */
    Map<Driver, Integer> getResults();
    
    /**
     * Calculates the points earned by a driver based on their position.
     *
     * @param position The driver's position
     * @return The points earned
     */
    int calculatePoints(int position);
}