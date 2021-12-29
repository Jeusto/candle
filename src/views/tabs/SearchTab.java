package views.tabs;

import models.entities.Book;
import views.View;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;

public class SearchTab extends JPanel {
    private final View view;

    private final JPanel top_panel;
    private final JPanel main_panel;
    private JPanel main;
    private JButton search_button;
    private JTextField search_field;
    private JLabel search_label;

    public SearchTab(View view) throws IOException {
        this.view = view;

        // Parametres ==================================================================================================
        setLayout(new BorderLayout());

        // Composants ==================================================================================================
        top_panel = create_top_panel();
        main_panel = create_main_panel();

        // Contenu =====================================================================================================
        add(top_panel, BorderLayout.NORTH);
        add(main_panel, BorderLayout.CENTER);
    }

    // Fonctions pour creer l'interface ================================================================================
    JPanel create_top_panel() throws IOException {
        // Parametres ==================================================================================================
        JPanel top_panel = new JPanel();
        top_panel.setLayout(new BoxLayout(top_panel, FlowLayout.RIGHT));
        top_panel.setBorder(BorderFactory.createEmptyBorder(20, 150, 20, 150));

        // Composants ==================================================================================================
        // Barre de recherche
        search_field = new JTextField();
        search_field.setPreferredSize(new Dimension(200, 30));

        // Bouton de recherche
        Image search_icon = ImageIO.read(getClass().getResource("/assets/search.png"));
        search_button = new JButton("Faire une recherche");
        search_button.setIcon(new ImageIcon(search_icon));
        search_button.addActionListener(e -> {search_button_action_listener();});

        // Contenu =====================================================================================================
        top_panel.add(search_field);
        top_panel.add(Box.createHorizontalStrut(10));
        top_panel.add(search_button);
        return top_panel;
    }

    JPanel create_main_panel() throws IOException {
        // Composants ==================================================================================================
        // Le mot recherché
        main = new JPanel();
        search_label = new JLabel("Veuillez entrer un mot de recherche...");
        search_label.setFont((search_label.getFont()).deriveFont(Font.PLAIN, 22));

        // Parametres ==================================================================================================
        JPanel main_panel = new JPanel();
        main_panel.setLayout(new BoxLayout(main_panel, BoxLayout.Y_AXIS));
        main.setLayout(new FlowLayout());
        main.setBorder(BorderFactory.createEmptyBorder(0, 20, 20, 20));

        // Contenu =====================================================================================================
        JPanel top = new JPanel();
        top.add(search_label);
//        String[] links = {"https://www.gutenberg.org/cache/epub/66709/pg66709.cover.medium.jpg", "https://www.gutenberg.org/cache/epub/6130/pg6130.cover.medium.jpg", "https://www.gutenberg.org/cache/epub/64709/pg64709.cover.medium.jpg", "https://www.gutenberg.org/cache/epub/2852/pg2852.cover.medium.jpg", "https://www.gutenberg.org/cache/epub/2554/pg2554.cover.medium.jpg"};
//        for (String link : links) {
//            main.add(new ImageCard(link));
//        }

        main_panel.add(top);
        main_panel.add(main);
        return main_panel;
    }

    // Fonctions diverses ==============================================================================================
    public void show_result(ArrayList<Book> results) throws IOException {
        System.out.println(results.size());
//        for (Book book : results) {
//            main.add(new ImageCard(book));
//        }
        search_label.setText("Résultats pour " + search_field.getText());
        search_field.setText("");
    }

    // Actions listeners ===============================================================================================
    public void search_button_action_listener() {
        // Si on a selectionne une categorie valide, on notifie la vue qu'on veut afficher les livres de cette categorie
        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {

            @Override
            protected Void doInBackground() throws Exception {
                System.out.println("Search button clicked");
                if (search_field.getText().isEmpty()) {
                    new JOptionPane().showMessageDialog(null, "Veuillez entrer un mot de recherche non vide.", "Erreur",
                            JOptionPane.ERROR_MESSAGE);
                    return null;
                }
                try {
                    System.out.println("Search button clicked");
                    view.notify_search_performed(search_field.getText());
                } catch (IOException ex) {
                    new JOptionPane().showMessageDialog(null, "Il y a eu une erreur lors de la recherche.", "Erreur",
                            JOptionPane.ERROR_MESSAGE);
                }
            return null;
            }
        };
        worker.execute();
    }
}
