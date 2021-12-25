package app.models;
import javax.swing.event.EventListenerList;

public class Model {
    // Settings
    // Books
    private EventListenerList listeners;

    public Model() {
        listeners = new EventListenerList();
        //
    }

    public Object getSettings() {
        return null;
    }

    public Object getLibrary() {
        return null;
    }


    public void changeState(Object settings, Object library) {
        return;
    }

    public int getTheme() {
        return 0;
    }
    public void setTheme() {fireChangeEvent();}

    public int getFontSize() {
        return 0;
    }
    public void setFontSize() {fireChangeEvent();}

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
            listener.stateChanged(event);
        }
    }

}
