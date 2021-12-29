package views.components;

import models.entities.Annotation;
import models.entities.Book;
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
    private final View view;
    private Book current_book;

    private final JPanel toolbar;
    private JEditorPane text_html;
    private JScrollPane text_html_wrapper;
    private JButton back_button;
    private JButton settings_button;
    private JLabel current_book_title;
    private final JPopupMenu popup_menu;

    private final HashMap<Integer, Highlighter.Highlight> highlights;
    private HashMap<Integer, Annotation> annotations;
    private String current_category;
    private int last_mouse_position;

    public BookView(View view) throws IOException {
        this.view = view;

        // Parametres ==================================================================================================
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        // Composants ==================================================================================================
        text_html_wrapper = create_text_wrapper();
        toolbar = createToolbar();
        popup_menu = create_popup_menu();
        highlights = new HashMap<>();

        // Contenu =====================================================================================================
        add(toolbar);
        add(text_html_wrapper);
    }

    private JPanel createToolbar() throws IOException {
        // Composants ==================================================================================================
        // Boutons
        Image back_icon = ImageIO.read(getClass().getResource("/assets/back.png"));
        back_button = new JButton("Revenir en arrière");
        back_button.setIcon(new ImageIcon(back_icon));

        Image settings_icon = ImageIO.read(getClass().getResource("/assets/settings.png"));
        settings_button = new JButton("Paramètres d'affichage");
        settings_button.setIcon(new ImageIcon(settings_icon));

        // Action listeners
        back_button.addActionListener(e -> {
            back_button_action_listener();
        });
        settings_button.addActionListener(e -> {
            settings_button_action_listener();
        });

        // Titre du livre
        current_book_title = new JLabel("Chargement du livre en cours...", SwingConstants.CENTER);
        current_book_title.setFont((current_book_title.getFont()).deriveFont(Font.PLAIN, 22));
        current_book_title.setPreferredSize(new Dimension(0, 27));
        current_book_title.setMinimumSize(new Dimension(0, 27));
        current_book_title.setMaximumSize(new Dimension(Short.MAX_VALUE * 10, 27));

        // Parametres ==================================================================================================
        JPanel toolbar = new JPanel();
        toolbar.setLayout(new BoxLayout(toolbar, FlowLayout.RIGHT));
        toolbar.setBorder(new CompoundBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, new Color(128, 128, 128, 128)),
                BorderFactory.createEmptyBorder(10, 20, 10, 20)
        ));

        // Contenu =====================================================================================================
        toolbar.add(back_button);
        toolbar.add(Box.createHorizontalGlue());
        toolbar.add(current_book_title);
        toolbar.add(Box.createHorizontalGlue());
        toolbar.add(settings_button);

        return toolbar;
    }

    // Fonctions pour creer l'interface ================================================================================
    private JScrollPane create_text_wrapper() {
        // Parametres ==================================================================================================
        text_html = new JEditorPane();
        text_html.setEditable(false);
        text_html.setCursor(new Cursor(Cursor.TEXT_CURSOR));
        text_html.setContentType("text/html");
        text_html.setPreferredSize(new Dimension(500, 500));
        text_html.setBorder(BorderFactory.createEmptyBorder(0, 320, 0, 320));
        text_html.setOpaque(false);

        // Contenu =====================================================================================================
        text_html_wrapper = new JScrollPane(text_html);
        text_html_wrapper.setMaximumSize(new Dimension(1280, 720));
        text_html_wrapper.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 0));
        text_html_wrapper.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        text_html_wrapper.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        return text_html_wrapper;
    }

    private JPopupMenu create_popup_menu(){
        // Composants ==================================================================================================
        // Boutons
        JMenuItem copy_btn = new JMenuItem("Copier");
        JMenuItem view_annotation_btn = new JMenuItem("Visualiser l'annotation");
        JMenuItem annotate_btn = new JMenuItem("Créer une annotation");
        JMenuItem remove_annotation_btn = new JMenuItem("Supprimer l'annotation");
        JMenuItem define_btn = new JMenuItem("Obtenir la définition");

        Image view_annotation_icon = null; Image copy_icon = null; Image annotate_icon = null;
        Image remove_annotation_icon = null; Image define_icon = null;

        try {
            view_annotation_icon = ImageIO.read(getClass().getResource("/assets/view.png"));
            copy_icon = ImageIO.read(getClass().getResource("/assets/copy.png"));
            annotate_icon = ImageIO.read(getClass().getResource("/assets/note.png"));
            remove_annotation_icon = ImageIO.read(getClass().getResource("/assets/remove.png"));
            define_icon = ImageIO.read(getClass().getResource("/assets/define.png"));
        } catch (IOException e) {
            // ignorer, au pire on aura pas d'iconen
        }

        copy_btn.setIcon(new ImageIcon(copy_icon));
        view_annotation_btn.setIcon(new ImageIcon(view_annotation_icon));
        annotate_btn.setIcon(new ImageIcon(annotate_icon));
        remove_annotation_btn.setIcon(new ImageIcon(remove_annotation_icon));
        define_btn.setIcon(new ImageIcon(define_icon));

        // On associe des actions a chaque bouton
        copy_btn.addActionListener(e -> {
            copy_button_action_listener();
        });
        view_annotation_btn.addActionListener(e -> {
            view_annotate_button_action_listener();
        });
        annotate_btn.addActionListener(e -> {
            annotate_button_action_listener();
        });
        remove_annotation_btn.addActionListener(e -> {
            remove_annotation_button_action_listener();
        });
        define_btn.addActionListener(e -> {
            define_button_action_listener();
        });

        // On cree le menu de clique droit
        JPopupMenu popup_menu = new JPopupMenu();
        popup_menu.add(copy_btn);
        popup_menu.addSeparator();
        popup_menu.add(view_annotation_btn);
        popup_menu.add(annotate_btn);
        popup_menu.add(remove_annotation_btn);
        popup_menu.addSeparator();
        popup_menu.add(define_btn);

        // Afficher le menu quand on clique droit
        text_html.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getButton() == BUTTON3) {
                    // Garder en memoire la position du clique
                    last_mouse_position = text_html.viewToModel2D(e.getPoint());
                    // Afficher le menu
                    popup_menu.show(e.getComponent(), e.getX(), e.getY());
                }
            }
        });

        return popup_menu;
    }

    // Fonctions appelle par le presentateur en reponse a une action utilisateur =======================================
    public void show_book(Book book) {
        // Modifier le titre et la cateorie
        current_book_title.setText(book.get_title());
        current_category = book.get_category();
        this.current_book = book;

        // Set text
        if (book.is_downloaded()) {
            try {
                File file = new File(book.get_path());
                text_html.setPage(file.toURI().toURL());
            } catch (IOException e) {
                e.printStackTrace();
                this.text_html.setText("<html>Fichier local du livre non trouvé. " +
                        "Merci de réessayer avec un autre.</html>");
            }
        } else {
            try {
                text_html.setPage(book.get_path());
            } catch (IOException e) {
                e.printStackTrace();
                this.text_html.setText("<html>Fichier distant du livre non trouvé. " +
                        "Merci de réessayer avec un autre.</html>");
            }
        }

        // Charger les annotations
        show_highlights();
    }
    
    public void show_highlights() {
        // Charger les annotations
        annotations = new HashMap<>();
        annotations = current_book.load_annotations();
        text_html.getHighlighter().removeAllHighlights();

        // Ajouter un surlignage pour chaque annotation
        for (Map.Entry<Integer, Annotation> entry : annotations.entrySet()) {
            Annotation annotation = entry.getValue();
            Highlighter highlighter = text_html.getHighlighter();
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
        text_html.setFont(new Font(font, Font.PLAIN, Integer.parseInt(fontSize)));
    }

    public void show_definition_result(String word, String definition) {
        if (definition == null) {
            JOptionPane.showMessageDialog(this,
                    "Aucune définition trouvée. Merci de choisir un seul mot et en " +
                    "anglais.", "Erreur", JOptionPane.ERROR_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, definition,
                    "Définition de \"" + word + "\"", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    // Actions listeners ===============================================================================================
    private void back_button_action_listener() {
        try {
            // Notifier la vue qu'on veut aller en arrière
            view.notify_back_performed();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void settings_button_action_listener() {
        try {
            // On ouvre un nouvau dialogue pour les paramètres
            new SettingsDialog(view, this.getSize().width / 2, this.getSize().height / 2, this);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void copy_button_action_listener() {
       // Copier le texte selectionné dans le presse papier
       String selectedText = text_html.getSelectedText();
       if (selectedText != null) {
           StringSelection stringSelection = new StringSelection(selectedText);
           Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
           clipboard.setContents(stringSelection, null);
       }
    }

    private void view_annotate_button_action_listener() {
        // On verifie si un surlignage qui passe par la position du curseur existe
        Highlighter highlighter = text_html.getHighlighter();
        Highlighter.Highlight[] highlights = highlighter.getHighlights();

        for (int i = 0; i < highlights.length - 1; i++) {
            Highlighter.Highlight h = highlights[i];
            if (h.getStartOffset() <= last_mouse_position && h.getEndOffset() >= last_mouse_position) {
                // On a trouvé un surlignage qui passe par la position du curseur, on l'affiche
                JOptionPane.showMessageDialog(this, annotations.get(h.getStartOffset()).get_text(),
                        "Annotation", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
        }

        // On a pas trouvé de surlignage qui passe par la position du curseur, on affiche un message d'erreur
        JOptionPane.showMessageDialog(this, "Il faut sélectionner une annotation pour le visualiser.", "Erreur", JOptionPane.ERROR_MESSAGE);
    }

    private void annotate_button_action_listener() {
        // On recupere la position de debut et la fin du texte selectionne
        int start = text_html.getSelectionStart();
        int end = text_html.getSelectionEnd();

        // Si le livre n'est pas telecharge, on peut pas annoter, on affiche un message d'erreur
        if (current_book.is_downloaded() == false) {
            JOptionPane.showMessageDialog(this, "Il faut télécharger le livre avant de " +
                    "pouvoir ajouter des annotations.", "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }
        // Si y a aucun texte selectionne, on affiche un message d'erreur
        if (start - end == 0) {
            JOptionPane.showMessageDialog(this, "Il faut sélectionner un texte pour " +
                    "l'annoter.", "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // On verifie si un surlignage qui passe par la position du curseur existe
        Highlighter highlighter = text_html.getHighlighter();
        Highlighter.Highlight[] highlightIndex = highlighter.getHighlights();

        for (int i = 0; i < highlightIndex.length - 1; i++) {
            Highlighter.Highlight h = highlightIndex[i];
            if ((start <= h.getStartOffset() && end >= h.getStartOffset()) ||
                    (start <= h.getEndOffset() && end >= h.getEndOffset()) ||
                    (start >= h.getStartOffset() && end <= h.getEndOffset())) {
                // Il y a un surlignage qui passe par la position du curseur, on affiche un message d'erreur
                JOptionPane.showMessageDialog(this, "Il y a une autre annotation qui " +
                        "empiète sur l'emplacement séléctionné.", "Erreur", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }

        try {
            // On affiche l'annotation
            new AnnotationDialog(view, this.getSize().width / 2, this.getSize().height / 2,
                    this, current_category, current_book.get_id(), start, end);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void remove_annotation_button_action_listener() {
        // On verifie si un surlignage qui passe par la position du curseur existe
        Highlighter highlighter = text_html.getHighlighter();
        Highlighter.Highlight[] highlightIndex = highlighter.getHighlights();

        for (Highlighter.Highlight h : highlightIndex) {
            if (h.getStartOffset() <= last_mouse_position && h.getEndOffset() >= last_mouse_position) {
                Annotation a = annotations.get(h.getStartOffset());
                if (a != null) {
                    try {
                        // Si oui on notifie la vue qu'on veut supprimer l'annotation
                        view.notify_annotation_remove_performed(current_category, current_book.get_id(), a.get_text(),
                                a.get_start(), a.get_start());
                        // On supprime le surlignage
                        highlighter.removeHighlight(h);
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
                return;
            }
        }

        // On a pas trouvé de surlignage qui passe par la position du curseur, on affiche un message d'erreur
        JOptionPane.showMessageDialog(this, "Il faut sélectionner une annotation pour " +
                "le supprimer.", "Erreur", JOptionPane.ERROR_MESSAGE);
    }

    private void define_button_action_listener() {
        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() throws Exception {
                view.notify_definition_request(text_html.getSelectedText());
                return null;
            }
        };
        worker.execute();
    }
}
