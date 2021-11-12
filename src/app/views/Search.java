package app.views;

import app.views.components.ImageCard;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class Search extends JPanel {
    public Search() throws IOException, InterruptedException {
        // Title
        JLabel title = new JLabel("Chercher un livre pour l'ajouter à votre bibliothéque");
        title.setFont(new Font("Inter", Font.BOLD, 18));
        title.setBorder(BorderFactory.createLineBorder(Color.yellow));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Search bar section
        JTextField input;
        input=new JTextField();
        input.setPreferredSize(new Dimension(400, 30));

        JButton button=new JButton("Rechercher");
        button.setPreferredSize(new Dimension(120, 30));

        JPanel search_section = new JPanel();
        search_section.add(input);
        search_section.add(button);

        // Search results section
        JPanel results = new JPanel();

        String[] links = {"https://www.gutenberg.org/cache/epub/66709/pg66709.cover.medium.jpg", "https://www.gutenberg.org/cache/epub/6130/pg6130.cover.medium.jpg", "https://www.gutenberg.org/cache/epub/64709/pg64709.cover.medium.jpg", "https://www.gutenberg.org/cache/epub/2852/pg2852.cover.medium.jpg", "https://www.gutenberg.org/cache/epub/2554/pg2554.cover.medium.jpg"};
        for (String link : links) {
            results.add(new ImageCard(link));
        }



        // Contenu
        add(title);
        add(search_section);
        add(results);

        // Reglages
        setLayout (new BoxLayout (this, BoxLayout.Y_AXIS));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));



        // //////////////////////////////
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create("https://gutendex.com/books/?search=death"))
                .setHeader("User-Agent", "Java 11 HttpClient Bot")
                .build();

        HttpClient httpClient = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_2)
                .build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        System.out.println(response.statusCode());
        System.out.println(response.body());

        JLabel labelGET = new JLabel(response.body());
        add(labelGET);
    }
}
