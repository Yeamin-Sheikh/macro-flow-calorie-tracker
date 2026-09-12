package com.yeaminsheikh.macroflow.models;

import java.io.Serializable;

/**
 * Biometric profile and goal configuration for Mifflin-St Jeor calculations.
 */
public class UserProfile implements Serializable {
    private int age;
    private String gender; // Male or Female
    private double heightCm;
    private double weightKg;
    private String activityLevel; // Sedentary, Light, Moderate, Active, Very Active
    private String goal; // Cut, Maintain, Bulk
    private int targetCalories;
    private int targetProtein;
    private int targetCarbs;
    private int targetFat;
    private int targetWaterMl;

    public UserProfile() {
        this.age = 28;
        this.gender = "Male";
        this.heightCm = 175;
        this.weightKg = 75;
        this.activityLevel = "Moderate";
        this.goal = "Maintain";
        this.targetCalories = 2200;
        this.targetProtein = 160;
        this.targetCarbs = 240;
        this.targetFat = 65;
        this.targetWaterMl = 3000;
    }

    // Getters and Setters
    public int getAge() { return age; }
    public void setAge(int age) { this.age = age; }

    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }

    public double getHeightCm() { return heightCm; }
    public void setHeightCm(double heightCm) { this.heightCm = heightCm; }

    public double getWeightKg() { return weightKg; }
    public void setWeightKg(double weightKg) { this.weightKg = weightKg; }

    public String getActivityLevel() { return activityLevel; }
    public void setActivityLevel(String activityLevel) { this.activityLevel = activityLevel; }

    public String getGoal() { return goal; }
    public void setGoal(String goal) { this.goal = goal; }

    public int getTargetCalories() { return targetCalories; }
    public void setTargetCalories(int targetCalories) { this.targetCalories = targetCalories; }

    public int getTargetProtein() { return targetProtein; }
    public void setTargetProtein(int targetProtein) { this.targetProtein = targetProtein; }

    public int getTargetCarbs() { return targetCarbs; }
    public void setTargetCarbs(int targetCarbs) { this.targetCarbs = targetCarbs; }

    public int getTargetFat() { return targetFat; }
    public void setTargetFat(int targetFat) { this.targetFat = targetFat; }

    public int getTargetWaterMl() { return targetWaterMl; }
    public void setTargetWaterMl(int targetWaterMl) { this.targetWaterMl = targetWaterMl; }
}
