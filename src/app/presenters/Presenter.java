package app.presenters;

import app.models.Model;
import app.models.entities.Book;
import app.views.View;

import javax.swing.text.BadLocationException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.prefs.BackingStoreException;

public class Presenter {
    View view;
    Model model;

    public Presenter(View view, Model model){
        this.view = view;
        this.model = model;

        view.set_presenter(this);
        model.set_controller(this);
    }

    public void start() throws Exception {
        view.initialize_content(model.get_library(), model.get_settings());
        view.show_view();
    }

    public void search_performed(String query) throws IOException, InterruptedException, BadLocationException {
        ArrayList<Book> results =  model.get_search(query);
        view.show_search_result(results);
    }

    public void definition_requested(String selected_text) {
        String definition = model.get_definition(selected_text);
        view.show_definition_result(selected_text, definition);
    }

    public void bookshelf_change_performed(String bookshelf) throws IOException {
        HashMap<Integer, Book> books = model.get_bookshelf_books(bookshelf);
        view.show_bookshelf_results(books);
    }

    public void read_performed(String bookshelf, Integer id)
            throws IOException, InterruptedException, BadLocationException {
        view.show_book_view(model.get_book(bookshelf, id));
    }

    public void back_button_clicked(String bookshelf, Integer book_id, Integer last_position) throws IOException {
        model.update_last_position(bookshelf, book_id, last_position);
    }

    public void download_button_clicked(String bookshelf, Integer id) throws IOException {
        view.show_download_result(model.download_book(bookshelf, id));
    }

    public void delete_button_clicked(Integer id) throws IOException {
        view.show_delete_result(model.delete_book(id));
    }

    public void annotation_add_performed(String bookshelf, Integer id, String annotation, Integer start, Integer end)
            throws IOException {
        model.annotation_added(bookshelf, id, annotation, start, end);
    }

    public void annotation_remove_performed(String bookshelf, Integer id, String annotation, Integer start, Integer end)
            throws IOException{
        model.annotation_deleted(bookshelf, id, annotation, start, end);
    }

    public void settings_change_performed(String theme, String font, String fontSize) throws BackingStoreException {
        model.set_settings(theme, font, fontSize);
        view.change_settings(model.get_settings());
    }
}
