package app.views;

import app.controllers.Controller;
import app.models.ChangeEvent;
import app.views.components.BookView;
import app.views.components.Tabs;
import app.views.tabs.Home;
import app.views.tabs.Library;
import app.views.tabs.Search;

import java.awt.*;
import java.util.HashMap;
import javax.swing.*;
import javax.swing.event.ChangeListener;

public class View extends JFrame implements app.models.ChangeEventListener, ChangeListener {
    private Controller controller;
    HashMap<String, Integer> categoriesList ;
    private Tabs tabsView;
    private BookView bookView;
    private Home homeView;
    private Search searchView;
    private Library libraryView;

    public View() {
        // ===== Settings ======
        setLayout(new CardLayout());
        setTitle("Candle : an e-book reader");
        setMinimumSize(new Dimension(960, 540));
        setSize(1280, 720);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public void addController(Controller controller) {
        this.controller = controller;
    }

    public void createContent(HashMap<String, Integer> categoriesList, Object localBooks, Object getUserSettings) throws Exception {
        // ===== Content ======
        this.categoriesList = categoriesList;
        bookView = new BookView();
        homeView = new Home();
        searchView = new Search();
        libraryView = new Library(categoriesList);
        tabsView = new Tabs(homeView, searchView, libraryView);

        add(tabsView, BorderLayout.CENTER);
//        add(bookView, BorderLayout.CENTER);
    }

    @Override
    public void modelStateChanged(ChangeEvent e) {
        // Update view state because model state changed
    }

    @Override
    public void stateChanged(javax.swing.event.ChangeEvent changeEvent) {
        // Tell the controller to update model state because action was performed
        controller.viewStateChanged();
        System.out.println(changeEvent.getSource());
    }
}