import { TrackerStore } from './tracker-store.js';
import { foodDatabase } from './database.js';

document.addEventListener('DOMContentLoaded', () => {
  const store = new TrackerStore();
  let activeTargetMeal = 'breakfast';

  // Elements
  const caloriesRemainingEl = document.getElementById('calories-remaining');
  const caloriesConsumedEl = document.getElementById('calories-consumed');
  const caloriesTargetEl = document.getElementById('calories-target');
  const ringBarEl = document.getElementById('ring-bar');
  
  const proteinValEl = document.getElementById('protein-val');
  const proteinBarEl = document.getElementById('protein-bar');
  const carbsValEl = document.getElementById('carbs-val');
  const carbsBarEl = document.getElementById('carbs-bar');
  const fatValEl = document.getElementById('fat-val');
  const fatBarEl = document.getElementById('fat-bar');

  const waterAmountEl = document.getElementById('water-amount');
  const addWaterBtn = document.getElementById('add-water-btn');
  const frameToggleBtn = document.getElementById('frame-toggle-btn');
  const appStage = document.getElementById('app-stage');
  
  const modalOverlay = document.getElementById('modal-overlay');
  const foodModal = document.getElementById('food-modal');
  const foodSelectEl = document.getElementById('food-select');
  const foodForm = document.getElementById('food-form');

  // Populate Food Options
  if (foodSelectEl) {
    foodSelectEl.innerHTML = `
      <option value="">-- Or Choose Verified Food --</option>
      ${foodDatabase.map(f => `
        <option value="${f.id}">${f.name} (${f.calories} kcal &bull; P:${f.protein}g C:${f.carbs}g F:${f.fat}g)</option>
      `).join('')}
    `;

    foodSelectEl.addEventListener('change', () => {
      const selected = foodDatabase.find(f => f.id === foodSelectEl.value);
      if (selected) {
        document.getElementById('input-food-name').value = selected.name;
        document.getElementById('input-food-calories').value = selected.calories;
        document.getElementById('input-food-protein').value = selected.protein;
        document.getElementById('input-food-carbs').value = selected.carbs;
        document.getElementById('input-food-fat').value = selected.fat;
      }
    });
  }

  // Render Dashboard Dials & Numbers
  function updateDashboard() {
    const summary = store.getTotals();

    // Calorie Ring (circumference = 2 * PI * 45 = 282.74)
    const circumference = 282.74;
    const offset = circumference - (circumference * (summary.caloriePercent / 100));
    if (ringBarEl) ringBarEl.style.strokeDashoffset = offset;

    if (caloriesRemainingEl) caloriesRemainingEl.textContent = summary.caloriesRemaining;
    if (caloriesConsumedEl) caloriesConsumedEl.textContent = summary.consumed.calories;
    if (caloriesTargetEl) caloriesTargetEl.textContent = summary.targets.calories;

    // Macro Dials
    if (proteinValEl) proteinValEl.textContent = `${summary.consumed.protein}g / ${summary.targets.protein}g`;
    if (proteinBarEl) proteinBarEl.style.width = `${summary.proteinPercent}%`;

    if (carbsValEl) carbsValEl.textContent = `${summary.consumed.carbs}g / ${summary.targets.carbs}g`;
    if (carbsBarEl) carbsBarEl.style.width = `${summary.carbsPercent}%`;

    if (fatValEl) fatValEl.textContent = `${summary.consumed.fat}g / ${summary.targets.fat}g`;
    if (fatBarEl) fatBarEl.style.width = `${summary.fatPercent}%`;

    // Water
    if (waterAmountEl) waterAmountEl.textContent = `${store.data.waterLoggedMl} ml`;

    // Render Meals
    renderMeals();
  }

  // Render Meals
  function renderMeals() {
    ['breakfast', 'lunch', 'dinner', 'snacks'].forEach(mealType => {
      const listEl = document.getElementById(`${mealType}-list`);
      const calBadgeEl = document.getElementById(`${mealType}-cal-badge`);
      const items = store.data.meals[mealType] || [];

      const mealCals = items.reduce((sum, item) => sum + item.calories, 0);
      if (calBadgeEl) calBadgeEl.textContent = `${mealCals} kcal`;

      if (listEl) {
        if (items.length === 0) {
          listEl.innerHTML = `<div style="font-size:0.8rem; color:var(--text-muted); font-style:italic;">No foods logged yet.</div>`;
        } else {
          listEl.innerHTML = items.map(item => `
            <div class="food-row">
              <div>
                <div class="food-name">${item.name}</div>
                <div class="food-macros">P: ${item.protein}g &bull; C: ${item.carbs}g &bull; F: ${item.fat}g</div>
              </div>
              <div style="display:flex; align-items:center; gap:0.6rem;">
                <span class="food-calories">${item.calories} kcal</span>
                <button class="delete-food-btn" data-meal="${mealType}" data-id="${item.id}" style="border:none;background:none;color:#94A3B8;cursor:pointer;font-size:1rem;">&times;</button>
              </div>
            </div>
          `).join('');
        }
      }
    });
  }

  updateDashboard();

  // Water Quick Add
  addWaterBtn?.addEventListener('click', () => {
    store.addWater(250);
    updateDashboard();
  });

  // Food Deletion
  document.addEventListener('click', (e) => {
    const delBtn = e.target.closest('.delete-food-btn');
    if (delBtn) {
      const meal = delBtn.getAttribute('data-meal');
      const id = delBtn.getAttribute('data-id');
      store.removeFood(meal, id);
      updateDashboard();
    }

    const addBtn = e.target.closest('.add-food-btn');
    if (addBtn) {
      activeTargetMeal = addBtn.getAttribute('data-meal');
      document.getElementById('modal-meal-title').textContent = activeTargetMeal.toUpperCase();
      modalOverlay?.classList.add('open');
      foodModal?.classList.add('open');
    }
  });

  // Modal Closers
  function closeModal() {
    modalOverlay?.classList.remove('open');
    foodModal?.classList.remove('open');
  }

  document.querySelectorAll('.modal-close, #modal-overlay').forEach(el => {
    el.addEventListener('click', closeModal);
  });

  // Food Form Submit
  foodForm?.addEventListener('submit', (e) => {
    e.preventDefault();
    store.addFood(activeTargetMeal, {
      name: document.getElementById('input-food-name').value,
      calories: document.getElementById('input-food-calories').value,
      protein: document.getElementById('input-food-protein').value,
      carbs: document.getElementById('input-food-carbs').value,
      fat: document.getElementById('input-food-fat').value
    });

    closeModal();
    foodForm.reset();
    updateDashboard();
  });

  // Frame toggle
  frameToggleBtn?.addEventListener('click', () => {
    appStage?.classList.toggle('fullscreen-mode');
    const isFull = appStage?.classList.contains('fullscreen-mode');
    frameToggleBtn.textContent = isFull ? '📱 Switch to Phone Mockup' : '🖥️ Switch to Fullscreen';
  });
});
