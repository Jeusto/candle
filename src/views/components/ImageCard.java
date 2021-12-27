package views.components;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

public class ImageCard extends JLabel {
    public ImageCard(String path) throws IOException {
        URL url = new URL(path);
        BufferedImage imagebuff = ImageIO.read(url);
        Image image = imagebuff.getScaledInstance(170, 270,  java.awt.Image.SCALE_SMOOTH);
        setIcon(new ImageIcon(image));
        setCursor(new Cursor(Cursor.HAND_CURSOR));
        setToolTipText(path);
    }
}
