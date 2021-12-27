package views.components;

import models.entities.Book;
import views.View;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.text.*;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

import static java.awt.event.MouseEvent.BUTTON3;

public class BookView extends JPanel {
    private JEditorPane textHtml;
    private JScrollPane textHtmlWrapper;
    private JButton back_button;
    private JButton settingsButton;
    private JPanel toolbar;
    private JLabel currentBookTitle;
    private View view;
    private JPopupMenu popupMenu;
    private HashMap<Integer, Highlighter.Highlight> highlights;
    private Book book;
    private int lastMousePosition;

    public BookView(View view) throws IOException {
        this.view = view;

        // ===== Content components ======
        textHtmlWrapper = createTextWrapper();
        toolbar = createToolbar();
        popupMenu = createPopupMenu();
        highlights = new HashMap<>();

        // ===== Settings ======
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        // ===== Content ======
        add(toolbar);
        add(textHtmlWrapper);
    }











    private JPanel createToolbar() throws IOException {
        // ===== Content components ======
        // Go back button
        back_button = new JButton("Revenir en arrière");
        back_button.setEnabled(true);
        Image backIcon = ImageIO.read(getClass().getResource("/assets/back.png"));
        back_button.setIcon(new ImageIcon(backIcon));
        back_button.addActionListener(e -> {
            // set cursor at the end
//            textHtml.setCaretPosition(textHtml.getDocument().getLength());
//            System.out.println(textHtml.getText().split("\n").length);

            System.out.println("line count: " + (textHtml.getDocument().getDefaultRootElement().getElementCount() - 1));
            System.out.println("offset: " + textHtml.getDocument().getDefaultRootElement().getElement(textHtml.getDocument().getDefaultRootElement().getElementCount() - 1).getStartOffset());

            textHtml.setCaretPosition(0);
            textHtml.setCaretPosition(textHtml.getDocument().getDefaultRootElement().getElement(100).getStartOffset());

            System.out.println(textHtml.getText().indexOf("Arnold"));

            return;
//            try {
//                view.notify_back_performed();
//            } catch (IOException ex) {
//                ex.printStackTrace();
//            }
        });

        // Settings button
        settingsButton = new JButton("Paramètres d'affichage");
        settingsButton.setEnabled(true);
        Image settingsIcon = ImageIO.read(getClass().getResource("/assets/settings.png"));
        settingsButton.setIcon(new ImageIcon(settingsIcon));

        // Title
        currentBookTitle = new JLabel("Titre du livre", SwingConstants.CENTER);
        currentBookTitle.setFont((currentBookTitle.getFont()).deriveFont(Font.PLAIN, 22));
        currentBookTitle.setPreferredSize(new Dimension(0, 27));
        currentBookTitle.setMinimumSize(new Dimension(0, 27));
        currentBookTitle.setMaximumSize(new Dimension(Short.MAX_VALUE * 10, 27));

        // ===== Settings ======
        JPanel toolbar = new JPanel();
        toolbar.setLayout(new BoxLayout(toolbar, FlowLayout.RIGHT));
        toolbar.setBorder(new CompoundBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, new Color(128, 128, 128, 128)),
                BorderFactory.createEmptyBorder(10, 20, 10, 20)
        ));

        // Show the settings dialog when clicking on the settings button
        settingsButton.addActionListener(e -> {
            try {
                new Settings(view, this.getSize().width / 2, this.getSize().height / 2, this);
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
        textHtml.setBorder(BorderFactory.createEmptyBorder(0, 320, 0, 320));
        textHtml.setOpaque(false);

        // ===== Content ======
        textHtmlWrapper = new JScrollPane(textHtml);
        textHtmlWrapper.setMaximumSize(new Dimension(1280, 720));
        textHtmlWrapper.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 0));
        textHtmlWrapper.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        textHtmlWrapper.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        return textHtmlWrapper;
    }

    private JPopupMenu createPopupMenu() throws IOException {
        // Create menu items
        JMenuItem copyItem = new JMenuItem("Copier");
        Image copyIcon = ImageIO.read(getClass().getResource("/assets/copy.png"));
        copyItem.setIcon(new ImageIcon(copyIcon));
        JMenuItem annotateItem = new JMenuItem("Annotater");
        Image noteIcon = ImageIO.read(getClass().getResource("/assets/note.png"));
        annotateItem.setIcon(new ImageIcon(noteIcon));
        JMenuItem deleteAnnotateItem = new JMenuItem("Supprimer annotation");
        Image deleteNoteIcon = ImageIO.read(getClass().getResource("/assets/remove.png"));
        deleteAnnotateItem.setIcon(new ImageIcon(deleteNoteIcon));
        JMenuItem highlightItem = new JMenuItem("Surligner");
        Image highlightIcon = ImageIO.read(getClass().getResource("/assets/highlight.png"));
        highlightItem.setIcon(new ImageIcon(highlightIcon));
        JMenuItem unhighlightItem = new JMenuItem("Supprimer surlignage");
        Image unhighlightIcon = ImageIO.read(getClass().getResource("/assets/unhighlight.png"));
        unhighlightItem.setIcon(new ImageIcon(unhighlightIcon));
        JMenuItem defineItem = new JMenuItem("Obtenir la définition");
        Image defineIcon = ImageIO.read(getClass().getResource("/assets/define.png"));
        defineItem.setIcon(new ImageIcon(defineIcon));

        // Associate actions to menu items
        copyItem.addActionListener(e -> {
            StringSelection stringSelection = new StringSelection(textHtml.getSelectedText());
            Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
            clipboard.setContents(stringSelection, null);
        });

        highlightItem.addActionListener(e -> {
            StringSelection stringSelection = new StringSelection(textHtml.getSelectedText());
            int start = textHtml.getSelectionStart();
            int end = textHtml.getSelectionEnd();
            if (start - end == 0) {
                JOptionPane.showMessageDialog(this, "Il faut sélectionner un texte pour le surligner", "Erreur", JOptionPane.ERROR_MESSAGE);
                return;
            }
            Highlighter highlighter = textHtml.getHighlighter();
            DefaultHighlighter.DefaultHighlightPainter highlightPainter =
                    new DefaultHighlighter.DefaultHighlightPainter(new Color(20, 150, 233, 60));
            try {
                highlighter.addHighlight(start, end,
                        highlightPainter);
            } catch (BadLocationException ex) {
                ex.printStackTrace();
            }
            Highlighter.Highlight[] highlightIndex = highlighter.getHighlights();
            highlights.put(start, highlightIndex[highlightIndex.length - 1]);
            textHtml.setSelectionEnd(start);
        });

        unhighlightItem.addActionListener(e -> {
            // On verifie si un surlignage qui passe par la position du curseur existe
            Highlighter highlighter = textHtml.getHighlighter();
            Highlighter.Highlight[] highlightIndex = highlighter.getHighlights();
            for (Highlighter.Highlight h : highlightIndex) {
                System.out.println(h.getStartOffset() + " " + h.getEndOffset());
                if (h.getStartOffset() <= lastMousePosition && h.getEndOffset() >= lastMousePosition) {
                    highlighter.removeHighlight(h);
                    return;
                }
            }
            JOptionPane.showMessageDialog(this, "Aucun surlignage n'est sélectionné", "Erreur", JOptionPane.ERROR_MESSAGE);
        });

        annotateItem.addActionListener(e -> {
            StringSelection stringSelection = new StringSelection(textHtml.getSelectedText());
            int start = textHtml.getSelectionStart();
            int end = textHtml.getSelectionEnd();
            if (start - end == 0) {
                JOptionPane.showMessageDialog(this, "Il faut sélectionner un texte pour le surligner", "Erreur", JOptionPane.ERROR_MESSAGE);
                return;
            }
            Highlighter highlighter = textHtml.getHighlighter();
            DefaultHighlighter.DefaultHighlightPainter highlightPainter =
                    new DefaultHighlighter.DefaultHighlightPainter(new Color(220, 40, 50, 60));
            try {
                highlighter.addHighlight(start, end,
                        highlightPainter);
            } catch (BadLocationException ex) {
                ex.printStackTrace();
            }
            Highlighter.Highlight[] highlightIndex = highlighter.getHighlights();
            highlights.put(start, highlightIndex[highlightIndex.length - 1]);
            textHtml.setSelectionEnd(start);
        });

        deleteAnnotateItem.addActionListener(e -> {
            // On verifie si un surlignage qui passe par la position du curseur existe
            Highlighter highlighter = textHtml.getHighlighter();
            Highlighter.Highlight[] highlightIndex = highlighter.getHighlights();
            for (Highlighter.Highlight h : highlightIndex) {
                System.out.println(h.getStartOffset() + " " + h.getEndOffset());
                if (h.getStartOffset() <= lastMousePosition && h.getEndOffset() >= lastMousePosition) {
                    highlighter.removeHighlight(h);
                    return;
                }
            }
            JOptionPane.showMessageDialog(this, "Aucun surlignage n'est sélectionné", "Erreur", JOptionPane.ERROR_MESSAGE);
        });

        // Create the popup menu and add the menu items
        JPopupMenu popupMenu = new JPopupMenu();
        popupMenu.add(copyItem);
        popupMenu.addSeparator();
        popupMenu.add(highlightItem);
        popupMenu.add(unhighlightItem);
        popupMenu.add(annotateItem);
        popupMenu.add(deleteAnnotateItem);
        popupMenu.addSeparator();
        popupMenu.add(defineItem);

        // Show the popup menu when right-click on the text area
        textHtml.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getButton() == BUTTON3) {
                    lastMousePosition = textHtml.viewToModel2D(e.getPoint());
                    
                    // Montrer le menu de clique droit
                    popupMenu.show(e.getComponent(), e.getX(), e.getY());
                }
            }
        });

        return popupMenu;
    }

    public void show_book(Book book) throws IOException, InterruptedException, BadLocationException {
        // Set title
        currentBookTitle.setText(book.get_title());
        System.out.println(book.get_path());

        // Set text
        if (book.is_downloaded()) {
            try {
                File file = new File(book.get_path());
                textHtml.setPage(file.toURI().toURL());
            } catch (IOException e) {
                e.printStackTrace();
                this.textHtml.setText("<html>Livre non trouvé. Merci de réessayer avec un autre.</html>");
            }
        } else {
            try {
                this.textHtml.setPage(book.get_path());
            } catch (IOException e) {
                e.printStackTrace();
                this.textHtml.setText("<html>Livre non trouvé. Merci de réessayer avec un autre.</html>");
            }
        }
    }

    public void change_font(String font, String fontSize) {
        textHtml.setFont(new Font(font, Font.PLAIN, Integer.parseInt(fontSize)));
    }
}
