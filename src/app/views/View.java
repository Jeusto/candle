package app.views;

import app.controllers.Controller;
import app.models.entities.Book;
import app.views.components.BookView;
import app.views.components.Tabs;
import app.views.tabs.Home;
import app.views.tabs.Library;
import app.views.tabs.Search;
import com.formdev.flatlaf.FlatDarculaLaf;
import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatIntelliJLaf;
import com.formdev.flatlaf.FlatLightLaf;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.prefs.BackingStoreException;
import javax.imageio.ImageIO;
import javax.swing.*;

public class View extends JFrame {
    private Controller controller;
    private app.models.entities.Library library_model;
    private HashMap<String, String> userSettings;
    private Tabs tabs_view;
    private BookView book_view;
    private Home home_tab;
    private Library library_tab;
    private Search search_tab;
    private CardLayout card_layout;
    private JPanel main_panel;
    private JPanel splash_screen;


    public View() {
        // ===== Settings ======
//        setLayout(new CardLayout());
        setTitle("Candle : an e-book reader");
        setMinimumSize(new Dimension(960, 540));
        setSize(1280, 720);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public void set_controller(Controller controller) {
        this.controller = controller;
    }

    public void create_view_content(app.models.entities.Library library_model, HashMap<String, String> userSettings) throws Exception {
        // ===== Content ======
        this.library_model = library_model;
        this.userSettings = userSettings;

        home_tab = new Home();
        search_tab = new Search(this);
        library_tab = new Library(this, library_model);
        tabs_view = new Tabs(home_tab, search_tab, library_tab);
        book_view = new BookView(this);

        card_layout = new CardLayout();
        main_panel = new JPanel();
        main_panel.setLayout(card_layout);
        main_panel.add("a", tabs_view);
        main_panel.add("b", book_view);

        add(main_panel);
    }

    public void notify_search_performed(String query) throws IOException {
        controller.search_performed(query);
    }

    public void update_search_results(String results) {
        search_tab.show_results(results);
    }

    public void notify_category_change_performed(String category) throws IOException {
        controller.category_change_performed(category);
    }

    public void update_book_list_results(HashMap<String, Book> results) {
        library_tab.show_results(results);
    }

    public void notify_read_performed(String category, String book_title) throws IOException {
        controller.read_button_clicked(category, book_title);
    }

    public void show_book_view(Book current_book) {
        card_layout.next(main_panel);
        book_view.show_book(current_book);
    }

    public void notify_back_performed() throws IOException {
        card_layout.next(main_panel);
    }

    public void notify_settings_changed(String theme, String font, String fontSize) throws BackingStoreException {
        controller.settings_changed(theme, font, fontSize);
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

    public void notify_download_performed(String category, String title) throws IOException {
        controller.download_button_clicked(category, title);
    }

    public void update_local_books() {
        library_tab.update_local_books();
    }
}