package app.views.tabs;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.tree.DefaultMutableTreeNode;
import java.awt.*;


public class Library extends JPanel {
    private JScrollPane leftPanel;
    private JPanel rightPanel;

    private DefaultMutableTreeNode[] categoryNodes;
    private String currentCategoryTitle;
    private int currentPage;

    public Library() throws Exception {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        // Create the left side of split panel: Categories tree
        leftPanel = createLeftPanel();

        // Create the right side of split panel: List of books
        rightPanel = createRightPanel();

        // Creat the split panel
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftPanel, rightPanel);
        splitPane.setOneTouchExpandable(false);
        splitPane.setDividerSize(0);
        add(splitPane);
    }

    private JScrollPane createLeftPanel() {
        // Create the tree categories
        DefaultMutableTreeNode node_all=new DefaultMutableTreeNode("Tous les livres");
        DefaultMutableTreeNode node_available=new DefaultMutableTreeNode("Livres disponibles");
        DefaultMutableTreeNode category_1=new DefaultMutableTreeNode("Animal");
        DefaultMutableTreeNode category_2=new DefaultMutableTreeNode("Histoire");
        DefaultMutableTreeNode category_3=new DefaultMutableTreeNode("Géographie");
        DefaultMutableTreeNode node_downloaded=new DefaultMutableTreeNode("Livres téléchargés");
        DefaultMutableTreeNode node_favorites=new DefaultMutableTreeNode("Favoris");
        node_available.add(category_1); node_available.add(category_2); node_available.add(category_3);
        node_all.add(node_available); node_all.add(node_downloaded); node_all.add(node_favorites);
        for(int i=0;i<50;i++) {
            node_available.add(new DefaultMutableTreeNode("Categorie "+i));
        }

        // Create the tree
        JTree tree = new JTree(node_all);
        tree.setRootVisible(false);
        tree.setShowsRootHandles(true);
        tree.setRowHeight(0);

        // Create the scroll pane that will contain the tree
        JScrollPane leftPane = new JScrollPane(tree);
        leftPane.setPreferredSize(new Dimension(200, 0));
        leftPane.setMinimumSize(new Dimension(150, 0));
        leftPane.setMaximumSize(new Dimension(200, Short.MAX_VALUE));
        leftPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        leftPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        leftPane.setBorder(new EmptyBorder(0, 0, 0, 0));

        return leftPane;
    }

    private JPanel createRightPanel() {
        // Create the category title in the top of the panel
        this.currentCategoryTitle = "Livres téléchargés";
        JLabel categoryTitle = new JLabel(this.currentCategoryTitle);
        categoryTitle.setFont((categoryTitle.getFont()).deriveFont(Font.PLAIN, 22 ));
        categoryTitle.setPreferredSize(new Dimension(0, 50));
        categoryTitle.setMinimumSize(new Dimension(0, 50));
        categoryTitle.setMaximumSize(new Dimension(Short.MAX_VALUE, 50));

        // Create the buttons in the top of the panel
        JButton readButton = new JButton("Lire");
        JButton downloadButton = new JButton("Télécharger");
        JButton favoriteButton = new JButton("Favoris");

        // Create the top part
        JPanel top = new JPanel();
        top.setLayout(new BoxLayout(top, FlowLayout.RIGHT));
        top.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        top.add(categoryTitle);
        top.add(readButton);
        top.add(downloadButton);
        top.add(favoriteButton);

        // Create the list of books in the center
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

        // Create the scroll pane that will contain the list
        JScrollPane center = new JScrollPane(list);
        center.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        // Create the bottom part with the page number and page change buttons
        JPanel bottom = new JPanel();
        bottom.setLayout(new BoxLayout(bottom, BoxLayout.X_AXIS));
        bottom.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        this.currentPage = 1;
        bottom.add(new JLabel("Page " + this.currentPage));
        bottom.add(Box.createHorizontalGlue());
        bottom.add(new JButton("<<"));
        bottom.add(new JButton("<"));
        bottom.add(new JButton(">"));
        bottom.add(new JButton(">>"));
        // make the buttons same size
        bottom.setMaximumSize(new Dimension(Short.MAX_VALUE, bottom.getMaximumSize().height));



        // Create the panel that will contain everything
        JPanel rightPanelWrapper = new JPanel();
        rightPanelWrapper.setLayout(new BorderLayout());
        rightPanelWrapper.setBorder(new EmptyBorder(10, 10, 10, 10));
        rightPanelWrapper.add(top, BorderLayout.NORTH);
        rightPanelWrapper.add(center, BorderLayout.CENTER);
        rightPanelWrapper.add(bottom, BorderLayout.SOUTH);

        return rightPanelWrapper;
    }
}
