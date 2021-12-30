package models.utilities;

import models.Model;
import models.entities.Book;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;

public class Search {
    private final String query;
    private String result;
    private static String api_url = "https://gutendex.com/books/?search=";

    public Search(String query) {
        this.query = query;
    }

    public static ArrayList<Book> get_search(String query, Model model) {
        ArrayList<Book> results = new ArrayList<>();
        try {
            URL url = new URL(api_url + query.replace(" ", "%20"));
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();

            int response_code = connection.getResponseCode();
            if (response_code != 200) {
                return null;
            }

            String inline = "";
            Scanner scanner = new Scanner(url.openStream());

            while (scanner.hasNext()) {
                inline += scanner.nextLine();
            }
            scanner.close();

            JSONParser parse = new JSONParser();
            JSONObject site = (JSONObject) parse.parse(inline);
            JSONArray results_array = (JSONArray) site.get("results");
            for (int i = 0; i < results_array.size() && i < 5; i++) {
                JSONObject book_obj = (JSONObject) results_array.get(i);
                JSONArray bookshelves = (JSONArray) book_obj.get("bookshelves");
                if (bookshelves.size() == 0) {
                    continue;
                }
                String bookshelf = (String) bookshelves.get(0);
                Long id = (Long) book_obj.get("id");

                Book book = model.get_book(bookshelf, Math.toIntExact(id));
                if (book != null) {
                    book.get_image_url();
                    results.add(book);
                }
            }
        } catch (Exception e) {
            return null;
        }

        return results;
    }
}
