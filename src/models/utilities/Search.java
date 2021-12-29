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
    private final String api_url = "https://api.dictionaryapi.dev/api/v2/entries/en/";

    public Search(String query) {
        this.query = query;
    }

    public static ArrayList<Book> get_search(String query, Model model) {
        String definition = null;
        JSONObject data_obj = null;
        ArrayList<Book> results = new ArrayList<>();
        try {
            // On se connecte à l'API
            URL url = new URL("https://gutendex.com/books/?search=" + query.replace(" ", "%20"));
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.connect();

            // Si la requête n'a pas abouti, on renvoie null
            int responsecode = conn.getResponseCode();
            if (responsecode != 200) {
                return null;
            }

            // La requete a reussi, on recupere la reponse JSON dans un String
            String inline = "";
            Scanner scanner = new Scanner(url.openStream());

            while (scanner.hasNext()) {
                inline += scanner.nextLine();
            }
            scanner.close();

            // On traite l'objet JSON pour avoir le resultat
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
            //
        }

        return results;
    }


    public String get_search() {
        return result;
    }
}
