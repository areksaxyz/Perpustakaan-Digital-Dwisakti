package ui;

import model.Book;
import org.apache.pdfbox.pdmodel.PDDocument;
import storage.DataStorage;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.logging.Logger;
import java.util.logging.Level;

public class ButtonComponents {
    private static final Logger LOGGER = Logger.getLogger(ButtonComponents.class.getName());

    // Konstanta untuk styling
    private static final Font BUTTON_FONT = new Font("Arial", Font.BOLD, 18);
    private static final Font LABEL_FONT = new Font("Arial", Font.PLAIN, 18);
    private static final Font DIALOG_LABEL_FONT = new Font("Arial", Font.ITALIC, 18);
    private static final Color READ_BUTTON_COLOR = new Color(0, 123, 255);
    private static final Color EDIT_BUTTON_COLOR = new Color(108, 117, 125);
    private static final Color TEXT_COLOR = Color.WHITE;

    // Renderer untuk tombol "Baca"
    public static class ButtonRenderer extends JLabel implements TableCellRenderer {
        public ButtonRenderer() {
            setOpaque(true);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, 
                boolean isSelected, boolean hasFocus, int row, int column) {
            if (value instanceof String && "Baca".equals(value)) {
                JButton button = new JButton("Baca");
                styleButton(button, READ_BUTTON_COLOR);
                return button;
            }
            setText(value != null ? value.toString() : "");
            setFont(LABEL_FONT);
            return this;
        }
    }

    // Editor untuk tombol "Baca"
    public static class ButtonEditor extends DefaultCellEditor {
        private String label;
        private JButton button;
        private boolean isPushed;
        private final DataStorage dataStorage;
        private final JTable table;

        public ButtonEditor(JCheckBox checkBox, DataStorage dataStorage, JTable table) {
            super(checkBox);
            this.dataStorage = dataStorage;
            this.table = table;
            button = new JButton();
            button.setOpaque(true);
            button.addActionListener(ignored -> fireEditingStopped());
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, 
                boolean isSelected, int row, int column) {
            if (value instanceof String && "Baca".equals(value)) {
                label = (String) value;
                button.setText(label);
                styleButton(button, READ_BUTTON_COLOR);
                isPushed = true;
                return button;
            }
            return null;
        }

        @Override
        public Object getCellEditorValue() {
            if (isPushed) {
                int row = table.getEditingRow();
                if (row >= 0) {
                    List<Book> digitalBooks = dataStorage.getBooks().stream()
                            .filter(Book::isDigital)
                            .collect(Collectors.toList());
                    if (row < digitalBooks.size()) {
                        String url = digitalBooks.get(row).getUrl();
                        if (!url.isEmpty()) {
                            try {
                                File file = new File(url);
                                if (file.exists()) {
                                    try (PDDocument document = PDDocument.load(file)) {
                                        PDFViewer viewer = new PDFViewer(digitalBooks.get(row).getTitle(), document);
                                        viewer.setVisible(true);
                                    }
                                } else {
                                    JOptionPane.showMessageDialog(null, "File PDF tidak ditemukan. Gunakan file lokal.");
                                }
                            } catch (IOException ex) {
                                LOGGER.log(Level.SEVERE, "Gagal membuka PDF", ex);
                                JOptionPane.showMessageDialog(null, "Gagal membuka PDF: " + ex.getMessage());
                            }
                        } else {
                            JOptionPane.showMessageDialog(null, "URL buku tidak tersedia.");
                        }
                    }
                }
            }
            isPushed = false;
            return label;
        }

        @Override
        public boolean stopCellEditing() {
            isPushed = false;
            return super.stopCellEditing();
        }
    }

    // Renderer untuk tombol "Edit"
    public static class EditButtonRenderer extends JLabel implements TableCellRenderer {
        public EditButtonRenderer() {
            setOpaque(true);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, 
                boolean isSelected, boolean hasFocus, int row, int column) {
            if (value instanceof String && "Edit".equals(value)) {
                JButton button = new JButton("Edit");
                styleButton(button, EDIT_BUTTON_COLOR);
                return button;
            }
            setText(value != null ? value.toString() : "");
            setFont(LABEL_FONT);
            return this;
        }
    }

    // Editor untuk tombol "Edit"
    public static class EditButtonEditor extends DefaultCellEditor {
        private String label;
        private JButton button;
        private boolean isPushed;
        private final DataStorage dataStorage;
        private final JTable table;
        private final DefaultTableModel tableModel;

        public EditButtonEditor(JCheckBox checkBox, DataStorage dataStorage, 
                JTable table, DefaultTableModel tableModel) {
            super(checkBox);
            this.dataStorage = dataStorage;
            this.table = table;
            this.tableModel = tableModel;
            button = new JButton();
            button.setOpaque(true);
            button.addActionListener(ignored -> fireEditingStopped());
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, 
                boolean isSelected, int row, int column) {
            if (value instanceof String && "Edit".equals(value)) {
                label = (String) value;
                button.setText(label);
                styleButton(button, EDIT_BUTTON_COLOR);
                isPushed = true;
                return button;
            }
            return null;
        }

        @Override
        public Object getCellEditorValue() {
            if (isPushed) {
                int row = table.getEditingRow();
                if (row >= 0) {
                    String bookTitle = (String) tableModel.getValueAt(row, 0);
                    Book book = dataStorage.getBooks().stream()
                            .filter(b -> b.getTitle().equals(bookTitle))
                            .findFirst()
                            .orElse(null);
                    if (book != null) {
                        showEditDialog(book, row);
                    }
                }
            }
            isPushed = false;
            return label;
        }

        @Override
        public boolean stopCellEditing() {
            isPushed = false;
            return super.stopCellEditing();
        }

        private void showEditDialog(Book book, int row) {
            JDialog editDialog = new JDialog();
            editDialog.setTitle("Edit Buku: " + book.getTitle());
            editDialog.setSize(500, 400);
            editDialog.setLayout(new GridBagLayout());
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(10, 10, 10, 10);
            gbc.fill = GridBagConstraints.HORIZONTAL;

            JLabel titleLabel = new JLabel("Judul:");
            titleLabel.setFont(DIALOG_LABEL_FONT);
            JTextField titleField = new JTextField(book.getTitle(), 20);
            titleField.setFont(LABEL_FONT);
            JLabel authorLabel = new JLabel("Penulis:");
            authorLabel.setFont(DIALOG_LABEL_FONT);
            JTextField authorField = new JTextField(book.getAuthor(), 20);
            authorField.setFont(LABEL_FONT);
            JLabel subjectLabel = new JLabel("Subjek:");
            subjectLabel.setFont(DIALOG_LABEL_FONT);
            JTextField subjectField = new JTextField(book.getSubject(), 20);
            subjectField.setFont(LABEL_FONT);
            JLabel urlLabel = new JLabel("URL (Jika Digital):");
            urlLabel.setFont(DIALOG_LABEL_FONT);
            JTextField urlField = new JTextField(book.getUrl(), 20);
            urlField.setFont(LABEL_FONT);
            JButton saveButton = new JButton("Simpan");
            styleButton(saveButton, READ_BUTTON_COLOR);

            gbc.gridx = 0;
            gbc.gridy = 0;
            editDialog.add(titleLabel, gbc);
            gbc.gridx = 1;
            editDialog.add(titleField, gbc);
            gbc.gridx = 0;
            gbc.gridy = 1;
            editDialog.add(authorLabel, gbc);
            gbc.gridx = 1;
            editDialog.add(authorField, gbc);
            gbc.gridx = 0;
            gbc.gridy = 2;
            editDialog.add(subjectLabel, gbc);
            gbc.gridx = 1;
            editDialog.add(subjectField, gbc);
            gbc.gridx = 0;
            gbc.gridy = 3;
            editDialog.add(urlLabel, gbc);
            gbc.gridx = 1;
            editDialog.add(urlField, gbc);
            gbc.gridx = 0;
            gbc.gridy = 4;
            gbc.gridwidth = 2;
            editDialog.add(saveButton, gbc);

            saveButton.addActionListener(ignored -> {
                book.setTitle(titleField.getText());
                book.setAuthor(authorField.getText());
                book.setSubject(subjectField.getText());
                book.setUrl(book.isDigital() ? urlField.getText() : "");
                tableModel.setValueAt(book.getTitle(), row, 0);
                tableModel.setValueAt(book.getAuthor(), row, 1);
                editDialog.dispose();
                JOptionPane.showMessageDialog(null, "Buku berhasil diperbarui.");
            });

            editDialog.setModal(true);
            editDialog.setLocationRelativeTo(null);
            editDialog.setVisible(true);
        }
    }

    // Helper method untuk styling tombol
    private static void styleButton(JButton button, Color backgroundColor) {
        button.setFont(BUTTON_FONT);
        button.setBackground(backgroundColor);
        button.setForeground(TEXT_COLOR);
        button.setFocusPainted(false);
    }
}