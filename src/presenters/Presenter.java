package presenters;

import models.Model;
import models.entities.Book;
import views.View;

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

        // On donne la reference du presentateur au modele et la vue pour la communication
        view.set_presenter(this);
        model.set_controller(this);
    }

    // Fonctions diverses ==============================================================================================
    public void start() throws Exception {
        view.initialize_content(model.get_library(), model.get_settings());
        view.show_view();
    }

    // Fonctions liees a la recherche ==================================================================================
    public void search_performed(String query) throws IOException, InterruptedException, BadLocationException {
        ArrayList<Book> results =  model.get_search(query);
        view.show_search_result(results);
    }

    // Fonctions liees au bouton "definition" dans la vue "livre" ======================================================
    public void definition_request(String selected_text) {
        String definition = model.get_definition(selected_text);
        view.show_definition_result(selected_text, definition);
    }

    // Fonctions liees au changement "d'etagere" dans la vue librairie ================================================
    public void bookshelf_change_performed(String bookshelf) throws IOException {
        HashMap<Integer, Book> books = model.get_category_books(bookshelf);
        view.show_bookshelf_results(books);
    }

    // Fonctions liees au bouton "lire" dans la vue librairie ==========================================================
    public void read_performed(String category, Integer id)
            throws IOException, InterruptedException, BadLocationException {
        view.show_book_view(model.get_book(category, id));
    }

    // Fonctions liees au bouton "retour" dans la vue librairie ========================================================
    public void back_button_clicked(String category, Integer book_id, Integer last_position) throws IOException {
        model.update_last_position(category, book_id, last_position);
    }

    // Fonctions liees au telechargement et suppression de livre =======================================================
    public String download_button_clicked(String category, Integer id) throws IOException {
        return model.download_book(category, id);
    }

    public String delete_button_clicked(Integer id) throws IOException {
        return model.delete_book(id);
    }

    // Fonctions liees a l'ajout et suppression d'annotations dans la vue "livre" ======================================
    public void annotation_add_performed(String category, Integer id, String annotation, Integer start, Integer end)
            throws IOException {
        model.annotation_added(category, id, annotation, start, end);
    }

    public void annotation_delete_performed(String category, Integer id, String annotation, Integer start, Integer end)
            throws IOException{
        model.annotation_deleted(category, id, annotation, start, end);
    }

    // Fonctions liees au changement de parametres dans la vue "livre" =================================================
    public void settings_changed(String theme, String font, String fontSize) throws BackingStoreException {
        model.set_settings(theme, font, fontSize);
        view.change_settings(model.get_settings());
    }


}
