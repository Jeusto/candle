package models.entities;

import java.util.ArrayList;
import java.util.HashMap;

public class Search {
    private String query;
    private String search_result;

    public Search(String query) {
        this.query = query;
        this.search_result = "a faire";
    }

    public String get_search_result() {
        return search_result;
    }
}
