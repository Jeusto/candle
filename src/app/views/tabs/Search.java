package app.views.tabs;

import app.views.View;
import app.views.components.ImageCard;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class Search extends JPanel {
    private JPanel topPanel;
    private JPanel mainPanel;
    private View view;
    private JButton search_button;
    private JTextField search_field;
    private JLabel search_label;

    public Search(View view) throws IOException {
        this.view = view;

        // ===== Content components ======
        topPanel = createTopPanel();
        mainPanel = createMainPanel();

        // ===== Settings ======
        setLayout(new BorderLayout());

        // ===== Content ======
        add(topPanel, BorderLayout.NORTH);
        add(mainPanel, BorderLayout.CENTER);
    }

    JPanel createTopPanel () throws IOException {
        // ===== Content components ======
        // Search field
        search_field = new JTextField();
        search_field.setPreferredSize(new Dimension(200, 30));

        // Search button
        search_button = new JButton("Faire une recherche");
        Image searchIcon = ImageIO.read(getClass().getResource("/app/assets/search.png"));
        search_button.setIcon(new ImageIcon(searchIcon));
        search_button.addActionListener(e -> {
            System.out.println("Searching for " + search_field.getText());
            try {
                view.notify_search_performed(search_field.getText());
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });

        // ===== Settings ======
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BoxLayout(topPanel, FlowLayout.RIGHT));
        topPanel.setBorder(BorderFactory.createEmptyBorder(20, 150, 20, 150));

        // ===== Content ======
        topPanel.add(search_field);
        topPanel.add(Box.createHorizontalStrut(10));
        topPanel.add(search_button);
        return topPanel;
    }

    JPanel createMainPanel () throws IOException {
        // ===== Content components ======
        // Search word
        search_label = new JLabel("Le mot de recherche sera ici..");
        search_label.setFont((search_label.getFont()).deriveFont(Font.PLAIN, 22));

        // ===== Settings ======
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new FlowLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(0, 20, 20, 20));

        // ===== Content ======
        mainPanel.add(search_label);
        String[] links = {"https://www.gutenberg.org/cache/epub/66709/pg66709.cover.medium.jpg", "https://www.gutenberg.org/cache/epub/6130/pg6130.cover.medium.jpg", "https://www.gutenberg.org/cache/epub/64709/pg64709.cover.medium.jpg", "https://www.gutenberg.org/cache/epub/2852/pg2852.cover.medium.jpg", "https://www.gutenberg.org/cache/epub/2554/pg2554.cover.medium.jpg"};
        for (String link : links) {
            mainPanel.add(new ImageCard(link));
        }

        return mainPanel;
    }

    public void show_results(String results) {
        System.out.println(results);
        search_field.setText("");
        search_label.setText(results);
    }
}
