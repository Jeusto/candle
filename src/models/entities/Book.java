package models.entities;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import javax.swing.plaf.synth.SynthTextAreaUI;
import java.io.*;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Scanner;

public class Book {
    private final String category;
    private final String title;
    private final Integer id;

    private HashMap<Integer, Annotation> annotations;
    private Integer last_position;
    private Boolean is_downloaded;
    private String path;
    private String image_url;

    public Book(String category, String title, Integer id, String path, Boolean is_downloaded, Integer last_position) throws IOException {
        this.category = category;
        this.title = title;
        this.id = id;
        this.is_downloaded = is_downloaded;
        this.path = path;
        this.last_position = last_position;
    }

    public String download() throws IOException {
        try {
            Thread.sleep(2000);
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (is_downloaded) {
            return "Ce livre est déjà téléchargé. Merci de réeassayer avec un autre livre.";
        }

        // On télécharge le fichier depuis l'url
        URL url = new URL(path);
        InputStream is = url.openStream();
        BufferedReader br = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));

        // On crée le fichier
        path = System.getProperty("user.home") + "/.candle-book-reader/" + title + ".txt";
        File file = new File(path);
        file.createNewFile();

        // On écrit le livre dans le fichier
        FileWriter fw = new FileWriter(file.getAbsoluteFile());
        BufferedWriter bw = new BufferedWriter(fw);
        String line;
        while ((line = br.readLine()) != null) {
            bw.write(line);
            bw.newLine();
        }

        // On crée l'objet json qui va contenir les informations sur le livre et les annotations
        JSONObject json = new JSONObject();
        json.put("last_position", 0);
        json.put("id", this.id);
        json.put("annotations", new JSONArray());

        // On écrit le json dans un autre fichier
        File myObj = new File(System.getProperty("user.home") + "/.candle-book-reader/" + title + ".json");
        myObj.createNewFile();
        FileWriter fileWriter = new FileWriter(path.substring(0, path.length() - 4) + ".json");

        fileWriter.write(json.toJSONString());
        fileWriter.close();

        is_downloaded = true;

        return "Le livre a été téléchargé.";
    }

    public String delete() throws IOException {
        if (is_downloaded == false) {
            return "Ce livre n'est pas téléchargé.";
        }

        path = System.getProperty("user.home") + "/.candle-book-reader/" + title + ".txt";
        Files.delete(Paths.get(path));

        path = System.getProperty("user.home") + "/.candle-book-reader/" + title + ".json";
        Files.delete(Paths.get(path));

        is_downloaded = false;
        return "Le livre a bien été supprimé.";
    }

    public String get_title() {
        return title;
    }

    public Integer get_id() {
        return id;
    }

    public String get_path() throws IOException {
        return path;
    }

    public boolean is_downloaded() {
        return is_downloaded;
    }

    public Integer get_last_position() {
        return last_position;
    }

    public void set_last_position(Integer last_position) {
        if (is_downloaded == false ) { return;}

        // Read json file and get last position
        JSONObject json = new JSONObject();
        JSONArray annotationsArray = new JSONArray();

        // Lire le fichier json et ajouter les elements qui y sont dans un nouveau objet json
        try {
            JSONParser parser = new JSONParser();
            Object obj = parser.parse(new FileReader(path.substring(0, path.length() - 4) + ".json"));
            JSONObject jsonObject = (JSONObject) obj;
            json.put("id", jsonObject.get("id"));
            annotationsArray = (JSONArray) jsonObject.get("annotations");
        }  catch (Exception e) {
            e.printStackTrace();
        }

        // Ajouter la nouvelle position dans le json
        json.put("last_position", last_position);
        json.put("annotations", annotationsArray);

        try {
            FileWriter fileWriter = new FileWriter(path.substring(0, path.length() - 4) + ".json");
            fileWriter.write(json.toJSONString());
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        this.last_position = last_position;
    }

    public void add_annotation(String annotation, Integer start, Integer end) {
        String annotation_texte = annotation;
        if (annotation_texte == null) { annotation_texte = " ";}
        if (is_downloaded == false ) { return;}


        // read json file and get last position
        JSONObject json = new JSONObject();
        JSONArray annotationsArray = new JSONArray();

        // Lire le fichier json et ajouter les elements qui y sont dans un nouveau objet json
        try {
            JSONParser parser = new JSONParser();
            Object obj = parser.parse(new FileReader(path.substring(0, path.length() - 4) + ".json"));
            JSONObject jsonObject = (JSONObject) obj;
            json.put("last_position", jsonObject.get("last_position"));
            json.put("id", jsonObject.get("id"));
            annotationsArray = (JSONArray) jsonObject.get("annotations");
        }  catch (Exception e) {
            e.printStackTrace();
        }


        // Ajouter la nouvelle annotation au tableau d'annotations json
        JSONObject annotation_json = new JSONObject();
        annotation_json.put("start", start);
        annotation_json.put("end", end);
        annotation_json.put("annotation", annotation_texte);
        annotationsArray.add(annotation_json);

        // Ajouter le tableau d'annotations au json et ecrire dans le fichier
        json.put("annotations", annotationsArray);


        try {
            FileWriter fileWriter = new FileWriter(path.substring(0, path.length() - 4) + ".json");
            fileWriter.write(json.toJSONString());
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Annotation new_annotation = new Annotation(start, end, annotation);
        annotations = load_annotations();
        annotations.put(start, new_annotation);
    }

    public void delete_annotation(String annotation, Integer start, Integer end) {
        String annotation_texte = annotation;
        if (is_downloaded == false ) { return; }

        JSONObject json = new JSONObject();
        JSONArray annotationsArray = new JSONArray();

        // Lire le fichier json et ajouter les elements qui y sont dans un nouveau objet json
        try {
            JSONParser parser = new JSONParser();
            Object obj = parser.parse(new FileReader(path.substring(0, path.length() - 4) + ".json"));
            JSONObject jsonObject = (JSONObject) obj;
            json.put("last_position", jsonObject.get("last_position"));
            json.put("id", jsonObject.get("id"));
            annotationsArray = (JSONArray) jsonObject.get("annotations");
        }  catch (Exception e) {
            e.printStackTrace();
        }

        // Supprimer l'annotation du tableau d'annotations json
        Long start_long = Long.valueOf(start);
        for (int i = 0; i < annotationsArray.size(); i++) {
            JSONObject annotation_json = (JSONObject) annotationsArray.get(i);
            if (annotation_json.get("start").equals(start_long)) {
                annotationsArray.remove(i);
            }
        }

        // Ajouter le tableau d'annotations au json et ecrire dans le fichier
        json.put("annotations", annotationsArray);

        try {
            FileWriter fileWriter = new FileWriter(path.substring(0, path.length() - 4) + ".json");
            fileWriter.write(json.toJSONString());
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        annotations = load_annotations();
        annotations.remove(start);
    }

    public HashMap<Integer, Annotation> load_annotations(){
        if (annotations == null) {
            annotations = new HashMap<>();
        }
        File info_file = new File(path.substring(0, path.length() - 4) + ".json");

        if (info_file.exists()) {
            JSONParser parser = new JSONParser();
            try {
                JSONObject jsonObject = (JSONObject) parser.parse(new FileReader(info_file));

                this.last_position = (Integer) jsonObject.get("last_start");

                JSONArray annotationsArray = (JSONArray) jsonObject.get("annotations");
                for (Object annotation : annotationsArray) {
                    JSONObject annotation_object = (JSONObject) annotation;
                    Long start = (Long) annotation_object.get("start");
                    Long end = (Long) annotation_object.get("end");
                    String text = (String) annotation_object.get("text");
                    this.annotations.put(start.intValue(), new Annotation(start.intValue(), end.intValue(), text));
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return annotations;
    }

    public String get_category() {
        return category;
    }

    private void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public String get_image_url() {
        this.image_url = "https://www.gutenberg.org/cache/epub/" + id + "/pg" + id + ".cover.medium.jpg";
        return image_url;
    }


}
