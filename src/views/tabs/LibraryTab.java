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
    private JList book_list;
    private JTree bookshelf_tree;
    private JLabel current_bookshelf;
    private JLabel number_of_results;

    private final View view;
    private DefaultListModel<String> books_list;
    private final models.entities.Library library;
    private HashMap<String, Integer> current_bookshelf_books_list;

    public LibraryTab(View view, models.entities.Library library) throws Exception {
        this.view = view;
        this.library = library;
        this.current_bookshelf_books_list = new HashMap<>();

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

    private JScrollPane create_left_panel() {
        DefaultMutableTreeNode node_all = new DefaultMutableTreeNode("Tous les livres");
        DefaultMutableTreeNode node_available = new DefaultMutableTreeNode("Catégories disponibles");
        DefaultMutableTreeNode node_downloaded = new DefaultMutableTreeNode("Livres téléchargés");

        node_all.add(node_downloaded);
        node_all.add(node_available);

        library.get_categories().keySet().stream().sorted().forEach(bookshelf -> {
            if (!bookshelf.equals("Livres téléchargés"))
                node_available.add(new DefaultMutableTreeNode(bookshelf));
        });

        bookshelf_tree = new JTree(node_all);
        bookshelf_tree.setRootVisible(false);
        bookshelf_tree.expandRow(1);
        bookshelf_tree.setShowsRootHandles(true);
        bookshelf_tree.setRowHeight(0);

        bookshelf_tree.addTreeSelectionListener(e -> {
            tree_selection_listener();
        });

        JScrollPane leftPane = new JScrollPane(bookshelf_tree);
        leftPane.setPreferredSize(new Dimension(300, 0));
        leftPane.setMinimumSize(new Dimension(235, 0));
        leftPane.setMaximumSize(new Dimension(500, Short.MAX_VALUE));
        leftPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        leftPane.setBorder(new EmptyBorder(0, 0, 0, 0));

        return leftPane;
    }

    private JPanel create_main_panel() throws IOException {
        JPanel main_panel = new JPanel();
        main_panel.setPreferredSize(new Dimension(800, 0));
        main_panel.setMinimumSize(new Dimension(500, 0));
        main_panel.setMaximumSize(new Dimension(1200, Short.MAX_VALUE));
        main_panel.setLayout(new BorderLayout());
        main_panel.setBorder(new EmptyBorder(10, 10, 10, 10));

        current_bookshelf = new JLabel("");
        current_bookshelf.setFont((this.current_bookshelf.getFont()).deriveFont(Font.PLAIN, 22));
        current_bookshelf.setPreferredSize(new Dimension(0, 50));
        current_bookshelf.setMinimumSize(new Dimension(0, 50));
        current_bookshelf.setMaximumSize(new Dimension(Short.MAX_VALUE, 50));

        JButton read_btn = new JButton("Lire");
        JButton download_btn = new JButton("Télécharger");
        JButton delete_btn = new JButton("Supprimer");

        Image read_icon = ImageIO.read(getClass().getResource("/assets/read.png"));
        Image download_icon = ImageIO.read(getClass().getResource("/assets/download.png"));
        Image delete_icon = ImageIO.read(getClass().getResource("/assets/delete.png"));

        read_btn.setIcon(new ImageIcon(read_icon));
        download_btn.setIcon(new ImageIcon(download_icon));
        delete_btn.setIcon(new ImageIcon(delete_icon));

        download_btn.addActionListener(e -> {
            download_button_action_listener();
        });
        read_btn.addActionListener(e -> {
            read_button_action_listener();
        });
        delete_btn.addActionListener(e -> {
            delete_button_action_listener();
        });

        JPanel top = new JPanel();
        top.setLayout(new BoxLayout(top, FlowLayout.RIGHT));
        top.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        top.add(this.current_bookshelf);
        top.add(read_btn);
        top.add(Box.createHorizontalStrut(10));
        top.add(download_btn);
        top.add(Box.createHorizontalStrut(10));
        top.add(delete_btn);

        books_list = new DefaultListModel<>();
        book_list = new JList<>(books_list);
        book_list.setOpaque(false);
        book_list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        book_list.setLayoutOrientation(JList.VERTICAL);
        book_list.setVisibleRowCount(-1);
        book_list.setFixedCellHeight(30);
        book_list.setFixedCellWidth(200);

        book_list.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    read_button_action_listener();
                }
            }
        });

        JScrollPane center = new JScrollPane(book_list);
        center.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        JPanel bottom = new JPanel();
        bottom.setLayout(new BoxLayout(bottom, BoxLayout.X_AXIS));
        bottom.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        number_of_results = new JLabel("Nombres de livres dans cette catégorie = 0");
        Image countIcon = ImageIO.read(getClass().getResource("/assets/count.png"));
        number_of_results.setIcon(new ImageIcon(countIcon));
        bottom.add(number_of_results);

        main_panel.add(top, BorderLayout.NORTH);
        main_panel.add(center, BorderLayout.CENTER);
        main_panel.add(bottom, BorderLayout.SOUTH);

        return main_panel;
    }

    public void show_results(HashMap<Integer, Book> results) {
        current_bookshelf_books_list = new HashMap<>();
        books_list.clear();

        for (Book book : results.values()) {
            books_list.addElement(book.get_title());
            current_bookshelf_books_list.put(book.get_title(), Integer.valueOf(book.get_id()));
        }

        number_of_results.setText("Nombres de livres dans cette catégorie = " + results.size());
    }

    public void show_download_result(String message) {
        JOptionPane.showMessageDialog(null, message, "Résultat du téléchargement",
                JOptionPane.INFORMATION_MESSAGE);
    }

    public void show_delete_result(String message) {
        JOptionPane.showMessageDialog(null, message, "Résultat de la suppression",
                JOptionPane.INFORMATION_MESSAGE);
    }

    public void afficher_erreur(String message) {
        JOptionPane.showMessageDialog(this, message, "Erreur", JOptionPane.ERROR_MESSAGE);
    }

    private void tree_selection_listener() {
        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {

            @Override
            protected Void doInBackground() throws Exception {
                DefaultMutableTreeNode node = (DefaultMutableTreeNode) bookshelf_tree.getLastSelectedPathComponent();
                if (node != null) {
                    Object node_info = node.getUserObject();
                    if (node.isLeaf()) {
                        current_bookshelf.setText(node_info.toString());
                        try {
                            view.notify_bookshelf_change_performed(node_info.toString());
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
                if (book_list.getSelectedValue() == null) {
                    afficher_erreur("Il faut choisir un livre avant de cliquer sur \"lire\" !");
                    return null;
                } else {
                    try {
                        view.notify_read_performed(current_bookshelf.getText(),
                                current_bookshelf_books_list.get(book_list.getSelectedValue()));
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
                if (book_list.getSelectedValue() == null) {
                    afficher_erreur("Il faut choisir un livre avant de cliquer sur \"télécharger\" !");
                    return null;
                } else {
                    try {
                        view.notify_download_performed(current_bookshelf.getText(),
                                current_bookshelf_books_list.get(book_list.getSelectedValue()));

                        if (current_bookshelf.getText().equals("Livres téléchargés")) {
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
                if (book_list.getSelectedValue() == null) {
                    afficher_erreur("Il faut choisir un livre avant de cliquer sur \"télécharger\" !");
                    return null;
                } else {
                    try {
                        view.notify_delete_performed(current_bookshelf_books_list.get(book_list.getSelectedValue()));

                        if (current_bookshelf.getText().equals("Livres téléchargés")) {
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
