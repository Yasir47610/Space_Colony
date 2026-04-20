package com.example.spacecolony;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Storage class - the central data store for the whole app.
 * Uses a Singleton pattern so every Activity shares the same data.
 * HashMap<Integer, CrewMember> stores crew members by their unique ID.
 * Also handles saving and loading crew data to/from a JSON file.
 */
public class Storage {

    // Singleton instance - only one Storage exists in the whole app
    private static Storage instance;

    // The main data structure: ID -> CrewMember
    private HashMap<Integer, CrewMember> crewMembers;

    // Tracks how many missions have been launched (used for threat scaling)
    private int missionCount;

    // File name for saving crew data on the device
    private static final String SAVE_FILE = "space_colony_save.json";

    // Private constructor - use getInstance() instead
    private Storage() {
        crewMembers = new HashMap<>();
        missionCount = 0;
    }

    /**
     * getInstance() - returns the single shared Storage object.
     * Creates it if it doesn't exist yet.
     */
    public static Storage getInstance() {
        if (instance == null) {
            instance = new Storage();
        }
        return instance;
    }

    // --- Crew Management ---

    /**
     * addCrewMember() - adds a new crew member to the HashMap.
     */
    public void addCrewMember(CrewMember cm) {
        crewMembers.put(cm.getId(), cm);
    }

    /**
     * getCrewMember() - retrieves a crew member by their ID.
     */
    public CrewMember getCrewMember(int id) {
        return crewMembers.get(id);
    }

    /**
     * removeCrewMember() - removes a crew member (called when they die in battle).
     */
    public void removeCrewMember(int id) {
        crewMembers.remove(id);
    }

    /**
     * listCrewMembers() - returns all crew members as a List.
     * Useful for RecyclerView adapters.
     */
    public List<CrewMember> listCrewMembers() {
        return new ArrayList<>(crewMembers.values());
    }

    /**
     * getCrewByLocation() - filters crew members by their current location.
     * Used by each screen to show only relevant crew members.
     *
     * @param location "Quarters", "Simulator", "MissionControl", or "Medbay"
     */
    public List<CrewMember> getCrewByLocation(String location) {
        List<CrewMember> result = new ArrayList<>();
        for (CrewMember cm : crewMembers.values()) {
            if (cm.getLocation().equals(location)) {
                result.add(cm);
            }
        }
        return result;
    }

    // --- Mission Counter ---

    public int getMissionCount() { return missionCount; }

    public void incrementMissionCount() { missionCount++; }

    // --- Save / Load ---

    /**
     * saveToFile() - serializes all crew data to JSON and writes it to device storage.
     * Uses Gson for easy Java object -> JSON conversion.
     */
    public void saveToFile(Context context) {
        try {
            Gson gson = new GsonBuilder().create();
            JsonArray jsonArray = new JsonArray();

            for (CrewMember cm : crewMembers.values()) {
                JsonObject obj = new JsonObject();
                // Save all the fields we need to reconstruct the crew member
                obj.addProperty("name", cm.getName());
                obj.addProperty("specialization", cm.getSpecialization());
                obj.addProperty("skill", cm.getSkill());
                obj.addProperty("resilience", cm.getResilience());
                obj.addProperty("experience", cm.getExperience());
                obj.addProperty("energy", cm.getEnergy());
                obj.addProperty("maxEnergy", cm.getMaxEnergy());
                obj.addProperty("id", cm.getId());
                obj.addProperty("location", cm.getLocation());
                obj.addProperty("missionsCompleted", cm.getMissionsCompleted());
                obj.addProperty("missionsWon", cm.getMissionsWon());
                obj.addProperty("trainingSessions", cm.getTrainingSessions());
                jsonArray.add(obj);
            }

            // Wrap crew array + mission count in a root object
            JsonObject root = new JsonObject();
            root.add("crew", jsonArray);
            root.addProperty("missionCount", missionCount);
            root.addProperty("idCounter", CrewMember.getIdCounter());

            // Write to file in app's private storage
            File file = new File(context.getFilesDir(), SAVE_FILE);
            FileWriter writer = new FileWriter(file);
            writer.write(gson.toJson(root));
            writer.close();

        } catch (IOException e) {
            e.printStackTrace(); // log it but don't crash - saving is optional
        }
    }

    /**
     * loadFromFile() - reads the JSON save file and reconstructs all crew members.
     * Called when the app starts up.
     */
    public boolean loadFromFile(Context context) {
        try {
            File file = new File(context.getFilesDir(), SAVE_FILE);
            if (!file.exists()) {
                return false; // no save file yet, that's fine
            }

            BufferedReader reader = new BufferedReader(new FileReader(file));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
            reader.close();

            // Parse the JSON back into crew members
            JsonObject root = JsonParser.parseString(sb.toString()).getAsJsonObject();
            missionCount = root.get("missionCount").getAsInt();
            int savedIdCounter = root.get("idCounter").getAsInt();
            CrewMember.setIdCounter(savedIdCounter);

            crewMembers.clear();
            JsonArray jsonArray = root.get("crew").getAsJsonArray();

            for (int i = 0; i < jsonArray.size(); i++) {
                JsonObject obj = jsonArray.get(i).getAsJsonObject();
                String name = obj.get("name").getAsString();
                String spec = obj.get("specialization").getAsString();
                int experience = obj.get("experience").getAsInt();
                int energy = obj.get("energy").getAsInt();
                int id = obj.get("id").getAsInt();
                String location = obj.get("location").getAsString();
                int missionsCompleted = obj.get("missionsCompleted").getAsInt();
                int missionsWon = obj.get("missionsWon").getAsInt();
                int trainingSessions = obj.get("trainingSessions").getAsInt();

                // Recreate the correct subclass based on specialization
                CrewMember cm = createCrewMember(spec, name);
                if (cm != null) {
                    cm.setExperience(experience);
                    cm.setEnergy(energy);
                    cm.setId(id);
                    cm.setLocation(location);
                    cm.setMissionsCompleted(missionsCompleted);
                    cm.setMissionsWon(missionsWon);
                    cm.setTrainingSessions(trainingSessions);
                    crewMembers.put(id, cm);
                }
            }

            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * createCrewMember() - factory method that creates the right subclass
     * based on the specialization string. Used during loading from file.
     */
    public static CrewMember createCrewMember(String specialization, String name) {
        switch (specialization) {
            case "Pilot":     return new Pilot(name);
            case "Engineer":  return new Engineer(name);
            case "Medic":     return new Medic(name);
            case "Scientist": return new Scientist(name);
            case "Soldier":   return new Soldier(name);
            default:          return null;
        }
    }

    /**
     * getTotalCrew() - returns total number of crew members recruited so far.
     */
    public int getTotalCrew() {
        return crewMembers.size();
    }
}
