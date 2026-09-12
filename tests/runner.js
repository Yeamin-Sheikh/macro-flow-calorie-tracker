import assert from 'node:assert';
import { foodDatabase } from '../js/database.js';
import { calculateNutritionSummary } from '../js/calculator.js';
import { TrackerStore } from '../js/tracker-store.js';

console.log('--- Running MacroFlow Calorie Tracker Tests ---');

// Mock localStorage
global.localStorage = (() => {
  let store = {};
  return {
    getItem: (key) => store[key] || null,
    setItem: (key, value) => { store[key] = value.toString(); },
    removeItem: (key) => { delete store[key]; },
    clear: () => { store = {}; }
  };
})();

// Test 1: Database verification
assert.strictEqual(foodDatabase.length, 10, 'Should have 10 default verified foods');
const salmon = foodDatabase.find(f => f.id === 'f-salmon');
assert.ok(salmon);
assert.strictEqual(salmon.protein, 45);
console.log('✓ Food database loaded and verified');

// Test 2: Nutrition Calculation
const consumed = { calories: 1500, protein: 120, carbs: 150, fat: 45 };
const targets = { calories: 2000, protein: 150, carbs: 200, fat: 60 };
const summary = calculateNutritionSummary(consumed, targets);

assert.strictEqual(summary.caloriesRemaining, 500);
assert.strictEqual(summary.caloriePercent, 75);
assert.strictEqual(summary.proteinRemaining, 30);
assert.strictEqual(summary.proteinPercent, 80);
assert.strictEqual(summary.carbsRemaining, 50);
assert.strictEqual(summary.fatRemaining, 15);
assert.ok(summary.macroSplit.proteinRatio > 0);
console.log('✓ Nutrition math and macro splits verified');

// Test 3: Tracker Store & Meal Logging
const store = new TrackerStore('test_macro_store');
const totalsBefore = store.getTotals();

// Add food to breakfast
const added = store.addFood('breakfast', {
  name: 'Pasture Eggs & Toast',
  calories: 300,
  protein: 18,
  carbs: 25,
  fat: 12
});

const totalsAfter = store.getTotals();
assert.strictEqual(totalsAfter.consumed.calories, totalsBefore.consumed.calories + 300);
assert.strictEqual(totalsAfter.consumed.protein, totalsBefore.consumed.protein + 18);
console.log('✓ Meal food addition and aggregate totals verified');

// Test 4: Water Hydration Tracking
const initialWater = store.data.waterLoggedMl;
store.addWater(250);
assert.strictEqual(store.data.waterLoggedMl, initialWater + 250);
console.log('✓ Water logging verified');

// Test 5: Remove Food Item
store.removeFood('breakfast', added.id);
const totalsFinal = store.getTotals();
assert.strictEqual(totalsFinal.consumed.calories, totalsBefore.consumed.calories);
console.log('✓ Food item removal verified');

console.log('\nAll MacroFlow Calorie Tracker tests passed successfully! (5/5)');
