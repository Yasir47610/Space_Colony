/**
 * Represents a rally car optimized for gravel surfaces.
 * Extends RallyCar and implements its own performance calculation.
 */
package main;

public class GravelCar extends RallyCar {
    private double suspensionTravel; // in inches
    private double tireGrip; // rating from 1-10

    /**
     * Creates a new GravelCar with the specified parameters.
     *
     * @param make            The car's make
     * @param model           The car's model
     * @param horsepower      The car's horsepower
     * @param suspensionTravel The car's suspension travel in inches
     * @param tireGrip        The car's tire grip rating (1-10)
     */
    public GravelCar(String make, String model, int horsepower, double suspensionTravel, double tireGrip) {
        super(make, model, horsepower);
        this.suspensionTravel = suspensionTravel;
        this.tireGrip = tireGrip;
    }

    /**
     * Gets the car's suspension travel.
     *
     * @return The suspension travel in inches
     */
    public double getSuspensionTravel() {
        return suspensionTravel;
    }

    /**
     * Sets the car's suspension travel.
     *
     * @param suspensionTravel The suspension travel in inches
     */
    public void setSuspensionTravel(double suspensionTravel) {
        this.suspensionTravel = suspensionTravel;
    }

    /**
     * Gets the car's tire grip rating.
     *
     * @return The tire grip rating (1-10)
     */

    public double getTireGrip() {
        return tireGrip;
    }

    /**
     * Sets the car's tire grip rating.
     *
     * @param tireGrip The tire grip rating (1-10)
     */
    public void setTireGrip(double tireGrip) {
        this.tireGrip = tireGrip;
    }

    /**
     * Calculates the performance rating for gravel surfaces.
     * Formula considers horsepower, tire grip, and suspension travel.
     *
     * @return The performance rating
     */
    @Override
    public double calculatePerformance() {
        // For gravel, we prioritize suspension travel and adaptive tire grip
        return (getHorsepower() * 0.5) + (suspensionTravel * 25) + (tireGrip * 15);
    }
}