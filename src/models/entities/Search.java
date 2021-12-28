package models.entities;

import models.Model;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;

public class Search {
    private String query;
    private String search_result;
    private String api_url = "https://api.dictionaryapi.dev/api/v2/entries/en/";

    public Search(String query) {
        this.query = query;
    }

    public static ArrayList<Book> get_search(String query, Model model) {
        String definition = null;
        JSONObject data_obj = null;
        ArrayList<Book> results = new ArrayList<>();
        try {
            // replace spaces in the query with %20
            URL url = new URL("https://gutendex.com/books/?search=" + query.replace(" ", "%20"));

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.connect();

            // Getting the response code
            int responsecode = conn.getResponseCode();
            if (responsecode != 200) {
                return null;
            }
            String inline = "";
            Scanner scanner = new Scanner(url.openStream());

            // Write all the JSON data into a string using a scanner
            while (scanner.hasNext()) {
                inline += scanner.nextLine();
            }

            // Close the scanner
            scanner.close();

            // Using the JSON simple library parse the string into a json object
            JSONParser parse = new JSONParser();
            JSONObject site = (JSONObject) parse.parse(inline);
            JSONArray results_array = (JSONArray) site.get("results");
            for (int i = 0; i < results_array.size(); i++) {
                if (i == 8) {
                    break;
                }
                JSONObject book_obj = (JSONObject) results_array.get(i);
                JSONArray bookshelves = (JSONArray) book_obj.get("bookshelves");
                if (bookshelves.size() > 0) {
                    String title = (String) book_obj.get("title");
                    String bookshelf = (String) bookshelves.get(0);
                    System.out.println(title);
                    System.out.println(bookshelf);

                    Book book = model.get_book(bookshelf, title);

                    if (book != null) {
                        book.get_image_url();
                        results.add(book);
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return results;
    }


    public String get_search() {
        return search_result;
    }
}
