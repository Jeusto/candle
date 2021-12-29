package views.tabs;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class HomeTab extends JPanel {

    public HomeTab() throws IOException {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(new EmptyBorder(20, 10, 0, 10));

        JLabel text = new JLabel("Bienvenue sur Candle. Une application pour lire des livres et bien plus...");
        text.setFont((text.getFont()).deriveFont(Font.PLAIN, 24));
        JPanel top = new JPanel();
        top.add(text);
        top.setPreferredSize(new Dimension(Integer.MAX_VALUE, 50));
        top.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));

        JPanel bottom = new JPanel();
        BufferedImage picture = ImageIO.read(getClass().getResource("/assets/accueil.png"));
        JLabel picture_label = new JLabel(new ImageIcon(picture));
        bottom.add(picture_label);
        bottom.setPreferredSize(new Dimension(Integer.MAX_VALUE, picture.getHeight()));
        bottom.setMaximumSize(new Dimension(Integer.MAX_VALUE, picture.getHeight()));

        // Contenu =====================================================================================================
        setLayout(new BorderLayout());
        panel.add(top);
        panel.add(bottom);
        add(panel, BorderLayout.CENTER);
    }
}