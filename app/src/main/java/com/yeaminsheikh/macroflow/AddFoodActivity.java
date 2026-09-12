package com.yeaminsheikh.macroflow;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.appbar.MaterialToolbar;
import com.yeaminsheikh.macroflow.adapters.FoodSearchAdapter;
import com.yeaminsheikh.macroflow.database.MacroDatabaseHelper;
import com.yeaminsheikh.macroflow.models.FoodItem;
import com.yeaminsheikh.macroflow.models.MealLogEntry;

import java.util.List;

/**
 * Activity for logging nutrition: searchable preloaded database + custom meal builder.
 */
public class AddFoodActivity extends AppCompatActivity {

    private MacroDatabaseHelper dbHelper;

    private EditText etSearch;
    private RecyclerView rvSearchResults;
    private FoodSearchAdapter searchAdapter;

    private Spinner spMealType;
    private EditText etFoodName;
    private EditText etServings;
    private EditText etServingSize;
    private EditText etCalories;
    private EditText etProtein;
    private EditText etCarbs;
    private EditText etFat;
    private Button btnLogFood;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_food);

        dbHelper = new MacroDatabaseHelper(this);
        initViews();
        setupToolbar();
        setupMealSpinner();
        setupSearch();

        btnLogFood.setOnClickListener(v -> logMealItem());
    }

    private void initViews() {
        etSearch = findViewById(R.id.et_search_food);
        rvSearchResults = findViewById(R.id.rv_search_results);
        spMealType = findViewById(R.id.sp_meal_type);
        etFoodName = findViewById(R.id.et_food_name);
        etServings = findViewById(R.id.et_servings);
        etServingSize = findViewById(R.id.et_serving_size);
        etCalories = findViewById(R.id.et_calories);
        etProtein = findViewById(R.id.et_protein);
        etCarbs = findViewById(R.id.et_carbs);
        etFat = findViewById(R.id.et_fat);
        btnLogFood = findViewById(R.id.btn_log_food);
    }

    private void setupToolbar() {
        MaterialToolbar toolbar = findViewById(R.id.toolbar_add_food);
        toolbar.setNavigationOnClickListener(v -> finish());
    }

    private void setupMealSpinner() {
        String[] meals = {"Breakfast", "Lunch", "Dinner", "Snacks"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, meals);
        spMealType.setAdapter(adapter);
    }

    private void setupSearch() {
        searchAdapter = new FoodSearchAdapter(this::populateFromSearch);
        rvSearchResults.setLayoutManager(new LinearLayoutManager(this));
        rvSearchResults.setAdapter(searchAdapter);

        // Initial search load
        List<FoodItem> initial = dbHelper.searchFoods("");
        searchAdapter.setItems(initial);

        etSearch.addTextChangedWatcher(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
                List<FoodItem> results = dbHelper.searchFoods(s.toString());
                searchAdapter.setItems(results);
            }
            @Override public void afterTextChanged(Editable s) {}
        });
    }

    private void populateFromSearch(FoodItem item) {
        etFoodName.setText(item.getName());
        etServingSize.setText(item.getServingUnit());
        etCalories.setText(String.valueOf(item.getCaloriesPerServing()));
        etProtein.setText(String.valueOf(item.getProteinGrams()));
        etCarbs.setText(String.valueOf(item.getCarbsGrams()));
        etFat.setText(String.valueOf(item.getFatGrams()));
        Toast.makeText(this, "Selected: " + item.getName(), Toast.LENGTH_SHORT).show();
    }

    private void logMealItem() {
        String name = etFoodName.getText().toString().trim();
        if (name.isEmpty()) {
            etFoodName.setError("Enter food name");
            etFoodName.requestFocus();
            return;
        }

        String mealType = (String) spMealType.getSelectedItem();
        double servings = 1.0;
        try {
            servings = Double.parseDouble(etServings.getText().toString().trim());
        } catch (Exception ignored) {}

        String unit = etServingSize.getText().toString().trim();
        if (unit.isEmpty()) unit = "portion";

        int baseCal = 0;
        double baseProt = 0.0;
        double baseCarb = 0.0;
        double baseFat = 0.0;

        try { baseCal = Integer.parseInt(etCalories.getText().toString().trim()); } catch (Exception ignored) {}
        try { baseProt = Double.parseDouble(etProtein.getText().toString().trim()); } catch (Exception ignored) {}
        try { baseCarb = Double.parseDouble(etCarbs.getText().toString().trim()); } catch (Exception ignored) {}
        try { baseFat = Double.parseDouble(etFat.getText().toString().trim()); } catch (Exception ignored) {}

        int totalCal = (int) Math.round(baseCal * servings);
        double totalProt = baseProt * servings;
        double totalCarb = baseCarb * servings;
        double totalFat = baseFat * servings;

        MealLogEntry entry = new MealLogEntry(0, name, mealType, servings, unit, totalCal, totalProt, totalCarb, totalFat, MacroDatabaseHelper.getTodayDateString());
        dbHelper.insertMealLog(entry);

        Toast.makeText(this, "Logged " + totalCal + " kcal to " + mealType + "!", Toast.LENGTH_SHORT).show();
        finish();
    }
}
