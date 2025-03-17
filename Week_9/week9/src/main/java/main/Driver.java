package main;
public class Driver {
    private String name;
    private String country;
    private int totalPoints;
    private RallyCar car;

    /**
     * Creates a new Driver with the specified name and country.
     *
     * @param name    The driver's name
     * @param country The driver's country
     */
    public Driver(String name, String country) {
        this.name = name;
        this.country = country;
        this.totalPoints = 0;
    }

    /**
     * Creates a new Driver with the specified name, country, and car.
     *
     * @param name    The driver's name
     * @param country The driver's country
     * @param car     The driver's car
     */
    public Driver(String name, String country, RallyCar car) {
        this(name, country);
        this.car = car;
    }

    /**
     * Gets the driver's name.
     *
     * @return The driver's name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the driver's name.
     *
     * @param name The driver's name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the driver's country.
     *
     * @return The driver's country
     */
    public String getCountry() {
        return country;
    }

    /**
     * Sets the driver's country.
     *
     * @param country The driver's country
     */
    public void setCountry(String country) {
        this.country = country;
    }

    /**
     * Gets the driver's total points.
     *
     * @return The driver's total points
     */
    public int getTotalPoints() {
        return totalPoints;
    }

    /**
     * Adds points to the driver's total.
     *
     * @param points The points to add
     */
    public void addPoints(int points) {
        this.totalPoints += points;
    }

    /**
     * Gets the driver's car.
     *
     * @return The driver's car
     */
    public RallyCar getCar() {
        return car;
    }

    /**
     * Sets the driver's car.
     *
     * @param car The driver's car
     */
    public void setCar(RallyCar car) {
        this.car = car;
    }

    @Override
    public String toString() {
        return name + " (" + country + "): " + totalPoints + " points";
    }
}