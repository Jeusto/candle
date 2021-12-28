package views.components;

import javax.swing.*;
import java.io.IOException;

public interface Dialog {
    JPanel create_top_panel(int width, int height) throws IOException;

    JPanel create_bottom_panel() throws IOException;
}
