package app.models;

import javax.swing.event.EventListenerList;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.List;

public class Model {
    private HashMap<String, Integer> categoriesList;
    private EventListenerList listeners;

    public Model() {
        listeners = new EventListenerList();
        //
    }

    public void addChangeListener(ChangeEventListener listener) {
        listeners.add(ChangeEventListener.class, listener);
    }

    public void removeChangeListener(ChangeEventListener listener) {
        listeners.remove(ChangeEventListener.class, listener);
    }

    private void fireChangeEvent() {
        ChangeEvent event = new ChangeEvent(this);
        ChangeEventListener[] listeners = this.listeners.getListeners(ChangeEventListener.class);

        for (ChangeEventListener listener : listeners) {
            listener.modelStateChanged(event);
        }
    }

    public void changeModelState() {
        return;
    }

    public HashMap<String, Integer> getCategories() throws MalformedURLException {
        this.categoriesList = new HashMap<>();
        URL u =  new URL("https://www.gutenberg.org/ebooks/bookshelf/");

        try {
            URLConnection uc = u.openConnection();
            BufferedReader in = new BufferedReader(new InputStreamReader(uc.getInputStream()));
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                int index = inputLine.indexOf("/ebooks/bookshelf/");
                while (index != -1) {
                    int index2 = inputLine.indexOf("\" title=\"", index + 18);
                    if (index2 == -1) { index=-1; continue; }
                    Integer id = Integer.parseInt(inputLine.substring(index + 18, index2));
                    index = inputLine.indexOf("title=\"", index2);
                    while (index != -1) {
                        index2 = inputLine.indexOf("\"", index + 7);
                        String title = inputLine.substring(index + 7, index2);
                        categoriesList.put(title, id);
                        index = inputLine.indexOf("/ebooks/bookshelf/", index2);
                    }
                }

            }
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return this.categoriesList;
    }

    public HashMap<String, Integer> getCategoryBooks() throws MalformedURLException {
        HashMap<String, List<Object>> currentCategoryBooks;
        URL u =  new URL("https://www.gutenberg.org/ebooks/bookshelf/");

        try {
            URLConnection uc = u.openConnection();
            BufferedReader in = new BufferedReader(new InputStreamReader(uc.getInputStream()));
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                int index = inputLine.indexOf("/ebooks/bookshelf/");
                while (index != -1) {
                    int index2 = inputLine.indexOf("\" title=\"", index + 18);
                    if (index2 == -1) { index=-1; continue; }
                    Integer id = Integer.parseInt(inputLine.substring(index + 18, index2));
                    index = inputLine.indexOf("title=\"", index2);
                    while (index != -1) {
                        index2 = inputLine.indexOf("\"", index + 7);
                        String title = inputLine.substring(index + 7, index2);
                        categoriesList.put(title, id);
                        index = inputLine.indexOf("/ebooks/bookshelf/", index2);
                    }
                }

            }
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return this.categoriesList;
    }


    public Object getLocalBooks() {
        return null;
    }

    public Object getUserSettings() {
        return null;
    }
}
