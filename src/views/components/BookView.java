package views.components;

import models.entities.Annotation;
import models.entities.Book;
import org.json.simple.JSONObject;
import views.View;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.text.*;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


import static java.awt.event.MouseEvent.BUTTON3;

public class BookView extends JPanel {
    private JEditorPane textHtml;
    private JScrollPane textHtmlWrapper;
    private JButton back_button;
    private JButton settingsButton;
    private JPanel toolbar;
    private JLabel currentBookTitle;
    private String currentCategory;
    private View view;
    private JPopupMenu popupMenu;
    private HashMap<Integer, Highlighter.Highlight> highlights;
    private Book book;
    private int lastMousePosition;
    HashMap<Integer, Annotation> annotations;

    public BookView(View view) throws IOException {
        this.view = view;

        // ===== Composants ======
        textHtmlWrapper = createTextWrapper();
        toolbar = createToolbar();
        popupMenu = createPopupMenu();
        highlights = new HashMap<>();

        // ===== Parametres ======
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        // ===== Contenu ======
        add(toolbar);
        add(textHtmlWrapper);
    }

    private JPanel createToolbar() throws IOException {
        // ===== Composants ======
        // Go back button
        back_button = new JButton("Revenir en arrière");
        back_button.setEnabled(true);
        Image backIcon = ImageIO.read(getClass().getResource("/assets/back.png"));
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
        Image settingsIcon = ImageIO.read(getClass().getResource("/assets/settings.png"));
        settingsButton.setIcon(new ImageIcon(settingsIcon));

        // Title
        currentBookTitle = new JLabel("Titre du livre", SwingConstants.CENTER);
        currentBookTitle.setFont((currentBookTitle.getFont()).deriveFont(Font.PLAIN, 22));
        currentBookTitle.setPreferredSize(new Dimension(0, 27));
        currentBookTitle.setMinimumSize(new Dimension(0, 27));
        currentBookTitle.setMaximumSize(new Dimension(Short.MAX_VALUE * 10, 27));

        // ===== Parametres ======
        JPanel toolbar = new JPanel();
        toolbar.setLayout(new BoxLayout(toolbar, FlowLayout.RIGHT));
        toolbar.setBorder(new CompoundBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, new Color(128, 128, 128, 128)),
                BorderFactory.createEmptyBorder(10, 20, 10, 20)
        ));

        // Show the settings dialog when clicking on the settings button
        settingsButton.addActionListener(e -> {
            try {
                new SettingsDialog(view, this.getSize().width / 2, this.getSize().height / 2, this);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });

        // ===== Contenu ======
        toolbar.add(back_button);
        toolbar.add(Box.createHorizontalGlue());
        toolbar.add(currentBookTitle);
        toolbar.add(Box.createHorizontalGlue());
        toolbar.add(settingsButton);

        return toolbar;
    }

    private JScrollPane createTextWrapper() {
        // ===== Composants ======

        // ===== Parametres ======
        textHtml = new JEditorPane();
        textHtml.setEditable(false);
        textHtml.setCursor(new Cursor(Cursor.TEXT_CURSOR));
        textHtml.setContentType("text/html");
        textHtml.setPreferredSize(new Dimension(500, 500));
        textHtml.setBorder(BorderFactory.createEmptyBorder(0, 320, 0, 320));
        textHtml.setOpaque(false);

        // ===== Contenu ======
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
        JMenuItem viewAnnotateItem = new JMenuItem("Visualiser l'annotation");
        Image viewAnnotateIcon = ImageIO.read(getClass().getResource("/assets/view.png"));
        viewAnnotateItem.setIcon(new ImageIcon(viewAnnotateIcon));
        JMenuItem annotateItem = new JMenuItem("Créer une annotation");
        Image noteIcon = ImageIO.read(getClass().getResource("/assets/note.png"));
        annotateItem.setIcon(new ImageIcon(noteIcon));
        JMenuItem deleteAnnotateItem = new JMenuItem("Supprimer l'annotation");
        Image deleteNoteIcon = ImageIO.read(getClass().getResource("/assets/remove.png"));
        deleteAnnotateItem.setIcon(new ImageIcon(deleteNoteIcon));
        JMenuItem defineItem = new JMenuItem("Obtenir la définition");
        Image defineIcon = ImageIO.read(getClass().getResource("/assets/define.png"));
        defineItem.setIcon(new ImageIcon(defineIcon));

        // Associate actions to menu items
        copyItem.addActionListener(e -> {
            // get the number of possible lines in the current view port
            int lines = textHtml.getPreferredSize().height / textHtml.getFontMetrics(textHtml.getFont()).getHeight();
            System.out.println(lines);

            System.out.println("line count: " + (textHtml.getDocument().getDefaultRootElement().getElementCount() - 1));
            int lineCount = textHtml.getDocument().getDefaultRootElement().getElementCount() - 1;
            System.out.println("offset: " + textHtml.getDocument().getDefaultRootElement().getElement(textHtml.getDocument().getDefaultRootElement().getElementCount() - 1).getStartOffset());
            int offset = textHtml.getDocument().getDefaultRootElement().getElement(textHtml.getDocument().getDefaultRootElement().getElementCount() - 1).getStartOffset();

            textHtml.setCaretPosition(0);
            textHtml.setCaretPosition(7442);

            System.out.println(textHtml.getText().indexOf("Arnold"));

            return;

        });

        viewAnnotateItem.addActionListener(e -> {
            // On verifie si un surlignage qui passe par la position du curseur existe
            Highlighter highlighter = textHtml.getHighlighter();
            Highlighter.Highlight[] highlightIndex = highlighter.getHighlights();
            for (int i = 0; i < highlightIndex.length - 1; i++) {
                Highlighter.Highlight h = highlightIndex[i];
                if (h.getStartOffset() <= lastMousePosition && h.getEndOffset() >= lastMousePosition) {
                    JOptionPane.showMessageDialog(this, annotations.get(h.getStartOffset()).get_text(), "Annotation", JOptionPane.INFORMATION_MESSAGE);
                    return;
                }
            }
            JOptionPane.showMessageDialog(this, "Il faut sélectionner une annotation pour le visualiser.", "Erreur", JOptionPane.ERROR_MESSAGE);
        });

        annotateItem.addActionListener(e -> {
            StringSelection stringSelection = new StringSelection(textHtml.getSelectedText());
            int start = textHtml.getSelectionStart();
            int end = textHtml.getSelectionEnd();

            if (start - end == 0) {
                JOptionPane.showMessageDialog(this, "Il faut sélectionner un texte pour l'annoter.", "Erreur", JOptionPane.ERROR_MESSAGE);
                return;
            }
            // Verifier si le texte est déjà annoté
            Highlighter highlighter = textHtml.getHighlighter();
            Highlighter.Highlight[] highlightIndex = highlighter.getHighlights();
            for (int i = 0; i < highlightIndex.length - 1; i++) {
                Highlighter.Highlight h = highlightIndex[i];
                if ((start <= h.getStartOffset() && end >= h.getStartOffset()) || (start <= h.getEndOffset() && end >= h.getEndOffset()) || (start >= h.getStartOffset() && end <= h.getEndOffset())) {
                    JOptionPane.showMessageDialog(this, "Il y a une autre annotation qui empiète sur l'emplacement séléctionné.", "Erreur", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }

            try {
                new AnnotationDialog(view, this.getSize().width / 2, this.getSize().height / 2, this, currentCategory, currentBookTitle.getText(), start, end);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });

        deleteAnnotateItem.addActionListener(e -> {
            // On verifie si un surlignage qui passe par la position du curseur existe
            Highlighter highlighter = textHtml.getHighlighter();
            Highlighter.Highlight[] highlightIndex = highlighter.getHighlights();
            for (Highlighter.Highlight h : highlightIndex) {
                if (h.getStartOffset() <= lastMousePosition && h.getEndOffset() >= lastMousePosition) {
                    Annotation a = annotations.get(h.getStartOffset());
                    if (a != null) {
                        try {
                            view.notify_delete_annotation(currentCategory, currentBookTitle.getText(), a.get_text(), a.get_start(), a.get_start());
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }
                    }
                    highlighter.removeHighlight(h);
                    return;
                }
            }
            JOptionPane.showMessageDialog(this, "Il faut sélectionner une annotation pour le supprimer.", "Erreur", JOptionPane.ERROR_MESSAGE);
        });

        defineItem.addActionListener(e -> {
            String definition = view.notify_definition_request(textHtml.getSelectedText());
            if (definition == null) {
                JOptionPane.showMessageDialog(this, "Aucune définition trouvée. Merci de choisir un seul mot et en anglais.", "Erreur", JOptionPane.ERROR_MESSAGE);
            }
            else {
                JOptionPane.showMessageDialog(this, definition, "Définition : " + textHtml.getSelectedText(), JOptionPane.INFORMATION_MESSAGE);
            }

        });

        // Create the popup menu and add the menu items
        JPopupMenu popupMenu = new JPopupMenu();
        popupMenu.add(copyItem);
        popupMenu.addSeparator();
        popupMenu.add(viewAnnotateItem);
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

    private boolean two_intervals_overlap(int start1, int end1, int start2, int end2) {
        return (start1 <= start2 && end1 >= start2) || (start1 <= end2 && end1 >= end2) || (start1 >= start2 && end1 <= end2);
    }

    public void show_book(Book book) throws IOException, InterruptedException, BadLocationException {
        // Si on a un livre en mémoire et qu'on veut réafficher le même livre, on a pas besoin de charger le fichier
        if (this.book != null && book.get_title().equals(this.book.get_title())) {
            return;
        }

        // Set title and category
        currentBookTitle.setText(book.get_title());
        currentCategory = book.get_category();
        String path = book.get_path();
        this.book = book;

        // Set text
        if (book.is_downloaded()) {
            try {
                File file = new File(book.get_path());
                textHtml.setPage(file.toURI().toURL());
            } catch (IOException e) {
                e.printStackTrace();
                this.textHtml.setText("<html>Fichier local du livre non trouvé. Merci de réessayer avec un autre.</html>");
            }
        } else {
            try {
                this.textHtml.setPage(book.get_path());
            } catch (IOException e) {
                e.printStackTrace();
                this.textHtml.setText("<html>Fichier distant du livre non trouvé. Merci de réessayer avec un autre.</html>");
            }
        }

        // Charger les annotations
        show_highlights();
    }

    public void show_highlights() {
        // Charger les annotations
        annotations = new HashMap<>();
        annotations = book.load_annotations();
        textHtml.getHighlighter().removeAllHighlights();

        // Ajouter un surlignage pour chaque annotation
        for (Map.Entry<Integer, Annotation> entry : annotations.entrySet()) {
            Annotation annotation = entry.getValue();
            Highlighter highlighter = textHtml.getHighlighter();
            DefaultHighlighter.DefaultHighlightPainter highlightPainter =
                    new DefaultHighlighter.DefaultHighlightPainter(new Color(20, 150, 233, 60));
            try {
                highlighter.addHighlight(annotation.get_start(), annotation.get_end(),
                        highlightPainter);
            } catch (BadLocationException ex) {
                ex.printStackTrace();
            }
            Highlighter.Highlight[] highlightIndex = highlighter.getHighlights();
            highlights.put(annotation.get_start(), highlightIndex[highlightIndex.length - 1]);
        }
    }

    public void change_font(String font, String fontSize) {
        textHtml.setFont(new Font(font, Font.PLAIN, Integer.parseInt(fontSize)));
    }
}
