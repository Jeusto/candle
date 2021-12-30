package views.tabs;

import models.entities.Book;
import views.View;
import views.components.ImageCard;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;

public class SearchTab extends JPanel {
    private final View view;

    private final JPanel top_panel;
    private final JPanel main_panel;
    private JPanel main;
    private JButton search_button;
    private JTextField search_field;
    private JLabel search_label;

    public SearchTab(View view) throws IOException {
        this.view = view;

        setLayout(new BorderLayout());

        top_panel = create_top_panel();
        main_panel = create_main_panel();

        add(top_panel, BorderLayout.NORTH);
        add(main_panel, BorderLayout.CENTER);
    }

    JPanel create_top_panel() throws IOException {
        JPanel top_panel = new JPanel();
        top_panel.setLayout(new BoxLayout(top_panel, FlowLayout.RIGHT));
        top_panel.setBorder(BorderFactory.createEmptyBorder(20, 150, 20, 150));

        search_field = new JTextField();
        search_field.setPreferredSize(new Dimension(200, 30));

        Image search_icon = ImageIO.read(getClass().getResource("/assets/search.png"));
        search_button = new JButton("Faire une recherche");
        search_button.setIcon(new ImageIcon(search_icon));
        search_button.addActionListener(e -> {
            search_button_action_listener();
        });

        top_panel.add(search_field);
        top_panel.add(Box.createHorizontalStrut(10));
        top_panel.add(search_button);
        return top_panel;
    }

    JPanel create_main_panel() {
        main = new JPanel();
        search_label = new JLabel("Veuillez entrer un mot de recherche...");
        search_label.setFont((search_label.getFont()).deriveFont(Font.PLAIN, 20));

        main.setLayout(new FlowLayout());
        main.setBorder(BorderFactory.createEmptyBorder(0, 20, 20, 20));

        JPanel top = new JPanel();
        top.add(search_label);
        top.setPreferredSize(new Dimension(Integer.MAX_VALUE, 50));
        top.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));

        JPanel main_panel = new JPanel();
        main_panel.setLayout(new BoxLayout(main_panel, BoxLayout.Y_AXIS));
        main_panel.add(top);
        main_panel.add(main);
        return main_panel;
    }

    public void show_result(ArrayList<Book> results) throws IOException {
        for (Book book : results) {
            main.add(new ImageCard(book, view));
        }
        if (results.size() == 0) {
            search_label.setText("Aucun résultat pour votre recherche... Veuillez réessayer.");
        } else {
            search_label.setText("Résultats pour: \"" + search_field.getText() + "\"");
        }
        search_field.setText("");
    }

    public void search_button_action_listener() {
        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {

            @Override
            protected Void doInBackground() throws Exception {
                main.removeAll();
                main.revalidate();
                if (search_field.getText().isEmpty()) {
                    new JOptionPane().showMessageDialog(null, "Veuillez entrer un mot de " +
                                    "recherche non vide.", "Erreur",
                            JOptionPane.ERROR_MESSAGE);
                    return null;
                }
                try {
                    search_label.setText("Veuillez patienter....");
                    view.notify_search_performed(search_field.getText());
                } catch (IOException ex) {
                    new JOptionPane().showMessageDialog(null, "Il y a eu une erreur lors " +
                                    "de la recherche.", "Erreur",
                            JOptionPane.ERROR_MESSAGE);
                }
                return null;
            }
        };
        worker.execute();
    }
}
