package views;

import presenters.Presenter;
import models.entities.Book;
import views.components.BookView;
import views.components.TabView;
import views.tabs.HomeTab;
import views.tabs.LibraryTab;
import views.tabs.SearchTab;
import com.formdev.flatlaf.FlatDarculaLaf;
import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatIntelliJLaf;
import com.formdev.flatlaf.FlatLightLaf;
import views.tabs.WriteTab;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.prefs.BackingStoreException;
import javax.swing.*;
import javax.swing.text.BadLocationException;

public class View extends JFrame {
    private Presenter presenter;

    private TabView tabs_view;
    private HomeTab home_tab;
    private LibraryTab library_tab;
    private SearchTab search_tab;
    private WriteTab write_tab;
    private BookView book_view;

    private HashMap<String, String> user_settings;

    private CardLayout card_layout;
    private JPanel main_panel;


    public View() {
        // Parametres ==================================================================================================
        setTitle("Candle : an e-book reader");
        setSize(1280, 720);
        setMinimumSize(new Dimension(960, 540));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    // Fonctions diverses ==============================================================================================
    public void set_presenter(Presenter presenter) {
        this.presenter = presenter;
    }

    public void initialize_content(models.entities.Library initial_library, HashMap<String,
            String> user_settings) throws Exception {
        this.user_settings = user_settings;

        // Contenu du composant ====================================================================
        home_tab = new HomeTab();
        search_tab = new SearchTab(this);
        library_tab = new LibraryTab(this, initial_library);
        write_tab = new WriteTab();
        tabs_view = new TabView(home_tab, search_tab, library_tab, write_tab);
        book_view = new BookView(this);

        // Parametres du composant =================================================================
        card_layout = new CardLayout();
        main_panel = new JPanel();

        main_panel.setLayout(card_layout);
        main_panel.add("tabs", tabs_view);
        main_panel.add("book", book_view);

        add(main_panel);
    }

    public void show_view() {
        SwingUtilities.invokeLater(() -> setVisible(true));
    }

    // Fonctions liees a la recherche ==================================================================================
    public void notify_search_performed(String query) throws IOException, InterruptedException, BadLocationException {
        presenter.search_performed(query);
    }

    public void show_search_result(ArrayList<Book> results) throws IOException, InterruptedException, BadLocationException {
        search_tab.show_result(results);
    }

    // Fonctions liees au changement "d'etagere" dans la vue librairie =================================================
    public void notify_bookshelf_change_performed(String category) throws IOException {
        presenter.bookshelf_change_performed(category);
    }

    public void show_bookshelf_results(HashMap<Integer, Book> results) {
        library_tab.show_results(results);
    }

    // Fonctions liees au bouton "lire" dans la vue librairie ==========================================================
    public void notify_read_performed(String category, Integer id) throws IOException,
            InterruptedException, BadLocationException {
        presenter.read_performed(category, id);
    }

    public void show_book_view(Book current_book) {
        book_view.show_book(current_book);
        card_layout.next(main_panel);
    }

    // Fonctions liees au telechargement et suppression de livre =======================================================
    public String notify_download_performed(String category, Integer id) throws IOException {
        return presenter.download_button_clicked(category, id);
    }

    public String notify_delete_performed(Integer id) throws IOException {
        return presenter.delete_button_clicked(id);
    }

    // Fonctions liees au bouton "retour" dans la vue "livre" ==========================================================
    public void notify_back_performed(String category, Integer book_id, Integer last_position) throws IOException {
        presenter.back_button_clicked(category, book_id, last_position);
        card_layout.next(main_panel);
    }

    // Fonctions liees a l'ajout et suppression d'annotations dans la vue "livre" ======================================
    public void notify_annotation_add_performed(String category, Integer id, String annotation,
                                                Integer start, Integer end) throws IOException {
        presenter.annotation_add_performed(category, id, annotation, start, end);
    }

    //    public void show_annotation_add_result(Integer id, String title, Integer start, Integer end,
//                                          String result) {
//        library_tab.show_add_result(id, title, start, end result);
//    }
    public void notify_annotation_remove_performed(String category, Integer id, String annotation,
                                                   Integer start, Integer end) throws IOException {
        presenter.annotation_delete_performed(category, id, annotation, start, end);
    }
//    public void show_annotation_remove_result(Integer id, String title, Integer start,
//                                              Integer end, String result) {
//        library_tab.show_remove_result(id, title, start, end, result);
//    }

    // Fonctions liees au bouton "definition" dans la vue "livre" ======================================================
    public void notify_definition_request(String selected_text) {
        presenter.definition_request(selected_text);
    }

    public void show_definition_result(String selected_text, String definition) {
        book_view.show_definition_result(selected_text, definition);
    }

    // Fonctions liees au changement de parametres dans la vue "livre" =================================================
    public void notify_settings_change_performed(String theme, String font, String fontSize)
            throws BackingStoreException {
        presenter.settings_changed(theme, font, fontSize);
    }

    public void change_settings(HashMap<String, String> settings) {
        // Changer la police et la taillle de la police dans la vue "livre"
        book_view.change_font(settings.get("font_family"), settings.get("font_size"));

        // Changer le theme
        try {
            // On verifie que le parametre choisie n'est pas celui actuellement utilisé pour
            // ne pas avoir à recharger la page
            if (settings.get("theme").equals("Flat Dark") && !this.user_settings.get("theme").equals("Flat Dark")) {
                UIManager.setLookAndFeel(new FlatDarkLaf());
            } else if (settings.get("theme").equals("Flat Light") && !this.user_settings.get("theme").equals("Flat Light")) {
                UIManager.setLookAndFeel(new FlatLightLaf());
            } else if (settings.get("theme").equals("Flat Intellij") && !this.user_settings.get("theme").equals("Flat Intellij")) {
                UIManager.setLookAndFeel(new FlatIntelliJLaf());
            } else if (settings.get("theme").equals("Flat Darcula") && !this.user_settings.get("theme").equals("Flat Darcula")) {
                UIManager.setLookAndFeel(new FlatDarculaLaf());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            SwingUtilities.updateComponentTreeUI(this);
        }

        this.user_settings = settings;
    }
}