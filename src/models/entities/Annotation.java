package models.entities;

public class Annotation {
    private Integer start;
    private Integer end;
    private String text;

    public Annotation(Integer start, Integer end, String text) {
        this.start = start;
        this.end = end;
        this.text = text;
    }

    public String get_text() {
        return text;
    }

    public Integer get_start() {
        return start;
    }

    public Integer get_end() {
        return end;
    }
}
