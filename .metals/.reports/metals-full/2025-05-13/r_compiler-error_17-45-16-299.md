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
import java.util.stream.Collectors;

public class CatalogPanel extends JPanel {
    private DataStorage dataStorage;
    private DefaultTableModel digitalTableModel;
    private DefaultTableModel physicalTableModel;

    public CatalogPanel(DataStorage dataStorage) {
        this.dataStorage = dataStorage;
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));
        setBackground(new Color(248, 249, 250));

        JLabel titleLabel = new JLabel("Katalog Buku", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        add(titleLabel, BorderLayout.NORTH);

        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Arial", Font.BOLD, 20));

        // Buku Digital
        String[] digitalColumns = {"Judul", "Penulis", "Subjek", "Status", "Aksi"};
        digitalTableModel = new DefaultTableModel(digitalColumns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 4;
            }
        };
        JTable digitalTable = new JTable(digitalTableModel);
        digitalTable.setFillsViewportHeight(true);
        digitalTable.setRowHeight(40);
        digitalTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 20));
        digitalTable.getTableHeader().setBackground(new Color(0, 123, 255));
        digitalTable.getTableHeader().setForeground(Color.WHITE);
        digitalTable.setFont(new Font("Arial", Font.PLAIN, 18));
        digitalTable.setGridColor(new Color(200, 200, 200));
        JScrollPane digitalScrollPane = new JScrollPane(digitalTable);
        tabbedPane.addTab("Buku Digital", digitalScrollPane);

        // Buku Fisik
        String[] physicalColumns = {"Judul", "Penulis", "Subjek", "Status"};
        physicalTableModel = new DefaultTableModel(physicalColumns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        JTable physicalTable = new JTable(physicalTableModel);
        physicalTable.setFillsViewportHeight(true);
        physicalTable.setRowHeight(40);
        physicalTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 20));
        physicalTable.getTableHeader().setBackground(new Color(0, 123, 255));
        physicalTable.getTableHeader().setForeground(Color.WHITE);
        physicalTable.setFont(new Font("Arial", Font.PLAIN, 18));
        physicalTable.setGridColor(new Color(200, 200, 200));
        JScrollPane physicalScrollPane = new JScrollPane(physicalTable);
        tabbedPane.addTab("Buku Fisik", physicalScrollPane);

        updateTables();

        digitalTable.getColumnModel().getColumn(4).setCellRenderer(new ButtonComponents.ButtonRenderer());
        digitalTable.getColumnModel().getColumn(4).setCellEditor(new ButtonComponents.ButtonEditor(new JCheckBox(), digitalTable));

        add(tabbedPane, BorderLayout.CENTER);
    }

    private void updateTables() {
        digitalTableModel.setRowCount(0);
        physicalTableModel.setRowCount(0);

        List<Book> digitalBooks = dataStorage.getBooks().stream()
                .filter(Book::isDigital)
                .collect(Collectors.toList());
        List<Book> physicalBooks = dataStorage.getBooks().stream()
                .filter(book -> !book.isDigital())
                .collect(Collectors.toList());

        for (Book book : digitalBooks) {
            digitalTableModel.addRow(new Object[]{
                book.getTitle(),
                book.getAuthor(),
                book.getSubject(),
                book.isBorrowed() ? "Dipinjam" : "Tersedia",
                "Baca"
            });
        }

        for (Book book : physicalBooks) {
            physicalTableModel.addRow(new Object[]{
                book.getTitle(),
                book.getAuthor(),
                book.getSubject(),
                book.isBorrowed() ? "Dipinjam" : "Tersedia"
            });
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