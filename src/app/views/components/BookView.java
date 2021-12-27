package app.views.components;

import app.models.entities.Book;
import app.views.View;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.CompoundBorder;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import static java.awt.event.MouseEvent.BUTTON3;

public class BookView extends JPanel {
    private JEditorPane textHtml;
    private JScrollPane textWrapper;
    private JButton back_button;
    private JButton settingsButton;
    private JPopupMenu popupMenu;
    private JPanel toolbar;
    private JLabel currentBookTitle;
    private Settings settingsDialog;
    private View view;

    public BookView(View view) throws IOException {
        this.view = view;

        // ===== Content components ======
        textWrapper = createTextWrapper();
        toolbar = createToolbar();
        popupMenu = createPopupMenu();

        // ===== Settings ======
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        // ===== Content ======
        add(toolbar);
        add(textWrapper);
    }

    private JPanel createToolbar() throws IOException {
        // ===== Content components ======
        // Go back button
        back_button = new JButton("Revenir en arrière");
        back_button.setEnabled(true);
        Image backIcon = ImageIO.read(getClass().getResource("/app/assets/back.png"));
        back_button.setIcon(new ImageIcon(backIcon));
        back_button.addActionListener(e -> {
            try {
                view.notify_back_performed();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });

        // Settings button
        settingsButton = new JButton("Paramètres d'affichage");
        settingsButton.setEnabled(true);
        Image settingsIcon = ImageIO.read(getClass().getResource("/app/assets/settings.png"));
        settingsButton.setIcon(new ImageIcon(settingsIcon));

        // Title
        currentBookTitle = new JLabel("Titre du livre", SwingConstants.CENTER);
        currentBookTitle.setFont((currentBookTitle.getFont()).deriveFont(Font.PLAIN, 22));
        currentBookTitle.setPreferredSize(new Dimension(0, 27));
        currentBookTitle.setMinimumSize(new Dimension(0, 27));
        currentBookTitle.setMaximumSize(new Dimension(Short.MAX_VALUE*10, 27));

        // ===== Settings ======
        JPanel toolbar = new JPanel();
        toolbar.setLayout(new BoxLayout(toolbar, FlowLayout.RIGHT));
        toolbar.setBorder(new CompoundBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, new Color(128, 128, 128, 128)),
                BorderFactory.createEmptyBorder(10, 20, 10, 20)
        ));

        // Show the settings dialog when clicking on the settings button
        settingsButton.addActionListener(e -> {
            try {
                settingsDialog = new Settings(view, this.getSize().width / 2, this.getSize().height / 2, this);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });

        // ===== Content ======
        toolbar.add(back_button);
        toolbar.add(Box.createHorizontalGlue());
        toolbar.add(currentBookTitle);
        toolbar.add(Box.createHorizontalGlue());
        toolbar.add(settingsButton);

        return toolbar;
    }

    private JScrollPane createTextWrapper() {
        // ===== Content components ======

        // ===== Settings ======
        textHtml = new JEditorPane();
        textHtml.setEditable(false);
        textHtml.setCursor(new Cursor(Cursor.TEXT_CURSOR));
        textHtml.setContentType("text/html");
        textHtml.setPreferredSize(new Dimension(500, 500));

        // ===== Content ======
        JScrollPane textHtmlWrapper = new JScrollPane(this.textHtml);
        textHtmlWrapper.setMaximumSize(new Dimension(1280, 720));
        textHtmlWrapper.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 0));
        textHtmlWrapper.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        textHtmlWrapper.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        // Change the scrollbar unit increment when window is resized
        textHtmlWrapper.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                Dimension textHeight = textHtml.getSize();
                System.out.println(textHeight);
                textHtmlWrapper.getVerticalScrollBar().setUnitIncrement(textHeight.height / 306);
            }
        });

        return textHtmlWrapper;
    }

    private JPopupMenu createPopupMenu() throws IOException {
        // Create menu items
        JMenuItem copyItem = new JMenuItem("Copier");
        Image copyIcon = ImageIO.read(getClass().getResource("/app/assets/copy.png"));
        copyItem.setIcon(new ImageIcon(copyIcon));
        JMenuItem annotateItem = new JMenuItem("Annoter");
        Image noteIcon = ImageIO.read(getClass().getResource("/app/assets/note.png"));
        annotateItem.setIcon(new ImageIcon(noteIcon));
        JMenuItem highlightItem = new JMenuItem("Surligner");
        Image highlightIcon = ImageIO.read(getClass().getResource("/app/assets/highlight.png"));
        highlightItem.setIcon(new ImageIcon(highlightIcon));
        JMenuItem defineItem = new JMenuItem("Obtenir la définition");
        Image defineIcon = ImageIO.read(getClass().getResource("/app/assets/define.png"));
        defineItem.setIcon(new ImageIcon(defineIcon));

        // Associate actions to menu items
        copyItem.addActionListener(e -> {
            StringSelection stringSelection = new StringSelection(textHtml.getSelectedText());
            Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
            clipboard.setContents(stringSelection, null);
        });

        // Create the popup menu and add the menu items
        JPopupMenu popupMenu = new JPopupMenu();
        popupMenu.add(copyItem);
        popupMenu.addSeparator();
        popupMenu.add(annotateItem);
        popupMenu.add(highlightItem);
        popupMenu.addSeparator();
        popupMenu.add(defineItem);

        // Show the popup menu when right-click on the text area
        this.textHtml.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getButton() == BUTTON3) {
                    popupMenu.show(e.getComponent(), e.getX(), e.getY());
                }
            }
        });

        return popupMenu;
    }

    public void show_book(Book book) {
        // Set title
        currentBookTitle.setText(book.get_title());

        // Set text
        if (book.is_downloaded()) {
            try {
                File file = new File(book.get_path());
                this.textHtml.setPage(file.toURI().toURL());
            } catch (IOException e) {
                e.printStackTrace();
                this.textHtml.setText("<html>Page not found.</html>");
            }
        } else {
            try {
                this.textHtml.setPage(book.get_path());
            } catch (IOException e) {
                e.printStackTrace();
                this.textHtml.setText("<html>Page not found.</html>");
            }
        }
    }

    public void change_font(String font, String fontSize) {
        textHtml.setFont(new Font(font, Font.PLAIN, Integer.parseInt(fontSize)));
    }
}
