package app;

import app.controllers.Controller;
import app.models.Model;
import app.views.View;
import com.formdev.flatlaf.FlatDarkLaf;

import javax.swing.*;

public class App {

    public static void createAndShowApp() throws Exception {
        Model model = new Model();
        View view = new View();

        Controller controller = new Controller(view, model);
        controller.setViewInitialState();
        controller.showView();
    }

    public static void main(String[] args) {
        // Change the look and feel of the application
        try {
            UIManager.setLookAndFeel(new FlatDarkLaf());
        } catch (Exception ex) {
            System.err.println("Une erreur est survenu lors du changement du thème visuel");
        }
        // Create the application on the event dispatch thread
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