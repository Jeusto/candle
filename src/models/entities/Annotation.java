package models.entities;

public class Annotation {
    private final Integer start;
    private final Integer end;
    private final String text;

    public Annotation(Integer start, Integer end, String text) {
        this.start = start;
        this.end = end;
        this.text = text;
    }

    public Integer get_start() {
        return start;
    }

    public Integer get_end() {
        return end;
    }

    public String get_text() {
        return text;
    }
}
