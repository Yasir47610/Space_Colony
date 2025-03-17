/**
 * Abstract class representing a rally car.
 * Following the Open/Closed Principle, this class can be extended
 * but not modified for different car types.
 */
package main;

public abstract class RallyCar {
    private String make;
    private String model;
    private int horsepower;

    /**
     * Creates a new RallyCar with the specified make, model, and horsepower.
     *
     * @param make       The car's make
     * @param model      The car's model
     * @param horsepower The car's horsepower
     */
    public RallyCar(String make, String model, int horsepower) {
        this.make = make;
        this.model = model;
        this.horsepower = horsepower;
    }

    /**
     * Gets the car's make.
     *
     * @return The car's make
     */
    public String getMake() {
        return make;
    }

    /**
     * Sets the car's make.
     *
     * @param make The car's make
     */
    public void setMake(String make) {
        this.make = make;
    }

    /**
     * Gets the car's model.
     *
     * @return The car's model
     */
    public String getModel() {
        return model;
    }

    /**
     * Sets the car's model.
     *
     * @param model The car's model
     */
    public void setModel(String model) {
        this.model = model;
    }

    /**
     * Gets the car's horsepower.
     *
     * @return The car's horsepower
     */
    public int getHorsepower() {
        return horsepower;
    }

    /**
     * Sets the car's horsepower.
     *
     * @param horsepower The car's horsepower
     */
    public void setHorsepower(int horsepower) {
        this.horsepower = horsepower;
    }

    /**
     * Calculates the performance rating of the car.
     * This is an abstract method that subclasses must implement.
     *
     * @return The performance rating
     */
    public abstract double calculatePerformance();

    @Override
    public String toString() {
        return make + " " + model + " (" + horsepower + " HP)";
    }
}