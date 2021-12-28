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

    public HashMap<Integer, Book> get_category_books(String category) {
        return library.get_categories().get(category).get_books();
    }

    public Book get_book(String category, Integer id) throws IOException {
        System.out.println(id);
        System.out.println(category);
        get_category_books(category);
        Book book = find_book(category, id);
        System.out.println(book == null);
        return book;
    }

    public String download_book(String category, Integer id) throws IOException {
        return find_book(category, id).download();
    }

    public String delete_book(Integer id) throws IOException {
        String result = library.get_categories().get("Livres téléchargés").get_books().get(id).delete();
        library.get_categories().get("Livres téléchargés").get_books().remove(id);
        return result;
    }

    public void annotation_added(String category, Integer id, String annotation, Integer start, Integer end) {
        find_book(category, id).add_annotation(annotation, start, end);
    }

    public void annotation_deleted(String category, Integer id, String annotation, Integer start, Integer end) {
        find_book(category, id).delete_annotation(annotation, start, end);
    }

    private Book find_book(String category, Integer id) {
        return library.get_categories().get(category).get_books().get(id);
    }

    public String get_definition(String selectedText) {
        return Definition.get_definition(selectedText);
    }
}

