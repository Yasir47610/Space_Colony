package com.example.spacecolony;

/**
 * Base class for all crew member types.
 * Pilot, Engineer, Medic, Scientist, and Soldier all extend this class.
 * This is the core of our OOP design - inheritance + polymorphism.
 */
public abstract class CrewMember {

    // --- Fields ---
    private String name;
    private String specialization;
    private int skill;
    private int resilience;
    private int experience;
    private int energy;
    private int maxEnergy;
    private int id;

    // Each crew member gets a unique ID using this counter
    private static int idCounter = 1;

    // Where the crew member currently is: "Quarters", "Simulator", "MissionControl", "Medbay"
    private String location;

    // Stats tracked for the Statistics screen
    private int missionsCompleted;
    private int missionsWon;
    private int trainingSessions;

    // --- Constructor ---
    public CrewMember(String name, String specialization, int skill, int resilience, int maxEnergy) {
        this.name = name;
        this.specialization = specialization;
        this.skill = skill;
        this.resilience = resilience;
        this.maxEnergy = maxEnergy;
        this.energy = maxEnergy;   // starts at full energy
        this.experience = 0;       // fresh recruit, no experience yet
        this.id = idCounter++;     // auto-assign unique ID
        this.location = "Quarters"; // all crew start at home
        this.missionsCompleted = 0;
        this.missionsWon = 0;
        this.trainingSessions = 0;
    }

    // --- Abstract method ---
    // Each specialization can override this to add a unique bonus description
    public abstract String getSpecialBonus();

    // --- Combat methods ---

    /**
     * act() returns how much damage this crew member deals.
     * Skill + experience gives the total attack power.
     * We add a small random factor (0-2) to keep missions exciting.
     */
    public int act() {
        int randomBonus = (int) (Math.random() * 3); // random 0, 1, or 2
        return skill + experience + randomBonus;
    }

    /**
     * defend() takes incoming damage and reduces it by resilience.
     * If damage would go below 0, we floor it at 0 (no healing from defense).
     */
    public void defend(int damage) {
        int actualDamage = Math.max(0, damage - resilience);
        energy = Math.max(0, energy - actualDamage);
    }

    /**
     * isAlive() - simple check if crew member still has energy left.
     */
    public boolean isAlive() {
        return energy > 0;
    }

    /**
     * restoreEnergy() - called when crew member returns to Quarters.
     * Fully restores energy but keeps experience points earned.
     */
    public void restoreEnergy() {
        this.energy = this.maxEnergy;
    }

    /**
     * train() - called from Simulator.
     * Adds 1 experience point which permanently increases skill power.
     */
    public void train() {
        this.experience++;
        this.trainingSessions++;
    }

    // --- Getters and Setters ---

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getSpecialization() { return specialization; }

    public int getSkill() { return skill; }
    public void setSkill(int skill) { this.skill = skill; }

    public int getResilience() { return resilience; }

    public int getExperience() { return experience; }
    public void setExperience(int experience) { this.experience = experience; }

    public int getEnergy() { return energy; }
    public void setEnergy(int energy) { this.energy = energy; }

    public int getMaxEnergy() { return maxEnergy; }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public int getMissionsCompleted() { return missionsCompleted; }
    public void setMissionsCompleted(int missionsCompleted) { this.missionsCompleted = missionsCompleted; }

    public int getMissionsWon() { return missionsWon; }
    public void setMissionsWon(int missionsWon) { this.missionsWon = missionsWon; }

    public int getTrainingSessions() { return trainingSessions; }
    public void setTrainingSessions(int trainingSessions) { this.trainingSessions = trainingSessions; }

    // Reset idCounter - used when loading saved data so IDs don't duplicate
    public static void setIdCounter(int value) { idCounter = value; }
    public static int getIdCounter() { return idCounter; }

    @Override
    public String toString() {
        return specialization + "(" + name + ") skill:" + (skill + experience) +
                " res:" + resilience + " energy:" + energy + "/" + maxEnergy;
    }
}