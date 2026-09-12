# MacroFlow calorie tracker mobile app

A mobile-first progressive web application for tracking daily calorie consumption, macronutrient targets (protein, carbs, fat), and hydration.

## Overview

MacroFlow provides an interactive nutrition dashboard with SVG ring progress meters, meal diary logging, and a built-in database of whole foods.

## Key features

- **Dynamic calorie ring gauge:** Displays calories consumed, daily goal, and remaining budget with animated circular SVG stroke-dashoffset transitions.
- **Macronutrient split dials:** Monitors daily protein, carbohydrate, and dietary fat intake against customizable nutritional targets.
- **Meal timeline logging:** Categorizes meals into Breakfast, Lunch, Dinner, and Snacks with individual meal calorie counters.
- **Verified food database:** Includes standard macro values for staple proteins, whole grains, and healthy fats.
- **Custom food entry:** Log meals by inputting custom calories, protein, carbs, and fat values.
- **Water intake tracker:** One-tap +250ml glass buttons to track daily fluid hydration towards a 2,500 ml target.
- **Dual viewport simulation:** Switch between an iPhone 16 Pro mockup frame and a fullscreen desktop layout.
- **Offline storage:** Keeps meal history and targets saved via localStorage.

## Project structure

```
macro-flow-calorie-tracker/
├── assets/
│   ├── images/
│   │   └── hero.jpg
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
