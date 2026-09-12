package com.yeaminsheikh.macroflow;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.appbar.MaterialToolbar;
import com.yeaminsheikh.macroflow.models.UserProfile;
import com.yeaminsheikh.macroflow.utils.CalorieCalculator;
import com.yeaminsheikh.macroflow.utils.PreferenceManager;

import java.util.Locale;

/**
 * Activity for calculating and tuning Mifflin-St Jeor TDEE energy targets,
 * daily calorie deficit/surplus, and macronutrient gram goals.
 */
public class ProfileGoalsActivity extends AppCompatActivity {

    private PreferenceManager prefManager;

    private EditText etAge;
    private Spinner spGender;
    private EditText etHeight;
    private EditText etWeight;
    private Spinner spActivityLevel;
    private Spinner spGoal;
    private TextView tvPreviewCalories;
    private TextView tvPreviewMacros;
    private Button btnCalculateSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_goals);

        prefManager = new PreferenceManager(this);

        MaterialToolbar toolbar = findViewById(R.id.toolbar_profile);
        toolbar.setNavigationOnClickListener(v -> finish());

        initViews();
        setupSpinners();
        loadExistingProfile();

        btnCalculateSave.setOnClickListener(v -> calculateAndSave());
    }

    private void initViews() {
        etAge = findViewById(R.id.et_profile_age);
        spGender = findViewById(R.id.sp_gender);
        etHeight = findViewById(R.id.et_profile_height);
        etWeight = findViewById(R.id.et_profile_weight);
        spActivityLevel = findViewById(R.id.sp_activity_level);
        spGoal = findViewById(R.id.sp_goal);
        tvPreviewCalories = findViewById(R.id.tv_preview_calories);
        tvPreviewMacros = findViewById(R.id.tv_preview_macros);
        btnCalculateSave = findViewById(R.id.btn_calculate_save);
    }

    private void setupSpinners() {
        String[] genders = {"Male", "Female"};
        spGender.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, genders));

        String[] activities = {"Sedentary", "Light", "Moderate", "Active", "Very Active"};
        spActivityLevel.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, activities));

        String[] goals = {"Cut", "Maintain", "Bulk"};
        spGoal.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, goals));
    }

    private void loadExistingProfile() {
        UserProfile p = prefManager.getProfile();
        etAge.setText(String.valueOf(p.getAge()));
        etHeight.setText(String.format(Locale.US, "%.0f", p.getHeightCm()));
        etWeight.setText(String.format(Locale.US, "%.1f", p.getWeightKg()));

        if ("Female".equalsIgnoreCase(p.getGender())) spGender.setSelection(1);
        else spGender.setSelection(0);

        String[] acts = {"Sedentary", "Light", "Moderate", "Active", "Very Active"};
        for (int i = 0; i < acts.length; i++) {
            if (acts[i].equalsIgnoreCase(p.getActivityLevel())) spActivityLevel.setSelection(i);
        }

        String[] goals = {"Cut", "Maintain", "Bulk"};
        for (int i = 0; i < goals.length; i++) {
            if (goals[i].equalsIgnoreCase(p.getGoal())) spGoal.setSelection(i);
        }

        updatePreview(p);
    }

    private void updatePreview(UserProfile p) {
        tvPreviewCalories.setText(String.format(Locale.US, "%,d kcal / day", p.getTargetCalories()));
        tvPreviewMacros.setText(String.format(Locale.US, "Protein: %dg | Carbs: %dg | Fats: %dg | Water: %,dml",
                p.getTargetProtein(), p.getTargetCarbs(), p.getTargetFat(), p.getTargetWaterMl()));
    }

    private void calculateAndSave() {
        int age = 25;
        double height = 175;
        double weight = 70;

        try { age = Integer.parseInt(etAge.getText().toString().trim()); } catch (Exception ignored) {}
        try { height = Double.parseDouble(etHeight.getText().toString().trim()); } catch (Exception ignored) {}
        try { weight = Double.parseDouble(etWeight.getText().toString().trim()); } catch (Exception ignored) {}

        String gender = (String) spGender.getSelectedItem();
        String activity = (String) spActivityLevel.getSelectedItem();
        String goal = (String) spGoal.getSelectedItem();

        UserProfile calculated = CalorieCalculator.calculateTargets(age, gender, height, weight, activity, goal);
        prefManager.saveProfile(calculated);
        updatePreview(calculated);

        Toast.makeText(this, "Macro targets calculated & saved!", Toast.LENGTH_SHORT).show();
        finish();
    }
}
