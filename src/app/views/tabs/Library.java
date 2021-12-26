package app.views.tabs;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.tree.DefaultMutableTreeNode;
import java.awt.*;
import java.io.IOException;
import java.util.HashMap;


public class Library extends JPanel {
    private JScrollPane leftPanel;
    private JPanel mainPanel;
    private HashMap<String, Integer> categoriesList;
    private DefaultMutableTreeNode[] categoryNodes;
    private JLabel currentCategory;
    private JLabel currentPage;
    private JButton nextPage;
    private JButton prevPage;

    public Library(HashMap<String, Integer> categoriesList) throws Exception {
        this.categoriesList = categoriesList;

        // ===== Content components ======
        leftPanel = createLeftPanel();
        mainPanel = createMainPanel();

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftPanel, mainPanel);
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
        for (String category : categoriesList.keySet()) {
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
                currentCategory.setText(nodeInfo.toString());
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
        this.currentCategory = new JLabel("Livres téléchargés");
        this.currentCategory.setFont((this.currentCategory.getFont()).deriveFont(Font.PLAIN, 22 ));
        this.currentCategory.setPreferredSize(new Dimension(0, 50));
        this.currentCategory.setMinimumSize(new Dimension(0, 50));
        this.currentCategory.setMaximumSize(new Dimension(Short.MAX_VALUE, 50));

        // Top buttons
        JButton readButton = new JButton("Lire");
        // add invisible border to avoid button overlap
        Image readIcon = ImageIO.read(getClass().getResource("/app/assets/read.png"));
        readButton.setIcon(new ImageIcon(readIcon));
        JButton downloadButton = new JButton("Télécharger");
        Image downloadIcon = ImageIO.read(getClass().getResource("/app/assets/download.png"));
        downloadButton.setIcon(new ImageIcon(downloadIcon));

        // Top part that contains the category title and the top buttons
        JPanel top = new JPanel();
        top.setLayout(new BoxLayout(top, FlowLayout.RIGHT));
        top.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        top.add(this.currentCategory);
        top.add(readButton);
        top.add(downloadButton);

        // List of books in the center
        DefaultListModel<String> l1 = new DefaultListModel<>();
        for (int i = 0; i < 25; i++) {
            l1.addElement("Livre " + i);
        }
        JList<String> list = new JList<>(l1);
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
        this.currentPage = new JLabel("Page 1/43");
        this.prevPage = new JButton("Précédent");
        Image prevIcon = ImageIO.read(getClass().getResource("/app/assets/previous.png"));
        prevPage.setIcon(new ImageIcon(prevIcon));
        this.nextPage = new JButton("Suivant");
        Image nextIcon = ImageIO.read(getClass().getResource("/app/assets/next.png"));
        nextPage.setIcon(new ImageIcon(nextIcon));
        bottom.add(currentPage);
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
}
