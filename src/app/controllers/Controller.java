package app.controllers;
import app.models.Model;
import app.views.View;

import javax.swing.plaf.synth.SynthTextAreaUI;
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

    public void set_initial_view_state() throws Exception {
        view.create_view_content(model.get_categories(), model.get_local_books(), model.get_user_all_settings());
    }


    public void show_view() {
        view.setVisible(true);
    }

    public void search_performed(String query) throws IOException {
        model.set_search_result(query);
        view.update_search_results(model.get_search_result());
    }

    public void category_change_performed(String category) throws IOException {
        model.set_current_category_books(category);
        view.update_book_list_results(model.get_current_category_books(category));
    }

    public void read_button_clicked(String book_title) {
        System.out.println(book_title);
        model.set_current_book(book_title);
        view.show_book_view((String) model.get_current_book());
    }

    public void back_button_clicked() {
        view.show_tabs_view();
    }

    public void settings_changed(String theme, String font, String fontSize) throws BackingStoreException {
        model.set_user_all_settings(theme, font, fontSize);
        view.update_settings(model.get_user_all_settings());
    }
}
