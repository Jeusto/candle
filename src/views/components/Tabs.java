package views.components;

import views.tabs.Home;
import views.tabs.Library;
import views.tabs.Search;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;

public class Tabs extends JTabbedPane {
    public Tabs(Home homeView, Search searchView, Library libraryView) throws Exception {
        // ===== Content ======
        Image homeIcon = ImageIO.read(getClass().getResource("/assets/home.png"));
        Image searchIcon = ImageIO.read(getClass().getResource("/assets/search.png"));
        Image libraryIcon = ImageIO.read(getClass().getResource("/assets/library.png"));

        addTab("Accueil", new ImageIcon(homeIcon), homeView);
        addTab("Recherche",new ImageIcon(searchIcon), searchView);
        addTab("Ma bibliothèque", new ImageIcon(libraryIcon), libraryView);

        // ===== Settings ======
        revalidate();
    }
}
