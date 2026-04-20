package com.example.spacecolony;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

/**
 * RecruitActivity - lets the user create a new crew member.
 * User picks a name and specialization, then clicks Recruit.
 * New crew member is added to Storage and placed in Quarters.
 */
public class RecruitActivity extends AppCompatActivity {

    private EditText etCrewName;
    private RadioGroup rgSpecialization;
    private TextView tvSpecialBonus;
    private Button btnCreate, btnCancel;

    private Storage storage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recruit);

        storage = Storage.getInstance();

        // Hook up UI elements
        etCrewName = findViewById(R.id.etCrewName);
        rgSpecialization = findViewById(R.id.rgSpecialization);
        tvSpecialBonus = findViewById(R.id.tvSpecialBonus);
        btnCreate = findViewById(R.id.btnCreate);
        btnCancel = findViewById(R.id.btnCancel);

        // Show the special bonus text when user picks a specialization
        rgSpecialization.setOnCheckedChangeListener((group, checkedId) -> {
            updateBonusText(checkedId);
        });

        // Recruit button - validate input and create the crew member
        btnCreate.setOnClickListener(v -> recruitCrewMember());

        // Cancel just closes this screen and goes back
        btnCancel.setOnClickListener(v -> finish());
    }

    /**
     * updateBonusText() - shows the special bonus description for the selected specialization.
     * This gives the user useful info before they commit to a choice.
     */
    private void updateBonusText(int checkedId) {
        CrewMember temp = null;

        if (checkedId == R.id.rbPilot) {
            temp = new Pilot("temp");
        } else if (checkedId == R.id.rbEngineer) {
            temp = new Engineer("temp");
        } else if (checkedId == R.id.rbMedic) {
            temp = new Medic("temp");
        } else if (checkedId == R.id.rbScientist) {
            temp = new Scientist("temp");
        } else if (checkedId == R.id.rbSoldier) {
            temp = new Soldier("temp");
        }

        if (temp != null) {
            tvSpecialBonus.setText("⭐ " + temp.getSpecialBonus());
        }
    }

    /**
     * recruitCrewMember() - validates input and adds the new crew member to storage.
     * Shows a toast message on success or error.
     */
    private void recruitCrewMember() {
        String name = etCrewName.getText().toString().trim();

        // Make sure the user actually typed a name
        if (name.isEmpty()) {
            Toast.makeText(this, "Please enter a name!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Make sure they picked a specialization
        int checkedId = rgSpecialization.getCheckedRadioButtonId();
        if (checkedId == -1) {
            Toast.makeText(this, "Please select a specialization!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Create the right subclass based on what was selected
        CrewMember newMember = null;
        if (checkedId == R.id.rbPilot) {
            newMember = new Pilot(name);
        } else if (checkedId == R.id.rbEngineer) {
            newMember = new Engineer(name);
        } else if (checkedId == R.id.rbMedic) {
            newMember = new Medic(name);
        } else if (checkedId == R.id.rbScientist) {
            newMember = new Scientist(name);
        } else if (checkedId == R.id.rbSoldier) {
            newMember = new Soldier(name);
        }

        if (newMember != null) {
            // Add to storage - they start in Quarters automatically
            storage.addCrewMember(newMember);

            // Save immediately so data isn't lost if app closes
            storage.saveToFile(this);

            Toast.makeText(this,
                    newMember.getSpecialization() + " " + name + " has joined the colony!",
                    Toast.LENGTH_SHORT).show();

            // Go back to main screen
            finish();
        }
    }
}