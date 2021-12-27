package app.controllers;
import app.models.Model;
import app.views.View;

import java.io.IOException;
import java.util.prefs.BackingStoreException;

public class Controller{
    View view = null;
    Model model = null;

    public Controller(View view, Model model) throws Exception {
        this.view = view;
        this.model = model;

        view.set_controller(this);
        model.set_controller(this);
    }

    public void start() throws Exception {
        set_initial_view_state();
        show_view();
    }

    public void show_view() {
        view.setVisible(true);
    }

    public void set_initial_view_state() throws Exception {
        view.create_view_content(model.get_library(), model.get_all_settings());
    }

    //
    //

    public void search_performed(String query) throws IOException {
        view.update_search_results(model.get_search_result(query));
    }

    public void category_change_performed(String category) throws IOException {
        view.update_book_list_results(model.get_category_books(category));
    }

    public void read_button_clicked(String category, String book_title) throws IOException {
        view.show_book_view(model.get_book(category, book_title));
    }

    public void settings_changed(String theme, String font, String fontSize) throws BackingStoreException {
        model.set_all_settings(theme, font, fontSize);
        view.update_settings(model.get_all_settings());
    }

    public void download_button_clicked(String category, String title) throws IOException {
        model.download_book(category, title);
        view.update_local_books();
    }
}
