package views.components;

import views.tabs.HomeTab;
import views.tabs.LibraryTab;
import views.tabs.SearchTab;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;

public class TabView extends JTabbedPane {
    public TabView(HomeTab homeView, SearchTab searchView, LibraryTab libraryView) throws Exception {
        // ===== Contenu ======
        Image homeIcon = ImageIO.read(getClass().getResource("/assets/home.png"));
        Image searchIcon = ImageIO.read(getClass().getResource("/assets/search.png"));
        Image libraryIcon = ImageIO.read(getClass().getResource("/assets/library.png"));

        addTab("Accueil", new ImageIcon(homeIcon), homeView);
        addTab("Recherche",new ImageIcon(searchIcon), searchView);
        addTab("Ma bibliothèque", new ImageIcon(libraryIcon), libraryView);

        // ===== Parametres ======
        revalidate();
    }
}
