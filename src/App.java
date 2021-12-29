import presenters.Presenter;
import models.Model;
import views.View;
import com.formdev.flatlaf.FlatDarkLaf;

import javax.swing.*;

public class App {

    public static void create_and_show_app() throws Exception {
        Model model = new Model();
        View view = new View();
        Presenter presenter = new Presenter(view, model);

        presenter.start();
    }

    public static void main(String[] args) throws Exception {
        // Changer le theme visuel de l'interface graphique
        try {
            UIManager.setLookAndFeel(new FlatDarkLaf());
        } catch (Exception ex) {
            System.out.println("Une erreur est survenu lors du changement du thème visuel");
        }
        // Creer l'application et l'afficher
        create_and_show_app();
    }
}