package com.example.spacecolony;

/**
 * Engineer specialization.
 * Stats: skill=6, resilience=3, maxEnergy=19
 * Special bonus: Gets +2 skill on repair/technical missions
 * Color theme: Yellow
 */
public class Engineer extends CrewMember {

    public Engineer(String name) {
        super(name, "Engineer", 6, 3, 19);
    }

    /**
     * Engineer's special bonus - expert at fixing technical failures.
     */
    @Override
    public String getSpecialBonus() {
        return "Tech Expert: +2 skill on repair/technical missions";
    }

    /**
     * Engineer gets a bonus against technical threat types like fuel leaks or system failures.
     */
    public int actWithBonus(String missionType) {
        int base = act();
        // Engineers shine when fixing things
        if (missionType.equalsIgnoreCase("Fuel Leakage") ||
                missionType.equalsIgnoreCase("System Failure")) {
            base += 2;
        }
        return base;
    }
}