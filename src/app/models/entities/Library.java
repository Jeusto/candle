package app.models.entities;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;

public class Library {
    private HashMap<String, Category> categories;

    public Library() throws MalformedURLException {
        categories = new HashMap<>();
        create_remote_bookshelves();
        create_local_bookshelves();
    }

    public HashMap<String, Category> get_categories() {
        return categories;
    }

    public void create_remote_bookshelves() throws MalformedURLException {
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
                        Category category = new Category(title, id, false);
                        categories.put(title, category);
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
        // Create app directory if it doesn't exist
        File appDir = new File(System.getProperty("user.home") + "/.candle-book-reader");
        if (!appDir.exists()) {
            appDir.mkdir();
        }
        // Create reference file if it doesn't exist
        File referenceFile = new File(System.getProperty("user.home") + "/.candle-book-reader/reference.txt");
        if (!referenceFile.exists()) {
            try {
                referenceFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // Load local books
        Category localCategory = new Category("Local", -1, true);
        categories.put("Livres téléchargés", localCategory);
    }

    public Book search_book(String category_name, String book_title) {
        return categories.get(category_name).get_books().get(book_title);
    }
}
