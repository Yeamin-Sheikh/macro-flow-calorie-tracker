package com.yeaminsheikh.macroflow.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.yeaminsheikh.macroflow.models.UserProfile;

/**
 * Manages persistent user preferences, nutritional targets, and goal settings.
 */
public class PreferenceManager {
    private static final String PREF_NAME = "macro_flow_prefs";

    private static final String KEY_TARGET_CALORIES = "target_calories";
    private static final String KEY_TARGET_PROTEIN = "target_protein";
    private static final String KEY_TARGET_CARBS = "target_carbs";
    private static final String KEY_TARGET_FAT = "target_fat";
    private static final String KEY_TARGET_WATER = "target_water";
    private static final String KEY_AGE = "user_age";
    private static final String KEY_GENDER = "user_gender";
    private static final String KEY_HEIGHT = "user_height";
    private static final String KEY_WEIGHT = "user_weight";
    private static final String KEY_ACTIVITY = "user_activity";
    private static final String KEY_GOAL = "user_goal";

    private final SharedPreferences prefs;

    public PreferenceManager(Context context) {
        this.prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public UserProfile getProfile() {
        UserProfile p = new UserProfile();
        p.setTargetCalories(prefs.getInt(KEY_TARGET_CALORIES, 2200));
        p.setTargetProtein(prefs.getInt(KEY_TARGET_PROTEIN, 160));
        p.setTargetCarbs(prefs.getInt(KEY_TARGET_CARBS, 240));
        p.setTargetFat(prefs.getInt(KEY_TARGET_FAT, 65));
        p.setTargetWaterMl(prefs.getInt(KEY_TARGET_WATER, 3000));
        p.setAge(prefs.getInt(KEY_AGE, 28));
        p.setGender(prefs.getString(KEY_GENDER, "Male"));
        p.setHeightCm(prefs.getFloat(KEY_HEIGHT, 175.0f));
        p.setWeightKg(prefs.getFloat(KEY_WEIGHT, 75.0f));
        p.setActivityLevel(prefs.getString(KEY_ACTIVITY, "Moderate"));
        p.setGoal(prefs.getString(KEY_GOAL, "Maintain"));
        return p;
    }

    public void saveProfile(UserProfile p) {
        prefs.edit()
                .putInt(KEY_TARGET_CALORIES, p.getTargetCalories())
                .putInt(KEY_TARGET_PROTEIN, p.getTargetProtein())
                .putInt(KEY_TARGET_CARBS, p.getTargetCarbs())
                .putInt(KEY_TARGET_FAT, p.getTargetFat())
                .putInt(KEY_TARGET_WATER, p.getTargetWaterMl())
                .putInt(KEY_AGE, p.getAge())
                .putString(KEY_GENDER, p.getGender())
                .putFloat(KEY_HEIGHT, (float) p.getHeightCm())
                .putFloat(KEY_WEIGHT, (float) p.getWeightKg())
                .putString(KEY_ACTIVITY, p.getActivityLevel())
                .putString(KEY_GOAL, p.getGoal())
                .apply();
    }
}
