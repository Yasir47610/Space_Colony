package com.example.spacecolony;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

/**
 * MissionLogActivity - the battle screen where missions play out.
 * Implements TACTICAL COMBAT (bonus feature): player chooses Attack, Defend, or Special
 * each turn instead of the mission being fully automatic.
 * Shows live energy bars for both crew members and the threat (Mission Visualization bonus).
 */
public class MissionLogActivity extends AppCompatActivity {

    // Threat and crew status UI
    private TextView tvThreatName, tvThreatEnergy, tvCrewAName, tvCrewAEnergy, tvCrewBName, tvCrewBEnergy;
    private ProgressBar pbThreatEnergy, pbCrewAEnergy, pbCrewBEnergy;
    private ImageView ivCrewA, ivCrewB;

    // Mission log text area
    private TextView tvMissionLog;
    private ScrollView scrollMissionLog;

    // Action buttons (tactical combat)
    private Button btnAttack, btnDefend, btnSpecial, btnFinish;
    private View layoutActions;

    // Mission state
    private CrewMember crewA, crewB;
    private Threat threat;
    private Storage storage;

    // Tracks whose turn it is: true = crewA, false = crewB
    private boolean isCrewATurn = true;
    private boolean missionOver = false;

    // StringBuilder accumulates the full mission log text
    private StringBuilder missionLog = new StringBuilder();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mission_log);

        storage = Storage.getInstance();

        // Get the crew member IDs passed from MissionControlActivity
        int crewAId = getIntent().getIntExtra("crewAId", -1);
        int crewBId = getIntent().getIntExtra("crewBId", -1);

        crewA = storage.getCrewMember(crewAId);
        crewB = storage.getCrewMember(crewBId);

        // Create a new threat scaled to current mission count
        threat = new Threat(storage.getMissionCount());

        // Hook up all UI elements
        tvThreatName = findViewById(R.id.tvThreatName);
        tvThreatEnergy = findViewById(R.id.tvThreatEnergy);
        tvCrewAName = findViewById(R.id.tvCrewAName);
        tvCrewAEnergy = findViewById(R.id.tvCrewAEnergy);
        tvCrewBName = findViewById(R.id.tvCrewBName);
        tvCrewBEnergy = findViewById(R.id.tvCrewBEnergy);
        pbThreatEnergy = findViewById(R.id.pbThreatEnergy);
        pbCrewAEnergy = findViewById(R.id.pbCrewAEnergy);
        pbCrewBEnergy = findViewById(R.id.pbCrewBEnergy);
        ivCrewA = findViewById(R.id.ivCrewA);
        ivCrewB = findViewById(R.id.ivCrewB);
        tvMissionLog = findViewById(R.id.tvMissionLog);
        scrollMissionLog = findViewById(R.id.scrollMissionLog);
        layoutActions = findViewById(R.id.layoutActions);
        btnAttack = findViewById(R.id.btnAttack);
        btnDefend = findViewById(R.id.btnDefend);
        btnSpecial = findViewById(R.id.btnSpecial);
        btnFinish = findViewById(R.id.btnFinish);

        // Set crew images based on specialization
        ivCrewA.setImageResource(getCrewImage(crewA.getSpecialization()));
        ivCrewB.setImageResource(getCrewImage(crewB.getSpecialization()));

        // Show mission intro in the log
        appendLog("=== MISSION: " + threat.getName() + " ===");
        appendLog("Threat: " + threat.toString());
        appendLog(crewA.toString());
        appendLog(crewB.toString());
        appendLog("\n--- Your turn: " + getCurrentCrewName() + " ---");

        // Update all the energy bars to starting values
        updateUI();

        // --- Tactical Combat Button Listeners ---

        // Attack: deal full damage based on skill + XP + random
        btnAttack.setOnClickListener(v -> {
            if (!missionOver) performAction("attack");
        });

        // Defend: deal half damage but crew member takes reduced damage this turn
        btnDefend.setOnClickListener(v -> {
            if (!missionOver) performAction("defend");
        });

        // Special: use specialization bonus attack (higher damage, based on threat type)
        btnSpecial.setOnClickListener(v -> {
            if (!missionOver) performAction("special");
        });

        // Finish button - return to Mission Control after mission ends
        btnFinish.setOnClickListener(v -> finish());
    }

    /**
     * performAction() - handles the player's chosen action for their crew member's turn.
     * After the player acts, the threat retaliates automatically.
     * Then switches to the other crew member's turn.
     */
    private void performAction(String action) {
        CrewMember current = isCrewATurn ? crewA : crewB;

        // --- Player's action ---
        int damageDealt = 0;
        switch (action) {
            case "attack":
                // Standard attack using act() which includes skill + XP + random
                damageDealt = current.act();
                appendLog("\n" + current.getSpecialization() + "(" + current.getName() +
                        ") ATTACKS " + threat.getName());
                break;

            case "defend":
                // Defensive move - half attack but crew gains temporary resilience boost
                damageDealt = current.act() / 2;
                appendLog("\n" + current.getSpecialization() + "(" + current.getName() +
                        ") DEFENDS and strikes for half damage");
                break;

            case "special":
                // Special ability - uses the specialization bonus actWithBonus()
                damageDealt = getSpecialDamage(current);
                appendLog("\n" + current.getSpecialization() + "(" + current.getName() +
                        ") uses SPECIAL ABILITY!");
                break;
        }

        // Apply damage to threat
        threat.takeDamage(damageDealt);
        appendLog("  Damage dealt: " + damageDealt +
                " | " + threat.getName() + " energy: " +
                threat.getEnergy() + "/" + threat.getMaxEnergy());

        // Check if threat is defeated
        if (threat.isDefeated()) {
            missionSuccess();
            return;
        }

        // --- Threat retaliates against the current crew member ---
        appendLog(threat.getName() + " retaliates against " + current.getName() + "!");

        // Defend action gives double resilience against retaliation
        if (action.equals("defend")) {
            // Temporarily boost resilience for this hit
            int extraDefense = current.getResilience();
            int threatDmg = threat.getSkill() - (current.getResilience() + extraDefense);
            int actualDmg = Math.max(0, threatDmg);
            int newEnergy = Math.max(0, current.getEnergy() - actualDmg);
            current.setEnergy(newEnergy);
            appendLog("  Defended! Reduced damage: " + actualDmg +
                    " | " + current.getName() + " energy: " +
                    current.getEnergy() + "/" + current.getMaxEnergy());
        } else {
            threat.attack(current);
            appendLog("  Damage taken: " + " | " + current.getName() +
                    " energy: " + current.getEnergy() + "/" + current.getMaxEnergy());
        }

        // Check if current crew member died
        if (!current.isAlive()) {
            appendLog("💀 " + current.getName() + " has been defeated!");
            handleCrewDeath(current);
            return;
        }

        // Switch to the other crew member's turn
        switchTurn();
        updateUI();

        // Scroll log to bottom so latest events are visible
        scrollMissionLog.post(() -> scrollMissionLog.fullScroll(ScrollView.FOCUS_DOWN));
    }

    /**
     * getSpecialDamage() - gets special ability damage based on crew specialization.
     * Calls actWithBonus() which is overridden in each subclass (polymorphism!).
     */
    private int getSpecialDamage(CrewMember cm) {
        if (cm instanceof Pilot) return ((Pilot) cm).actWithBonus(threat.getName());
        if (cm instanceof Engineer) return ((Engineer) cm).actWithBonus(threat.getName());
        if (cm instanceof Medic) return ((Medic) cm).actWithBonus(threat.getName());
        if (cm instanceof Scientist) return ((Scientist) cm).actWithBonus(threat.getName());
        if (cm instanceof Soldier) return ((Soldier) cm).actWithBonus(threat.getName());
        return cm.act(); // fallback
    }

    /**
     * switchTurn() - alternates between crew A and crew B.
     * Skips a crew member if they're dead.
     */
    private void switchTurn() {
        isCrewATurn = !isCrewATurn;
        // If the next crew member is dead, switch again
        if (isCrewATurn && !crewA.isAlive()) isCrewATurn = false;
        if (!isCrewATurn && !crewB.isAlive()) isCrewATurn = true;
        appendLog("\n--- " + getCurrentCrewName() + "'s turn ---");
    }

    /**
     * handleCrewDeath() - called when a crew member's energy hits 0.
     * Checks if both are dead (mission fail) or just one (other continues).
     */
    private void handleCrewDeath(CrewMember dead) {
        if (!crewA.isAlive() && !crewB.isAlive()) {
            missionFail();
        } else {
            // One crew member down, the other continues
            isCrewATurn = crewA.isAlive();
            appendLog("⚠️ " + dead.getName() + " is down! " +
                    (isCrewATurn ? crewA.getName() : crewB.getName()) + " fights alone!");
            updateUI();
        }
    }

    /**
     * missionSuccess() - called when the threat's energy reaches 0.
     * Awards XP to surviving crew members and updates stats.
     */
    private void missionSuccess() {
        missionOver = true;
        appendLog("\n=== MISSION COMPLETE ===");
        appendLog("The " + threat.getName() + " has been neutralized!");

        // Award XP to survivors and update their mission stats
        if (crewA.isAlive()) {
            crewA.train(); // +1 XP for surviving
            crewA.setMissionsCompleted(crewA.getMissionsCompleted() + 1);
            crewA.setMissionsWon(crewA.getMissionsWon() + 1);
            appendLog(crewA.getName() + " gains 1 XP! (total XP: " + crewA.getExperience() + ")");
        }
        if (crewB.isAlive()) {
            crewB.train();
            crewB.setMissionsCompleted(crewB.getMissionsCompleted() + 1);
            crewB.setMissionsWon(crewB.getMissionsWon() + 1);
            appendLog(crewB.getName() + " gains 1 XP! (total XP: " + crewB.getExperience() + ")");
        }

        // Increment global mission counter (affects threat scaling)
        storage.incrementMissionCount();
        storage.saveToFile(this);

        // Hide action buttons, show finish button
        layoutActions.setVisibility(View.GONE);
        btnFinish.setVisibility(View.VISIBLE);
        updateUI();

        scrollMissionLog.post(() -> scrollMissionLog.fullScroll(ScrollView.FOCUS_DOWN));
    }

    /**
     * missionFail() - called when both crew members are defeated.
     * Removes both crew members from the program permanently.
     */
    private void missionFail() {
        missionOver = true;
        appendLog("\n=== MISSION FAILED ===");
        appendLog("All crew members lost in battle.");

        // Update stats before removing
        crewA.setMissionsCompleted(crewA.getMissionsCompleted() + 1);
        crewB.setMissionsCompleted(crewB.getMissionsCompleted() + 1);

        // Remove dead crew members from storage permanently
        storage.removeCrewMember(crewA.getId());
        storage.removeCrewMember(crewB.getId());
        storage.incrementMissionCount();
        storage.saveToFile(this);

        layoutActions.setVisibility(View.GONE);
        btnFinish.setVisibility(View.VISIBLE);
        updateUI();

        scrollMissionLog.post(() -> scrollMissionLog.fullScroll(ScrollView.FOCUS_DOWN));
    }

    /**
     * updateUI() - refreshes all energy bars and text displays.
     * Called after every action so the player sees live updates.
     */
    private void updateUI() {
        // Threat status
        tvThreatName.setText("☠️ " + threat.getName());
        tvThreatEnergy.setText("Energy: " + threat.getEnergy() + "/" + threat.getMaxEnergy());
        pbThreatEnergy.setProgress(
                (int) ((threat.getEnergy() / (float) threat.getMaxEnergy()) * 100));

        // Crew A status
        tvCrewAName.setText(crewA.getSpecialization() + ": " + crewA.getName());
        tvCrewAEnergy.setText("Energy: " + crewA.getEnergy() + "/" + crewA.getMaxEnergy());
        pbCrewAEnergy.setProgress(
                (int) ((crewA.getEnergy() / (float) crewA.getMaxEnergy()) * 100));

        // Crew B status
        tvCrewBName.setText(crewB.getSpecialization() + ": " + crewB.getName());
        tvCrewBEnergy.setText("Energy: " + crewB.getEnergy() + "/" + crewB.getMaxEnergy());
        pbCrewBEnergy.setProgress(
                (int) ((crewB.getEnergy() / (float) crewB.getMaxEnergy()) * 100));
    }

    /**
     * appendLog() - adds a line to the mission log TextView.
     */
    private void appendLog(String text) {
        missionLog.append(text).append("\n");
        tvMissionLog.setText(missionLog.toString());
    }

    /**
     * getCurrentCrewName() - returns the name of whichever crew member's turn it is.
     */
    private String getCurrentCrewName() {
        CrewMember current = isCrewATurn ? crewA : crewB;
        return current.getSpecialization() + "(" + current.getName() + ")";
    }

    /**
     * getCrewImage() - returns drawable resource ID for specialization image.
     */
    private int getCrewImage(String specialization) {
        switch (specialization) {
            case "Pilot":     return R.drawable.ic_pilot;
            case "Engineer":  return R.drawable.ic_engineer;
            case "Medic":     return R.drawable.ic_medic;
            case "Scientist": return R.drawable.ic_scientist;
            case "Soldier":   return R.drawable.ic_soldier;
            default:          return android.R.drawable.ic_menu_myplaces;
        }
    }
}