package views.components;

import views.tabs.HomeTab;
import views.tabs.LibraryTab;
import views.tabs.SearchTab;
import views.tabs.WriteTab;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;

public class TabView extends JTabbedPane {
    public TabView(HomeTab home_tab, SearchTab search_tab, LibraryTab library_tab, WriteTab write_tab) throws Exception {
        // Contenu =====================================================================================================
        Image libraryIcon = ImageIO.read(getClass().getResource("/assets/library.png"));
        Image searchIcon = ImageIO.read(getClass().getResource("/assets/search.png"));
        Image writeIcon = ImageIO.read(getClass().getResource("/assets/write.png"));
        Image homeIcon = ImageIO.read(getClass().getResource("/assets/home.png"));

        addTab("Ma bibliothèque", new ImageIcon(libraryIcon), library_tab);
        addTab("Recherche", new ImageIcon(searchIcon), search_tab);
        addTab("Ecrire", new ImageIcon(writeIcon), write_tab);
        addTab("Accueil", new ImageIcon(homeIcon), home_tab);

        // Parametres ==================================================================================================
        setTabPlacement(JTabbedPane.TOP);
        revalidate();
    }
}
