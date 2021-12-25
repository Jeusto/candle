package app.models;

import java.util.EventListener;

public interface ChangeEventListener extends EventListener{
        public void stateChanged(ChangeEvent e);
}
