package presenters;
import models.Model;
import models.entities.Book;
import views.View;

import javax.swing.text.BadLocationException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.prefs.BackingStoreException;

public class Presenter {
    View view = null;
    Model model = null;

    public Presenter(View view, Model model) throws Exception {
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
        view.update_search_results(model.get_search(query));
    }

    public void category_change_performed(String category) throws IOException {
        view.update_book_list_results(model.get_category_books(category));
    }

    public void read_button_clicked(String category, String book_title) throws IOException, InterruptedException, BadLocationException {
        view.show_book_view(model.get_book(category, book_title));
    }

    public void settings_changed(String theme, String font, String fontSize) throws BackingStoreException {
        model.set_all_settings(theme, font, fontSize);
        view.update_settings(model.get_all_settings());
    }

    public String download_button_clicked(String category, String title) throws IOException {
        return model.download_book(category, title);
    }

    public String delete_button_clicked(String title) throws IOException {
        return model.delete_book(title);
    }

    public void annotation_added(String category, String title, String annotation, Integer start, Integer end) throws IOException {
        model.annotation_added(category, title, annotation, start, end);
    }

    public void annotation_deleted(String category, String title, String annotation, Integer start, Integer end) {
        model.annotation_deleted(category, title, annotation, start, end);
    }

    public String definition_request(String selectedText) {
        return model.get_definition(selectedText);
    }
}
