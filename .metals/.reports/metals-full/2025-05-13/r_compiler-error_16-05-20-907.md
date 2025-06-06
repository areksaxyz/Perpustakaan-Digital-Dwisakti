file://<WORKSPACE>/src/ui/CatalogPanel.java
### java.util.NoSuchElementException: next on empty iterator

occurred in the presentation compiler.

presentation compiler configuration:


action parameters:
offset: 5689
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
        setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));
        setBackground(new Color(248, 249, 250));

        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchPanel.setBackground(Color.WHITE);
        JLabel searchLabel = new JLabel("Pencarian Buku:");
        searchLabel.setFont(new Font("Arial", Font.ITALIC, 18));
        JTextField searchField = new JTextField(20);
        searchField.setFont(new Font("Arial", Font.PLAIN, 18));
        filterBox = new JComboBox<>(new String[]{"Judul", "Penulis", "Subjek"});
        filterBox.setFont(new Font("Arial", Font.PLAIN, 18));
        JButton searchButton = new JButton("Cari");
        searchButton.setFont(new Font("Arial", Font.BOLD, 18));
        searchButton.setBackground(new Color(0, 123, 255));
        searchButton.setForeground(Color.WHITE);
        searchButton.setFocusPainted(false);
        JButton resetButton = new JButton("Reset");
        resetButton.setFont(new Font("Arial", Font.BOLD, 18));
        resetButton.setBackground(new Color(220, 53, 69));
        resetButton.setForeground(Color.WHITE);
        resetButton.setFocusPainted(false);
        searchPanel.add(searchLabel);
        searchPanel.add(searchField);
        searchPanel.add(filterBox);
        searchPanel.add(searchButton);
        searchPanel.add(resetButton);

        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Arial", Font.BOLD, 20));

        String[] columnNames = {"Judul", "Penulis", "Tahun Terbit", "Status", "Aksi"};
        digitalTableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 4;
            }
        };
        digitalTable = new JTable(digitalTableModel);
        digitalTable.setFillsViewportHeight(true);
        digitalTable.setRowHeight(40);
        digitalTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 20));
        digitalTable.getTableHeader().setBackground(new Color(0, 123, 255));
        digitalTable.getTableHeader().setForeground(Color.WHITE);
        digitalTable.setFont(new Font("Arial", Font.PLAIN, 18));
        digitalTable.setGridColor(new Color(200, 200, 200));
        JScrollPane digitalScrollPane = new JScrollPane(digitalTable);
        tabbedPane.addTab("Buku Digital", digitalScrollPane);

        physicalTableModel = new DefaultTableModel(new String[]{"Judul", "Penulis", "Tahun Terbit", "Status"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        physicalTable = new JTable(physicalTableModel);
        physicalTable.setFillsViewportHeight(true);
        physicalTable.setRowHeight(40);
        physicalTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 20));
        physicalTable.getTableHeader().setBackground(new Color(0, 123, 255));
        physicalTable.getTableHeader().setForeground(Color.WHITE);
        physicalTable.setFont(new Font("Arial", Font.PLAIN, 18));
        physicalTable.setGridColor(new Color(200, 200, 200));
        JScrollPane physicalScrollPane = new JScrollPane(physicalTable);
        tabbedPane.addTab("Buku Fisik", physicalScrollPane);

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

        if (digitalTableModel.getRowCount() > 0) {
            digitalTable.getColumnModel().getColumn(4).setCellRenderer(new ButtonComponents.ButtonRenderer());
            digitalTable.getColumnModel().getColumn(4).setCellEditor(new ButtonComponents.ButtonEditor(new JCheckBox(), dataStorage, digitalTable));
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
                    "2020",
                    book.isBorrowed() ? "Dipinjam" : "Tersedia",
                    !book.getUrl@@().isEmpty() ? "Baca" : ""
                });
            } else {
                physicalTableModel.addRow(new Object[]{
                    book.getTitle(),
                    book.getAuthor(),
                    "2020",
                    book.isBorrowed() ? "Dipinjam" : "Tersedia"
                });
            }
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
	dotty.tools.pc.HoverProvider$.hover(HoverProvider.scala:40)
	dotty.tools.pc.ScalaPresentationCompiler.hover$$anonfun$1(ScalaPresentationCompiler.scala:389)
```
#### Short summary: 

java.util.NoSuchElementException: next on empty iterator