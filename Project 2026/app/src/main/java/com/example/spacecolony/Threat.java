package com.example.spacecolony;

/**
 * Threat class - represents the system-generated enemy for each mission.
 * MissionControl creates a new Threat for every mission launched.
 * Difficulty scales based on how many missions have been completed so far.
 */
public class Threat {

    private String name;
    private int skill;
    private int resilience;
    private int energy;
    private int maxEnergy;

    // All possible threat types - picked randomly each mission for variety
    private static final String[] THREAT_NAMES = {
            "Asteroid Storm",
            "Alien Attack",
            "Solar Flare",
            "Fuel Leakage",
            "System Failure",
            "Radiation Leak",
            "Alien Virus",
            "Pirate Raid",
            "Food Contamination",
            "Hull Breach"
    };

    /**
     * Constructor - builds a threat scaled to mission number.
     * Formula: skill = 4 + missionCount, so threats get harder over time.
     *
     * @param missionCount how many missions have been done (used for scaling)
     */
    public Threat(int missionCount) {
        // Pick a random threat name from our list
        int randomIndex = (int) (Math.random() * THREAT_NAMES.length);
        this.name = THREAT_NAMES[randomIndex];

        // Skill scales up with each mission completed - gets progressively harder
        this.skill = 4 + missionCount;

        // Resilience is fixed at 2 - threats always have some defense
        this.resilience = 2;

        // Energy also scales - tougher threats as player progresses
        this.maxEnergy = 20 + (missionCount * 2);
        this.energy = this.maxEnergy;
    }

    /**
     * The threat attacks a crew member.
     * Returns raw damage before the crew member's resilience is applied.
     */
    public void attack(CrewMember target) {
        // Small randomness in threat attacks too - keeps missions unpredictable
        int randomBonus = (int) (Math.random() * 3);
        int damage = this.skill + randomBonus;
        target.defend(damage);
    }

    /**
     * The threat takes damage from a crew member's action.
     * Resilience reduces incoming damage, same as crew members.
     */
    public void takeDamage(int damage) {
        int actualDamage = Math.max(0, damage - resilience);
        energy = Math.max(0, energy - actualDamage);
    }

    /**
     * isDefeated() - returns true when threat has no energy left.
     */
    public boolean isDefeated() {
        return energy <= 0;
    }

    // --- Getters ---

    public String getName() { return name; }
    public int getSkill() { return skill; }
    public int getResilience() { return resilience; }
    public int getEnergy() { return energy; }
    public int getMaxEnergy() { return maxEnergy; }

    @Override
    public String toString() {
        return name + " (skill:" + skill + " res:" + resilience +
                " energy:" + energy + "/" + maxEnergy + ")";
    }
}