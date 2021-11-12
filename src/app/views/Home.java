package app.views;

import app.views.components.ImageCard;

import javax.swing.*;
import java.awt.*;

public class Home extends JPanel {

    public Home() throws Exception {
        // Initialiser composants

        // Partie statistiques
        JLabel title_stats = new JLabel("Reprendre la lecture là ou vous vous êtes arrêté :");
        title_stats.setFont(new Font("Inter", Font.PLAIN, 18));
        title_stats.setBorder(BorderFactory.createLineBorder(Color.yellow));
        title_stats.setAlignmentX(Component.CENTER_ALIGNMENT);


        JPanel stats = new JPanel();
        stats.setBorder(BorderFactory.createLineBorder(Color.red));

        // Partie livres populaires
        JLabel title_popular = new JLabel("Les livres les plus populaires");
        title_popular.setFont(new Font("Inter", Font.PLAIN, 18));
        title_popular.setBorder(BorderFactory.createLineBorder(Color.yellow));
        title_popular.setAlignmentX(Component.CENTER_ALIGNMENT);

        String[] links = {"https://www.gutenberg.org/cache/epub/66709/pg66709.cover.medium.jpg", "https://www.gutenberg.org/cache/epub/6130/pg6130.cover.medium.jpg", "https://www.gutenberg.org/cache/epub/64709/pg64709.cover.medium.jpg", "https://www.gutenberg.org/cache/epub/2852/pg2852.cover.medium.jpg", "https://www.gutenberg.org/cache/epub/2554/pg2554.cover.medium.jpg"};
        JPanel popular = new JPanel();
        popular.setLayout(new FlowLayout());
        popular.setBorder(BorderFactory.createLineBorder(Color.red));
        for (String link : links) {
            popular.add(new ImageCard(link));
        }

        // Contenu
        add(title_stats);
        add(stats);
        add(title_popular);
        add(popular);

        // Reglages
        setLayout (new BoxLayout (this, BoxLayout.Y_AXIS));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    }
}
