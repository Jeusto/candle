package app.views.tabs;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;

public class WriteTab extends JPanel implements ActionListener {
    JTextArea text_area;
    JPanel main_panel;
    JMenuBar menu_bar;
    JMenu file_menu;

    public WriteTab() throws IOException {
        menu_bar = new JMenuBar();
        file_menu = new JMenu("Fichier");
        JMenuItem new_menu_item = new JMenuItem("Nouveau");
        JMenuItem open_menu_item = new JMenuItem("Ouvrir");
        JMenuItem save_menu_item = new JMenuItem("Enregistrer");
        JMenuItem close_menu_item = new JMenuItem("Fermer");

        new_menu_item.addActionListener(this);
        open_menu_item.addActionListener(this);
        save_menu_item.addActionListener(this);
        close_menu_item.addActionListener(this);

        file_menu.add(new_menu_item);
        file_menu.add(open_menu_item);
        file_menu.add(save_menu_item);
        file_menu.add(close_menu_item);
        menu_bar.add(file_menu);

        text_area = new JTextArea();

        main_panel = new JPanel();
        main_panel.setLayout(new BorderLayout());
        main_panel.add(menu_bar, BorderLayout.NORTH);
        main_panel.add(new JScrollPane(text_area), BorderLayout.CENTER);

        setLayout(new BorderLayout());
        add(main_panel, BorderLayout.CENTER);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        System.out.println(e);
        String command = e.getActionCommand();
        if (command.equals("Enregistrer")) {
            JFileChooser file_chooser = new JFileChooser(System.getProperty("user.home"));
            int response = file_chooser.showSaveDialog(null);
            if (response == JFileChooser.APPROVE_OPTION) {
                File fi = new File(file_chooser.getSelectedFile().getAbsolutePath());

                try {
                    FileWriter wr = new FileWriter(fi, false);
                    BufferedWriter writer = new BufferedWriter(wr);
                    writer.write(text_area.getText());
                    writer.flush();
                    writer.close();
                } catch (Exception exception) {
                    JOptionPane.showMessageDialog(this, "Il y a eu une erreur dans " +
                                    "l'enregistrement du fichier.",
                            "Erreur",
                            JOptionPane.ERROR_MESSAGE);
                }
            }

        } else if (command.equals("Ouvrir")) {
            JFileChooser file_chooser = new JFileChooser(System.getProperty("user.home"));
            int response = file_chooser.showOpenDialog(null);
            if (response == JFileChooser.APPROVE_OPTION) {
                File file = new File(file_chooser.getSelectedFile().getAbsolutePath());

                try {
                    String string_1 = "", string_l = "";
                    FileReader file_reader = new FileReader(file);
                    BufferedReader reader = new BufferedReader(file_reader);
                    string_l = reader.readLine();
                    while ((string_1 = reader.readLine()) != null) {
                        string_l = string_l + "\n" + string_1;
                    }
                    text_area.setText(string_l);
                } catch (Exception exception) {
                    JOptionPane.showMessageDialog(this, "Il y a eu une erreur dans " +
                                    "l'ouverture du fichier.", "Erreur",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        } else if (command.equals("Nouveau") || command.equals("Fermer")) {
            text_area.setText("");
        }
    }
}