package com.yeaminsheikh.macroflow.models;

import java.io.Serializable;

/**
 * Nutritional database item representing a standardized food item
 * with serving size, calories, and macronutrient profile.
 */
public class FoodItem implements Serializable {
    private long id;
    private String name;
    private String category;
    private String servingUnit;
    private double defaultServingAmount;
    private int caloriesPerServing;
    private double proteinGrams;
    private double carbsGrams;
    private double fatGrams;

    public FoodItem() {}

    public FoodItem(long id, String name, String category, String servingUnit,
                    double defaultServingAmount, int caloriesPerServing,
                    double proteinGrams, double carbsGrams, double fatGrams) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.servingUnit = servingUnit;
        this.defaultServingAmount = defaultServingAmount;
        this.caloriesPerServing = caloriesPerServing;
        this.proteinGrams = proteinGrams;
        this.carbsGrams = carbsGrams;
        this.fatGrams = fatGrams;
    }

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getServingUnit() { return servingUnit; }
    public void setServingUnit(String servingUnit) { this.servingUnit = servingUnit; }

    public double getDefaultServingAmount() { return defaultServingAmount; }
    public void setDefaultServingAmount(double amount) { this.defaultServingAmount = amount; }

    public int getCaloriesPerServing() { return caloriesPerServing; }
    public void setCaloriesPerServing(int calories) { this.caloriesPerServing = calories; }

    public double getProteinGrams() { return proteinGrams; }
    public void setProteinGrams(double protein) { this.proteinGrams = protein; }

    public double getCarbsGrams() { return carbsGrams; }
    public void setCarbsGrams(double carbs) { this.carbsGrams = carbs; }

    public double getFatGrams() { return fatGrams; }
    public void setFatGrams(double fat) { this.fatGrams = fat; }
}
