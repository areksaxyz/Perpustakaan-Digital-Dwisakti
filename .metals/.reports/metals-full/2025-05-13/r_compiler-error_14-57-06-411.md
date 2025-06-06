file://<WORKSPACE>/src/ui/BookPanel.java
### java.util.NoSuchElementException: next on empty iterator

occurred in the presentation compiler.

presentation compiler configuration:


action parameters:
uri: file://<WORKSPACE>/src/ui/BookPanel.java
text:
```scala
package ui;

import model.Book;
import storage.DataStorage;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import java.awt.*;

public class BookPanel extends JPanel {
    private DataStorage dataStorage;
    private DefaultTableModel tableModel;

    public BookPanel(DataStorage dataStorage) {
        this.dataStorage = dataStorage;
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Form
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel titleLabel = new JLabel("Judul:");
        titleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        JTextField titleField = new JTextField(20);
        titleField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        JLabel authorLabel = new JLabel("Penulis:");
        authorLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        JTextField authorField = new JTextField(20);
        authorField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        JLabel yearLabel = new JLabel("Tahun Terbit:");
        yearLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        JTextField yearField = new JTextField(20);
        yearField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        JLabel subjectLabel = new JLabel("Subjek:");
        subjectLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        JTextField subjectField = new JTextField(20);
        subjectField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        JLabel typeLabel = new JLabel("Tipe Buku:");
        typeLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        JComboBox<String> typeBox = new JComboBox<>(new String[]{"Digital", "Fisik"});
        typeBox.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        JLabel urlLabel = new JLabel("URL (Jika Digital):");
        urlLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        JTextField urlField = new JTextField(20);
        urlField.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(titleLabel, gbc);
        gbc.gridx = 1;
        formPanel.add(titleField, gbc);
        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(authorLabel, gbc);
        gbc.gridx = 1;
        formPanel.add(authorField, gbc);
        gbc.gridx = 0;
        gbc.gridy = 2;
        formPanel.add(yearLabel, gbc);
        gbc.gridx = 1;
        formPanel.add(yearField, gbc);
        gbc.gridx = 0;
        gbc.gridy = 3;
        formPanel.add(subjectLabel, gbc);
        gbc.gridx = 1;
        formPanel.add(subjectField, gbc);
        gbc.gridx = 0;
        gbc.gridy = 4;
        formPanel.add(typeLabel, gbc);
        gbc.gridx = 1;
        formPanel.add(typeBox, gbc);
        gbc.gridx = 0;
        gbc.gridy = 5;
        formPanel.add(urlLabel, gbc);
        gbc.gridx = 1;
        formPanel.add(urlField, gbc);

        JButton addButton = new JButton("Tambah Buku");
        addButton.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        addButton.setBackground(new Color(0, 123, 255));
        addButton.setForeground(Color.WHITE);
        addButton.setFocusPainted(false);
        JButton resetButton = new JButton("Hapus Buku Terpilih");
        resetButton.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        resetButton.setBackground(new Color(220, 53, 69));
        resetButton.setForeground(Color.WHITE);
        resetButton.setFocusPainted(false);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        buttonPanel.add(addButton);
        buttonPanel.add(resetButton);

        // Table
        String[] columnNames = {"Judul", "Penulis", "Tahun Terbit", "Tipe", "Status", "Aksi"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 5;
            }
        };
        JTable table = new JTable(tableModel);
        table.setFillsViewportHeight(true);
        table.setRowHeight(30);
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        table.getTableHeader().setBackground(new Color(0, 123, 255));
        table.getTableHeader().setForeground(Color.WHITE);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        table.setGridColor(new Color(200, 200, 200));
        JScrollPane scrollPane = new JScrollPane(table);

        // Populate initial data
        for (Book book : dataStorage.getBooks()) {
            tableModel.addRow(new Object[]{
                book.getTitle(),
                book.getAuthor(),
                "2020", // Placeholder tahun
                book.isDigital() ? "Digital" : "Fisik",
                book.isBorrowed() ? "Dipinjam" : "Tersedia",
                book.isDigital() && !book.getUrl().isEmpty() ? "Baca" : ""
            });
        }
        if (tableModel.getRowCount() > 0) {
            table.getColumnModel().getColumn(5).setCellRenderer(new ButtonRenderer());
            table.getColumnModel().getColumn(5).setCellEditor(new ButtonEditor(new JCheckBox(), dataStorage, table));
        }

        addButton.addActionListener(e -> {
            String title = titleField.getText();
            String author = authorField.getText();
            String year = yearField.getText();
            String subject = subjectField.getText();
            String type = (String) typeBox.getSelectedItem();
            String url = urlField.getText();
            if (!title.isEmpty() && !author.isEmpty() && !subject.isEmpty()) {
                String id = String.valueOf(dataStorage.getBooks().size() + 1);
                Book newBook = new Book(id, title, author, subject, type.equals("Digital"), type.equals("Digital") ? url : "");
                dataStorage.addBook(newBook);
                tableModel.addRow(new Object[]{
                    newBook.getTitle(),
                    newBook.getAuthor(),
                    year.isEmpty() ? "2020" : year,
                    newBook.isDigital() ? "Digital" : "Fisik",
                    newBook.isBorrowed() ? "Dipinjam" : "Tersedia",
                    newBook.isDigital() && !newBook.getUrl().isEmpty() ? "Baca" : ""
                });
                titleField.setText("");
                authorField.setText("");
                yearField.setText("");
                subjectField.setText("");
                urlField.setText("");
            } else {
                JOptionPane.showMessageDialog(this, "Judul, Penulis, dan Subjek harus diisi!");
            }
        });

        resetButton.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow >= 0) {
                String bookTitle = (String) tableModel.getValueAt(selectedRow, 0);
                dataStorage.getBooks().removeIf(book -> book.getTitle().equals(bookTitle));
                tableModel.removeRow(selectedRow);
            } else {
                JOptionPane.showMessageDialog(this, "Pilih buku yang ingin dihapus!");
            }
        });

        add(formPanel, BorderLayout.NORTH);
        add(buttonPanel, BorderLayout.CENTER);
        add(scrollPane, BorderLayout.SOUTH);
    }
}

class ButtonRenderer extends JLabel implements TableCellRenderer {
    public ButtonRenderer() {
        setOpaque(true);
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        if (value instanceof String && ((String) value).equals("Baca")) {
            JButton button = new JButton("Baca");
            button.setBackground(new Color(0, 123, 255));
            button.setForeground(Color.WHITE);
            button.setFocusPainted(false);
            button.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            return button;
        }
        setText(value != null ? value.toString() : "");
        return this;
    }
}

class ButtonEditor extends DefaultCellEditor {
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
        if (value instanceof String && ((String) value).equals("Baca")) {
            label = (String) value;
            button.setText(label);
            button.setBackground(new Color(0, 123, 255));
            button.setForeground(Color.WHITE);
            button.setFocusPainted(false);
            button.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            isPushed = true;
            return button;
        }
        return null;
    }

    @Override
    public Object getCellEditorValue() {
        if (isPushed) {
            int row = table.getEditingRow();
            if (row >= 0 && row < dataStorage.getBooks().size()) {
                String url = dataStorage.getBooks().get(row).getUrl();
                if (!url.isEmpty()) {
                    try {
                        java.awt.Desktop.getDesktop().browse(new java.net.URI(url));
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(null, "Gagal membuka PDF: " + ex.getMessage());
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