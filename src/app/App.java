package app;

import app.controllers.Controller;
import app.models.Model;
import app.views.View;
import com.formdev.flatlaf.FlatDarkLaf;

import javax.swing.*;

public class App {

    /**
     * Cree et affiche l'application
     * @throws Exception
     */
    public static void createAndShowApp() throws Exception {
        Model model = new Model();
        View view = new View();

        Controller controller = new Controller(view, model);
        controller.start();
    }

    /**
     * Point d'entrée du programme
     * @param args
     */
    public static void main(String[] args) {
        // Changer le theme visuel de l'interface graphique
        try {
            UIManager.setLookAndFeel(new FlatDarkLaf());
        } catch (Exception ex) {
            System.err.println("Une erreur est survenu lors du changement du thème visuel");
        }
        // Creer l'application et l'afficher
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                try {
                    createAndShowApp();
                } catch (Exception e) {
                    System.err.println("Une erreur est survenu dans l'application");
                    e.printStackTrace();
                }
            }
        });
    }
}