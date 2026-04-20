package com.example.spacecolony;

/**
 * Soldier specialization.
 * Stats: skill=9, resilience=0, maxEnergy=16
 * Special bonus: Gets +2 skill on alien attack missions
 * Color theme: Red
 * Note: Highest skill but lowest resilience - glass cannon playstyle
 */
public class Soldier extends CrewMember {

    public Soldier(String name) {
        super(name, "Soldier", 9, 0, 16);
    }

    /**
     * Soldier's special bonus - trained to fight hostile alien threats.
     */
    @Override
    public String getSpecialBonus() {
        return "Combat Expert: +2 skill on alien attack missions";
    }

    /**
     * Soldier gets a bonus against direct combat threats like alien attacks.
     */
    public int actWithBonus(String missionType) {
        int base = act();
        // Soldiers are at their best in direct combat situations
        if (missionType.equalsIgnoreCase("Alien Attack") ||
                missionType.equalsIgnoreCase("Pirate Raid")) {
            base += 2;
        }
        return base;
    }
}
