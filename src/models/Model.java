package models;

import models.utilities.Definition;
import models.utilities.Search;
import presenters.Presenter;
import models.entities.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.prefs.BackingStoreException;

public class Model {
    private Presenter presenter;

    private final Settings settings;
    private final Library library;

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

    private Book find_book(String bookshelf, Integer id) throws IOException {
        return library.get_categories().get(bookshelf).get_books().get(id);
    }

    public ArrayList<Book> get_search(String query) {
        return Search.get_search(query, this);
    }

    public HashMap<Integer, Book> get_bookshelf_books(String bookshelf) throws IOException {
        return library.get_categories().get(bookshelf).get_books();
    }

    public Book get_book(String bookshelf, Integer id) throws IOException {
        get_bookshelf_books(bookshelf);
        return find_book(bookshelf, id);
    }

    public void update_last_position(String bookshelf, Integer id, Integer last_position) throws IOException {
        find_book(bookshelf, id).set_last_position(last_position);
    }

    public String download_book(String bookshelf, Integer id) throws IOException {
        return find_book(bookshelf, id).download();
    }

    public String delete_book(Integer id) throws IOException {
        String result = library.get_categories().get("Livres téléchargés").get_books().get(id).delete();
        library.get_categories().get("Livres téléchargés").get_books().remove(id);
        return result;
    }

    public void annotation_added(String bookshelf, Integer id, String annotation, Integer start, Integer end)
            throws IOException {
        find_book(bookshelf, id).add_annotation(annotation, start, end);
    }

    public void annotation_deleted(String bookshelf, Integer id, String annotation, Integer start, Integer end)
            throws IOException {
        find_book(bookshelf, id).delete_annotation(annotation, start, end);
    }

    public void set_settings(String theme, String font_family, String font_size) {
        settings.set_all_settings(theme, font_family, font_size);
    }

    public HashMap<String, String> get_settings() {
        return settings.get_all_settings();
    }


    public String get_definition(String selectedText) {
        Definition definition = new Definition(selectedText);
        return definition.get_definition();
    }
}

