package app.views;

import app.controllers.Controller;
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
import javax.swing.*;

public class View extends JFrame {
    private Controller controller;
    private HashMap<String, Integer> categoriesList;
    private HashMap<String, String> userSettings;
    private Tabs tabs_view;
    private BookView book_view;
    private Home home_tab;
    private Library library_tab;
    private Search search_tab;
    private CardLayout card_layout;
    private JPanel main_panel;


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

    public void create_view_content(HashMap<String, Integer> categoriesList, Object localBooks, HashMap<String, String> userSettings) throws Exception {
        // ===== Content ======
        this.categoriesList = categoriesList;
        this.userSettings = userSettings;

        home_tab = new Home();
        search_tab = new Search(this);
        library_tab = new Library(this, categoriesList);
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

    public void notify_category_change_performed(String query) throws IOException {
        controller.category_change_performed(query);
    }

    public void update_book_list_results(HashMap<String, ArrayList<String>> results) {
        library_tab.show_results(results);
    }

    public void notify_read_performed(String book_title) throws IOException {
        controller.read_button_clicked(book_title);
    }

    public void show_book_view(String current_book) {
        card_layout.next(main_panel);
        book_view.show_book(current_book);
    }

    public void notify_back_performed() throws IOException {
        controller.back_button_clicked();
    }

    public void show_tabs_view() {
        card_layout.next(main_panel);
    }

    public void notify_settings_changed(String theme, String font, String fontSize) throws BackingStoreException {
        controller.settings_changed(theme, font, fontSize);
    }

    public void update_settings(HashMap<String, String> userSettings) {
        // Change theme
        try {
            if (userSettings.get("theme").equals("Flat Dark") && !this.userSettings.get("theme").equals("Flat Dark")) {
                UIManager.setLookAndFeel(new FlatDarkLaf());
            }
            else if (userSettings.get("theme").equals("Flat Light") && !this.userSettings.get("theme").equals("Flat Light")) {
                UIManager.setLookAndFeel(new FlatLightLaf());
            }
            else if (userSettings.get("theme").equals("Flat Intellij") && !this.userSettings.get("theme").equals("Flat Intellij")) {
                UIManager.setLookAndFeel(new FlatIntelliJLaf());
            }
            else if (userSettings.get("theme").equals("Flat Darcula") && !this.userSettings.get("theme").equals("Flat Darcula")) {
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
        book_view.change_font(userSettings.get("font"), userSettings.get("fontSize"));

        this.userSettings = userSettings;
    }
}