package app.views.components;

import app.views.View;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class AnnotationDialog extends JDialog implements Dialog {
    private final JPanel top_part;
    private final JPanel bottom_part;
    private JTextArea text_area;

    private final String bookshelf;
    private final Integer id;
    private final Integer start;
    private final Integer end;
    private final View view;
    private final BookView parent_panel;

    public AnnotationDialog(View view, int width, int height, BookView parent_panel, String bookshelf, Integer id,
                            Integer start, Integer end) throws IOException {
        this.view = view;
        this.bookshelf = bookshelf;
        this.id = id;
        this.start = start;
        this.end = end;
        this.parent_panel = parent_panel;

        setTitle("Créer une annotation");
        setSize(500, 300);
        setLocationRelativeTo(parent_panel);
        setResizable(false);
        setModal(true);
        setLayout(new BorderLayout());
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        top_part = create_top_panel(width, height);
        bottom_part = create_bottom_panel();

        add(top_part, BorderLayout.CENTER);
        add(bottom_part, BorderLayout.SOUTH);

        setVisible(true);
    }

    public JPanel create_top_panel(int width, int height) {
        JPanel top_panel = new JPanel();
        top_panel.setLayout(new BoxLayout(top_panel, BoxLayout.Y_AXIS));
        top_panel.setBorder(BorderFactory.createEmptyBorder(10, 20, 20, 20));
        top_panel.setMaximumSize(new Dimension(width, height));

        text_area = new JTextArea();
        text_area.setLineWrap(true);
        text_area.setWrapStyleWord(true);
        text_area.setText("");

        top_panel.add(text_area);

        return top_panel;
    }

    public JPanel create_bottom_panel() throws IOException {
        JPanel bottom_panel = new JPanel();
        bottom_panel.setBorder(BorderFactory.createEmptyBorder(0, 200, 20, 10));

        JButton confirm_btn = new JButton("Confirmer");
        Image confirm_icon = ImageIO.read(getClass().getResource("/assets/confirm.png"));
        confirm_btn.setIcon(new ImageIcon(confirm_icon));

        confirm_btn.addActionListener(e -> {
            try {
                view.notify_annotation_add_performed(bookshelf, id, text_area.getText(), start, end);
                parent_panel.show_highlights();
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Il y a eu une erreur dans l'ajout de l'annotation.", "Erreur",
                        JOptionPane.ERROR_MESSAGE);
            }
            dispose();
        });

        JButton cancel_btn = new JButton("Annuler");
        Image cancel_icon = ImageIO.read(getClass().getResource("/assets/cancel.png"));
        cancel_btn.setIcon(new ImageIcon(cancel_icon));
        cancel_btn.addActionListener(e -> dispose());

        bottom_panel.add(Box.createHorizontalGlue());
        bottom_panel.add(Box.createHorizontalGlue());
        bottom_panel.add(confirm_btn);
        bottom_panel.add(cancel_btn);

        return bottom_panel;
    }
}
