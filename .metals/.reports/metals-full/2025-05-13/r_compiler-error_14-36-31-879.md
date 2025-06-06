file://<WORKSPACE>/src/ui/FinesPanel.java
### java.util.NoSuchElementException: next on empty iterator

occurred in the presentation compiler.

presentation compiler configuration:


action parameters:
uri: file://<WORKSPACE>/src/ui/FinesPanel.java
text:
```scala
package ui;

import model.Transaction;
import storage.DataStorage;
import utils.DateUtils;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.Date;

public class FinesPanel extends JPanel {
    private DataStorage dataStorage;

    public FinesPanel(DataStorage dataStorage) {
        this.dataStorage = dataStorage;
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton refreshButton = new JButton("Segarkan");
        refreshButton.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        refreshButton.setBackground(new Color(0, 123, 255));
        refreshButton.setForeground(Color.WHITE);
        refreshButton.setFocusPainted(false);
        JButton notifyButton = new JButton("Kirim Peringatan");
        notifyButton.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        notifyButton.setBackground(new Color(0, 123, 255));
        notifyButton.setForeground(Color.WHITE);
        notifyButton.setFocusPainted(false);
        buttonPanel.add(refreshButton);
        buttonPanel.add(notifyButton);

        // Table
        String[] columnNames = {"ID Peminjaman", "Buku", "Peminjam", "Tanggal Pinjam", "Hari Terlambat", "Aksi"};
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
        table.getTableHeader().setBackground(new Color(0, 123, 255));
        table.getTableHeader().setForeground(Color.WHITE);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        table.setGridColor(new Color(200, 200, 200));
        JScrollPane scrollPane = new JScrollPane(table);

        // Populate data
        updateTable(tableModel);

        refreshButton.addActionListener(e -> updateTable(tableModel));
        notifyButton.addActionListener(e -> JOptionPane.showMessageDialog(this, "Peringatan telah dikirim (simulasi)."));

        add(buttonPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
    }

    private void updateTable(DefaultTableModel tableModel) {
        tableModel.setRowCount(0);
        for (Transaction transaction : dataStorage.getTransactions()) {
            if (transaction.getReturnDate() == null) {
                Date now = new Date();
                if (now.after(transaction.getDueDate())) {
                    long overdueDays = DateUtils.daysBetween(transaction.getDueDate(), now);
                    double fine = overdueDays * 500.0;
                    transaction.setFine(fine);
                    tableModel.addRow(new Object[]{
                        "LOAN" + transaction.getBookId(),
                        dataStorage.getBooks().stream().filter(b -> b.getId().equals(transaction.getBookId())).findFirst().map(Book::getTitle).orElse(""),
                        dataStorage.getUsers().stream().filter(u -> u.getId().equals(transaction.getUserId())).findFirst().map(User::getName).orElse(""),
                        transaction.getBorrowDate().toString(),
                        overdueDays,
                        "Kirim Peringatan"
                    });
                }
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