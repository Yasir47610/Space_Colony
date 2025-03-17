/**
 * Represents a rally car optimized for asphalt surfaces.
 * Extends RallyCar and implements its own performance calculation.
 */
package main;

public class AsphaltCar extends RallyCar {
    private double aerodynamics; // rating from 1-10
    private double brakeEfficiency; // rating from 1-10

    /**
     * Creates a new AsphaltCar with the specified parameters.
     *
     * @param make           The car's make
     * @param model          The car's model
     * @param horsepower     The car's horsepower
     * @param aerodynamics   The car's aerodynamics rating (1-10)
     * @param brakeEfficiency The car's brake efficiency rating (1-10)
     */
    public AsphaltCar(String make, String model, int horsepower, double aerodynamics, double brakeEfficiency) {
        super(make, model, horsepower);
        this.aerodynamics = aerodynamics;
        this.brakeEfficiency = brakeEfficiency;
    }

    /**
     * Gets the car's aerodynamics rating.
     *
     * @return The aerodynamics rating (1-10)
     */
    public double getAerodynamics() {
        return aerodynamics;
    }

    /**
     * Sets the car's aerodynamics rating.
     *
     * @param aerodynamics The aerodynamics rating (1-10)
     */
    public void setAerodynamics(double aerodynamics) {
        this.aerodynamics = aerodynamics;
    }

    /**
     * Gets the car's brake efficiency rating.
     *
     * @return The brake efficiency rating (1-10)
     */
    public double getBrakeEfficiency() {
        return brakeEfficiency;
    }

    /**
     * Sets the car's brake efficiency rating.
     *
     * @param brakeEfficiency The brake efficiency rating (1-10)
     */
    public void setBrakeEfficiency(double brakeEfficiency) {
        this.brakeEfficiency = brakeEfficiency;
    }

    /**
     * Calculates the performance rating for asphalt surfaces.
     * Formula considers horsepower, aerodynamics, and brake efficiency.
     *
     * @return The performance rating
     */
    @Override
    public double calculatePerformance() {
        // For asphalt, we prioritize horsepower, aerodynamics, and braking
        return (getHorsepower() * 0.7) + (aerodynamics * 20) + (brakeEfficiency * 15);
    }
}