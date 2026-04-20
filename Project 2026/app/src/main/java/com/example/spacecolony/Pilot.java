package com.example.spacecolony;

/**
 * Pilot specialization.
 * Stats: skill=5, resilience=4, maxEnergy=20
 * Special bonus: Gets +2 skill on navigation/asteroid missions (specialization bonus feature)
 * Color theme: Blue
 */
public class Pilot extends CrewMember {

    public Pilot(String name) {
        super(name, "Pilot", 5, 4, 20);
    }

    /**
     * Pilot's special bonus - great at navigating dangerous space conditions.
     * Used by MissionControl to apply specialization bonuses on certain mission types.
     */
    @Override
    public String getSpecialBonus() {
        return "Navigation Expert: +2 skill on asteroid/navigation missions";
    }

    /**
     * Pilot gets a skill bonus on asteroid-type missions.
     * This demonstrates polymorphism - each class handles mission bonuses differently.
     */
    public int actWithBonus(String missionType) {
        int base = act();
        // Pilots get extra skill against asteroid threats
        if (missionType.equalsIgnoreCase("Asteroid Storm") ||
                missionType.equalsIgnoreCase("Navigation")) {
            base += 2;
        }
        return base;
    }
}