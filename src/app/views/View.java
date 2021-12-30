package app.views;

import app.presenters.Presenter;
import app.models.entities.Book;
import app.views.components.BookView;
import app.views.components.TabView;
import app.views.tabs.HomeTab;
import app.views.tabs.LibraryTab;
import app.views.tabs.SearchTab;
import com.formdev.flatlaf.FlatDarculaLaf;
import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatIntelliJLaf;
import com.formdev.flatlaf.FlatLightLaf;
import app.views.tabs.WriteTab;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.prefs.BackingStoreException;
import javax.swing.*;
import javax.swing.text.BadLocationException;

public class View extends JFrame {
    private Presenter presenter;

    private BookView book_view;
    private TabView tab_view;

    private HomeTab home_tab;
    private LibraryTab library_tab;
    private SearchTab search_tab;
    private WriteTab write_tab;

    private HashMap<String, String> user_settings;

    private CardLayout card_layout;
    private JPanel main_panel;

    public View() {
        setTitle("Candle : an e-book reader");
        setSize(1280, 720);
        setMinimumSize(new Dimension(960, 540));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public void set_presenter(Presenter presenter) {
        this.presenter = presenter;
    }

    public void initialize_content(app.models.entities.Library initial_library, HashMap<String, String> user_settings)
            throws Exception {
        this.user_settings = user_settings;

        home_tab = new HomeTab();
        library_tab = new LibraryTab(this, initial_library);
        search_tab = new SearchTab(this);
        write_tab = new WriteTab();

        tab_view = new TabView(home_tab, library_tab, search_tab, write_tab);
        book_view = new BookView(this);

        card_layout = new CardLayout();
        main_panel = new JPanel();

        main_panel.setLayout(card_layout);
        main_panel.add("tabs", tab_view);
        main_panel.add("book", book_view);

        add(main_panel);
    }

    public void show_view() {
        SwingUtilities.invokeLater(() -> setVisible(true));
    }

    public void notify_search_performed(String query) throws IOException, InterruptedException, BadLocationException {
        presenter.search_performed(query);
    }

    public void show_search_result(ArrayList<Book> results) throws IOException, InterruptedException,
            BadLocationException {
        search_tab.show_result(results);
    }

    public void notify_bookshelf_change_performed(String bookshelf) throws IOException {
        presenter.bookshelf_change_performed(bookshelf);
    }

    public void show_bookshelf_results(HashMap<Integer, Book> results) {
        library_tab.show_results(results);
    }

    public void notify_read_performed(String bookshelf, Integer id) throws IOException, InterruptedException,
            BadLocationException {
        presenter.read_performed(bookshelf, id);
    }

    public void show_book_view(Book current_book) {
        book_view.show_book(current_book);
        card_layout.next(main_panel);
    }

    public void notify_download_performed(String bookshelf, Integer id) throws IOException {
        presenter.download_button_clicked(bookshelf, id);
    }

    public void show_download_result(String result) {
        library_tab.show_download_result(result);
    }

    public void notify_delete_performed(Integer id) throws IOException {
        presenter.delete_button_clicked(id);
    }

    public void show_delete_result(String result) {
        library_tab.show_delete_result(result);
    }

    public void notify_back_performed(String bookshelf, Integer book_id, Integer last_position) throws IOException {
        presenter.back_button_clicked(bookshelf, book_id, last_position);
        card_layout.next(main_panel);
    }

    public void notify_annotation_add_performed(String bookshelf, Integer id, String annotation,
                                                Integer start, Integer end) throws IOException {
        presenter.annotation_add_performed(bookshelf, id, annotation, start, end);
    }

    public void notify_annotation_remove_performed(String bookshelf, Integer id, String annotation,
                                                   Integer start, Integer end) throws IOException {
        presenter.annotation_remove_performed(bookshelf, id, annotation, start, end);
    }

    public void notify_definition_requested(String selected_text) {
        presenter.definition_requested(selected_text);
    }

    public void show_definition_result(String selected_text, String definition) {
        book_view.show_definition_result(selected_text, definition);
    }

    public void notify_settings_change_performed(String theme, String font_family, String font_size)
            throws BackingStoreException {
        presenter.settings_change_performed(theme, font_family, font_size);
    }

    public void change_settings(HashMap<String, String> settings) {
        book_view.change_font(settings.get("font_family"), settings.get("font_size"));

        try {
            if (settings.get("theme").equals("Flat Dark") && !user_settings.get("theme").equals("Flat Dark")) {
                UIManager.setLookAndFeel(new FlatDarkLaf());
            } else if (settings.get("theme").equals("Flat Light") && !user_settings.get("theme").equals("Flat Light")) {
                UIManager.setLookAndFeel(new FlatLightLaf());
            } else if (settings.get("theme").equals("Flat Intellij") && !user_settings.get("theme").equals("Flat Intellij")) {
                UIManager.setLookAndFeel(new FlatIntelliJLaf());
            } else if (settings.get("theme").equals("Flat Darcula") && !user_settings.get("theme").equals("Flat Darcula")) {
                UIManager.setLookAndFeel(new FlatDarculaLaf());
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Il y a eu une erreur dans le changement " +
                            "de thème.", "Erreur", JOptionPane.ERROR_MESSAGE);
        } finally {
            SwingUtilities.updateComponentTreeUI(this);
        }

        this.user_settings = settings;
    }
}