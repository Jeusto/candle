package models.utilities;


import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class Definition {
    private final String word;
    private final String definition;
    private static String api_url = "https://api.dictionaryapi.dev/api/v2/entries/en/";

    public Definition(String word) {
        this.word = word;
        this.definition = define_word(word);
    }

    private static String define_word(String word) {
        String definition;
        JSONObject data_obj;

        try {
            URL url = new URL(api_url + word);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();

            if (connection.getResponseCode() != 200) {
                return null;
            }

            String inline = "";
            Scanner scanner = new Scanner(url.openStream());

            while (scanner.hasNext()) {
                inline += scanner.nextLine();
            }
            scanner.close();

            JSONParser parse = new JSONParser();
            JSONArray site = (JSONArray) parse.parse(inline);
            data_obj = (JSONObject) parse.parse(site.get(0).toString());
            JSONArray meanings = (JSONArray) data_obj.get("meanings");
            data_obj = (JSONObject) parse.parse(meanings.get(0).toString());
            meanings = (JSONArray) data_obj.get("definitions");
            data_obj = (JSONObject) parse.parse(meanings.get(0).toString());
            definition = data_obj.get("definition").toString();
        } catch (Exception e) {
            return null;
        }

        return definition;
    }

    public String get_definition() {
        return definition;
    }
}
