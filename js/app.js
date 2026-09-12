import { TrackerStore } from './tracker-store.js';
import { foodDatabase, featuredMeals } from './database.js';

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

  const featuredModal = document.getElementById('featured-modal');
  const openFeaturedModalBtn = document.getElementById('open-featured-modal-btn');
  const featuredMealsContainer = document.getElementById('featured-meals-container');

  // Populate Food Options in Custom Log Modal
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

  // Populate Curated Featured Meals with High-Resolution Photography
  if (featuredMealsContainer) {
    featuredMealsContainer.innerHTML = featuredMeals.map(meal => `
      <div class="featured-meal-card" data-id="${meal.id}">
        <img src="assets/images/${meal.image}" alt="${meal.name}" class="featured-meal-img" loading="lazy">
        <div class="featured-meal-info">
          <div>
            <h4 class="featured-meal-title">${meal.name}</h4>
            <p class="featured-meal-desc">${meal.description}</p>
          </div>
          <div class="featured-meal-stats">
            <div class="featured-macro-pills">
              <span class="macro-p-pill">${meal.protein}g P</span>
              <span>&bull;</span>
              <span class="macro-c-pill">${meal.carbs}g C</span>
              <span>&bull;</span>
              <span class="macro-f-pill">${meal.fat}g F</span>
            </div>
            <button class="featured-add-btn" data-meal-id="${meal.id}">+ Log (${meal.calories}k)</button>
          </div>
        </div>
      </div>
    `).join('');

    // Handle 1-Click Logging from Featured Meals
    featuredMealsContainer.addEventListener('click', (e) => {
      const btn = e.target.closest('.featured-add-btn');
      if (!btn) return;
      const mealId = btn.getAttribute('data-meal-id');
      const meal = featuredMeals.find(m => m.id === mealId);
      if (meal) {
        store.addFood(meal.defaultMeal, {
          name: meal.name,
          calories: meal.calories,
          protein: meal.protein,
          carbs: meal.carbs,
          fat: meal.fat
        });
        closeModal();
        updateDashboard();
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

  // Render Meals Timeline
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
                <button class="delete-food-btn" data-meal="${mealType}" data-id="${item.id}" title="Delete entry" style="border:none;background:none;color:#94A3B8;cursor:pointer;font-size:1rem;">&times;</button>
              </div>
            </div>
          `).join('');
        }
      }
    });
  }

  updateDashboard();

  // Water Quick Add (+250 ml glass)
  addWaterBtn?.addEventListener('click', () => {
    store.addWater(250);
    updateDashboard();
  });

  // Food Deletion & Food Modal Trigger
  document.addEventListener('click', (e) => {
    const delBtn = e.target.closest('.delete-food-btn');
    if (delBtn) {
      const meal = delBtn.getAttribute('data-meal');
      const id = delBtn.getAttribute('data-id');
      store.removeFood(meal, id);
      updateDashboard();
      return;
    }

    const addBtn = e.target.closest('.add-food-btn');
    if (addBtn) {
      activeTargetMeal = addBtn.getAttribute('data-meal');
      const titleEl = document.getElementById('modal-meal-title');
      if (titleEl) titleEl.textContent = activeTargetMeal.toUpperCase();
      modalOverlay?.classList.add('open');
      foodModal?.classList.add('open');
    }
  });

  // Featured Modal Open Trigger
  openFeaturedModalBtn?.addEventListener('click', () => {
    modalOverlay?.classList.add('open');
    featuredModal?.classList.add('open');
  });

  // Modal Closers
  function closeModal() {
    modalOverlay?.classList.remove('open');
    foodModal?.classList.remove('open');
    featuredModal?.classList.remove('open');
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

  // Toggle Mobile Frame vs Desktop Fullscreen Mode
  frameToggleBtn?.addEventListener('click', () => {
    appStage?.classList.toggle('fullscreen-mode');
    const isFull = appStage?.classList.contains('fullscreen-mode');
    frameToggleBtn.textContent = isFull ? '📱 Switch to Phone Mockup' : '🖥️ Switch to Fullscreen';
  });

  // Right-Click Context Menu Implementation (User Rule Compliance)
  const contextMenu = document.getElementById('custom-context-menu');
  window.addEventListener('contextmenu', (e) => {
    e.preventDefault();
    if (!contextMenu) return;
    contextMenu.style.left = `${Math.min(e.clientX, window.innerWidth - 180)}px`;
    contextMenu.style.top = `${Math.min(e.clientY, window.innerHeight - 180)}px`;
    contextMenu.classList.add('open');
  });

  window.addEventListener('click', () => {
    contextMenu?.classList.remove('open');
  });

  contextMenu?.addEventListener('click', async (e) => {
    const item = e.target.closest('.context-menu-item');
    if (!item) return;
    const action = item.getAttribute('data-action');
    try {
      if (action === 'copy') {
        const sel = window.getSelection()?.toString();
        if (sel) await navigator.clipboard.writeText(sel);
      } else if (action === 'paste') {
        const text = await navigator.clipboard.readText();
        const active = document.activeElement;
        if (active && (active.tagName === 'INPUT' || active.tagName === 'TEXTAREA')) {
          active.value += text;
        }
      } else if (action === 'cut') {
        const active = document.activeElement;
        if (active && (active.tagName === 'INPUT' || active.tagName === 'TEXTAREA')) {
          await navigator.clipboard.writeText(active.value);
          active.value = '';
        }
      } else if (action === 'selectall') {
        const active = document.activeElement;
        if (active && (active.tagName === 'INPUT' || active.tagName === 'TEXTAREA')) {
          active.select();
        } else {
          document.execCommand('selectAll');
        }
      }
    } catch {
      // Clipboard permissions fallback
    }
    contextMenu.classList.remove('open');
  });
});
