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

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.prefs.BackingStoreException;
import javax.swing.*;
import javax.swing.text.BadLocationException;

public class View extends JFrame {
    private Presenter presenter;
    private models.entities.Library library_model;
    private HashMap<String, String> userSettings;
    private TabView tabs_view;
    private BookView book_view;
    private HomeTab home_tab;
    private LibraryTab library_tab;
    private SearchTab search_tab;
    private CardLayout card_layout;
    private JPanel main_panel;
    private JPanel splash_screen;


    public View() {
        // ===== Parametres ======
        setTitle("Candle : an e-book reader");
        setMinimumSize(new Dimension(960, 540));
        setSize(1280, 720);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public void set_controller(Presenter presenter) {
        this.presenter = presenter;
    }

    public void create_view_content(models.entities.Library library_model, HashMap<String, String> userSettings) throws Exception {
        // ===== Contenu ======
        this.library_model = library_model;
        this.userSettings = userSettings;

        home_tab = new HomeTab();
        search_tab = new SearchTab(this);
        library_tab = new LibraryTab(this, library_model);
        tabs_view = new TabView(home_tab, search_tab, library_tab);
        book_view = new BookView(this);

        card_layout = new CardLayout();
        main_panel = new JPanel();
        main_panel.setLayout(card_layout);
        main_panel.add("a", tabs_view);
        main_panel.add("b", book_view);

        add(main_panel);
    }

    public void notify_search_performed(String query) throws IOException {
        presenter.search_performed(query);
    }

    public void update_search_results(ArrayList<Book> results) throws IOException {
        search_tab.show_results(results);
    }

    public void notify_category_change_performed(String category) throws IOException {
        presenter.category_change_performed(category);
    }

    public void update_book_list_results(HashMap<Integer, Book> results) {
        library_tab.show_results(results);
    }

    public void notify_read_performed(String category, Integer id) throws IOException, InterruptedException, BadLocationException {
        presenter.read_button_clicked(category, id);
    }

    public void show_book_view(Book current_book) throws IOException, InterruptedException, BadLocationException {
        card_layout.next(main_panel);
        book_view.show_book(current_book);
    }

    public void notify_annotation_added(String category, Integer id, String annotation, Integer start, Integer end) throws IOException {
        presenter.annotation_added(category, id, annotation, start, end);
    }

    public void notify_delete_annotation(String category, Integer id, String annotation, Integer start, Integer end) throws IOException {
        presenter.annotation_deleted(category, id, annotation, start, end);
    }

    public void notify_back_performed() throws IOException {
        card_layout.next(main_panel);
    }

    public void notify_settings_changed(String theme, String font, String fontSize) throws BackingStoreException {
        presenter.settings_changed(theme, font, fontSize);
    }

    public void update_settings(HashMap<String, String> settings) {
        // Change theme
        try {
            if (settings.get("theme").equals("Flat Dark") && !this.userSettings.get("theme").equals("Flat Dark")) {
                UIManager.setLookAndFeel(new FlatDarkLaf());
            }
            else if (settings.get("theme").equals("Flat Light") && !this.userSettings.get("theme").equals("Flat Light")) {
                UIManager.setLookAndFeel(new FlatLightLaf());
            }
            else if (settings.get("theme").equals("Flat Intellij") && !this.userSettings.get("theme").equals("Flat Intellij")) {
                UIManager.setLookAndFeel(new FlatIntelliJLaf());
            }
            else if (settings.get("theme").equals("Flat Darcula") && !this.userSettings.get("theme").equals("Flat Darcula")) {
                UIManager.setLookAndFeel(new FlatDarculaLaf());
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            SwingUtilities.updateComponentTreeUI(this);
        }

        // Change font
        book_view.change_font(settings.get("font_family"), settings.get("font_size"));

        this.userSettings = settings;
    }

    public String notify_download_performed(String category, Integer id) throws IOException {
        return presenter.download_button_clicked(category, id);
    }

    public String notify_delete_performed(Integer id) throws IOException {
        return presenter.delete_button_clicked(id);
    }

    public String notify_definition_request(String selectedText) {
        return presenter.definition_request(selectedText);
    }
}