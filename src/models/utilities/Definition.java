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
    private final String api_url = "https://api.dictionaryapi.dev/api/v2/entries/en/";

    public Definition(String word) {
        this.word = word;
        this.definition = define_word(word);
    }

    private static String define_word(String word) {
        String definition = null;
        JSONObject data_obj = null;

        try {
            // On se connecte à l'API
            URL url = new URL("https://api.dictionaryapi.dev/api/v2/entries/en/" + word);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();

            // Si la requête n'a pas abouti, on renvoie null
            if (connection.getResponseCode() != 200) {
                return null;
            }

            // La requete a reussi, on recupere la reponse JSON dans un String
            String inline = "";
            Scanner scanner = new Scanner(url.openStream());

            while (scanner.hasNext()) {
                inline += scanner.nextLine();
            }
            scanner.close();

            // On traite l'objet JSON pour avoir la definition du mot
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
