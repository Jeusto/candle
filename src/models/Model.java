package models;

import presenters.Presenter;
import models.entities.*;

import java.io.IOException;
import java.util.ArrayList;
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

    public ArrayList<Book> get_search(String query) {
        return Search.get_search(query, this);
    }

    public HashMap<String, Book> get_category_books(String category) {
        return library.get_categories().get(category).get_books();
    }

    public Book get_book(String category, String title) throws IOException {
        System.out.println(title);
        System.out.println(category);
        get_category_books(category);
        Book book = find_book(category, title).get_book();
        System.out.println(book == null);
        return book;
    }

    public String download_book(String category, String title) throws IOException {
        return find_book(category, title).download();
    }

    public String delete_book(String title) throws IOException {
        String result = library.get_categories().get("Livres téléchargés").get_books().get(title).delete();
        library.get_categories().get("Livres téléchargés").get_books().remove(title);
        return result;
    }

    public void annotation_added(String category, String title, String annotation, Integer start, Integer end) {
        find_book(category, title).add_annotation(annotation, start, end);
    }

    public void annotation_deleted(String category, String title, String annotation, Integer start, Integer end) {
        find_book(category, title).delete_annotation(annotation, start, end);
    }

    private Book find_book(String category, String title) {
        return library.get_categories().get(category).get_books().get(title);
    }

    private Book find_book(String title) {
        for (Category category : library.get_categories().values()) {
            if (category.get_books().containsKey(title)) {
                return category.get_books().get(title);
            }
        }
        return null;
    }

    public String get_definition(String selectedText) {
        return Definition.get_definition(selectedText);
    }
}

