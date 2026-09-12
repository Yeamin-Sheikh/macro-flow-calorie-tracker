package com.yeaminsheikh.macroflow;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.appbar.MaterialToolbar;
import com.yeaminsheikh.macroflow.adapters.MealLogAdapter;
import com.yeaminsheikh.macroflow.database.MacroDatabaseHelper;
import com.yeaminsheikh.macroflow.models.DailyNutritionSummary;
import com.yeaminsheikh.macroflow.models.MealLogEntry;
import com.yeaminsheikh.macroflow.models.UserProfile;
import com.yeaminsheikh.macroflow.utils.PreferenceManager;

import java.util.List;
import java.util.Locale;

/**
 * Historical activity displaying full meal log records for today.
 */
public class DailyLogActivity extends AppCompatActivity implements MealLogAdapter.OnDeleteClickListener {

    private MacroDatabaseHelper dbHelper;
    private PreferenceManager prefManager;
    private TextView tvSummary;
    private RecyclerView rvLogs;
    private MealLogAdapter adapter;
    private String today;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_log);

        dbHelper = new MacroDatabaseHelper(this);
        prefManager = new PreferenceManager(this);
        today = MacroDatabaseHelper.getTodayDateString();

        MaterialToolbar toolbar = findViewById(R.id.toolbar_daily_log);
        toolbar.setNavigationOnClickListener(v -> finish());

        tvSummary = findViewById(R.id.tv_history_summary);
        rvLogs = findViewById(R.id.rv_history_logs);

        adapter = new MealLogAdapter(this);
        rvLogs.setLayoutManager(new LinearLayoutManager(this));
        rvLogs.setAdapter(adapter);

        refreshLogs();
    }

    private void refreshLogs() {
        UserProfile profile = prefManager.getProfile();
        DailyNutritionSummary summary = dbHelper.getDailySummary(today, profile);

        String sumStr = String.format(Locale.US, "Today: %d kcal | Protein: %.1fg | Carbs: %.1fg | Fats: %.1fg | Water: %dml",
                summary.getConsumedCalories(), summary.getConsumedProtein(), summary.getConsumedCarbs(), summary.getConsumedFat(), summary.getConsumedWaterMl());
        tvSummary.setText(sumStr);

        List<MealLogEntry> entries = dbHelper.getLogsForDate(today);
        adapter.setEntries(entries);
    }

    @Override
    public void onDeleteClick(MealLogEntry entry) {
        dbHelper.deleteMealLog(entry.getId());
        refreshLogs();
    }
}
