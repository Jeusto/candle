package models.entities;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.*;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Book {
    private String category;
    private String title;
    private String id;
    private Integer last_position;
    private HashMap<Integer, Annotation> annotations;
    private String image_url;
    Boolean is_downloaded;
    String path;

    public Book(String category, String title, String id, String path, Boolean is_downloaded) throws IOException {
        this.category = category;
        this.title = title;
        this.id = id;
        this.is_downloaded = is_downloaded;
        this.path = path;
        this.last_position = null;
    }

    public String download() throws IOException {
        if (path == null) {
            find_path();
        }
        if (is_downloaded) {
            return "Ce livre est déjà téléchargé. Merci de réeassayer avec un autre livre.";
        }

        // download the book from the url using utf8 encoding
        URL url = new URL(path);
        InputStream is = url.openStream();
        BufferedReader br = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));

        // create a new file
        path = System.getProperty("user.home") + "/.candle-book-reader/" + title + ".txt";
        File file = new File(path);
        file.createNewFile();

        // write the content to the file
        FileWriter fw = new FileWriter(file.getAbsoluteFile());
        BufferedWriter bw = new BufferedWriter(fw);
        String line;
        while ((line = br.readLine()) != null) {
            bw.write(line);
            bw.newLine();
        }


        // Create json object
        JSONObject json = new JSONObject();
        json.put("last_position", 0);
        json.put("annotations", new JSONArray());

        // Write to file
        File myObj = new File(System.getProperty("user.home") + "/.candle-book-reader/" + title + ".json");
        try {
            myObj.createNewFile();
        }
        catch (IOException e) {
            System.out.println("An error occurred.");
        }
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
    }

    public Book get_book() {
        return this;
    }

    public boolean is_downloaded() {
        return is_downloaded;
    }

    public Integer get_last_position() throws FileNotFoundException {
        if (last_position == null) {
            File info_file = new File(path.substring(0, path.length() - 4) + ".json");
            if (info_file.exists()) {
                last_position = Integer.parseInt(new Scanner(info_file).nextLine());
            }
        }
        return last_position;
    }

    public void update_last_position() {}

    public void add_annotation(String annotation, Integer start, Integer end) {
        String annotation_texte = annotation;
        if (annotation_texte == null) { annotation_texte = " ";}
        if (is_downloaded == false ) { return;}

        // Update json
        JSONObject json = new JSONObject();
        json.put("last_position", 0);

        JSONArray annotationsArray = new JSONArray();
        annotations = load_annotations();
        for (Annotation a : annotations.values()) {
            JSONObject annotation_json = new JSONObject();
            annotation_json.put("start", a.get_start());
            annotation_json.put("end", a.get_end());
            annotation_json.put("annotation", a.get_text() != null ? a.get_text() : " ");
            annotationsArray.add(annotation_json);
        }
        // add new annotation
        JSONObject annotation_json = new JSONObject();
        annotation_json.put("start", start);
        annotation_json.put("end", end);
        annotation_json.put("annotation", annotation_texte);
        annotationsArray.add(annotation_json);

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
        // Write to file
        try {
            FileWriter myWriter = new FileWriter(path.substring(0, path.length() - 4) + ".json", true);
            BufferedWriter writer = new BufferedWriter(myWriter);
            // Find the start and end of the annotation
            writer.close();
            myWriter.close();
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
