package views.tabs;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class HomeTab extends JPanel {
    JPanel main_panel;
    JLabel welcome_text;

    public HomeTab() throws IOException {
        JPanel top = new JPanel();
        welcome_text = new JLabel("Bienvenue sur Candle. Une application pour lire des livres et bien plus...");
        welcome_text.setFont((welcome_text.getFont()).deriveFont(Font.PLAIN, 24));
        top.add(welcome_text);
        top.setPreferredSize(new Dimension(Integer.MAX_VALUE, 50));
        top.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));

        JPanel bottom = new JPanel();
        BufferedImage picture = ImageIO.read(getClass().getResource("/assets/accueil.png"));
        JLabel picture_label = new JLabel(new ImageIcon(picture));
        bottom.add(picture_label);
        bottom.setPreferredSize(new Dimension(Integer.MAX_VALUE, picture.getHeight()));
        bottom.setMaximumSize(new Dimension(Integer.MAX_VALUE, picture.getHeight()));

        main_panel = new JPanel();
        main_panel.setLayout(new BoxLayout(main_panel, BoxLayout.Y_AXIS));
        main_panel.setBorder(new EmptyBorder(20, 10, 0, 10));
        main_panel.add(top);
        main_panel.add(bottom);

        setLayout(new BorderLayout());
        add(main_panel, BorderLayout.CENTER);
    }
}