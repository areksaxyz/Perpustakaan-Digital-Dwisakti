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
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.util.List;
import java.util.stream.Collectors;

public class CatalogPanel extends JPanel {
    private DataStorage dataStorage;
    private JTable digitalTable;
    private JTable physicalTable;
    private DefaultTableModel digitalTableModel;
    private DefaultTableModel physicalTableModel;
    private JComboBox<String> filterBox;

    public CatalogPanel(DataStorage dataStorage) {
        this.dataStorage = dataStorage;
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Search and Filter
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel searchLabel = new JLabel("Pencarian Buku:");
        searchLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        JTextField searchField = new JTextField(20);
        searchField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        filterBox = new JComboBox<>(new String[]{"Judul", "Penulis", "Subjek"});
        filterBox.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        JButton searchButton = new JButton("Cari");
        searchButton.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        searchButton.setBackground(new Color(0, 123, 255));
        searchButton.setForeground(Color.WHITE);
        searchButton.setFocusPainted(false);
        JButton resetButton = new JButton("Reset");
        resetButton.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        resetButton.setBackground(new Color(220, 53, 69));
        resetButton.setForeground(Color.WHITE);
        resetButton.setFocusPainted(false);
        searchPanel.add(searchLabel);
        searchPanel.add(searchField);
        searchPanel.add(filterBox);
        searchPanel.add(searchButton);
        searchPanel.add(resetButton);

        // Tabs for Digital and Physical Books
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        // Digital Books Table
        String[] columnNames = {"Judul", "Penulis", "Tahun Terbit", "Status", "Aksi"};
        digitalTableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 4;
            }
        };
        digitalTable = new JTable(digitalTableModel);
        digitalTable.setFillsViewportHeight(true);
        digitalTable.setRowHeight(30);
        digitalTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        digitalTable.getTableHeader().setBackground(new Color(0, 123, 255));
        digitalTable.getTableHeader().setForeground(Color.WHITE);
        digitalTable.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        digitalTable.setGridColor(new Color(200, 200, 200));
        JScrollPane digitalScrollPane = new JScrollPane(digitalTable);
        tabbedPane.addTab("Buku Digital", digitalScrollPane);

        // Physical Books Table
        physicalTableModel = new DefaultTableModel(new String[]{"Judul", "Penulis", "Tahun Terbit", "Status"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        physicalTable = new JTable(physicalTableModel);
        physicalTable.setFillsViewportHeight(true);
        physicalTable.setRowHeight(30);
        physicalTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        physicalTable.getTableHeader().setBackground(new Color(0, 123, 255));
        physicalTable.getTableHeader().setForeground(Color.WHITE);
        physicalTable.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        physicalTable.setGridColor(new Color(200, 200, 200));
        JScrollPane physicalScrollPane = new JScrollPane(physicalTable);
        tabbedPane.addTab("Buku Fisik", physicalScrollPane);

        // Populate initial data
        updateTables("");

        searchButton.addActionListener(e -> {
            String query = searchField.getText().toLowerCase();
            updateTables(query);
        });

        resetButton.addActionListener(e -> {
            searchField.setText("");
            updateTables("");
        });

        add(searchPanel, BorderLayout.NORTH);
        add(tabbedPane, BorderLayout.CENTER);

        // Set renderer and editor for digital table
        if (digitalTableModel.getRowCount() > 0) {
            digitalTable.getColumnModel().getColumn(4).setCellRenderer(new ButtonRenderer());
            digitalTable.getColumnModel().getColumn(4).setCellEditor(new ButtonEditor(new JCheckBox(), dataStorage, digitalTable));
        }
    }

    private void updateTables(String query) {
        digitalTableModel.setRowCount(0);
        physicalTableModel.setRowCount(0);

        List<Book> filteredBooks = dataStorage.getBooks().stream()
                .filter(book -> {
                    if (query.isEmpty()) return true;
                    String filter = (String) filterBox.getSelectedItem();
                    if (filter.equals("Judul")) return book.getTitle().toLowerCase().contains(query);
                    if (filter.equals("Penulis")) return book.getAuthor().toLowerCase().contains(query);
                    return book.getSubject().toLowerCase().contains(query);
                })
                .collect(Collectors.toList());

        for (Book book : filteredBooks) {
            if (book.isDigital()) {
                digitalTableModel.addRow(new Object[]{
                    book.getTitle(),
                    book.getAuthor(),
                    "2020", // Placeholder tahun
                    book.isBorrowed() ? "Dipinjam" : "Tersedia",
                    !book.getUrl().isEmpty() ? "Baca" : ""
                });
            } else {
                physicalTableModel.addRow(new Object[]{
                    book.getTitle(),
                    book.getAuthor(),
                    "2020", // Placeholder tahun
                    book.isBorrowed() ? "Dipinjam" : "Tersedia"
                });
            }
        }
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
            if (row >= 0) {
                List<Book> digitalBooks = dataStorage.getBooks().stream()
                        .filter(Book::isDigital)
                        .collect(Collectors.toList());
                if (row < digitalBooks.size()) {
                    String url = digitalBooks.get(row).getUrl();
                    if (!url.isEmpty()) {
                        // Simulasi pembukaan PDF di aplikasi menggunakan JFrame dan JEditorPane
                        JFrame pdfViewer = new JFrame("Membaca: " + digitalBooks.get(row).getTitle());
                        pdfViewer.setSize(800, 600);
                        pdfViewer.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

                        JEditorPane editorPane = new JEditorPane();
                        editorPane.setEditable(false);
                        editorPane.setContentType("text/plain");
                        editorPane.setText("Simulasi PDF untuk buku: " + digitalBooks.get(row).getTitle() + "\n\nKonten: " + url + "\n\nIni adalah simulasi teks dari buku digital. Dalam implementasi nyata, gunakan library seperti PDFBox untuk menampilkan PDF asli.");
                        JScrollPane scrollPane = new JScrollPane(editorPane);

                        pdfViewer.add(scrollPane);
                        pdfViewer.setVisible(true);
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