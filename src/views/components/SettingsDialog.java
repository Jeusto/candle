package views.components;

import views.View;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.prefs.BackingStoreException;

public class SettingsDialog extends JDialog implements Dialog {
    private JPanel topPanel;
    private JPanel buttonPanel;
    private JComboBox<String> themeSelect;
    private JComboBox<String> fontSelect;
    private JComboBox<String> fontSizeSelect;
    View view;

    public SettingsDialog(View view, int width, int height, JPanel parentPanel) throws IOException {
        this.view = view;
        // ===== Composants ======
        topPanel = create_top_panel(width, height);
        buttonPanel = create_bottom_panel();

        // ===== Parametres ======
        setTitle("Paramètres d'affichage");
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
        // Theme selection
        JLabel themeSelectLabel = new JLabel("Choix du thème général");
        themeSelect = new JComboBox<>();
        String[] themePossibleValues = {"Flat Light", "Flat Dark", "Flat Intellij", "Flat Darcula"};
        for (String themePossibleValue : themePossibleValues) {
            themeSelect.addItem(themePossibleValue);
        }

        // Font selection
        JLabel fontSelectLabel = new JLabel("Choix de la police d'écriture", SwingConstants.CENTER);
        fontSelect = new JComboBox<>();
        String[] fontsPossibleValues = {"Arial", "Courier", "Helvetica", "Times New Roman", "Verdana"};
        for (String fontSize : fontsPossibleValues) {
            fontSelect.addItem(fontSize);
        }

        // Font size selection
        JLabel fontSizeLabel = new JLabel("Choisir la ta taille de la police");
        fontSizeSelect = new JComboBox<>();
        String[] fontSizePossibleValues = {"8", "9", "10", "11", "12", "14", "16", "18", "20", "22", "24", "26", "28", "36", "48", "72"};
        for (String fontSize : fontSizePossibleValues) {
            fontSizeSelect.addItem(fontSize);
        }

        // ===== Parametres ======
        JPanel selectionPanel = new JPanel();
        selectionPanel.setLayout(new BoxLayout(selectionPanel, BoxLayout.Y_AXIS));
        selectionPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 20, 20));
        selectionPanel.setMaximumSize(new Dimension(width, height));

        // ===== Contenu ======
        selectionPanel.add(themeSelectLabel); selectionPanel.add(themeSelect); selectionPanel.add(Box.createVerticalStrut(20));
        selectionPanel.add(fontSelectLabel); selectionPanel.add(fontSelect); selectionPanel.add(Box.createVerticalStrut(20));
        selectionPanel.add(fontSizeLabel); selectionPanel.add(fontSizeSelect); selectionPanel.add(Box.createVerticalStrut(20));

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
                view.notify_settings_changed(themeSelect.getSelectedItem().toString(), fontSelect.getSelectedItem().toString(), fontSizeSelect.getSelectedItem().toString());
                dispose();
            } catch (BackingStoreException ex) {
                ex.printStackTrace();
            }
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
