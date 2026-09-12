import { calculateNutritionSummary } from './calculator.js';

/**
 * Calorie & Macro State Store
 */
export class TrackerStore {
  constructor(storageKey = 'macro_flow_state') {
    this.storageKey = storageKey;
    this.data = this.loadData();
  }

  loadData() {
    try {
      if (typeof localStorage !== 'undefined') {
        const saved = localStorage.getItem(this.storageKey);
        if (saved) return JSON.parse(saved);
      }
    } catch {
      // Fallback
    }
    return this.getDefaultData();
  }

  saveData() {
    try {
      if (typeof localStorage !== 'undefined') {
        localStorage.setItem(this.storageKey, JSON.stringify(this.data));
      }
    } catch (e) {
      console.warn('Unable to persist tracker data', e);
    }
  }

  getDefaultData() {
    return {
      targets: {
        calories: 2200,
        protein: 160,
        carbs: 220,
        fat: 65,
        waterMl: 2500
      },
      waterLoggedMl: 1500,
      meals: {
        breakfast: [
          { id: 'b1', name: 'Rolled Oats with Greek Yogurt & Honey', calories: 340, protein: 26, carbs: 48, fat: 4 }
        ],
        lunch: [
          { id: 'l1', name: 'Grilled Atlantic Salmon & Quinoa Bowl', calories: 638, protein: 53, carbs: 39, fat: 30 }
        ],
        dinner: [
          { id: 'd1', name: 'Chicken Breast with Baked Sweet Potato', calories: 350, protein: 48, carbs: 24, fat: 5 }
        ],
        snacks: [
          { id: 's1', name: 'Whey Protein Shake with Raw Almonds', calories: 284, protein: 31, carbs: 8, fat: 15 }
        ]
      }
    };
  }

  addFood(mealType, food) {
    if (!this.data.meals[mealType]) return;
    const item = {
      id: 'food-' + Date.now().toString(36),
      name: food.name,
      calories: Number(food.calories) || 0,
      protein: Number(food.protein) || 0,
      carbs: Number(food.carbs) || 0,
      fat: Number(food.fat) || 0
    };

    this.data.meals[mealType].push(item);
    this.saveData();
    return item;
  }

  removeFood(mealType, foodId) {
    if (!this.data.meals[mealType]) return;
    this.data.meals[mealType] = this.data.meals[mealType].filter(f => f.id !== foodId);
    this.saveData();
  }

  addWater(deltaMl = 250) {
    this.data.waterLoggedMl = Math.max(0, this.data.waterLoggedMl + deltaMl);
    this.saveData();
    return this.data.waterLoggedMl;
  }

  getTotals() {
    let calories = 0;
    let protein = 0;
    let carbs = 0;
    let fat = 0;

    Object.values(this.data.meals).forEach(mealList => {
      mealList.forEach(item => {
        calories += item.calories;
        protein += item.protein;
        carbs += item.carbs;
        fat += item.fat;
      });
    });

    const consumed = { calories, protein, carbs, fat };
    return calculateNutritionSummary(consumed, this.data.targets);
  }
}
