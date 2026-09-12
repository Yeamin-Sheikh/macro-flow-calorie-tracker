# MacroFlow calorie tracker mobile app

A mobile-first progressive web application for tracking daily calorie consumption, macronutrient targets (protein, carbs, fat), and hydration.

## Overview

MacroFlow provides an interactive nutrition dashboard with SVG ring progress meters, meal diary logging, and a built-in database of whole foods.

## Key features

- **Dynamic calorie ring gauge:** Displays calories consumed, daily goal, and remaining budget with animated circular SVG stroke-dashoffset transitions.
- **Macronutrient split dials:** Monitors daily protein, carbohydrate, and dietary fat intake against customizable nutritional targets.
- **Curated chef meals showcase:** High-resolution meal cards with 1-click batch logging (Macro Fuel Power Bowl, Berry Protein Super Oats).
- **Meal timeline logging:** Categorizes meals into Breakfast, Lunch, Dinner, and Snacks with individual meal calorie counters.
- **Verified food database:** Includes standard macro values for staple proteins, whole grains, and healthy fats.
- **Custom food entry:** Log meals by inputting custom calories, protein, carbs, and fat values.
- **Water intake tracker:** One-tap +250ml glass buttons to track daily fluid hydration towards a 2,500 ml target.
- **Dual viewport simulation:** Switch between an interactive mobile mockup frame with Dynamic Island and a fullscreen desktop layout.
- **Context menu support:** Custom right-click menu with Cut, Copy, Paste, and Select All.
- **Configuration persistence:** `config.json` stores daily calorie goals, macro splits, and user preferences.

## Project structure

```
macro-flow-calorie-tracker/
├── assets/
│   ├── images/
│   │   ├── hero.jpg
│   │   ├── meal-healthy-bowl.jpg
│   │   └── meal-berry-oatmeal.jpg
│   └── svgs/
│       ├── logo.svg
│       └── icons.svg
├── css/
│   ├── main.css
│   └── components.css
├── js/
│   ├── app.js
│   ├── calculator.js
│   ├── database.js
│   └── tracker-store.js
├── tests/
│   └── runner.js
├── config.json
├── index.html
├── package.json
└── README.md
```

## Running locally

Serve with Python or Node:

```powershell
# Using Python
python -m http.server 8000

# Or using Node
npm start
```

Visit `http://localhost:8000` in your web browser.

## Running tests

Execute the verification tests with Node:

```powershell
npm test
# or
node tests/runner.js
```

## License

MIT License.
