package models.entities;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;

public class Library {
    private final HashMap<String, Category> categories;

    public Library() throws MalformedURLException {
        categories = new HashMap<>();
        create_remote_bookshelves();
        create_local_bookshelves();
    }

    public HashMap<String, Category> get_categories() {
        return categories;
    }

    public void create_remote_bookshelves() throws MalformedURLException {
        // On récupère la liste des catégories disponibles en lisant la page des categories et en extraitant les liens
        URL u = new URL("https://www.gutenberg.org/ebooks/bookshelf/");

        try {
            URLConnection uc = u.openConnection();
            BufferedReader in = new BufferedReader(new InputStreamReader(uc.getInputStream()));
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                int index = inputLine.indexOf("/ebooks/bookshelf/");
                while (index != -1) {
                    int index2 = inputLine.indexOf("\" title=\"", index + 18);
                    if (index2 == -1) {
                        index = -1;
                        continue;
                    }
                    Integer id = Integer.parseInt(inputLine.substring(index + 18, index2));
                    index = inputLine.indexOf("title=\"", index2);
                    while (index != -1) {
                        index2 = inputLine.indexOf("\"", index + 7);
                        String title = inputLine.substring(index + 7, index2);
                        Category bookshelf = new Category(title, id, false);
                        categories.put(title, bookshelf);
                        index = inputLine.indexOf("/ebooks/bookshelf/", index2);
                    }
                }
            }
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void create_local_bookshelves() {
        // Creer le repertoire de l'application si il n'existe pas
        File app_directory = new File(System.getProperty("user.home") + "/.candle-book-reader");
        if (!app_directory.exists()) {
            app_directory.mkdir();
        }

        // Charger les livres disponibles localement
        Category localCategory = new Category("Livres téléchargés", -1, true);
        categories.put("Livres téléchargés", localCategory);
    }
}
