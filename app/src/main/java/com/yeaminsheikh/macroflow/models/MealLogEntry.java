package com.yeaminsheikh.macroflow.models;

import java.io.Serializable;

/**
 * Represents a food consumption log entry tied to a specific meal
 * (Breakfast, Lunch, Dinner, Snack) on a specific calendar date.
 */
public class MealLogEntry implements Serializable {
    private long id;
    private String foodName;
    private String mealType; // Breakfast, Lunch, Dinner, Snack
    private double servings;
    private String servingUnit;
    private int totalCalories;
    private double totalProtein;
    private double totalCarbs;
    private double totalFat;
    private String logDate; // YYYY-MM-DD
    private long timestamp;

    public MealLogEntry() {
        this.timestamp = System.currentTimeMillis();
    }

    public MealLogEntry(long id, String foodName, String mealType, double servings,
                        String servingUnit, int totalCalories, double totalProtein,
                        double totalCarbs, double totalFat, String logDate) {
        this.id = id;
        this.foodName = foodName;
        this.mealType = mealType;
        this.servings = servings;
        this.servingUnit = servingUnit;
        this.totalCalories = totalCalories;
        this.totalProtein = totalProtein;
        this.totalCarbs = totalCarbs;
        this.totalFat = totalFat;
        this.logDate = logDate;
        this.timestamp = System.currentTimeMillis();
    }

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    public String getFoodName() { return foodName; }
    public void setFoodName(String foodName) { this.foodName = foodName; }

    public String getMealType() { return mealType; }
    public void setMealType(String mealType) { this.mealType = mealType; }

    public double getServings() { return servings; }
    public void setServings(double servings) { this.servings = servings; }

    public String getServingUnit() { return servingUnit; }
    public void setServingUnit(String servingUnit) { this.servingUnit = servingUnit; }

    public int getTotalCalories() { return totalCalories; }
    public void setTotalCalories(int totalCalories) { this.totalCalories = totalCalories; }

    public double getTotalProtein() { return totalProtein; }
    public void setTotalProtein(double totalProtein) { this.totalProtein = totalProtein; }

    public double getTotalCarbs() { return totalCarbs; }
    public void setTotalCarbs(double totalCarbs) { this.totalCarbs = totalCarbs; }

    public double getTotalFat() { return totalFat; }
    public void setTotalFat(double totalFat) { this.totalFat = totalFat; }

    public String getLogDate() { return logDate; }
    public void setLogDate(String logDate) { this.logDate = logDate; }

    public long getTimestamp() { return timestamp; }
    public void setTimestamp(long timestamp) { this.timestamp = timestamp; }
}
