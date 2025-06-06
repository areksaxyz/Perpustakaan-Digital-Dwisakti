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
        setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));
        setBackground(new Color(248, 249, 250));

        // Statistik (Kartu)
        JPanel statsPanel = new JPanel(new GridLayout(2, 2, 20, 20));
        statsPanel.setBackground(new Color(248, 249, 250));

        JPanel totalBooksPanel = new JPanel(new BorderLayout());
        totalBooksPanel.setBackground(Color.WHITE);
        totalBooksPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));
        JLabel totalBooksLabel = new JLabel("📚 Total Buku: " + dataStorage.getBooks().size());
        totalBooksLabel.setFont(new Font("Arial", Font.BOLD, 20));
        totalBooksPanel.add(totalBooksLabel, BorderLayout.CENTER);
        statsPanel.add(totalBooksPanel);

        JPanel totalUsersPanel = new JPanel(new BorderLayout());
        totalUsersPanel.setBackground(Color.WHITE);
        totalUsersPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));
        JLabel totalUsersLabel = new JLabel("👥 Total Pengguna: " + dataStorage.getUsers().size());
        totalUsersLabel.setFont(new Font("Arial", Font.BOLD, 20));
        totalUsersPanel.add(totalUsersLabel, BorderLayout.CENTER);
        statsPanel.add(totalUsersPanel);

        JPanel totalTransactionsPanel = new JPanel(new BorderLayout());
        totalTransactionsPanel.setBackground(Color.WHITE);
        totalTransactionsPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));
        JLabel totalTransactionsLabel = new JLabel("📜 Total Peminjaman: " + dataStorage.getTransactions().size());
        totalTransactionsLabel.setFont(new Font("Arial", Font.BOLD, 20));
        totalTransactionsPanel.add(totalTransactionsLabel, BorderLayout.CENTER);
        statsPanel.add(totalTransactionsPanel);

        JPanel totalFinesPanel = new JPanel(new BorderLayout());
        totalFinesPanel.setBackground(Color.WHITE);
        totalFinesPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));
        JLabel totalFinesLabel = new JLabel("💸 Total Denda: Rp " + dataStorage.getFines().stream().mapToDouble(Transaction::getFine).sum());
        totalFinesLabel.setFont(new Font("Arial", Font.BOLD, 20));
        totalFinesPanel.add(totalFinesLabel, BorderLayout.CENTER);
        statsPanel.add(totalFinesPanel);

        // Tabel
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Arial", Font.BOLD, 20));

        // Tabel Buku Populer
        DefaultTableModel popularBooksModel = new DefaultTableModel(new String[]{"Judul", "Jumlah Peminjaman"}, 0);
        Map<String, Integer> bookCount = new HashMap<>();
        for (Transaction t : dataStorage.getTransactions()) {
            String bookId = t.getBookId();
            bookCount.merge(bookId, 1, Integer::sum);
        }
        bookCount.entrySet().stream()
                .sorted((e1, e2) -> e2.getValue().compareTo(e1.getValue()))
                .limit(5)
                .forEach(e -> {
                    String title = dataStorage.getBooks().stream()
                            .filter(b -> b.getId().equals(e.getKey()))
                            .findFirst().map(Book::getTitle).orElse("Unknown");
                    popularBooksModel.addRow(new Object[]{title, e.getValue()});
                });
        JTable popularBooksTable = new JTable(popularBooksModel);
        popularBooksTable.setFillsViewportHeight(true);
        popularBooksTable.setRowHeight(40);
        popularBooksTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 20));
        popularBooksTable.getTableHeader().setBackground(new Color(0, 123, 255));
        popularBooksTable.getTableHeader().setForeground(Color.WHITE);
        JScrollPane popularBooksScroll = new JScrollPane(popularBooksTable);
        tabbedPane.addTab("Buku Populer", popularBooksScroll);

        // Tabel Peminjaman Aktif
        DefaultTableModel activeLoansModel = new DefaultTableModel(new String[]{"ID Transaksi", "Judul Buku", "Peminjam", "Tanggal Kembali"}, 0);
        for (Transaction t : dataStorage.getTransactions()) {
            if (t.getReturnDate() == null) {
                String userName = dataStorage.getUsers().stream().filter(u -> u.getId().equals(t.getUserId())).findFirst().map(User::getName).orElse("Unknown");
                String bookTitle = dataStorage.getBooks().stream().filter(b -> b.getId().equals(t.getBookId())).findFirst().map(Book::getTitle).orElse("Unknown");
                activeLoansModel.addRow(new Object[]{"LOAN" + t.getBookId(), bookTitle, userName, t.getDueDate().toString()});
            }
        }
        JTable activeLoansTable = new JTable(activeLoansModel);
        activeLoansTable.setFillsViewportHeight(true);
        activeLoansTable.setRowHeight(40);
        activeLoansTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 20));
        activeLoansTable.getTableHeader().setBackground(new Color(0, 123, 255));
        activeLoansTable.getTableHeader().setForeground(Color.WHITE);
        JScrollPane activeLoansScroll = new JScrollPane(activeLoansTable);
        tabbedPane.addTab("Peminjaman Aktif", activeLoansScroll);

        // Tabel Rekam Jejak Siswa
        DefaultTableModel studentHistoryModel = new DefaultTableModel(new String[]{"Nama Siswa", "Jumlah Peminjaman", "Total Denda"}, 0);
        for (User u : dataStorage.getUsers()) {
            long loanCount = dataStorage.getTransactions().stream().filter(t -> t.getUserId().equals(u.getId())).count();
            double totalFine = dataStorage.getFines().stream().filter(t -> t.getUserId().equals(u.getId())).mapToDouble(Transaction::getFine).sum();
            studentHistoryModel.addRow(new Object[]{u.getName(), loanCount, "Rp " + totalFine});
        }
        JTable studentHistoryTable = new JTable(studentHistoryModel);
        studentHistoryTable.setFillsViewportHeight(true);
        studentHistoryTable.setRowHeight(40);
        studentHistoryTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 20));
        studentHistoryTable.getTableHeader().setBackground(new Color(0, 123, 255));
        studentHistoryTable.getTableHeader().setForeground(Color.WHITE);
        JScrollPane studentHistoryScroll = new JScrollPane(studentHistoryTable);
        tabbedPane.addTab("Rekam Jejak Siswa", studentHistoryScroll);

        JPanel container = new JPanel(new BorderLayout());
        container.setBackground(new Color(248, 249, 250));
        container.add(statsPanel, BorderLayout.NORTH);
        container.add(tabbedPane, BorderLayout.CENTER);
        add(container, BorderLayout.CENTER);
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