package com.yeaminsheikh.macroflow.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.yeaminsheikh.macroflow.models.DailyNutritionSummary;
import com.yeaminsheikh.macroflow.models.FoodItem;
import com.yeaminsheikh.macroflow.models.MealLogEntry;
import com.yeaminsheikh.macroflow.models.UserProfile;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * SQLite database helper for MacroFlow.
 * Houses preloaded nutrition foods database, daily meal logs,
 * and hydration counters.
 */
public class MacroDatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "macro_flow.db";
    private static final int DATABASE_VERSION = 1;

    // Table: foods (preloaded standard food library)
    public static final String TABLE_FOODS = "foods";
    public static final String COL_FOOD_ID = "id";
    public static final String COL_FOOD_NAME = "name";
    public static final String COL_FOOD_CAT = "category";
    public static final String COL_FOOD_UNIT = "serving_unit";
    public static final String COL_FOOD_SERVING_AMT = "default_serving_amount";
    public static final String COL_FOOD_KCAL = "calories_per_serving";
    public static final String COL_FOOD_PROT = "protein_grams";
    public static final String COL_FOOD_CARB = "carbs_grams";
    public static final String COL_FOOD_FAT = "fat_grams";

    // Table: meal_logs
    public static final String TABLE_MEAL_LOGS = "meal_logs";
    public static final String COL_LOG_ID = "id";
    public static final String COL_LOG_FOOD_NAME = "food_name";
    public static final String COL_LOG_MEAL_TYPE = "meal_type";
    public static final String COL_LOG_SERVINGS = "servings";
    public static final String COL_LOG_UNIT = "serving_unit";
    public static final String COL_LOG_CALORIES = "total_calories";
    public static final String COL_LOG_PROTEIN = "total_protein";
    public static final String COL_LOG_CARBS = "total_carbs";
    public static final String COL_LOG_FAT = "total_fat";
    public static final String COL_LOG_DATE = "log_date";
    public static final String COL_LOG_TIMESTAMP = "timestamp";

    // Table: water_intake
    public static final String TABLE_WATER = "water_intake";
    public static final String COL_WATER_ID = "id";
    public static final String COL_WATER_DATE = "log_date";
    public static final String COL_WATER_ML = "amount_ml";

    public MacroDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createFoods = "CREATE TABLE " + TABLE_FOODS + " ("
                + COL_FOOD_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COL_FOOD_NAME + " TEXT NOT NULL, "
                + COL_FOOD_CAT + " TEXT, "
                + COL_FOOD_UNIT + " TEXT, "
                + COL_FOOD_SERVING_AMT + " REAL DEFAULT 100.0, "
                + COL_FOOD_KCAL + " INTEGER NOT NULL, "
                + COL_FOOD_PROT + " REAL NOT NULL, "
                + COL_FOOD_CARB + " REAL NOT NULL, "
                + COL_FOOD_FAT + " REAL NOT NULL"
                + ");";
        db.execSQL(createFoods);

        String createMealLogs = "CREATE TABLE " + TABLE_MEAL_LOGS + " ("
                + COL_LOG_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COL_LOG_FOOD_NAME + " TEXT NOT NULL, "
                + COL_LOG_MEAL_TYPE + " TEXT NOT NULL, "
                + COL_LOG_SERVINGS + " REAL NOT NULL, "
                + COL_LOG_UNIT + " TEXT, "
                + COL_LOG_CALORIES + " INTEGER NOT NULL, "
                + COL_LOG_PROTEIN + " REAL NOT NULL, "
                + COL_LOG_CARBS + " REAL NOT NULL, "
                + COL_LOG_FAT + " REAL NOT NULL, "
                + COL_LOG_DATE + " TEXT NOT NULL, "
                + COL_LOG_TIMESTAMP + " INTEGER"
                + ");";
        db.execSQL(createMealLogs);

        String createWater = "CREATE TABLE " + TABLE_WATER + " ("
                + COL_WATER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COL_WATER_DATE + " TEXT NOT NULL, "
                + COL_WATER_ML + " INTEGER NOT NULL"
                + ");";
        db.execSQL(createWater);

        // Seed food database
        seedFoods(db);
        seedTodaySample(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FOODS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MEAL_LOGS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_WATER);
        onCreate(db);
    }

    private void seedFoods(SQLiteDatabase db) {
        Object[][] foods = {
                {"Chicken Breast (Skinless)", "Poultry", "100g", 100.0, 165, 31.0, 0.0, 3.6},
                {"Salmon Fillet (Atlantic)", "Fish", "100g", 100.0, 208, 20.0, 0.0, 13.0},
                {"Large Whole Egg", "Dairy & Eggs", "1 egg (50g)", 50.0, 72, 6.3, 0.4, 4.8},
                {"Egg Whites", "Dairy & Eggs", "100g", 100.0, 52, 11.0, 0.7, 0.2},
                {"Rolled Oats", "Grains", "100g", 100.0, 389, 13.0, 66.0, 6.9},
                {"Brown Rice (Cooked)", "Grains", "100g", 100.0, 112, 2.6, 24.0, 0.9},
                {"Greek Yogurt (Non-Fat)", "Dairy", "100g", 100.0, 59, 10.0, 3.6, 0.4},
                {"Whey Protein Powder", "Supplements", "1 scoop (30g)", 30.0, 120, 24.0, 2.0, 1.5},
                {"Avocado", "Produce", "1/2 medium (100g)", 100.0, 160, 2.0, 8.5, 14.7},
                {"Natural Peanut Butter", "Nuts", "2 tbsp (32g)", 32.0, 190, 8.0, 7.0, 16.0},
                {"Almonds", "Nuts", "1 oz (28g)", 28.0, 164, 6.0, 6.0, 14.0},
                {"Broccoli (Steamed)", "Vegetables", "100g", 100.0, 35, 2.4, 7.2, 0.4},
                {"Sweet Potato (Baked)", "Vegetables", "100g", 100.0, 90, 2.0, 21.0, 0.2},
                {"Banana", "Fruit", "1 medium (118g)", 118.0, 105, 1.3, 27.0, 0.3},
                {"Blueberries", "Fruit", "100g", 100.0, 57, 0.7, 14.0, 0.3},
                {"Olive Oil", "Oils", "1 tbsp (14g)", 14.0, 119, 0.0, 0.0, 13.5}
        };

        for (Object[] row : foods) {
            ContentValues cv = new ContentValues();
            cv.put(COL_FOOD_NAME, (String) row[0]);
            cv.put(COL_FOOD_CAT, (String) row[1]);
            cv.put(COL_FOOD_UNIT, (String) row[2]);
            cv.put(COL_FOOD_SERVING_AMT, (Double) row[3]);
            cv.put(COL_FOOD_KCAL, (Integer) row[4]);
            cv.put(COL_FOOD_PROT, (Double) row[5]);
            cv.put(COL_FOOD_CARB, (Double) row[6]);
            cv.put(COL_FOOD_FAT, (Double) row[7]);
            db.insert(TABLE_FOODS, null, cv);
        }
    }

    private void seedTodaySample(SQLiteDatabase db) {
        String today = getTodayDateString();
        // Seed initial sample logs for immediate dashboard visibility
        ContentValues cv1 = new ContentValues();
        cv1.put(COL_LOG_FOOD_NAME, "Rolled Oats with Blueberries");
        cv1.put(COL_LOG_MEAL_TYPE, "Breakfast");
        cv1.put(COL_LOG_SERVINGS, 1.0);
        cv1.put(COL_LOG_UNIT, "1 bowl");
        cv1.put(COL_LOG_CALORIES, 446);
        cv1.put(COL_LOG_PROTEIN, 13.7);
        cv1.put(COL_LOG_CARBS, 80.0);
        cv1.put(COL_LOG_FAT, 7.2);
        cv1.put(COL_LOG_DATE, today);
        cv1.put(COL_LOG_TIMESTAMP, System.currentTimeMillis());
        db.insert(TABLE_MEAL_LOGS, null, cv1);

        ContentValues cv2 = new ContentValues();
        cv2.put(COL_LOG_FOOD_NAME, "Grilled Chicken & Brown Rice");
        cv2.put(COL_LOG_MEAL_TYPE, "Lunch");
        cv2.put(COL_LOG_SERVINGS, 1.5);
        cv2.put(COL_LOG_UNIT, "portion");
        cv2.put(COL_LOG_CALORIES, 580);
        cv2.put(COL_LOG_PROTEIN, 52.0);
        cv2.put(COL_LOG_CARBS, 48.0);
        cv2.put(COL_LOG_FAT, 8.5);
        cv2.put(COL_LOG_DATE, today);
        cv2.put(COL_LOG_TIMESTAMP, System.currentTimeMillis());
        db.insert(TABLE_MEAL_LOGS, null, cv2);

        // Seed initial water intake
        ContentValues wcv = new ContentValues();
        wcv.put(COL_WATER_DATE, today);
        wcv.put(COL_WATER_ML, 1500);
        db.insert(TABLE_WATER, null, wcv);
    }

    public static String getTodayDateString() {
        return new SimpleDateFormat("yyyy-MM-dd", Locale.US).format(new Date());
    }

    // -------------------------------------------------------------
    // Food Library Queries
    // -------------------------------------------------------------

    public List<FoodItem> searchFoods(String query) {
        List<FoodItem> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "SELECT * FROM " + TABLE_FOODS;
        String[] args = null;
        if (query != null && !query.trim().isEmpty()) {
            sql += " WHERE " + COL_FOOD_NAME + " LIKE ? OR " + COL_FOOD_CAT + " LIKE ?";
            String wildcard = "%" + query.trim() + "%";
            args = new String[]{wildcard, wildcard};
        }
        sql += " ORDER BY " + COL_FOOD_NAME + " ASC LIMIT 30";

        Cursor cursor = db.rawQuery(sql, args);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                FoodItem item = new FoodItem();
                item.setId(cursor.getLong(cursor.getColumnIndexOrThrow(COL_FOOD_ID)));
                item.setName(cursor.getString(cursor.getColumnIndexOrThrow(COL_FOOD_NAME)));
                item.setCategory(cursor.getString(cursor.getColumnIndexOrThrow(COL_FOOD_CAT)));
                item.setServingUnit(cursor.getString(cursor.getColumnIndexOrThrow(COL_FOOD_UNIT)));
                item.setDefaultServingAmount(cursor.getDouble(cursor.getColumnIndexOrThrow(COL_FOOD_SERVING_AMT)));
                item.setCaloriesPerServing(cursor.getInt(cursor.getColumnIndexOrThrow(COL_FOOD_KCAL)));
                item.setProteinGrams(cursor.getDouble(cursor.getColumnIndexOrThrow(COL_FOOD_PROT)));
                item.setCarbsGrams(cursor.getDouble(cursor.getColumnIndexOrThrow(COL_FOOD_CARB)));
                item.setFatGrams(cursor.getDouble(cursor.getColumnIndexOrThrow(COL_FOOD_FAT)));
                list.add(item);
            } while (cursor.moveToNext());
            cursor.close();
        }
        return list;
    }

    // -------------------------------------------------------------
    // Meal Logs CRUD
    // -------------------------------------------------------------

    public long insertMealLog(MealLogEntry entry) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COL_LOG_FOOD_NAME, entry.getFoodName());
        cv.put(COL_LOG_MEAL_TYPE, entry.getMealType());
        cv.put(COL_LOG_SERVINGS, entry.getServings());
        cv.put(COL_LOG_UNIT, entry.getServingUnit());
        cv.put(COL_LOG_CALORIES, entry.getTotalCalories());
        cv.put(COL_LOG_PROTEIN, entry.getTotalProtein());
        cv.put(COL_LOG_CARBS, entry.getTotalCarbs());
        cv.put(COL_LOG_FAT, entry.getTotalFat());
        cv.put(COL_LOG_DATE, entry.getLogDate() != null ? entry.getLogDate() : getTodayDateString());
        cv.put(COL_LOG_TIMESTAMP, entry.getTimestamp() > 0 ? entry.getTimestamp() : System.currentTimeMillis());
        return db.insert(TABLE_MEAL_LOGS, null, cv);
    }

    public int deleteMealLog(long id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_MEAL_LOGS, COL_LOG_ID + " = ?", new String[]{String.valueOf(id)});
    }

    public void clearDateLogs(String date) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_MEAL_LOGS, COL_LOG_DATE + " = ?", new String[]{date});
        db.delete(TABLE_WATER, COL_WATER_DATE + " = ?", new String[]{date});
    }

    public List<MealLogEntry> getLogsForDate(String date) {
        List<MealLogEntry> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_MEAL_LOGS + " WHERE " + COL_LOG_DATE + " = ? ORDER BY " + COL_LOG_ID + " DESC", new String[]{date});
        if (cursor != null && cursor.moveToFirst()) {
            do {
                MealLogEntry entry = new MealLogEntry();
                entry.setId(cursor.getLong(cursor.getColumnIndexOrThrow(COL_LOG_ID)));
                entry.setFoodName(cursor.getString(cursor.getColumnIndexOrThrow(COL_LOG_FOOD_NAME)));
                entry.setMealType(cursor.getString(cursor.getColumnIndexOrThrow(COL_LOG_MEAL_TYPE)));
                entry.setServings(cursor.getDouble(cursor.getColumnIndexOrThrow(COL_LOG_SERVINGS)));
                entry.setServingUnit(cursor.getString(cursor.getColumnIndexOrThrow(COL_LOG_UNIT)));
                entry.setTotalCalories(cursor.getInt(cursor.getColumnIndexOrThrow(COL_LOG_CALORIES)));
                entry.setTotalProtein(cursor.getDouble(cursor.getColumnIndexOrThrow(COL_LOG_PROTEIN)));
                entry.setTotalCarbs(cursor.getDouble(cursor.getColumnIndexOrThrow(COL_LOG_CARBS)));
                entry.setTotalFat(cursor.getDouble(cursor.getColumnIndexOrThrow(COL_LOG_FAT)));
                entry.setLogDate(cursor.getString(cursor.getColumnIndexOrThrow(COL_LOG_DATE)));
                entry.setTimestamp(cursor.getLong(cursor.getColumnIndexOrThrow(COL_LOG_TIMESTAMP)));
                list.add(entry);
            } while (cursor.moveToNext());
            cursor.close();
        }
        return list;
    }

    public DailyNutritionSummary getDailySummary(String date, UserProfile profile) {
        SQLiteDatabase db = this.getReadableDatabase();
        int totalKcal = 0;
        double totalProt = 0.0;
        double totalCarb = 0.0;
        double totalFat = 0.0;

        String sql = "SELECT SUM(" + COL_LOG_CALORIES + "), SUM(" + COL_LOG_PROTEIN + "), SUM(" + COL_LOG_CARBS + "), SUM(" + COL_LOG_FAT + ") "
                + "FROM " + TABLE_MEAL_LOGS + " WHERE " + COL_LOG_DATE + " = ?";
        Cursor cursor = db.rawQuery(sql, new String[]{date});
        if (cursor != null && cursor.moveToFirst()) {
            totalKcal = cursor.getInt(0);
            totalProt = cursor.getDouble(1);
            totalCarb = cursor.getDouble(2);
            totalFat = cursor.getDouble(3);
            cursor.close();
        }

        int waterMl = getWaterForDate(date);

        return new DailyNutritionSummary(
                totalKcal, profile.getTargetCalories(),
                totalProt, profile.getTargetProtein(),
                totalCarb, profile.getTargetCarbs(),
                totalFat, profile.getTargetFat(),
                waterMl, profile.getTargetWaterMl()
        );
    }

    // -------------------------------------------------------------
    // Water Intake
    // -------------------------------------------------------------

    public int getWaterForDate(String date) {
        SQLiteDatabase db = this.getReadableDatabase();
        int water = 0;
        Cursor cursor = db.rawQuery("SELECT " + COL_WATER_ML + " FROM " + TABLE_WATER + " WHERE " + COL_WATER_DATE + " = ?", new String[]{date});
        if (cursor != null && cursor.moveToFirst()) {
            water = cursor.getInt(0);
            cursor.close();
        }
        return water;
    }

    public void addWater(String date, int mlToAdd) {
        SQLiteDatabase db = this.getWritableDatabase();
        int current = getWaterForDate(date);
        int newTotal = current + mlToAdd;

        ContentValues cv = new ContentValues();
        cv.put(COL_WATER_DATE, date);
        cv.put(COL_WATER_ML, newTotal);

        if (current == 0) {
            db.insert(TABLE_WATER, null, cv);
        } else {
            db.update(TABLE_WATER, cv, COL_WATER_DATE + " = ?", new String[]{date});
        }
    }
}
