package app.views.components;

import app.views.View;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.io.IOException;
import java.util.prefs.BackingStoreException;

public class Settings extends JDialog {
    private JPanel selectionPanel;
    private JPanel buttonPanel;
    private JComboBox<String> themeSelect;
    private JComboBox<String> fontSelect;
    private JComboBox<String> fontSizeSelect;
    View view;

    public Settings(View view, int width, int height, JPanel parentPanel) throws IOException {
        this.view = view;
        // ===== Content components ======
        selectionPanel = createSelectionPanel(width, height);
        buttonPanel = createButtonPanel();

        // ===== Settings ======
        setTitle("Paramètres d'affichage");
        setSize(500, 300);
        setLocationRelativeTo(parentPanel);
        setResizable(false);
        setModal(true);
        setLayout(new BorderLayout());
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        // ===== Content ======
        add(selectionPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    JPanel createSelectionPanel (int width, int height) throws IOException {
        // ===== Content components ======
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

        // ===== Settings ======
        JPanel selectionPanel = new JPanel();
        selectionPanel.setLayout(new BoxLayout(selectionPanel, BoxLayout.Y_AXIS));
        selectionPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 20, 20));
        selectionPanel.setMaximumSize(new Dimension(width, height));

        // ===== Content ======
        selectionPanel.add(themeSelectLabel); selectionPanel.add(themeSelect); selectionPanel.add(Box.createVerticalStrut(20));
        selectionPanel.add(fontSelectLabel); selectionPanel.add(fontSelect); selectionPanel.add(Box.createVerticalStrut(20));
        selectionPanel.add(fontSizeLabel); selectionPanel.add(fontSizeSelect); selectionPanel.add(Box.createVerticalStrut(20));

        return selectionPanel;
    }

    JPanel createButtonPanel () throws IOException {
        // ===== Content components ======
        // Confirm button
        JButton confirmButton = new JButton("Confirmer");
        Image confirmIcon = ImageIO.read(getClass().getResource("/app/assets/confirm.png"));
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
        Image cancelIcon = ImageIO.read(getClass().getResource("/app/assets/cancel.png"));
        cancelButton.setIcon(new ImageIcon(cancelIcon));
        cancelButton.addActionListener(e -> dispose());

        // ===== Settings ======
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(0, 200, 20, 10));

        // ===== Content ======
        buttonPanel.add(Box.createHorizontalGlue());
        buttonPanel.add(Box.createHorizontalGlue());
        buttonPanel.add(confirmButton);
        buttonPanel.add(cancelButton);

        return buttonPanel;
    }
}
