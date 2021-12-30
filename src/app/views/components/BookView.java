package app.views.components;

import app.models.entities.Annotation;
import app.models.entities.Book;
import app.views.View;

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
    private final JPanel top_panel;
    private JPanel main_panel;
    private JEditorPane text_pane;
    private JScrollPane text_scroll_pane;
    private JPanel text_scroll_pane_wrapper;
    private JButton back_button;
    private JButton settings_button;
    private JLabel current_book_title;
    private final JPopupMenu popup_menu;
    private JLabel scroll_percentage;

    private final View view;
    private Book current_book;
    private final HashMap<Integer, Highlighter.Highlight> highlights;
    private HashMap<Integer, Annotation> annotations;
    private String current_bookshelf;
    private int last_mouse_position;

    public BookView(View view) throws IOException {
        this.view = view;

        // Parametres ==================================================================================================
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        // Composants ==================================================================================================
        popup_menu = create_popup_menu();
        main_panel = create_main_panel();
        top_panel = create_top_panel();
        highlights = new HashMap<>();

        // Contenu =====================================================================================================
        add(top_panel);
        add(create_main_panel());
    }

    private JPanel create_top_panel() throws IOException {
        Image back_icon = ImageIO.read(getClass().getResource("/assets/back.png"));
        back_button = new JButton("Revenir en arrière");
        back_button.setIcon(new ImageIcon(back_icon));

        Image settings_icon = ImageIO.read(getClass().getResource("/assets/settings.png"));
        settings_button = new JButton("Paramètres d'affichage");
        settings_button.setIcon(new ImageIcon(settings_icon));

        back_button.addActionListener(e -> {
            back_button_action_listener();
        });
        settings_button.addActionListener(e -> {
            settings_button_action_listener();
        });

        current_book_title = new JLabel("Chargement du livre en cours...", SwingConstants.CENTER);
        current_book_title.setFont((current_book_title.getFont()).deriveFont(Font.PLAIN, 22));
        current_book_title.setPreferredSize(new Dimension(0, 27));
        current_book_title.setMinimumSize(new Dimension(0, 27));
        current_book_title.setMaximumSize(new Dimension(Short.MAX_VALUE * 10, 27));

        JPanel toolbar = new JPanel();
        toolbar.setLayout(new BoxLayout(toolbar, FlowLayout.RIGHT));
        toolbar.setBorder(new CompoundBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, new Color(128, 128, 128, 128)),
                BorderFactory.createEmptyBorder(10, 20, 10, 20)
        ));

        toolbar.add(back_button);
        toolbar.add(Box.createHorizontalGlue());
        toolbar.add(current_book_title);
        toolbar.add(Box.createHorizontalGlue());
        toolbar.add(settings_button);

        return toolbar;
    }

    private JPanel create_main_panel() {
        text_pane = new JEditorPane();
        text_pane.setEditable(false);
        text_pane.setOpaque(false);
        text_pane.setCursor(new Cursor(Cursor.TEXT_CURSOR));
        text_pane.setContentType("text/plain");

        text_pane.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getButton() == BUTTON3) {
                    last_mouse_position = text_pane.viewToModel2D(e.getPoint());
                    popup_menu.show(e.getComponent(), e.getX(), e.getY());
                }
            }
        });

        text_scroll_pane = new JScrollPane(text_pane);
        text_scroll_pane.setBorder(BorderFactory.createEmptyBorder(20, 20, 0, 20));
        text_scroll_pane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        text_scroll_pane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        text_scroll_pane.getVerticalScrollBar().setUnitIncrement(100);
        text_scroll_pane.getVerticalScrollBar().addAdjustmentListener(e -> {
            int scrolledAmount_ = e.getValue();
            int percentage = (int) (((double) scrolledAmount_ / (double) e.getAdjustable().getMaximum()) * 100);
            scroll_percentage.setText("Progression dans la lecture : " + percentage + "%");
        });

        text_scroll_pane_wrapper = new JPanel();
        text_scroll_pane_wrapper.setMaximumSize(new Dimension(700, Short.MAX_VALUE));
        text_scroll_pane_wrapper.setLayout(new BoxLayout(text_scroll_pane_wrapper, BoxLayout.X_AXIS));
        text_scroll_pane_wrapper.add(Box.createHorizontalGlue());
        text_scroll_pane_wrapper.add(text_scroll_pane);
        text_scroll_pane_wrapper.add(Box.createHorizontalGlue());

        JPanel bottom = new JPanel();
        bottom.setLayout(new BoxLayout(bottom, BoxLayout.X_AXIS));
        bottom.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        scroll_percentage = new JLabel("Progression dans la lecture : 0%");
        Image progressionIcon = null;
        try {
            progressionIcon = ImageIO.read(getClass().getResource("/assets/progression.png"));
        } catch (IOException e) {
            //
        }

        scroll_percentage.setIcon(new ImageIcon(progressionIcon));
        bottom.add(scroll_percentage);

        main_panel = new JPanel();
        main_panel.setLayout(new BoxLayout(main_panel, BoxLayout.Y_AXIS));

        main_panel.add(text_scroll_pane_wrapper);
        main_panel.add(bottom);

        return main_panel;
    }

    private JPopupMenu create_popup_menu() {
        JMenuItem copy_btn = new JMenuItem("Copier");
        JMenuItem view_annotation_btn = new JMenuItem("Visualiser l'annotation");
        JMenuItem annotate_btn = new JMenuItem("Créer une annotation");
        JMenuItem remove_annotation_btn = new JMenuItem("Supprimer l'annotation");
        JMenuItem define_btn = new JMenuItem("Obtenir la définition");

        Image view_annotation_icon = null;
        Image copy_icon = null;
        Image annotate_icon = null;
        Image remove_annotation_icon = null;
        Image define_icon = null;

        try {
            view_annotation_icon = ImageIO.read(getClass().getResource("/assets/view.png"));
            copy_icon = ImageIO.read(getClass().getResource("/assets/copy.png"));
            annotate_icon = ImageIO.read(getClass().getResource("/assets/note.png"));
            remove_annotation_icon = ImageIO.read(getClass().getResource("/assets/remove.png"));
            define_icon = ImageIO.read(getClass().getResource("/assets/define.png"));
        } catch (IOException e) {
            //
        }

        copy_btn.setIcon(new ImageIcon(copy_icon));
        view_annotation_btn.setIcon(new ImageIcon(view_annotation_icon));
        annotate_btn.setIcon(new ImageIcon(annotate_icon));
        remove_annotation_btn.setIcon(new ImageIcon(remove_annotation_icon));
        define_btn.setIcon(new ImageIcon(define_icon));

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

        JPopupMenu popup_menu = new JPopupMenu();
        popup_menu.add(copy_btn);
        popup_menu.addSeparator();
        popup_menu.add(view_annotation_btn);
        popup_menu.add(annotate_btn);
        popup_menu.add(remove_annotation_btn);
        popup_menu.addSeparator();
        popup_menu.add(define_btn);

        return popup_menu;
    }

    public void show_book(Book book) {
        current_book_title.setText(book.get_title());
        current_bookshelf = book.get_bookshelf();
        current_book = book;

        JScrollBar scrollBar = text_scroll_pane.getVerticalScrollBar();
        int percentage = (int) (((double) book.get_last_position() / (double) scrollBar.getMaximum()) * 100);
        SwingUtilities.invokeLater(() -> {
            text_scroll_pane.getVerticalScrollBar().setValue(book.get_last_position());
        });
        scroll_percentage.setText("Progression dans la lecture : " + percentage + "%");


        if (book.is_downloaded()) {
            try {
                File file = new File(book.get_path());
                text_pane.setPage(file.toURI().toURL());
            } catch (IOException e) {
                e.printStackTrace();
                this.text_pane.setText("Fichier local du livre non trouvé. " +
                        "Merci de réessayer avec un autre.");
            }
        } else {
            try {
                text_pane.setPage(book.get_path());
            } catch (IOException e) {
                e.printStackTrace();
                text_pane.setText("Fichier distant du livre non trouvé. " +
                        "Merci de réessayer avec un autre.");
            }
        }

        show_highlights();
    }

    public void show_highlights() {
        annotations = new HashMap<>();
        annotations = current_book.load_annotations();
        text_pane.getHighlighter().removeAllHighlights();

        for (Map.Entry<Integer, Annotation> entry : annotations.entrySet()) {
            Annotation annotation = entry.getValue();
            Highlighter highlighter = text_pane.getHighlighter();
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
        text_pane.setFont(new Font(font, Font.PLAIN, Integer.parseInt(fontSize)));
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

    private void back_button_action_listener() {
        try {
            int scrolledAmount = text_scroll_pane.getVerticalScrollBar().getValue();
            view.notify_back_performed(current_book.get_bookshelf(), current_book.get_id(),
                    scrolledAmount);
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this,
                    "Erreur liee au bouton retour", "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void settings_button_action_listener() {
        try {
            new SettingsDialog(view, this.getSize().width / 2, this.getSize().height / 2, this);
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this,
                    "Erreur liee au bouton retour", "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void copy_button_action_listener() {
        String selectedText = text_pane.getSelectedText();

        if (selectedText != null) {
            StringSelection stringSelection = new StringSelection(selectedText);
            Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
            clipboard.setContents(stringSelection, null);
        }
    }

    private void view_annotate_button_action_listener() {
        Highlighter highlighter = text_pane.getHighlighter();
        Highlighter.Highlight[] highlights = highlighter.getHighlights();

        for (int i = 0; i < highlights.length; i++) {
            Highlighter.Highlight h = highlights[i];

            if (h.getStartOffset() <= last_mouse_position && h.getEndOffset() >= last_mouse_position) {
                JOptionPane.showMessageDialog(this, annotations.get(h.getStartOffset()).get_text(),
                        "Annotation", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
        }

        JOptionPane.showMessageDialog(this, "Il faut sélectionner une annotation pour " +
                "le visualiser.", "Erreur", JOptionPane.ERROR_MESSAGE);
    }

    private void annotate_button_action_listener() {
        int start = text_pane.getSelectionStart();
        int end = text_pane.getSelectionEnd();

        if (current_book.is_downloaded() == false) {
            JOptionPane.showMessageDialog(this, "Il faut télécharger le livre avant de " +
                    "pouvoir ajouter des annotations.", "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (start - end == 0) {
            JOptionPane.showMessageDialog(this, "Il faut sélectionner un texte pour " +
                    "l'annoter.", "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Highlighter highlighter = text_pane.getHighlighter();
        Highlighter.Highlight[] highlightIndex = highlighter.getHighlights();

        for (int i = 0; i < highlightIndex.length - 1; i++) {
            Highlighter.Highlight h = highlightIndex[i];
            if ((start <= h.getStartOffset() && end >= h.getStartOffset()) ||
                    (start <= h.getEndOffset() && end >= h.getEndOffset()) ||
                    (start >= h.getStartOffset() && end <= h.getEndOffset())) {
                JOptionPane.showMessageDialog(this, "Il y a une autre annotation qui " +
                        "empiète sur l'emplacement séléctionné.", "Erreur", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }

        try {
            new AnnotationDialog(view, this.getSize().width / 2, this.getSize().height / 2,
                    this, current_bookshelf, current_book.get_id(), start, end);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void remove_annotation_button_action_listener() {
        Highlighter highlighter = text_pane.getHighlighter();
        Highlighter.Highlight[] highlightIndex = highlighter.getHighlights();

        for (Highlighter.Highlight h : highlightIndex) {
            if (h.getStartOffset() <= last_mouse_position && h.getEndOffset() >= last_mouse_position) {
                Annotation a = annotations.get(h.getStartOffset());
                if (a != null) {
                    try {
                        view.notify_annotation_remove_performed(current_bookshelf, current_book.get_id(), a.get_text(),
                                a.get_start(), a.get_start());
                        highlighter.removeHighlight(h);
                        annotations.remove(h.getStartOffset());
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
                return;
            }
        }

        JOptionPane.showMessageDialog(this, "Il faut sélectionner une annotation pour " +
                "le supprimer.", "Erreur", JOptionPane.ERROR_MESSAGE);
    }

    private void define_button_action_listener() {
        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() {
                String s;
                if (text_pane.getSelectedText() == null) {
                    s = " ";
                } else {
                    s = text_pane.getSelectedText();
                }
                view.notify_definition_requested(s);
                return null;
            }
        };
        worker.execute();
    }
}
