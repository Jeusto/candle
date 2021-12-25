package app;

import app.controllers.Controller;
import app.models.Model;
import app.views.*;
import com.formdev.flatlaf.FlatDarkLaf;

import javax.swing.*;

public class App {

    public static void createAndShowApp() throws Exception {
        // Creer le modele
        Model model = new Model();
        // Creer le controleur qui va initialiser les vues
        Controller controller = new Controller(model);
        controller.showView();
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(new FlatDarkLaf());
        } catch (Exception ex) {
            System.err.println("Erreur set look and feel");
        }
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                try {
                    createAndShowApp();
                } catch (Exception e) {
                    System.err.println("Erreur create and show app");
                    e.printStackTrace();
                }
            }
        });
    }
}