package com.yeaminsheikh.macroflow;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.yeaminsheikh.macroflow.adapters.MealLogAdapter;
import com.yeaminsheikh.macroflow.database.MacroDatabaseHelper;
import com.yeaminsheikh.macroflow.models.DailyNutritionSummary;
import com.yeaminsheikh.macroflow.models.MealLogEntry;
import com.yeaminsheikh.macroflow.models.UserProfile;
import com.yeaminsheikh.macroflow.utils.PreferenceManager;

import java.util.List;
import java.util.Locale;

/**
 * Primary Dashboard for MacroFlow Android App.
 * Visualizes daily calorie ring progress, macronutrient bars (Protein, Carbs, Fats),
 * hydration water counter, and interactive meal logging list.
 */
public class MainActivity extends AppCompatActivity implements MealLogAdapter.OnDeleteClickListener {

    private MacroDatabaseHelper dbHelper;
    private PreferenceManager prefManager;

    private MaterialToolbar toolbar;
    private TextView tvTodayDate;
    private TextView tvCaloriesStat;
    private ProgressBar pbCalories;
    private TextView tvCaloriesRemaining;

    private TextView tvProteinStat;
    private TextView tvProteinPct;
    private ProgressBar pbProtein;

    private TextView tvCarbsStat;
    private TextView tvCarbsPct;
    private ProgressBar pbCarbs;

    private TextView tvFatsStat;
    private TextView tvFatsPct;
    private ProgressBar pbFats;

    private TextView tvWaterStat;
    private ProgressBar pbWater;
    private Button btnAddWater;

    private RecyclerView rvMealLogs;
    private TextView tvEmptyMeals;
    private FloatingActionButton fabAddFood;

    private MealLogAdapter logAdapter;
    private String selectedDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = new MacroDatabaseHelper(this);
        prefManager = new PreferenceManager(this);
        selectedDate = MacroDatabaseHelper.getTodayDateString();

        initViews();
        setupToolbar();
        setupRecyclerView();
        setupListeners();
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshDashboard();
    }

    private void initViews() {
        toolbar = findViewById(R.id.toolbar);
        tvTodayDate = findViewById(R.id.tv_today_date);
        tvCaloriesStat = findViewById(R.id.tv_calories_stat);
        pbCalories = findViewById(R.id.pb_calories);
        tvCaloriesRemaining = findViewById(R.id.tv_calories_remaining);

        tvProteinStat = findViewById(R.id.tv_protein_stat);
        tvProteinPct = findViewById(R.id.tv_protein_pct);
        pbProtein = findViewById(R.id.pb_protein);

        tvCarbsStat = findViewById(R.id.tv_carbs_stat);
        tvCarbsPct = findViewById(R.id.tv_carbs_pct);
        pbCarbs = findViewById(R.id.pb_carbs);

        tvFatsStat = findViewById(R.id.tv_fats_stat);
        tvFatsPct = findViewById(R.id.tv_fats_pct);
        pbFats = findViewById(R.id.pb_fats);

        tvWaterStat = findViewById(R.id.tv_water_stat);
        pbWater = findViewById(R.id.pb_water);
        btnAddWater = findViewById(R.id.btn_add_water);

        rvMealLogs = findViewById(R.id.rv_meal_logs);
        tvEmptyMeals = findViewById(R.id.tv_empty_meals);
        fabAddFood = findViewById(R.id.fab_add_food);

        tvTodayDate.setText(String.format("Nutrition Log: %s", selectedDate));
    }

    private void setupToolbar() {
        setSupportActionBar(toolbar);
    }

    private void setupRecyclerView() {
        logAdapter = new MealLogAdapter(this);
        rvMealLogs.setLayoutManager(new LinearLayoutManager(this));
        rvMealLogs.setAdapter(logAdapter);
    }

    private void setupListeners() {
        fabAddFood.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AddFoodActivity.class);
            startActivity(intent);
        });

        btnAddWater.setOnClickListener(v -> {
            dbHelper.addWater(selectedDate, 250);
            Toast.makeText(this, "+250 ml logged!", Toast.LENGTH_SHORT).show();
            refreshDashboard();
        });

        findViewById(R.id.card_calorie_overview).setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, ProfileGoalsActivity.class));
        });
    }

    private void refreshDashboard() {
        UserProfile profile = prefManager.getProfile();
        DailyNutritionSummary summary = dbHelper.getDailySummary(selectedDate, profile);

        // Calories
        tvCaloriesStat.setText(String.format(Locale.US, "%d / %d kcal", summary.getConsumedCalories(), summary.getTargetCalories()));
        pbCalories.setProgress(summary.getCaloriePercentage());
        tvCaloriesRemaining.setText(String.format(Locale.US, "%d kcal remaining", summary.getRemainingCalories()));

        // Protein
        tvProteinStat.setText(String.format(Locale.US, "Protein: %.0fg / %.0fg", summary.getConsumedProtein(), summary.getTargetProtein()));
        tvProteinPct.setText(String.format(Locale.US, "%d%%", summary.getProteinPercentage()));
        pbProtein.setProgress(summary.getProteinPercentage());

        // Carbs
        tvCarbsStat.setText(String.format(Locale.US, "Carbs: %.0fg / %.0fg", summary.getConsumedCarbs(), summary.getTargetCarbs()));
        tvCarbsPct.setText(String.format(Locale.US, "%d%%", summary.getCarbsPercentage()));
        pbCarbs.setProgress(summary.getCarbsPercentage());

        // Fats
        tvFatsStat.setText(String.format(Locale.US, "Fats: %.0fg / %.0fg", summary.getConsumedFat(), summary.getTargetFat()));
        tvFatsPct.setText(String.format(Locale.US, "%d%%", summary.getFatPercentage()));
        pbFats.setProgress(summary.getFatPercentage());

        // Water
        tvWaterStat.setText(String.format(Locale.US, "Water: %d / %d ml", summary.getConsumedWaterMl(), summary.getTargetWaterMl()));
        pbWater.setProgress(summary.getWaterPercentage());

        // Meals List
        List<MealLogEntry> logs = dbHelper.getLogsForDate(selectedDate);
        logAdapter.setEntries(logs);

        if (logs.isEmpty()) {
            tvEmptyMeals.setVisibility(View.VISIBLE);
            rvMealLogs.setVisibility(View.GONE);
        } else {
            tvEmptyMeals.setVisibility(View.GONE);
            rvMealLogs.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onDeleteClick(MealLogEntry entry) {
        dbHelper.deleteMealLog(entry.getId());
        Toast.makeText(this, entry.getFoodName() + " removed", Toast.LENGTH_SHORT).show();
        refreshDashboard();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(0, 1, 0, R.string.menu_history);
        menu.add(0, 2, 1, R.string.menu_goals);
        menu.add(0, 3, 2, R.string.menu_reset_day);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == 1) {
            startActivity(new Intent(this, DailyLogActivity.class));
            return true;
        } else if (id == 2) {
            startActivity(new Intent(this, ProfileGoalsActivity.class));
            return true;
        } else if (id == 3) {
            new AlertDialog.Builder(this)
                    .setTitle(R.string.menu_reset_day)
                    .setMessage(R.string.confirm_reset_day)
                    .setPositiveButton(R.string.yes, (d, w) -> {
                        dbHelper.clearDateLogs(selectedDate);
                        Toast.makeText(this, "Day cleared", Toast.LENGTH_SHORT).show();
                        refreshDashboard();
                    })
                    .setNegativeButton(R.string.cancel, null)
                    .show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
