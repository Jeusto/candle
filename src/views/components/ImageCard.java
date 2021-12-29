package views.components;

import models.entities.Book;
import views.View;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.text.BadLocationException;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
public class ImageCard extends JPanel {
    View view;
    Book book;

    public ImageCard(Book book, View view) throws  IOException {
        this.book = book;
        this.view = view;

        URL url = new URL(book.get_image_url());
        BufferedImage imagebuff = ImageIO.read(url);
        Image image = imagebuff.getScaledInstance(170, 270,  java.awt.Image.SCALE_SMOOTH);

        JLabel label = new JLabel(new ImageIcon(image));
        JLabel title = new JLabel(book.get_title());
        title.setPreferredSize(new Dimension(170, 150));
        title.setMinimumSize(new Dimension(170, 150));
        title.setHorizontalAlignment(SwingConstants.CENTER);
        title.setVerticalAlignment(SwingConstants.CENTER);
        title.setHorizontalTextPosition(SwingConstants.CENTER);

        setCursor(new Cursor(Cursor.HAND_CURSOR));
        setToolTipText(book.get_title());
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(BorderFactory.createEmptyBorder(0, 15, 0, 15));

        add(label);
        add(title);
        // open book view on click
        addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                try {
                    view.notify_read_performed(book.get_bookshelf(),book.get_id());
                } catch (IOException | BadLocationException | InterruptedException e) {
                    new JOptionPane().showMessageDialog(null, "Il y a eu une erreur de l'affichage du livre", "Erreur",
                            JOptionPane.ERROR_MESSAGE);                }
            }
        });
    }
}
