package com.yeaminsheikh.macroflow.models;

/**
 * Aggregates daily nutritional intake against user targets:
 * Consumed vs Target Calories, Protein, Carbs, Fats, and Hydration.
 */
public class DailyNutritionSummary {
    private int consumedCalories;
    private int targetCalories;
    private double consumedProtein;
    private double targetProtein;
    private double consumedCarbs;
    private double targetCarbs;
    private double consumedFat;
    private double targetFat;
    private int consumedWaterMl;
    private int targetWaterMl;

    public DailyNutritionSummary(int consumedCalories, int targetCalories,
                                 double consumedProtein, double targetProtein,
                                 double consumedCarbs, double targetCarbs,
                                 double consumedFat, double targetFat,
                                 int consumedWaterMl, int targetWaterMl) {
        this.consumedCalories = consumedCalories;
        this.targetCalories = targetCalories;
        this.consumedProtein = consumedProtein;
        this.targetProtein = targetProtein;
        this.consumedCarbs = consumedCarbs;
        this.targetCarbs = targetCarbs;
        this.consumedFat = consumedFat;
        this.targetFat = targetFat;
        this.consumedWaterMl = consumedWaterMl;
        this.targetWaterMl = targetWaterMl;
    }

    public int getConsumedCalories() { return consumedCalories; }
    public int getTargetCalories() { return targetCalories; }
    public int getRemainingCalories() { return Math.max(0, targetCalories - consumedCalories); }
    public int getCaloriePercentage() {
        if (targetCalories <= 0) return 0;
        return Math.min(100, (int) Math.round(((double) consumedCalories / targetCalories) * 100));
    }

    public double getConsumedProtein() { return consumedProtein; }
    public double getTargetProtein() { return targetProtein; }
    public int getProteinPercentage() {
        if (targetProtein <= 0) return 0;
        return Math.min(100, (int) Math.round((consumedProtein / targetProtein) * 100));
    }

    public double getConsumedCarbs() { return consumedCarbs; }
    public double getTargetCarbs() { return targetCarbs; }
    public int getCarbsPercentage() {
        if (targetCarbs <= 0) return 0;
        return Math.min(100, (int) Math.round((consumedCarbs / targetCarbs) * 100));
    }

    public double getConsumedFat() { return consumedFat; }
    public double getTargetFat() { return targetFat; }
    public int getFatPercentage() {
        if (targetFat <= 0) return 0;
        return Math.min(100, (int) Math.round((consumedFat / targetFat) * 100));
    }

    public int getConsumedWaterMl() { return consumedWaterMl; }
    public int getTargetWaterMl() { return targetWaterMl; }
    public int getWaterPercentage() {
        if (targetWaterMl <= 0) return 0;
        return Math.min(100, (int) Math.round(((double) consumedWaterMl / targetWaterMl) * 100));
    }
}
