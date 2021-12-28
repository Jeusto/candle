package models.entities;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import javax.swing.plaf.basic.BasicInternalFrameTitlePane;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class Category {
    private HashMap<Integer, Book> books;
    private String name;
    private Integer id;
    private Boolean is_local;

    public Category(String name, Integer id, Boolean is_local) {
        this.name = name;
        this.id = id;
        this.is_local = is_local;
        this.books = new HashMap<Integer, Book>();
    }

    public String get_name(){
        return name;
    }

    public HashMap<Integer, Book> get_books() {
        try {
            load_books();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return books;
    }

    private void load_books() throws IOException {
        // If the category is already loaded, return
        if (!books.isEmpty() && !is_local) {
            return;
        }
        // If the category is local, load the books from the local file
        if (is_local) {
            load_local_books();
        }
        else {
            load_remote_books();
        }
    }

    private void load_local_books() throws IOException {
        File appDir = new File(System.getProperty("user.home") + "/.candle-book-reader");

        File[] files = appDir.listFiles();
        for (File file : files) {
            if (file.isFile()) {
                String fileName = file.getName();
                String filePath = System.getProperty("user.home") + "/.candle-book-reader/" + fileName;
                if (fileName.endsWith(".txt")) {
                    String title = fileName.substring(0, fileName.length() - 4);
                    Integer id = -1;
                    try {
                            JSONParser parser = new JSONParser();
                            String jsonFile = filePath.replace(".txt", ".json");
                            JSONObject jsonObject = (JSONObject) parser.parse(new FileReader(jsonFile));
                            Long idlong = (Long) jsonObject.get("id");
                            id = idlong.intValue();
                        }   catch (Exception e) {
                            e.printStackTrace();
                        }
                            Book book = new Book(this.name, title, id, filePath, true);
                    books.put(id, book);
                }
            }
        }
        System.out.println(books.size());
    }

    private void load_remote_books() throws IOException {
        String categoryUrl = "https://www.gutenberg.org/ebooks/bookshelf/" + id.toString();
        while (true) {
            // Connect to the URL
            Document doc = Jsoup.connect(categoryUrl).get();

            // Get book titles
            Elements titles = doc.select(".booklink a.link .title");

            // Get book IDs
            Elements IDs = doc.select(".booklink > a");
            ArrayList<String> idList = new ArrayList<String>();
            for (Element ID : IDs) {
                String id = ID.attr("href").substring(8);
                idList.add(id);
            }

            // Get book links
            Elements links = doc.select(".booklink a.link");
            ArrayList<String> linkList = new ArrayList<String>();
            for (Element link : links) {
                linkList.add(link.attr("href"));
            }

            // Add everything to hashmap
            for (int i = 0; i < titles.size(); i++) {
                String title = titles.get(i).text();
                Book book = new Book(this.name, title, Integer.valueOf(idList.get(i)), null, false);
                books.put(Integer.valueOf(idList.get(i)), book);
            }

            // Check if a .links > [title] exists
            Elements links_titles = doc.select(".links > a[accesskey^=\"+\"]");
            if (links_titles.size() == 0) {
                return;
            }

            String title = links_titles.get(0).attr("href");
            categoryUrl = "https://www.gutenberg.org" + title;
        }
    }
}
