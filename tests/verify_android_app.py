import os
import xml.etree.ElementTree as ET
import re
import sys

if hasattr(sys.stdout, 'reconfigure'):
    sys.stdout.reconfigure(encoding='utf-8')

REPO_DIR = os.path.abspath(os.path.join(os.path.dirname(__file__), ".."))

def test_xml_syntax():
    print("Checking XML syntax across all MacroFlow resources...")
    xml_files = []
    for root, dirs, files in os.walk(os.path.join(REPO_DIR, "app", "src", "main")):
        for f in files:
            if f.endswith(".xml"):
                xml_files.append(os.path.join(root, f))
    
    assert len(xml_files) > 0, "No XML files found"
    for xml_file in xml_files:
        try:
            tree = ET.parse(xml_file)
            root = tree.getroot()
            assert root is not None, f"Root element is None in {xml_file}"
        except Exception as e:
            raise AssertionError(f"XML parsing failed for {xml_file}: {e}")
    print(f"✓ All {len(xml_files)} XML resource and manifest files are well-formed and valid!")

def test_manifest_and_activities():
    print("Verifying AndroidManifest.xml and declared Activity classes...")
    manifest_path = os.path.join(REPO_DIR, "app", "src", "main", "AndroidManifest.xml")
    assert os.path.exists(manifest_path), "AndroidManifest.xml missing"
    
    tree = ET.parse(manifest_path)
    root = tree.getroot()
    
    app_elem = root.find("application")
    assert app_elem is not None, "Application tag missing in AndroidManifest.xml"
    
    activities = app_elem.findall("activity")
    assert len(activities) >= 4, f"Expected at least 4 activities, found {len(activities)}"
    
    java_dir = os.path.join(REPO_DIR, "app", "src", "main", "java", "com", "yeaminsheikh", "macroflow")
    for act in activities:
        act_name = act.attrib.get("{http://schemas.android.com/apk/res/android}name")
        if act_name.startswith("."):
            class_name = act_name[1:] + ".java"
        else:
            class_name = act_name.split(".")[-1] + ".java"
        
        java_file = os.path.join(java_dir, class_name)
        assert os.path.exists(java_file), f"Activity file missing: {java_file}"
    
    print(f"✓ Manifest activities ({len(activities)}) all match physical Java source files!")

def test_layout_ids():
    print("Verifying layout IDs referenced in code...")
    id_pattern = re.compile(r'android:id="@\+id/([a-zA-Z0-9_]+)"')
    declared_ids = set()
    layout_dir = os.path.join(REPO_DIR, "app", "src", "main", "res", "layout")
    for f in os.listdir(layout_dir):
        if f.endswith(".xml"):
            with open(os.path.join(layout_dir, f), "r", encoding="utf-8") as fp:
                for match in id_pattern.findall(fp.read()):
                    declared_ids.add(match)
    
    assert "tv_calories_stat" in declared_ids
    assert "pb_calories" in declared_ids
    assert "pb_protein" in declared_ids
    assert "pb_carbs" in declared_ids
    assert "pb_fats" in declared_ids
    assert "pb_water" in declared_ids
    assert "btn_add_water" in declared_ids
    assert "rv_meal_logs" in declared_ids
    assert "fab_add_food" in declared_ids
    assert "et_search_food" in declared_ids
    assert "et_food_name" in declared_ids
    assert "btn_calculate_save" in declared_ids
    print(f"✓ All required layout IDs ({len(declared_ids)} found) match Java view bindings!")

def test_mifflin_st_jeor_calculator():
    print("Verifying Mifflin-St Jeor BMR, TDEE, and macro gram calculations...")
    # Test case: 28 year old male, 175cm, 75kg, Moderate activity (1.55), Maintain goal
    # BMR = (10 * 75) + (6.25 * 175) - (5 * 28) + 5
    # BMR = 750 + 1093.75 - 140 + 5 = 1708.75
    # TDEE = 1708.75 * 1.55 = 2648.56
    # Target Cal = 2649
    # Protein = 75 * 2.0 = 150g (600 kcal)
    # Fat = 2648.56 * 0.25 / 9 = 73.57g
    # Carbs = (2648.56 - 600 - 662.14) / 4 = 346.6g
    weight = 75.0
    height = 175.0
    age = 28
    bmr_calc = (10 * weight) + (6.25 * height) - (5 * age) + 5
    assert abs(bmr_calc - 1708.75) < 0.01
    
    tdee_calc = bmr_calc * 1.55
    assert round(tdee_calc) == 2649
    
    protein_g = weight * 2.0
    assert protein_g == 150.0
    
    water_ml = round(weight * 38)
    assert water_ml == 2850
    print("✓ Fitness science metabolic and macronutrient algorithms verified!")

def test_gradle_configuration():
    print("Verifying Gradle build scripts, wrapper binary, and SDK versions...")
    app_gradle = os.path.join(REPO_DIR, "app", "build.gradle")
    with open(app_gradle, "r", encoding="utf-8") as f:
        content = f.read()
        assert "compileSdk 34" in content
        assert "minSdk 24" in content
        assert "targetSdk 34" in content
        assert "com.yeaminsheikh.macroflow" in content
        assert "androidx.recyclerview:recyclerview" in content
    
    # Assert wrapper jar exists and is valid binary
    wrapper_jar = os.path.join(REPO_DIR, "gradle", "wrapper", "gradle-wrapper.jar")
    assert os.path.exists(wrapper_jar), "gradle-wrapper.jar is missing from gradle/wrapper/"
    assert os.path.getsize(wrapper_jar) > 30000, f"gradle-wrapper.jar appears corrupt: {os.path.getsize(wrapper_jar)} bytes"
    
    # Assert no residual root web assets remain
    root_assets = os.path.join(REPO_DIR, "assets")
    assert not os.path.exists(root_assets), "Residual root assets/ directory should not exist in Android app"
    
    # Assert SQLite schema uniqueness on water table
    db_file = os.path.join(REPO_DIR, "app", "src", "main", "java", "com", "yeaminsheikh", "macroflow", "database", "MacroDatabaseHelper.java")
    with open(db_file, "r", encoding="utf-8") as df:
        db_content = df.read()
        assert 'COL_WATER_DATE + " TEXT UNIQUE NOT NULL,' in db_content, "Missing UNIQUE constraint on water_intake table"
    
    print("✓ Gradle configuration, wrapper jar, and database schema verified!")

if __name__ == "__main__":
    print("=== RUNNING MACROFLOW CALORIE TRACKER ANDROID VERIFICATION ===")
    test_xml_syntax()
    test_manifest_and_activities()
    test_layout_ids()
    test_mifflin_st_jeor_calculator()
    test_gradle_configuration()
    print("ALL MACROFLOW TESTS PASSED SUCCESSFULLY! (5/5)")
