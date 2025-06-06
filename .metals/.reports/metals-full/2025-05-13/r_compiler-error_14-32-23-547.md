file://<WORKSPACE>/src/ui/CatalogPanel.java
### java.util.NoSuchElementException: next on empty iterator

occurred in the presentation compiler.

presentation compiler configuration:


action parameters:
uri: file://<WORKSPACE>/src/ui/CatalogPanel.java
text:
```scala
package ui;

import model.Book;
import storage.DataStorage;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class CatalogPanel extends JPanel {
    private DataStorage dataStorage;

    public CatalogPanel(DataStorage dataStorage) {
        this.dataStorage = dataStorage;
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Search and Filter
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JTextField searchField = new JTextField(20);
        JComboBox<String> filterBox = new JComboBox<>(new String[]{"Judul", "Penulis", "Subjek"});
        JButton searchButton = new JButton("Cari");
        searchPanel.add(new JLabel("Pencarian Buku:"));
        searchPanel.add(searchField);
        searchPanel.add(filterBox);
        searchPanel.add(searchButton);

        // Table
        String[] columnNames = {"Judul", "Penulis", "Tahun Terbit", "Tipe", "Status", "Aksi"};
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        JTable table = new JTable(tableModel);
        table.setFillsViewportHeight(true);
        table.setRowHeight(30);
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        table.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        JScrollPane scrollPane = new JScrollPane(table);

        // Populate initial data
        updateTable(tableModel, "");

        searchButton.addActionListener(e -> {
            String query = searchField.getText().toLowerCase();
            updateTable(tableModel, query);
        });

        JButton resetButton = new JButton("Reset");
        resetButton.addActionListener(e -> {
            searchField.setText("");
            updateTable(tableModel, "");
        });
        searchPanel.add(resetButton);

        add(searchPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
    }

    private void updateTable(DefaultTableModel model, String query) {
        model.setRowCount(0);
        List<Book> filteredBooks = dataStorage.getBooks().stream()
                .filter(book -> {
                    if (query.isEmpty()) return true;
                    String filter = (String) ((JComboBox) getComponent(0).getComponent(2)).getSelectedItem();
                    if (filter.equals("Judul")) return book.getTitle().toLowerCase().contains(query);
                    if (filter.equals("Penulis")) return book.getAuthor().toLowerCase().contains(query);
                    return book.getSubject().toLowerCase().contains(query);
                })
                .collect(Collectors.toList());
        for (Book book : filteredBooks) {
            model.addRow(new Object[]{
                book.getTitle(),
                book.getAuthor(),
                "2020", // Placeholder tahun (bisa diperbarui)
                book.isDigital() ? "Digital" : "Fisik",
                book.isBorrowed() ? "Dipinjam" : "Tersedia",
                book.isDigital() && !book.getUrl().isEmpty() ? "Baca" : ""
            });
        }
        if (model.getRowCount() > 0) {
            table.getColumnModel().getColumn(5).setCellRenderer(new ButtonRenderer());
            table.getColumnModel().getColumn(5).setCellEditor(new ButtonEditor(new JCheckBox()));
        }
    }
}

// Renderer untuk tombol "Baca"
class ButtonRenderer extends DefaultTableCellRenderer {
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        if (value instanceof String && ((String) value).equals("Baca")) {
            JButton button = new JButton("Baca");
            button.setBackground(new Color(0, 123, 255));
            button.setForeground(Color.WHITE);
            button.setFocusPainted(false);
            return button;
        }
        return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
    }
}

// Editor untuk tombol "Baca"
class ButtonEditor extends DefaultCellEditor {
    private String label;
    private JButton button;
    private boolean isPushed;

    public ButtonEditor(JCheckBox checkBox) {
        super(checkBox);
        button = new JButton();
        button.setOpaque(true);
        button.addActionListener(e -> fireEditingStopped());
    }

    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        if (value instanceof String && ((String) value).equals("Baca")) {
            label = (String) value;
            button.setText(label);
            button.setBackground(new Color(0, 123, 255));
            button.setForeground(Color.WHITE);
            isPushed = true;
            return button;
        }
        return null;
    }

    public Object getCellEditorValue() {
        if (isPushed) {
            int row = table.getEditingRow();
            String url = dataStorage.getBooks().get(row).getUrl();
            if (!url.isEmpty()) {
                try {
                    java.awt.Desktop.getDesktop().browse(new java.net.URI(url));
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "Gagal membuka PDF: " + ex.getMessage());
                }
            }
        }
        isPushed = false;
        return label;
    }

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