package app.models;

import app.controllers.Controller;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

public class Model {
    private HashMap<String, Integer> categories;
    private HashMap<String, ArrayList<String>> current_category_books;
    private String current_category;
    private String search_result;
    private HashMap<String, ArrayList<String>> local_books;
    private Controller controller;

    private Preferences user_settings;
    private String[] possible_themes = {"Flat Dark", "Flat Light", "Flat Intellij", "Flat Darcula"};
    private String[] possible_fonts = {"Arial", "Courier", "Helvetica", "Times New Roman", "Verdana"};
    private Integer[] possible_fontSizes = {8, 9, 10, 11, 12, 14, 16, 18, 20, 22, 24, 26, 28, 36, 48, 72};

    public Model() throws IOException, BackingStoreException {
        set_categories();
        load_local_books();
        set_user_all_settings("Flat Dark", "Arial", "12");
    }

    public void set_controller(Controller controller) {
        this.controller = controller;
    }

    public void set_search_result(String search_result) throws IOException {
        search_result = "Search result";
    }

    public String get_search_result() {
        return search_result;
    }

    public void set_categories() throws IOException {
        if (categories == null) {
            categories = new HashMap<>();
        }
        if (categories.isEmpty() == false) {
            return;
        }

        URL u = new URL("https://www.gutenberg.org/ebooks/bookshelf/");

        try {
            URLConnection uc = u.openConnection();
            BufferedReader in = new BufferedReader(new InputStreamReader(uc.getInputStream()));
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                int index = inputLine.indexOf("/ebooks/bookshelf/");
                while (index != -1) {
                    int index2 = inputLine.indexOf("\" title=\"", index + 18);
                    if (index2 == -1) {
                        index = -1;
                        continue;
                    }
                    Integer id = Integer.parseInt(inputLine.substring(index + 18, index2));
                    index = inputLine.indexOf("title=\"", index2);
                    while (index != -1) {
                        index2 = inputLine.indexOf("\"", index + 7);
                        String title = inputLine.substring(index + 7, index2);
                        categories.put(title, id);
                        index = inputLine.indexOf("/ebooks/bookshelf/", index2);
                    }
                }
            }
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        get_current_category_books("Islam");
    }

    public HashMap<String, Integer> get_categories() {
        return categories;
    }

    public void set_current_category_books(String category) throws IOException {
        if (category.equals(current_category)) {
            return;
        }

        String categoryUrl = "https://www.gutenberg.org/ebooks/bookshelf/" + categories.get(category).toString();

        // Connect to the URL
        Document doc = Jsoup.connect(categoryUrl).get();

        // Get book titles
        Elements titles = doc.select(".booklink a.link .title");

        // Get book IDs
        Elements IDs = doc.select(".booklink > a");
        ArrayList<String> idList = new ArrayList<String>();
        for (Element ID : IDs) {
            idList.add(ID.attr("href"));
        }

        // Get book downloads count
        Elements downloads = doc.select(".booklink .extra");

        // Add everything to hashmap
        current_category_books = new HashMap<String, ArrayList<String>>();
        for (int i = 0; i < titles.size(); i++) {
            ArrayList<String> values = new ArrayList<String>();
            values.add(idList.get(i));
            values.add(downloads.get(i).text());
            current_category_books.put(titles.get(i).text(), values);
        }
    }

    public HashMap<String, ArrayList<String>> get_current_category_books(String category) throws IOException {
        return current_category_books;
    }

    private void load_local_books() {
        //
    }

    public Object get_local_books() throws IOException {
        return local_books;
    }

    public void set_user_all_settings(String theme, String font, String fontSize) throws BackingStoreException {
        user_settings = Preferences.userRoot().node(this.getClass().getName());

        user_settings.put("theme", theme);
        user_settings.put("font_family", font);
        user_settings.put("font_size", fontSize);
    }

    public HashMap<String, String> get_user_all_settings() {
        HashMap<String, String> settings = new HashMap<String, String>();
        settings.put("theme", user_settings.get("theme", "Flat Dark"));
        settings.put("font_family", user_settings.get("font_family", "Arial"));
        settings.put("font_size", user_settings.get("font_size", "12"));
        return settings;
    }

    public void set_current_book(String book_title) {
    }

    public Object get_current_book() {
        return null;
    }
}
