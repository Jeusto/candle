package views.tabs;

import models.entities.Book;
import views.View;
import views.components.ImageCard;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;

public class SearchTab extends JPanel {
    private JPanel topPanel;
    private JPanel mainPanel;
    private JPanel main;
    private View view;
    private JButton search_button;
    private JTextField search_field;
    private JLabel search_label;

    public SearchTab(View view) throws IOException {
        this.view = view;

        // ===== Composants ======
        topPanel = createTopPanel();
        mainPanel = createMainPanel();

        // ===== Parametres ======
        setLayout(new BorderLayout());

        // ===== Contenu ======
        add(topPanel, BorderLayout.NORTH);
        add(mainPanel, BorderLayout.CENTER);
    }

    JPanel createTopPanel() throws IOException {
        // ===== Composants ======
        // Search field
        search_field = new JTextField();
        search_field.setPreferredSize(new Dimension(200, 30));

        // Search button
        search_button = new JButton("Faire une recherche");
        Image searchIcon = ImageIO.read(getClass().getResource("/assets/search.png"));
        search_button.setIcon(new ImageIcon(searchIcon));
        search_button.addActionListener(e -> {
            if (search_field.getText().isEmpty()) {
                new JOptionPane().showMessageDialog(null, "Veuillez entrer un mot de recherche.", "Erreur", JOptionPane.ERROR_MESSAGE);
                return;
            }
            try {
                view.notify_search_performed(search_field.getText());
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });

        // ===== Parametres ======
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BoxLayout(topPanel, FlowLayout.RIGHT));
        topPanel.setBorder(BorderFactory.createEmptyBorder(20, 150, 20, 150));

        // ===== Contenu ======
        topPanel.add(search_field);
        topPanel.add(Box.createHorizontalStrut(10));
        topPanel.add(search_button);
        return topPanel;
    }

    JPanel createMainPanel() throws IOException {
        // ===== Composants ======
        // Search word
        JPanel top = new JPanel();
        main = new JPanel();
        search_label = new JLabel("Veuillez entrer un mot de recherche...");
        search_label.setFont((search_label.getFont()).deriveFont(Font.PLAIN, 22));

        // ===== Parametres ======
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        main.setLayout(new FlowLayout());
        main.setBorder(BorderFactory.createEmptyBorder(0, 20, 20, 20));

        // ===== Contenu ======
        top.add(search_label);
//        String[] links = {"https://www.gutenberg.org/cache/epub/66709/pg66709.cover.medium.jpg", "https://www.gutenberg.org/cache/epub/6130/pg6130.cover.medium.jpg", "https://www.gutenberg.org/cache/epub/64709/pg64709.cover.medium.jpg", "https://www.gutenberg.org/cache/epub/2852/pg2852.cover.medium.jpg", "https://www.gutenberg.org/cache/epub/2554/pg2554.cover.medium.jpg"};
//        for (String link : links) {
//            main.add(new ImageCard(link));
//        }

        mainPanel.add(top);
        mainPanel.add(main);
        return mainPanel;
    }

    public void show_results(ArrayList<Book> results) throws IOException {
//        for (Book book : results) {
//            main.add(new ImageCard(book));
//        }
        search_label.setText("Résultats pour " + search_field.getText());
        search_field.setText("");
    }
}
