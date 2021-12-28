package models.entities;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.Scanner;

public class Book {
    private String category;
    private String title;
    private String id;
    private Integer last_position;
    private HashMap<Integer, Annotation> annotations;
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

        // Telecharger le livre dans un fichier texte
        InputStream in = new URL(path).openStream();
        path = System.getProperty("user.home") + "/.candle-book-reader/" + title + ".txt";
        Files.copy(in, Paths.get(path), StandardCopyOption.REPLACE_EXISTING);

        // Creer un deuxieme fichier pour stocker les annotations et la derniere position de lecture
        File myObj = new File(System.getProperty("user.home") + "/.candle-book-reader/" + title + ".info");
        // Write 0 to the file
        try {
            myObj.createNewFile();
            FileWriter myWriter = new FileWriter(myObj);
            BufferedWriter writer = new BufferedWriter(myWriter);
            System.out.println("Writing to file");
            writer.write("0");
            writer.close();
            myWriter.close();
        }
        catch (IOException e) {
            System.out.println("An error occurred.");
        }
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
        System.out.println(path);
    }

    public Book get_book() {
        return this;
    }

    public boolean is_downloaded() {
        return is_downloaded;
    }

    public Integer get_last_position() throws FileNotFoundException {
        if (last_position == null) {
            File info_file = new File(path.substring(0, path.length() - 4) + ".info");
            if (info_file.exists()) {
                last_position = Integer.parseInt(new Scanner(info_file).nextLine());
            }
        }
        return last_position;
    }

    public void update_last_position() {}

    public void add_annotation(String annotation, Integer start, Integer end) {
        System.out.println("Annotation added");
        System.out.println(this);
        System.out.println(annotation);
        System.out.println(start);
        System.out.println(end);
        System.out.println(annotations == null);

        // Write to file
        try {
            FileWriter myWriter = new FileWriter(path.substring(0, path.length() - 4) + ".info", true);
            BufferedWriter writer = new BufferedWriter(myWriter);
            System.out.println("Writing to file");
            writer.write(":::\n" + start + "\n" + end + "\n" + annotation + "\n");
            writer.close();
            myWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Annotation new_annotation = new Annotation(start, end, annotation);
        annotations = load_annotations();
        annotations.put(start, new_annotation);
    }

    public void delete_annotation(String annotation, Integer start, Integer end) {
        System.out.println("Annotation deleted");
        System.out.println(this);
        System.out.println(annotation);
        System.out.println(start);
        System.out.println(end);
        System.out.println(annotations == null);

        // Write to file
        try {
            FileWriter myWriter = new FileWriter(path.substring(0, path.length() - 4) + ".info", true);
            BufferedWriter writer = new BufferedWriter(myWriter);
            System.out.println("Removing from file");
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
        System.out.println("Annotations loaded");
        System.out.println(this);
        if (annotations == null) {
            annotations = new HashMap<>();
        }
        File info_file = new File(path.substring(0, path.length() - 4) + ".info");

        Scanner scanner = null;
        try {
            scanner = new Scanner(info_file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        if (info_file.exists()) {
            last_position = Integer.parseInt(scanner.nextLine());
            System.out.println(last_position);
            // Read 1 line as int and then read as strin until ":::" is found
            while (scanner.hasNextLine()) {
                // Read 2 line as int
                int start = Integer.parseInt(scanner.nextLine());
                int end = Integer.parseInt(scanner.nextLine());

                // Read as string until ":::" is found
                // Store everythin between two ":::" as a string
                String annotation = "";
                while (scanner.hasNextLine()) {
                    String line = scanner.nextLine();
                    if (line.equals(":::")) {
                        break;
                    }
                    annotation += line + "\n";
                }
                annotations.put(start, new Annotation(start, end, annotation));
            }
        }

        System.out.println("load_annotations() retourne");
        System.out.println(annotations == null);
        return annotations;
    }

    public String get_category() {
        return category;
    }

}
