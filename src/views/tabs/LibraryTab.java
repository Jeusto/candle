package views.tabs;

import models.entities.Book;
import views.View;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.text.BadLocationException;
import javax.swing.tree.DefaultMutableTreeNode;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.HashMap;


public class LibraryTab extends JPanel {

    private final JScrollPane left_panel;
    private final JPanel main_panel;
    private JList list;
    private JTree tree;
    private JLabel current_category;
    private JLabel number_of_results;

    private final View view;
    private DefaultListModel<String> books_list;
    private final models.entities.Library library;
    private HashMap<String, Integer> current_category_books_list;

    public LibraryTab(View view, models.entities.Library library) throws Exception {
        this.view = view;
        this.library = library;
        this.current_category_books_list = new HashMap<>();

        // Parameteres =================================================================================================
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        // Composants ==================================================================================================
        left_panel = create_left_panel();
        main_panel = create_main_panel();

        JSplitPane split_pane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, left_panel, main_panel);
        split_pane.setOneTouchExpandable(true);
        split_pane.setDividerSize(5);

        // Contenu =====================================================================================================
        add(split_pane);
    }

    // Fonctions pour creer l'interface ================================================================================
    private JScrollPane create_left_panel() {
        // ===== Composants ======
        // Tree structure
        DefaultMutableTreeNode node_all = new DefaultMutableTreeNode("Tous les livres");
        DefaultMutableTreeNode node_available = new DefaultMutableTreeNode("Catégories disponibles");
        DefaultMutableTreeNode node_downloaded = new DefaultMutableTreeNode("Livres téléchargés");
        node_all.add(node_downloaded);
        node_all.add(node_available);

        // Tree categories
        library.get_categories().keySet().stream().sorted().forEach(category -> {
            if (!category.equals("Livres téléchargés"))
                node_available.add(new DefaultMutableTreeNode(category));
        });

        // Tree
        tree = new JTree(node_all);
        tree.setRootVisible(false);
        tree.expandRow(1);
        tree.setShowsRootHandles(true);
        tree.setRowHeight(0);

        // Add listeners
        tree.addTreeSelectionListener(e -> {
            tree_selection_listener();
        });

        // ===== Parametres ======
        JScrollPane leftPane = new JScrollPane(tree);
        leftPane.setPreferredSize(new Dimension(300, 0));
        leftPane.setMinimumSize(new Dimension(235, 0));
        leftPane.setMaximumSize(new Dimension(500, Short.MAX_VALUE));
        leftPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        leftPane.setBorder(new EmptyBorder(0, 0, 0, 0));

        return leftPane;
    }

    private JPanel create_main_panel() throws IOException {
        // Parameteres =================================================================================================
        JPanel main_panel = new JPanel();
        main_panel.setPreferredSize(new Dimension(800, 0));
        main_panel.setMinimumSize(new Dimension(500, 0));
        main_panel.setMaximumSize(new Dimension(1200, Short.MAX_VALUE));
        main_panel.setLayout(new BorderLayout());
        main_panel.setBorder(new EmptyBorder(10, 10, 10, 10));

        // Composants ==================================================================================================
        // Titre de la category
        current_category = new JLabel("");
        current_category.setFont((this.current_category.getFont()).deriveFont(Font.PLAIN, 22));
        current_category.setPreferredSize(new Dimension(0, 50));
        current_category.setMinimumSize(new Dimension(0, 50));
        current_category.setMaximumSize(new Dimension(Short.MAX_VALUE, 50));

        // Boutons en haut a droite
        JButton read_btn = new JButton("Lire");
        JButton download_btn = new JButton("Télécharger");
        JButton delete_btn = new JButton("Supprimer");

        Image read_icon = ImageIO.read(getClass().getResource("/assets/read.png"));
        Image download_icon = ImageIO.read(getClass().getResource("/assets/download.png"));
        Image delete_icon = ImageIO.read(getClass().getResource("/assets/delete.png"));

        read_btn.setIcon(new ImageIcon(read_icon));
        download_btn.setIcon(new ImageIcon(download_icon));
        delete_btn.setIcon(new ImageIcon(delete_icon));

        // On associe les boutons a leurs actions
        download_btn.addActionListener(e -> {
            download_button_action_listener();
        });
        read_btn.addActionListener(e -> {
            read_button_action_listener();
        });
        delete_btn.addActionListener(e -> {
            delete_button_action_listener();
        });

        // Le panel en haut
        JPanel top = new JPanel();
        top.setLayout(new BoxLayout(top, FlowLayout.RIGHT));
        top.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        top.add(this.current_category);
        top.add(read_btn);
        top.add(Box.createHorizontalStrut(10));
        top.add(download_btn);
        top.add(Box.createHorizontalStrut(10));
        top.add(delete_btn);

        // Liste de livres au centre
        books_list = new DefaultListModel<>();
        list = new JList<>(books_list);
        list.setOpaque(false);
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        list.setLayoutOrientation(JList.VERTICAL);
        list.setVisibleRowCount(-1);
        list.setFixedCellHeight(30);
        list.setFixedCellWidth(200);

        // Double clique sur un livre = lire le livre
        list.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    read_button_action_listener();
                }
            }
        });

        // Panneau defilant qui contient la liste
        JScrollPane center = new JScrollPane(list);
        center.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        // Partie du bas qui contient qui contient le nombre de livres dans la categorie
        JPanel bottom = new JPanel();
        bottom.setLayout(new BoxLayout(bottom, BoxLayout.X_AXIS));
        bottom.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        number_of_results = new JLabel("Nombres de livres dans cette catégorie = 0");
        Image countIcon = ImageIO.read(getClass().getResource("/assets/count.png"));
        number_of_results.setIcon(new ImageIcon(countIcon));
        bottom.add(number_of_results);

        // Contenu =====================================================================================================
        main_panel.add(top, BorderLayout.NORTH);
        main_panel.add(center, BorderLayout.CENTER);
        main_panel.add(bottom, BorderLayout.SOUTH);

        return main_panel;
    }

    // Fonctions diverses ==============================================================================================
    public void show_results(HashMap<Integer, Book> results) {
        // On vide la liste
        current_category_books_list = new HashMap<>();
        books_list.clear();

        // Mise a jour de la liste de livres
        for (Book book : results.values()) {
            books_list.addElement(book.get_title());
            current_category_books_list.put(book.get_title(), Integer.valueOf(book.get_id()));
        }
        // Mise a jour du nombre de livres
        number_of_results.setText("Nombres de livres dans cette catégorie = " + results.size());
    }

    public void afficher_erreur(String message) {
        JOptionPane.showMessageDialog(this, message, "Erreur", JOptionPane.ERROR_MESSAGE);
    }

    // Actions listeners ===============================================================================================
    private void tree_selection_listener() {
        // Si on a selectionne une categorie valide, on notifie la vue qu'on veut afficher les livres de cette categorie
        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {

            @Override
            protected Void doInBackground() throws Exception {
                DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
                if (node != null) {
                    Object nodeInfo = node.getUserObject();
                    if (node.isLeaf()) {
                        current_category.setText(nodeInfo.toString());
                        try {
                            view.notify_bookshelf_change_performed(nodeInfo.toString());
                        } catch (IOException ex) {
                            afficher_erreur("Il y a eu une erreur dans l'affichage des livres de cette catégorie");
                        }
                    }
                }
                return null;
            }
        };
        worker.execute();
    }

    private void read_button_action_listener() {
        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() {
                if (list.getSelectedValue() == null) {
                    afficher_erreur("Il faut choisir un livre avant de cliquer sur \"lire\" !");
                    return null;
                } else {
                    try {
                        view.notify_read_performed(current_category.getText(), current_category_books_list.get(list.getSelectedValue()));
                    } catch (IOException | BadLocationException | InterruptedException ex) {
                        afficher_erreur("Il y a eu une erreur dans l'affichage du livre voulu.");
                    }
                }
                return null;
            }
        };
        worker.execute();
    }

    private void download_button_action_listener() {
        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() {
                if (list.getSelectedValue() == null) {
                    afficher_erreur("Il faut choisir un livre avant de cliquer sur \"télécharger\" !");
                    return null;
                } else {
                    try {
                        // On notifie la vue de l'action de téléchargement et on affiche ensuite le résultat
                        String result = view.notify_download_performed(current_category.getText(), current_category_books_list.get(list.getSelectedValue()));
                        JOptionPane.showMessageDialog(null, result, "Résultat du téléchargement",
                                JOptionPane.INFORMATION_MESSAGE);

                        // Si on est actuellement dans la catégorie "Livres téléchargés", on doit rafraichir la liste
                        if (current_category.getText().equals("Livres téléchargés")) {
                            tree_selection_listener();
                        }
                    } catch (IOException ex) {
                        afficher_erreur("Il y a eu une erreur dans le téléchargement du livre voulu.");
                    }
                    return null;
                }
            }
        };
        worker.execute();

    }

    private void delete_button_action_listener() {
        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() {
                if (list.getSelectedValue() == null) {
                    afficher_erreur("Il faut choisir un livre avant de cliquer sur \"télécharger\" !");
                    return null;
                } else {
                    try {
                        // On notifie la vue de l'action de suppression et on affiche ensuite le résultat
                        String result = view.notify_delete_performed(current_category_books_list.get(list.getSelectedValue()));
                        JOptionPane.showMessageDialog(null, result, "Résultat de la suppression",
                                JOptionPane.INFORMATION_MESSAGE);

                        // Si on est actuellement dans la catégorie "Livres téléchargés", on doit rafraichir la liste
                        if (current_category.getText().equals("Livres téléchargés")) {
                            tree_selection_listener();
                        }
                    } catch (IOException ex) {
                        afficher_erreur("Il y a eu une erreur dans la suppression du livre.");
                    }
                    return null;
                }
            }
        };
        worker.execute();
    }
}
