package models.entities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.prefs.Preferences;

public class Settings {
    private final String[] possible_themes = {"Flat Dark", "Flat Light", "Flat Intellij", "Flat Darcula"};
    private final String[] possible_font_families = {"Arial", "Courier", "Helvetica", "Times New Roman", "Verdana"};
    private final String[] possible_font_sizes = {"11", "12", "14", "16", "18", "20", "22", "24", "26", "28", "36",
            "48", "72"};

    private Preferences preferences;

    public Settings() {
        set_all_settings("Flat Dark", "Arial", "12");
    }

    public void set_all_settings(String theme, String font, String fontSize) {
        preferences = Preferences.userRoot().node(this.getClass().getName());
        preferences.put("theme", theme);
        preferences.put("font_family", font);
        preferences.put("font_size", fontSize);
    }

    public HashMap<String, String> get_all_settings() {
        HashMap<String, String> settings = new HashMap<>();
        settings.put("theme", preferences.get("theme", "Flat Dark"));
        settings.put("font_family", preferences.get("font_family", "Arial"));
        settings.put("font_size", preferences.get("font_size", "14"));
        return settings;
    }

    public ArrayList<Object[]> get_possible_values() {
        ArrayList<Object[]> values = new ArrayList<Object[]>();
        values.add(possible_themes);
        values.add(possible_font_families);
        values.add(possible_font_sizes);
        return values;
    }
}
