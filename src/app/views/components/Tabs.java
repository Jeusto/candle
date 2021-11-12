package app.views.components;

import app.views.Home;
import app.views.Library;
import app.views.Search;

import javax.swing.*;
import java.awt.*;

public class Tabs extends JTabbedPane {
    public Tabs() throws Exception {
        // Contenu
        Home home_panel = new Home();
        Library library_panel = new Library();
        addTab("Accueil", home_panel);
        addTab("Recherche", new Search());
        addTab("Ma bibliothèque", library_panel);
        addTab("Mes statistiques", new JPanel());

        // Reglages
        JTabbedPane tab = new JTabbedPane();
        Dimension newDim = new Dimension(50, 50);

        setMinimumSize(newDim);
        setPreferredSize(newDim);
        setMaximumSize(newDim);
        setSize(newDim);
        setFont(new Font(Font.SANS_SERIF, Font.BOLD, 14));
        revalidate();
    }
}
