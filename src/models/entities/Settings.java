package models.entities;

import java.util.HashMap;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

public class Settings {
    private String[] possible_themes = {"Flat Dark", "Flat Light", "Flat Intellij", "Flat Darcula"};
    private String[] possible_font_families = {"Arial", "Courier", "Helvetica", "Times New Roman", "Verdana"};
    private Integer[] possible_font_Sizes = {8, 9, 10, 11, 12, 14, 16, 18, 20, 22, 24, 26, 28, 36, 48, 72};

    private Preferences preferences;

    public Settings() throws BackingStoreException {
        set_all_settings("Flat Dark", "Arial", "12");
    }

    public void set_all_settings(String theme, String font, String fontSize) throws BackingStoreException {
        preferences = Preferences.userRoot().node(this.getClass().getName());
        preferences.put("theme", theme);
        preferences.put("font_family", font);
        preferences.put("font_size", fontSize);
    }

    public HashMap<String, String> get_all_settings() {
        HashMap<String, String> settings = new HashMap<String, String>();
        settings.put("theme", preferences.get("theme", "Flat Dark"));
        settings.put("font_family", preferences.get("font_family", "Arial"));
        settings.put("font_size", preferences.get("font_size", "12"));
        return settings;
    }

}
