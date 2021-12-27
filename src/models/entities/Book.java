package models.entities;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

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
        this.is_downloaded = is_downloaded;
        this.path = path;
    }

    public String download() throws IOException {
        if (path == null) {
            find_path();
        }
        if (is_downloaded) {
            return "Ce livre est déjà téléchargé. Merci de réeassayer avec un autre livre.";
        }
        // Download book to a file
        InputStream in = new URL(path).openStream();
        path = System.getProperty("user.home") + "/.candle-book-reader/" + title + ".txt";
        Files.copy(in, Paths.get(path), StandardCopyOption.REPLACE_EXISTING);

        is_downloaded = true;
        return "Le livre a été téléchargé.";
    }

    public String delete() throws IOException {
        if (is_downloaded == false) {
            return "Ce livre n'est pas téléchargé.";
        }

        path = System.getProperty("user.home") + "/.candle-book-reader/" + title + ".txt";
        Files.delete(Paths.get(path));

        is_downloaded = false;
        return "Le livre a bien été supprimé.";
    }

    public String get_title() {
        return title;
    }

    public String get_path() throws IOException {
        if (path == null) {
            find_path();
        }
        return path;
    }

    private void find_path() throws IOException {
        // Remote path
        String remote_path = "https://gutenberg.org/files/" + id;

        Document doc = Jsoup.connect(remote_path).get();

        Elements titles = doc.select("td > a");

        path = remote_path + "/" + titles.get(1).attr("href");
        System.out.println(path);
    }

    public Book get_book() throws IOException {
        return this;
    }

    public boolean is_downloaded() {
        return is_downloaded;
    }
}
