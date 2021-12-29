package views.components;

import views.View;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.prefs.BackingStoreException;

public class SettingsDialog extends JDialog implements Dialog {

    private final JPanel top_panel;
    private final JPanel bottom_panel;
    private JComboBox<String> theme_selector;
    private JComboBox<String> font_family_selector;
    private JComboBox<String> font_size_selector;
    View view;

    public SettingsDialog(View view, int width, int height, JPanel parent_panel) throws IOException {
        this.view = view;

        // Parametres ==================================================================================================
        setTitle("Paramètres d'affichage");
        setSize(500, 300);
        setLocationRelativeTo(parent_panel);
        setResizable(false);
        setModal(true);
        setLayout(new BorderLayout());
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        // Composants ==================================================================================================
        top_panel = create_top_panel(width, height);
        bottom_panel = create_bottom_panel();

        // Contenu =====================================================================================================
        add(top_panel, BorderLayout.CENTER);
        add(bottom_panel, BorderLayout.SOUTH);

        setVisible(true);
    }

    public JPanel create_top_panel(int width, int height) throws IOException {
        // Parametres ==================================================================================================
        JPanel top_panel = new JPanel();
        top_panel.setLayout(new BoxLayout(top_panel, BoxLayout.Y_AXIS));
        top_panel.setBorder(BorderFactory.createEmptyBorder(10, 20, 20, 20));
        top_panel.setMaximumSize(new Dimension(width, height));

        // Composants ==================================================================================================
        // Selecteur de theme
        JLabel theme_label = new JLabel("Choix du thème général");
        theme_selector = new JComboBox<>();
        String[] theme_possible_values = {"Flat Light", "Flat Dark", "Flat Intellij", "Flat Darcula"};
        for (String possible_value : theme_possible_values) {
            theme_selector.addItem(possible_value);
        }

        // Selecteur de police
        JLabel font_family_label = new JLabel("Choix de la police d'écriture", SwingConstants.CENTER);
        font_family_selector = new JComboBox<>();
        String[] font_families_possible_values = {"Arial", "Courier", "Helvetica", "Times New Roman", "Verdana"};
        for (String possible_value : font_families_possible_values) {
            font_family_selector.addItem(possible_value);
        }

        // Selecteur de taille de police
        JLabel font_size_label = new JLabel("Choisir la ta taille de la police");
        font_size_selector = new JComboBox<>();
        String[] font_size_possible_values = {"8", "9", "10", "11", "12", "14", "16", "18", "20", "22", "24", "26", "28", "36", "48", "72"};
        for (String possible_value : font_size_possible_values) {
            font_size_selector.addItem(possible_value);
        }

        // Contenu =====================================================================================================
        top_panel.add(theme_label);
        top_panel.add(theme_selector);
        top_panel.add(Box.createVerticalStrut(20));
        top_panel.add(font_family_label);
        top_panel.add(font_family_selector);
        top_panel.add(Box.createVerticalStrut(20));
        top_panel.add(font_size_label);
        top_panel.add(font_size_selector);
        top_panel.add(Box.createVerticalStrut(20));

        return top_panel;
    }

    public JPanel create_bottom_panel() throws IOException {
        // Parametres ==================================================================================================
        JPanel bottom_panel = new JPanel();
        bottom_panel.setBorder(BorderFactory.createEmptyBorder(0, 200, 20, 10));

        // Composants ==================================================================================================
        // Bouton pour confirmer les parametres
        JButton confirm_btn = new JButton("Confirmer");
        Image confirm_icon = ImageIO.read(getClass().getResource("/assets/confirm.png"));
        confirm_btn.setIcon(new ImageIcon(confirm_icon));

        // Notifier la vue pour changer modifier les parametres
        confirm_btn.addActionListener(e -> {
            try {
                view.notify_settings_change_performed(theme_selector.getSelectedItem().toString(),
                        font_family_selector.getSelectedItem().toString(), font_size_selector.getSelectedItem().toString());
            } catch (BackingStoreException ex) {
                JOptionPane.showMessageDialog(this, "Il y a eu une erreur dans le changement de parametres", "Erreur",
                        JOptionPane.ERROR_MESSAGE);
            }
            dispose();
        });

        // Bouton pour annuler et fermer la fenetre
        JButton cancel_btn = new JButton("Annuler");
        Image cancel_icon = ImageIO.read(getClass().getResource("/assets/cancel.png"));
        cancel_btn.setIcon(new ImageIcon(cancel_icon));
        cancel_btn.addActionListener(e -> dispose());

        // Contenu =====================================================================================================
        bottom_panel.add(Box.createHorizontalGlue());
        bottom_panel.add(Box.createHorizontalGlue());
        bottom_panel.add(confirm_btn);
        bottom_panel.add(cancel_btn);

        return bottom_panel;
    }
}
