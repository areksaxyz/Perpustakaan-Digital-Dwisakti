file://<WORKSPACE>/src/ui/StatsPanel.java
### java.util.NoSuchElementException: next on empty iterator

occurred in the presentation compiler.

presentation compiler configuration:


action parameters:
uri: file://<WORKSPACE>/src/ui/StatsPanel.java
text:
```scala
package ui;

import model.Book;
import model.Transaction;
import model.User;
import storage.DataStorage;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class StatsPanel extends JPanel {
    private DataStorage dataStorage;

    public StatsPanel(DataStorage dataStorage) {
        this.dataStorage = dataStorage;
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        setBackground(Color.WHITE);

        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        // Tab Buku Populer
        JPanel popularBooksPanel = new JPanel(new BorderLayout());
        popularBooksPanel.setBackground(Color.WHITE);
        String[] popularColumns = {"Judul Buku", "Kategori", "Jumlah Peminjaman"};
        DefaultTableModel popularModel = new DefaultTableModel(popularColumns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        JTable popularTable = new JTable(popularModel);
        popularTable.setFillsViewportHeight(true);
        popularTable.setRowHeight(30);
        popularTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        popularTable.getTableHeader().setBackground(new Color(0, 123, 255));
        popularTable.getTableHeader().setForeground(Color.WHITE);
        popularTable.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        popularTable.setGridColor(new Color(200, 200, 200));
        popularBooksPanel.add(new JScrollPane(popularTable), BorderLayout.CENTER);
        tabbedPane.addTab("Buku Populer", popularBooksPanel);

        // Tab Peminjam Aktif
        JPanel activeBorrowersPanel = new JPanel(new BorderLayout());
        activeBorrowersPanel.setBackground(Color.WHITE);
        String[] borrowerColumns = {"Nama Peminjam", "Jumlah Peminjaman"};
        DefaultTableModel borrowerModel = new DefaultTableModel(borrowerColumns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        JTable borrowerTable = new JTable(borrowerModel);
        borrowerTable.setFillsViewportHeight(true);
        borrowerTable.setRowHeight(30);
        borrowerTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        borrowerTable.getTableHeader().setBackground(new Color(0, 123, 255));
        borrowerTable.getTableHeader().setForeground(Color.WHITE);
        borrowerTable.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        borrowerTable.setGridColor(new Color(200, 200, 200));
        activeBorrowersPanel.add(new JScrollPane(borrowerTable), BorderLayout.CENTER);
        tabbedPane.addTab("Peminjam Aktif", activeBorrowersPanel);

        // Tab Rekam Jejak Siswa
        JPanel historyPanel = new JPanel(new BorderLayout());
        historyPanel.setBackground(Color.WHITE);
        String[] historyColumns = {"Nama Peminjam", "Judul Buku", "Tanggal Pinjam", "Tanggal Kembali", "Status"};
        DefaultTableModel historyModel = new DefaultTableModel(historyColumns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        JTable historyTable = new JTable(historyModel);
        historyTable.setFillsViewportHeight(true);
        historyTable.setRowHeight(30);
        historyTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        historyTable.getTableHeader().setBackground(new Color(0, 123, 255));
        historyTable.getTableHeader().setForeground(Color.WHITE);
        historyTable.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        historyTable.setGridColor(new Color(200, 200, 200));
        historyPanel.add(new JScrollPane(historyTable), BorderLayout.CENTER);
        tabbedPane.addTab("Rekam Jejak Siswa", historyPanel);

        // Populate data for Buku Populer
        Map<String, Integer> bookBorrowCount = new HashMap<>();
        for (Transaction transaction : dataStorage.getTransactions()) {
            String bookId = transaction.getBookId();
            bookBorrowCount.put(bookId, bookBorrowCount.getOrDefault(bookId, 0) + 1);
        }
        for (Map.Entry<String, Integer> entry : bookBorrowCount.entrySet()) {
            Book book = dataStorage.getBooks().stream()
                    .filter(b -> b.getId().equals(entry.getKey()))
                    .findFirst()
                    .orElse(null);
            if (book != null) {
                popularModel.addRow(new Object[]{
                    book.getTitle(),
                    book.getSubject(),
                    entry.getValue()
                });
            }
        }

        // Populate data for Peminjam Aktif
        Map<String, Integer> userBorrowCount = new HashMap<>();
        for (Transaction transaction : dataStorage.getTransactions()) {
            String userId = transaction.getUserId();
            userBorrowCount.put(userId, userBorrowCount.getOrDefault(userId, 0) + 1);
        }
        for (Map.Entry<String, Integer> entry : userBorrowCount.entrySet()) {
            User user = dataStorage.getUsers().stream()
                    .filter(u -> u.getId().equals(entry.getKey()))
                    .findFirst()
                    .orElse(null);
            if (user != null) {
                borrowerModel.addRow(new Object[]{
                    user.getName(),
                    entry.getValue()
                });
            }
        }

        // Populate data for Rekam Jejak Siswa
        for (Transaction transaction : dataStorage.getTransactions()) {
            historyModel.addRow(new Object[]{
                dataStorage.getUsers().stream().filter(u -> u.getId().equals(transaction.getUserId())).findFirst().map(User::getName).orElse(""),
                dataStorage.getBooks().stream().filter(b -> b.getId().equals(transaction.getBookId())).findFirst().map(Book::getTitle).orElse(""),
                transaction.getBorrowDate().toString(),
                transaction.getDueDate().toString(),
                transaction.getReturnDate() == null ? "Dipinjam" : "Dikembalikan"
            });
        }

        add(tabbedPane, BorderLayout.CENTER);
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