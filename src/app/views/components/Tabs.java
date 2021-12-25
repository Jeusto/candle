package app.views.components;

import app.views.tabs.Home;
import app.views.tabs.Library;
import app.views.tabs.Search;

import javax.swing.*;
import java.awt.*;

public class Tabs extends JTabbedPane {
    public Tabs() throws Exception {
        // Contenu
        Home home_panel = new Home();
        Library library_panel = new Library();
        Search search_panel = new Search();

        addTab("Accueil", home_panel);
        addTab("Recherche", search_panel);
        addTab("Ma bibliothèque", library_panel);

        // Reglages
        JTabbedPane tab = new JTabbedPane();
        Dimension newDim = new Dimension(50, 50);

        setMinimumSize(newDim);
        setPreferredSize(newDim);
        setMaximumSize(newDim);
        setSize(newDim);
        revalidate();
    }
}
