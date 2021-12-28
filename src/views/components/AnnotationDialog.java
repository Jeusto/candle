package views.components;

import views.View;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class AnnotationDialog extends JDialog implements Dialog {
    private JPanel topPanel;
    private JPanel buttonPanel;
    private JTextArea text_area;
    private String category;
    private Integer id;
    private Integer start;
    private Integer end;
    private BookView parentPanel;
    View view;

    public AnnotationDialog(View view, int width, int height, BookView parentPanel, String category, Integer id, Integer start, Integer end) throws IOException {
        this.view = view;
        this.category = category;
        this.id = id;
        this.start = start;
        this.end = end;
        this.parentPanel = parentPanel;

        // ===== Composants ======
        topPanel = create_top_panel(width, height);
        buttonPanel = create_bottom_panel();

        // ===== Parametres ======
        setTitle("Créer une annotation");
        setSize(500, 300);
        setLocationRelativeTo(parentPanel);
        setResizable(false);
        setModal(true);
        setLayout(new BorderLayout());
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        // ===== Contenu ======
        add(topPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    public JPanel create_top_panel(int width, int height) throws IOException {
        // ===== Composants ======
        // Text area
        text_area = new JTextArea();
        text_area.setLineWrap(true);
        text_area.setWrapStyleWord(true);
        text_area.setText("");

        // ===== Parametres ======
        JPanel selectionPanel = new JPanel();
        selectionPanel.setLayout(new BoxLayout(selectionPanel, BoxLayout.Y_AXIS));
        selectionPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 20, 20));
        selectionPanel.setMaximumSize(new Dimension(width, height));

        // ===== Contenu ======
        selectionPanel.add(text_area);

        return selectionPanel;
    }

    public JPanel create_bottom_panel() throws IOException {
        // ===== Composants ======
        // Confirm button
        JButton confirmButton = new JButton("Confirmer");
        Image confirmIcon = ImageIO.read(getClass().getResource("/assets/confirm.png"));
        confirmButton.setIcon(new ImageIcon(confirmIcon));
        confirmButton.addActionListener(e -> {
            try {
                view.notify_annotation_added(category, id, text_area.getText(), start, end);
                parentPanel.show_highlights();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            dispose();
        });

        // Cancel button
        JButton cancelButton = new JButton("Annuler");
        Image cancelIcon = ImageIO.read(getClass().getResource("/assets/cancel.png"));
        cancelButton.setIcon(new ImageIcon(cancelIcon));
        cancelButton.addActionListener(e -> dispose());

        // ===== Parametres ======
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(0, 200, 20, 10));

        // ===== Contenu ======
        buttonPanel.add(Box.createHorizontalGlue());
        buttonPanel.add(Box.createHorizontalGlue());
        buttonPanel.add(confirmButton);
        buttonPanel.add(cancelButton);

        return buttonPanel;
    }
}
