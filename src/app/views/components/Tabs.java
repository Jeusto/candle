package app.views.components;

import app.views.tabs.Home;
import app.views.tabs.Library;
import app.views.tabs.Search;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.util.HashMap;

public class Tabs extends JTabbedPane {
    public Tabs(Home homeView, Search searchView, Library libraryView) throws Exception {
        // ===== Content ======
        Image homeIcon = ImageIO.read(getClass().getResource("/app/assets/home.png"));
        Image searchIcon = ImageIO.read(getClass().getResource("/app/assets/search.png"));
        Image libraryIcon = ImageIO.read(getClass().getResource("/app/assets/library.png"));

        addTab("Accueil", new ImageIcon(homeIcon), homeView);
        addTab("Recherche",new ImageIcon(searchIcon), searchView);
        addTab("Ma bibliothèque", new ImageIcon(libraryIcon), libraryView);

        // ===== Settings ======
        JTabbedPane tab = new JTabbedPane();
        Dimension newDim = new Dimension(50, 50);

        setMinimumSize(newDim);
        setPreferredSize(newDim);
        setMaximumSize(newDim);
        setSize(newDim);
        revalidate();
    }
}
