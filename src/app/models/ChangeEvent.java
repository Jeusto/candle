package app.models;

import java.util.EventObject;

public class ChangeEvent extends EventObject {

    public ChangeEvent(Object source) {
        super(source);
    }

    public Object getLibrary() {
        return null;
    }

    public Object getSettings() {
        return null;
    }
}
