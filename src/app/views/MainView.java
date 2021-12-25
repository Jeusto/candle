package app.views;

import app.controllers.Controller;
import app.views.components.Tabs;

import java.awt.*;
import javax.swing.*;

public class MainView extends JFrame {

    private Tabs tabsView;
    private BookView bookView;

    public MainView(Controller controller, Object settings, Object library) throws Exception {
        // *****************
        // **** Contenu ****
        // *****************
        tabsView = new Tabs();
        bookView = new BookView();
        add(tabsView, BorderLayout.CENTER);

        // Absolutely position a button above everything
        JButton button = new JButton("Button");
        button.setBounds(0, 0, 100, 100);
        add(button, BorderLayout.PAGE_START);



        // ******************
        // **** Reglages ****
        // ******************
        setLayout(new CardLayout());
        setTitle("Candle : an e-book reader");
        setMinimumSize(new Dimension(960, 540));
        setSize(1280, 720);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}