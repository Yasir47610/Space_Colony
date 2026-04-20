package com.example.spacecolony;

/**
 * Scientist specialization.
 * Stats: skill=8, resilience=1, maxEnergy=17
 * Special bonus: Gets +2 skill on solar/radiation missions
 * Color theme: Purple
 */
public class Scientist extends CrewMember {

    public Scientist(String name) {
        super(name, "Scientist", 8, 1, 17);
    }

    /**
     * Scientist's special bonus - understands radiation and cosmic phenomena.
     */
    @Override
    public String getSpecialBonus() {
        return "Research Expert: +2 skill on radiation/solar missions";
    }

    /**
     * Scientist gets a bonus against solar flares or radiation threats.
     */
    public int actWithBonus(String missionType) {
        int base = act();
        // Scientists handle cosmic and radiation threats the best
        if (missionType.equalsIgnoreCase("Solar Flare") ||
                missionType.equalsIgnoreCase("Radiation Leak")) {
            base += 2;
        }
        return base;
    }
}