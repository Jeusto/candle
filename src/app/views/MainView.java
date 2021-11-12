package app.views;

import app.views.components.Tabs;

import java.awt.*;
import javax.swing.*;

public class MainView extends JFrame {

    public MainView() throws Exception {
        // Contenu
        add(new Tabs());



        // Reglages
        setLayout(new CardLayout());
        setTitle("Candle : an e-book reader");
        setMinimumSize(new Dimension(640, 360));
        setSize(1280, 720);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }
}