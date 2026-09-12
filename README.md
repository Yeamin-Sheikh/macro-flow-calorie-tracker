# MacroFlow Calorie Tracker (Native Android App)

Native Android application for tracking daily calories, macronutrient distributions (Protein, Carbohydrates, Fats), and water hydration targets.

Built with Java, AndroidX, Material Design 3, and a local SQLite nutrition database.

## Features

- **Daily Energy Dashboard:** Visual progress tracking for daily target calories with real-time remaining calorie computation.
- **Macronutrient Tri-Split Bars:** Independent progress indicators for daily Protein, Carbohydrate, and Fat targets in grams and percentages.
- **Hydration Water Counter:** Single-tap logging (+250 ml increments) with daily target water progress.
- **Verified Preloaded Nutrition Database:** Instant search across staples (chicken breast, oats, eggs, avocado, salmon, sweet potato, whey protein, etc.) with pre-calculated macros.
- **Custom Food and Meal Builder:** Log custom home-cooked meals by specifying serving size, calories, protein, carbs, and fat.
- **Mifflin-St Jeor TDEE & Macro Calculator:** Built-in metabolic calculator based on user age, gender, height, weight, activity level, and fitness goal (Cut, Maintain, Bulk). Automatically sets optimal calorie targets and protein allocations.
- **Historical Daily Logs:** Browse meal history and macro totals by date.
- **Local SQLite Engine:** Zero external login or network dependency. All records persist locally on-device.

## Project Structure

```
macro-flow-calorie-tracker/
├── app/
│   ├── build.gradle                       # Module build settings and dependencies
│   ├── proguard-rules.pro                 # Proguard optimization rules
│   └── src/main/
│       ├── AndroidManifest.xml            # Application manifest and activity declarations
│       ├── java/com/yeaminsheikh/macroflow/
│       │   ├── MainActivity.java          # Dashboard, macro progress bars, water counter, meal list
│       │   ├── AddFoodActivity.java       # Food search library & custom recipe logger
│       │   ├── DailyLogActivity.java      # Historical daily nutrition logs
│       │   ├── ProfileGoalsActivity.java  # TDEE & macro goals calculator
│       │   ├── adapters/                  # RecyclerView adapters for meals and food search
│       │   ├── database/MacroDatabaseHelper.java # Local SQLite schema, seeds, queries
│       │   ├── models/                    # Data models (FoodItem, MealLogEntry, UserProfile)
│       │   └── utils/                     # CalorieCalculator & PreferenceManager
│       └── res/
│           ├── drawable/                  # Vector icons, flame, water drop, and macro pills
│           ├── layout/                    # Activity and list item XML layouts
│           ├── values/                    # Colors, strings, dimens, and Material themes
│           └── mipmap-anydpi-v26/         # Adaptive launcher icons
├── gradle/wrapper/                        # Gradle 8.5 wrapper distribution
├── build.gradle                           # Top-level Gradle script
├── settings.gradle                        # Project settings
├── config.json                            # Local project configuration
└── tests/verify_android_app.py            # Automated structure and logic verification suite
```

## Requirements

- Android Studio Hedgehog (2023.1.1) or newer
- JDK 17
- Android SDK 34 (compileSdk 34, minSdk 24, targetSdk 34)

## How to Build and Run

### Option 1: Android Studio (Recommended)
1. Open Android Studio.
2. Select **File > Open** and choose the `macro-flow-calorie-tracker` directory.
3. Allow Gradle to sync dependencies.
4. Select a connected device or Android Virtual Device (API 24+).
5. Click **Run** (or press Shift + F10).

### Option 2: Command Line (Gradle Wrapper)
```bash
# Debug APK compilation
./gradlew assembleDebug

# Run unit tests
./gradlew test
```
The compiled APK will be located at `app/build/outputs/apk/debug/app-debug.apk`.

## Running Verification Tests

Run the built-in test suite to verify XML layouts, manifest bindings, Mifflin-St Jeor algorithms, and Gradle build settings:

```bash
python tests/verify_android_app.py
```
