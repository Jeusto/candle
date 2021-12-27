package app.models.entities;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class Book {
    private String category;
    private String title;
    private String id;
    Boolean is_downloaded;
    String path;

    public Book(String category, String title, String id, String path, Boolean is_downloaded) throws IOException {
        this.category = category;
        this.title = title;
        this.id = id;
        this.path = path;
        this.is_downloaded = is_downloaded;
    }

    public String download() throws IOException {
        if (path == null) {
            this.path = find_path();
        }
        if (is_downloaded) {
            return "Ce livre est déjà téléchargé";
        }
        // Download book to a file
        final Connection.Response response = Jsoup.connect(path).execute();
        final Document doc = response.parse();

        path = System.getProperty("user.home") + "/.candle-book-reader/" + title + ".html";
        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter( new FileWriter(path));
            writer.write(doc.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
        // Change path
        is_downloaded = true;
        return "1 livre a été téléchargé";
    }

    private String find_path() throws IOException {
            // Remote path
            String categoryUrl = "https://www.gutenberg.org" + id.toString();

            // Connect to the URL
            Document doc = Jsoup.connect(categoryUrl).get();

            // Get url of the book
            Elements titles = doc.select("tr.even > .icon_save.unpadded > .link");

            return "https://www.gutenberg.org" + titles.get(0).attr("href");
    }

    public String get_title() {
        return title;
    }

    public String get_path() {
        return path;
    }

    public Book get_book() throws IOException {
        if (path == null) {
            this.path = find_path();
        }
        return this;
    }

    public boolean is_downloaded() {
        return is_downloaded;
    }
}
