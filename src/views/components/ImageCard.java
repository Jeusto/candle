package views.components;

import models.entities.Book;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

public class ImageCard extends JPanel {
    public ImageCard(Book book) throws IOException {
        URL url = new URL(book.get_image_url());
        BufferedImage imagebuff = ImageIO.read(url);
        Image image = imagebuff.getScaledInstance(170, 270,  java.awt.Image.SCALE_SMOOTH);

        JLabel label = new JLabel(new ImageIcon(image));
        setCursor(new Cursor(Cursor.HAND_CURSOR));
        setToolTipText(book.get_title());
    }
}
