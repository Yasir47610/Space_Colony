package com.example.spacecolony;

/**
 * Medic specialization.
 * Stats: skill=7, resilience=2, maxEnergy=18
 * Special bonus: Gets +2 skill on disease/biological threat missions
 * Color theme: Green
 */
public class Medic extends CrewMember {

    public Medic(String name) {
        super(name, "Medic", 7, 2, 18);
    }

    /**
     * Medic's special bonus - handles biological and health threats best.
     */
    @Override
    public String getSpecialBonus() {
        return "Medical Expert: +2 skill on biological/disease missions";
    }

    /**
     * Medic gets a bonus against biological threats like alien viruses or food poisoning.
     */
    public int actWithBonus(String missionType) {
        int base = act();
        // Medics are best at handling anything health-related
        if (missionType.equalsIgnoreCase("Alien Virus") ||
                missionType.equalsIgnoreCase("Food Contamination")) {
            base += 2;
        }
        return base;
    }
}