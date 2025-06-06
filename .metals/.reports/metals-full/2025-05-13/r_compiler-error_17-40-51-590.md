file://<WORKSPACE>/src/ui/ButtonComponents.java
### java.util.NoSuchElementException: next on empty iterator

occurred in the presentation compiler.

presentation compiler configuration:


action parameters:
uri: file://<WORKSPACE>/src/ui/ButtonComponents.java
text:
```scala
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

public class ButtonComponents {
    public static class ButtonRenderer extends JLabel implements TableCellRenderer {
        public ButtonRenderer() {
            setOpaque(true);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            if (value instanceof String) {
                JButton button = new JButton((String) value);
                button.setFont(new Font("Arial", Font.BOLD, 18));
                if (((String) value).equals("Baca")) {
                    button.setBackground(new Color(0, 123, 255));
                } else if (((String) value).equals("Edit")) {
                    button.setBackground(new Color(108, 117, 125));
                }
                button.setForeground(Color.WHITE);
                button.setFocusPainted(false);
                return button;
            }
            setText(value != null ? value.toString() : "");
            setFont(new Font("Arial", Font.PLAIN, 18));
            return this;
        }
    }

    public static class ButtonEditor extends DefaultCellEditor {
        private String label;
        private JButton button;
        private boolean isPushed;
        private DataStorage dataStorage;
        private JTable table;

        public ButtonEditor(JCheckBox checkBox, DataStorage dataStorage, JTable table) {
            super(checkBox);
            this.dataStorage = dataStorage;
            this.table = table;
            button = new JButton();
            button.setOpaque(true);
            button.addActionListener(e -> fireEditingStopped());
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            if (value instanceof String) {
                label = (String) value;
                button.setText(label);
                button.setFont(new Font("Arial", Font.BOLD, 18));
                if (label.equals("Baca")) {
                    button.setBackground(new Color(0, 123, 255));
                } else if (label.equals("Edit")) {
                    button.setBackground(new Color(108, 117, 125));
                }
                button.setForeground(Color.WHITE);
                button.setFocusPainted(false);
                isPushed = true;
                return button;
            }
            return null;
        }

        @Override
        public Object getCellEditorValue() {
            if (isPushed) {
                int row = table.getEditingRow();
                if (row >= 0 && label.equals("Baca")) {
                    String bookTitle = (String) table.getValueAt(row, 0);
                    Book book = dataStorage.getBooks().stream()
                            .filter(b -> b.getTitle().equals(bookTitle))
                            .findFirst()
                            .orElse(null);
                    if (book != null && book.isDigital()) {
                        try {
                            File pdfFile = new File(book.getUrl());
                            if (pdfFile.exists()) {
                                PDDocument document = PDDocument.load(pdfFile);
                                JOptionPane.showMessageDialog(null, "Membuka PDF: " + book.getTitle() + "\nJumlah Halaman: " + document.getNumberOfPages());
                                document.close();
                            } else {
                                JOptionPane.showMessageDialog(null, "File PDF tidak ditemukan: " + book.getUrl());
                            }
                        } catch (IOException e) {
                            JOptionPane.showMessageDialog(null, "Gagal membuka PDF: " + e.getMessage());
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

    public static class EditButtonEditor extends DefaultCellEditor {
        private String label;
        private JButton button;
        private boolean isPushed;
        private DataStorage dataStorage;
        private JTable table;
        private DefaultTableModel tableModel;

        public EditButtonEditor(JCheckBox checkBox, DataStorage dataStorage, JTable table, DefaultTableModel tableModel) {
            super(checkBox);
            this.dataStorage = dataStorage;
            this.table = table;
            this.tableModel = tableModel;
            button = new JButton();
            button.setOpaque(true);
            button.addActionListener(e -> fireEditingStopped());
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            if (value instanceof String && ((String) value).equals("Edit")) {
                label = (String) value;
                button.setText(label);
                button.setFont(new Font("Arial", Font.BOLD, 18));
                button.setBackground(new Color(108, 117, 125));
                button.setForeground(Color.WHITE);
                button.setFocusPainted(false);
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
                        JDialog editDialog = new JDialog();
                        editDialog.setTitle("Edit Buku: " + book.getTitle());
                        editDialog.setSize(500, 400);
                        editDialog.setLayout(new GridBagLayout());
                        GridBagConstraints gbc = new GridBagConstraints();
                        gbc.insets = new Insets(10, 10, 10, 10);
                        gbc.fill = GridBagConstraints.HORIZONTAL;

                        JLabel titleLabel = new JLabel("Judul:");
                        titleLabel.setFont(new Font("Arial", Font.ITALIC, 18));
                        JTextField titleField = new JTextField(book.getTitle(), 20);
                        titleField.setFont(new Font("Arial", Font.PLAIN, 18));
                        JLabel authorLabel = new JLabel("Penulis:");
                        authorLabel.setFont(new Font("Arial", Font.ITALIC, 18));
                        JTextField authorField = new JTextField(book.getAuthor(), 20);
                        authorField.setFont(new Font("Arial", Font.PLAIN, 18));
                        JLabel subjectLabel = new JLabel("Subjek:");
                        subjectLabel.setFont(new Font("Arial", Font.ITALIC, 18));
                        JTextField subjectField = new JTextField(book.getSubject(), 20);
                        subjectField.setFont(new Font("Arial", Font.PLAIN, 18));
                        JLabel urlLabel = new JLabel("URL (Jika Digital):");
                        urlLabel.setFont(new Font("Arial", Font.ITALIC, 18));
                        JTextField urlField = new JTextField(book.getUrl(), 20);
                        urlField.setFont(new Font("Arial", Font.PLAIN, 18));
                        JButton saveButton = new JButton("Simpan");
                        saveButton.setFont(new Font("Arial", Font.BOLD, 18));
                        saveButton.setBackground(new Color(0, 123, 255));
                        saveButton.setForeground(Color.WHITE);

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

                        saveButton.addActionListener(e -> {
                            book.setTitle(titleField.getText());
                            book.setAuthor(authorField.getText());
                            book.setSubject(subjectField.getText());
                            book.setUrl(book.isDigital() ? urlField.getText() : "");
                            tableModel.setValueAt(book.getTitle(), row, 0);
                            tableModel.setValueAt(book.getAuthor(), row, 1);
                            tableModel.setValueAt(book.getSubject(), row, 2);
                            tableModel.setValueAt(book.isBorrowed() ? "Dipinjam" : "Tersedia", row, 3);
                            editDialog.dispose();
                            JOptionPane.showMessageDialog(null, "Buku berhasil diperbarui.");
                        });

                        editDialog.setModal(true);
                        editDialog.setLocationRelativeTo(null);
                        editDialog.setVisible(true);
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
}
```



#### Error stacktrace:

```
scala.collection.Iterator$$anon$19.next(Iterator.scala:973)
	scala.collection.Iterator$$anon$19.next(Iterator.scala:971)
	scala.collection.mutable.MutationTracker$CheckedIterator.next(MutationTracker.scala:76)
	scala.collection.IterableOps.head(Iterable.scala:222)
	scala.collection.IterableOps.head$(Iterable.scala:222)
	scala.collection.AbstractIterable.head(Iterable.scala:935)
	dotty.tools.dotc.interactive.InteractiveDriver.run(InteractiveDriver.scala:164)
	dotty.tools.pc.CachingDriver.run(CachingDriver.scala:45)
	dotty.tools.pc.WithCompilationUnit.<init>(WithCompilationUnit.scala:31)
	dotty.tools.pc.SimpleCollector.<init>(PcCollector.scala:351)
	dotty.tools.pc.PcSemanticTokensProvider$Collector$.<init>(PcSemanticTokensProvider.scala:63)
	dotty.tools.pc.PcSemanticTokensProvider.Collector$lzyINIT1(PcSemanticTokensProvider.scala:63)
	dotty.tools.pc.PcSemanticTokensProvider.Collector(PcSemanticTokensProvider.scala:63)
	dotty.tools.pc.PcSemanticTokensProvider.provide(PcSemanticTokensProvider.scala:88)
	dotty.tools.pc.ScalaPresentationCompiler.semanticTokens$$anonfun$1(ScalaPresentationCompiler.scala:111)
```
#### Short summary: 

java.util.NoSuchElementException: next on empty iterator