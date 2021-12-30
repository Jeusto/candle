package models.entities;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class Category {
    private final HashMap<Integer, Book> books;
    private final String name;
    private final Integer id;
    private final Boolean is_local;

    public Category(String name, Integer id, Boolean is_local) {
        this.name = name;
        this.id = id;
        this.is_local = is_local;
        this.books = new HashMap<>();
    }

    public HashMap<Integer, Book> get_books() throws IOException {
        load_books();
        return books;
    }

    private void load_books() throws IOException {
        if (!books.isEmpty() && !is_local) {
            return;
        }
        if (is_local) {
            load_local_books();
        } else {
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
                    Integer lastPosition = 0;
                    try {
                        JSONParser parser = new JSONParser();
                        String jsonFile = filePath.replace(".txt", ".json");
                        JSONObject jsonObject = (JSONObject) parser.parse(new FileReader(jsonFile));
                        Long idlong = (Long) jsonObject.get("id");
                        id = idlong.intValue();
                        Long lastPositionLong = (Long) jsonObject.get("last_position");
                        lastPosition = lastPositionLong.intValue();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    Book book = new Book(this.name, title, id, filePath, true, lastPosition);
                    books.put(id, book);
                }
            }
        }
    }

    private void load_remote_books() throws IOException {
        String bookshelfUrl = "https://www.gutenberg.org/ebooks/bookshelf/" + id.toString();

        while (true) {
            Document doc = Jsoup.connect(bookshelfUrl).get();

            Elements titles = doc.select(".booklink a.link .title");

            Elements IDs = doc.select(".booklink > a");
            ArrayList<String> idList = new ArrayList<String>();
            for (Element ID : IDs) {
                String id = ID.attr("href").substring(8);
                idList.add(id);
            }

            Elements links = doc.select(".booklink a.link");
            ArrayList<String> linkList = new ArrayList<String>();
            for (Element link : links) {
                linkList.add(link.attr("href"));
            }

            for (int i = 0; i < titles.size(); i++) {
                String title = titles.get(i).text();
                Integer id = Integer.valueOf(idList.get(i));
                String path = "https://www.gutenberg.org/cache/epub/" + id + "/pg" + id + ".txt";
                Book book = new Book(this.name, title, id, path, false, 0);
                books.put(Integer.valueOf(idList.get(i)), book);
            }

            Elements links_titles = doc.select(".links > a[accesskey^=\"+\"]");
            if (links_titles.size() == 0) {
                return;
            }

            String title = links_titles.get(0).attr("href");
            bookshelfUrl = "https://www.gutenberg.org" + title;
        }
    }
}
