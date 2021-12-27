package models;

import controllers.Controller;
import models.entities.*;
import models.entities.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.prefs.BackingStoreException;

public class Model {
    private Settings settings;
    private Library library;
    private Category current_category;
    private Book current_book;

    private Controller controller;

    public Model() throws Exception {
        library = new Library();
        settings = new Settings();
    }

    public void set_controller(Controller controller) {
        this.controller = controller;
    }

    public Library get_library() {
        return library;
    }

    public void set_all_settings(String theme, String font_family, String font_size) throws BackingStoreException {
        settings.set_all_settings(theme, font_family, font_size);
    }

    public HashMap<String, String> get_all_settings() {
        return settings.get_all_settings();
    }

    public String get_search_result(String query) {
        return new Search(query).get_search_result();
    }

    public HashMap<String, Book> get_category_books(String category) {
        return library.get_categories().get(category).get_books();
    }

    public Book get_book(String category, String book) throws IOException {
        return library.get_categories().get(category).get_books().get(book).get_book();
    }

    public String download_book(String category, String book) throws IOException {
        return library.get_categories().get(category).get_books().get(book).download();
    }

    public String delete_book(String category, String book) throws IOException {
        String result = library.get_categories().get(category).get_books().get(book).delete();
        library.get_categories().get(category).get_books().remove(book);
        return result;
    }


}

