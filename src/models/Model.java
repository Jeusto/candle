package models;

import presenters.Presenter;
import models.entities.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.prefs.BackingStoreException;

public class Model {
    private Presenter presenter;
    private Settings settings;
    private Library library;


    public Model() throws Exception {
        library = new Library();
        settings = new Settings();
    }

    public void set_controller(Presenter presenter) {
        this.presenter = presenter;
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

    public Book get_book(String category, String title) throws IOException {
        return find_book(category, title).get_book();
    }

    public String download_book(String category, String title) throws IOException {
        return find_book(category, title).download();
    }

    public String delete_book(String category, String title) throws IOException {
        String result = library.get_categories().get(category).get_books().get(title).delete();
        library.get_categories().get(category).get_books().remove(title);
        return result;
    }

    public void annotation_added(String category, String title, String annotation, Integer start, Integer end) {
        find_book(category, title).add_annotation(annotation, start, end);
    }

    public void annotation_deleted(String category, String title, String annotation, Integer start, Integer end) {
        find_book(category, title).delete_annotation(annotation, start, end);
    }

    private Book find_book(String category,String title) {
        return library.get_categories().get(category).get_books().get(title);
    }


}

