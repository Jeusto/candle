import presenters.Presenter;
import models.Model;
import views.View;
import com.formdev.flatlaf.FlatDarkLaf;

import javax.swing.*;

public class App {
    public static void create_and_start_app() throws Exception {
        Model model = new Model();
        View view = new View();
        Presenter presenter = new Presenter(view, model);

        presenter.start();
    }

    public static void main(String[] args) throws Exception {
        try {
            UIManager.setLookAndFeel(new FlatDarkLaf());
        } catch (Exception ex) {
            System.out.println("Une erreur est survenu lors du changement du thème visuel");
        }
        create_and_start_app();
    }
}