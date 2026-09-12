/**
 * Macro & Calorie Science Calculations
 */
export function calculateNutritionSummary(consumed, targets) {
  const caloriesRemaining = Math.max(0, targets.calories - consumed.calories);
  const caloriePercent = Math.min(100, Math.round((consumed.calories / targets.calories) * 100));

  const proteinRemaining = Math.max(0, targets.protein - consumed.protein);
  const proteinPercent = Math.min(100, Math.round((consumed.protein / targets.protein) * 100));

  const carbsRemaining = Math.max(0, targets.carbs - consumed.carbs);
  const carbsPercent = Math.min(100, Math.round((consumed.carbs / targets.carbs) * 100));

  const fatRemaining = Math.max(0, targets.fat - consumed.fat);
  const fatPercent = Math.min(100, Math.round((consumed.fat / targets.fat) * 100));

  // Compute energy distribution from actual macros
  // 1g Protein = 4 kcal, 1g Carbs = 4 kcal, 1g Fat = 9 kcal
  const proteinCals = consumed.protein * 4;
  const carbsCals = consumed.carbs * 4;
  const fatCals = consumed.fat * 9;
  const totalMacroCals = proteinCals + carbsCals + fatCals || 1;

  const macroSplit = {
    proteinRatio: Math.round((proteinCals / totalMacroCals) * 100),
    carbsRatio: Math.round((carbsCals / totalMacroCals) * 100),
    fatRatio: Math.round((fatCals / totalMacroCals) * 100)
  };

  return {
    consumed,
    targets,
    caloriesRemaining,
    caloriePercent,
    proteinRemaining,
    proteinPercent,
    carbsRemaining,
    carbsPercent,
    fatRemaining,
    fatPercent,
    macroSplit
  };
}
