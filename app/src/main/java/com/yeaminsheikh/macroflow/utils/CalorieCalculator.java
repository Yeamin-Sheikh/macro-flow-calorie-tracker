package com.yeaminsheikh.macroflow.utils;

import com.yeaminsheikh.macroflow.models.UserProfile;

/**
 * Standard fitness science nutrition calculator:
 * Uses the Mifflin-St Jeor equation to calculate Basal Metabolic Rate (BMR)
 * and Total Daily Energy Expenditure (TDEE), followed by goal adjustments
 * and optimal macronutrient gram partitioning.
 */
public class CalorieCalculator {

    public static UserProfile calculateTargets(int age, String gender, double heightCm,
                                              double weightKg, String activityLevel, String goal) {
        UserProfile profile = new UserProfile();
        profile.setAge(age);
        profile.setGender(gender);
        profile.setHeightCm(heightCm);
        profile.setWeightKg(weightKg);
        profile.setActivityLevel(activityLevel);
        profile.setGoal(goal);

        // 1. Calculate Basal Metabolic Rate (BMR) using Mifflin-St Jeor
        // Men: 10 * weight(kg) + 6.25 * height(cm) - 5 * age(y) + 5
        // Women: 10 * weight(kg) + 6.25 * height(cm) - 5 * age(y) - 161
        double bmr = (10 * weightKg) + (6.25 * heightCm) - (5 * age);
        if ("Female".equalsIgnoreCase(gender)) {
            bmr -= 161;
        } else {
            bmr += 5;
        }

        // 2. Activity Multiplier for TDEE
        double multiplier = 1.2; // Sedentary
        if ("Light".equalsIgnoreCase(activityLevel)) multiplier = 1.375;
        else if ("Moderate".equalsIgnoreCase(activityLevel)) multiplier = 1.55;
        else if ("Active".equalsIgnoreCase(activityLevel)) multiplier = 1.725;
        else if ("Very Active".equalsIgnoreCase(activityLevel)) multiplier = 1.9;

        double tdee = bmr * multiplier;

        // 3. Goal Adjustment
        double targetCalories = tdee;
        if ("Cut".equalsIgnoreCase(goal)) {
            targetCalories -= 500; // standard 500 kcal deficit
        } else if ("Bulk".equalsIgnoreCase(goal)) {
            targetCalories += 300; // clean lean bulk surplus
        }
        int finalCalories = (int) Math.round(targetCalories);

        // 4. Macro Gram Partitioning:
        // Protein: 2.0g per kg body weight (4 kcal / g)
        double proteinGrams = weightKg * 2.0;
        double proteinCalories = proteinGrams * 4.0;

        // Fat: 25% of total daily energy (9 kcal / g)
        double fatCalories = targetCalories * 0.25;
        double fatGrams = fatCalories / 9.0;

        // Carbohydrates: Remaining energy budget (4 kcal / g)
        double remainingCalories = targetCalories - proteinCalories - fatCalories;
        double carbGrams = Math.max(50.0, remainingCalories / 4.0);

        profile.setTargetCalories(finalCalories);
        profile.setTargetProtein((int) Math.round(proteinGrams));
        profile.setTargetCarbs((int) Math.round(carbGrams));
        profile.setTargetFat((int) Math.round(fatGrams));

        // Hydration recommendation: ~35-40 ml per kg of body weight
        int waterMl = (int) Math.round(weightKg * 38);
        profile.setTargetWaterMl(Math.max(2000, waterMl));

        return profile;
    }
}
