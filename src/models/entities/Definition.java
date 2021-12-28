package models.entities;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class Definition {
    private String query;
    private String search_result;
    private String api_url = "https://api.dictionaryapi.dev/api/v2/entries/en/";

    public Definition(String query) {
        this.query = query;
    }

    public static String get_definition(String query) {
        String definition = null;
        JSONObject data_obj = null;
        try {
            URL url = new URL("https://api.dictionaryapi.dev/api/v2/entries/en/" + query);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.connect();

            //Getting the response code
            int responsecode = conn.getResponseCode();

            if (responsecode != 200) {
                return null;
            } else {

                String inline = "";
                Scanner scanner = new Scanner(url.openStream());

                // Write all the JSON data into a string using a scanner
                while (scanner.hasNext()) {
                    inline += scanner.nextLine();
                }

                //Close the scanner
                scanner.close();

                // Using the JSON simple library parse the string into a json object
                JSONParser parse = new JSONParser();
                // get meaning of the word
                JSONArray site = (JSONArray) parse.parse(inline);
                data_obj = (JSONObject) parse.parse(site.get(0).toString());
                // Get the required object from the above created object
                JSONArray meanings = (JSONArray) data_obj.get("meanings");
                data_obj = (JSONObject) parse.parse(meanings.get(0).toString());
                meanings = (JSONArray) data_obj.get("definitions");
                data_obj = (JSONObject) parse.parse(meanings.get(0).toString());
                definition = data_obj.get("definition").toString();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return definition;
    }


    public String get_search_result() {
        return search_result;
    }
}
