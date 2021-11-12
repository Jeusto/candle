package app;

import app.views.*;
import com.formdev.flatlaf.FlatDarkLaf;
import javax.swing.*;

public class App {

    public static void createAndShowGUI() throws Exception {
        new MainView();
    }
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(new FlatDarkLaf());
            createAndShowGUI();
            // TODO: Recuperer config et creer si ca existe pas
        } catch (Exception ex) {
            System.err.println("Erreur");
        }
    }
}