package app.views.tabs;

import app.views.View;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.tree.DefaultMutableTreeNode;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;


public class Library extends JPanel {
    private View view;
    private JScrollPane left_panel;
    private JPanel main_panel;
    private JList list;
    private DefaultListModel<String> books_list;
    private HashMap<String, Integer> categories_list;
    private HashMap<String, ArrayList<String>> current_category_books;
    private DefaultMutableTreeNode[] category_nodes;
    private JLabel current_category;
    private JLabel number_of_results;
    private JButton nextPage;
    private JButton prevPage;

    public Library(View view, HashMap<String, Integer> categoriesList) throws Exception {
        this.view = view;
        this.categories_list = categoriesList;

        // ===== Content components ======
        left_panel = createLeftPanel();
        main_panel = createMainPanel();

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, left_panel, main_panel);
        splitPane.setOneTouchExpandable(true);
        splitPane.setDividerSize(5);

        // ===== Settings ======
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        // ===== Content ======
        add(splitPane);
    }

    private JScrollPane createLeftPanel() {
        // ===== Content components ======
        // Tree structure
        DefaultMutableTreeNode node_all=new DefaultMutableTreeNode("Tous les livres");
        DefaultMutableTreeNode node_available=new DefaultMutableTreeNode("Catégories disponibles");
        DefaultMutableTreeNode node_read=new DefaultMutableTreeNode("Livres lus");
        DefaultMutableTreeNode node_not_read=new DefaultMutableTreeNode("Livres non lus");
        DefaultMutableTreeNode node_downloaded=new DefaultMutableTreeNode("Livres téléchargés");
        node_all.add(node_downloaded); node_all.add(node_read); node_all.add(node_not_read); node_all.add(node_available);

        // Tree categories
        for (String category : categories_list.keySet()) {
            node_available.add(new DefaultMutableTreeNode(category));
        }

        // Tree
        JTree tree = new JTree(node_all);
        tree.setRootVisible(false);
        tree.setShowsRootHandles(true);
        tree.setRowHeight(0);

        // Tree selection listener
        tree.addTreeSelectionListener(e -> {
            DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
            if (node == null) {
                return;
            }
            Object nodeInfo = node.getUserObject();
            if (node.isLeaf()) {
                current_category.setText(nodeInfo.toString());
                try {
                    view.notify_category_change_performed(nodeInfo.toString());
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });

        // ===== Settings ======
        JScrollPane leftPane = new JScrollPane(tree);
        leftPane.setPreferredSize(new Dimension(300, 0));
        leftPane.setMinimumSize(new Dimension(235, 0));
        leftPane.setMaximumSize(new Dimension(500, Short.MAX_VALUE));
        leftPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        leftPane.setBorder(new EmptyBorder(0, 0, 0, 0));

        return leftPane;
    }

    private JPanel createMainPanel() throws IOException {
        // ===== Content components ======
        // Category title
        this.current_category = new JLabel("Livres téléchargés");
        this.current_category.setFont((this.current_category.getFont()).deriveFont(Font.PLAIN, 22 ));
        this.current_category.setPreferredSize(new Dimension(0, 50));
        this.current_category.setMinimumSize(new Dimension(0, 50));
        this.current_category.setMaximumSize(new Dimension(Short.MAX_VALUE, 50));

        // Top buttons
        JButton read_button = new JButton("Lire");
        Image readIcon = ImageIO.read(getClass().getResource("/app/assets/read.png"));
        read_button.setIcon(new ImageIcon(readIcon));
        read_button.addActionListener(e -> {
            try {
                view.notify_read_performed((String) list.getSelectedValue());
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });
        
        JButton downloadButton = new JButton("Télécharger");
        Image downloadIcon = ImageIO.read(getClass().getResource("/app/assets/download.png"));
        downloadButton.setIcon(new ImageIcon(downloadIcon));

        // Top part that contains the category title and the top buttons
        JPanel top = new JPanel();
        top.setLayout(new BoxLayout(top, FlowLayout.RIGHT));
        top.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        top.add(this.current_category);
        top.add(read_button);
        top.add(Box.createHorizontalStrut(10));
        top.add(downloadButton);

        // List of books in the center
        books_list = new DefaultListModel<>();
        list = new JList<>(books_list);
        list.setOpaque(false);
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        list.setLayoutOrientation(JList.VERTICAL);
        list.setVisibleRowCount(-1);
        list.setFixedCellHeight(30);
        list.setFixedCellWidth(200);

        // Scroll pane that contains the list
        JScrollPane center = new JScrollPane(list);
        center.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        // Bottom part that contains the bottom buttons and page indicator
        JPanel bottom = new JPanel();
        bottom.setLayout(new BoxLayout(bottom, BoxLayout.X_AXIS));
        bottom.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        this.number_of_results = new JLabel("Nombres de livres dans cette catégorie = 0");
        Image countIcon = ImageIO.read(getClass().getResource("/app/assets/count.png"));
        number_of_results.setIcon(new ImageIcon(countIcon));
        this.prevPage = new JButton("Précédent");
        Image prevIcon = ImageIO.read(getClass().getResource("/app/assets/previous.png"));
        prevPage.setIcon(new ImageIcon(prevIcon));
        this.nextPage = new JButton("Suivant");
        Image nextIcon = ImageIO.read(getClass().getResource("/app/assets/next.png"));
        nextPage.setIcon(new ImageIcon(nextIcon));
        bottom.add(number_of_results);
        bottom.add(Box.createHorizontalGlue());
        bottom.add(prevPage);
        bottom.add(nextPage);

        // ===== Settings ======
        JPanel mainPanelWrapper = new JPanel();
        mainPanelWrapper.setPreferredSize(new Dimension(800, 0));
        mainPanelWrapper.setMinimumSize(new Dimension(500, 0));
        mainPanelWrapper.setMaximumSize(new Dimension(1200, Short.MAX_VALUE));
        mainPanelWrapper.setLayout(new BorderLayout());
        mainPanelWrapper.setBorder(new EmptyBorder(10, 10, 10, 10));
        
        // ===== Content ======
        mainPanelWrapper.add(top, BorderLayout.NORTH);
        mainPanelWrapper.add(center, BorderLayout.CENTER);
        mainPanelWrapper.add(bottom, BorderLayout.SOUTH);

        return mainPanelWrapper;
    }

    public void show_results(HashMap<String, ArrayList<String>> results) {
        current_category_books = results;
        books_list.clear();
        for (String key : results.keySet()) {
            books_list.addElement(key);
        }
        number_of_results.setText("Nombres de livres dans cette catégorie = " + results.size()) ;
    }
}
