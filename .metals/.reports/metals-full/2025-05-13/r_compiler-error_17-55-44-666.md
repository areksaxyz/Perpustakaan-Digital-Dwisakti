file://<WORKSPACE>/src/ui/BorrowPanel.java
### java.util.NoSuchElementException: next on empty iterator

occurred in the presentation compiler.

presentation compiler configuration:


action parameters:
uri: file://<WORKSPACE>/src/ui/BorrowPanel.java
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
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class BorrowPanel extends JPanel {
    private DataStorage dataStorage;
    private DefaultTableModel tableModel;

    public BorrowPanel(DataStorage dataStorage) {
        this.dataStorage = dataStorage;
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));
        setBackground(new Color(248, 249, 250));

        JLabel titleLabel = new JLabel("Peminjaman Buku", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        add(titleLabel, BorderLayout.NORTH);

        String[] columnNames = {"ID Transaksi", "Nama Peminjam", "Judul Buku", "Tanggal Pinjam", "Tanggal Kembali", "Denda"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        JTable table = new JTable(tableModel);
        table.setFillsViewportHeight(true);
        table.setRowHeight(40);
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 20));
        table.getTableHeader().setBackground(new Color(0, 123, 255));
        table.getTableHeader().setForeground(Color.WHITE);
        table.setFont(new Font("Arial", Font.PLAIN, 18));
        table.setGridColor(new Color(200, 200, 200));
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        JPanel inputPanel = new JPanel(new GridBagLayout());
        inputPanel.setBackground(new Color(248, 249, 250));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel userLabel = new JLabel("Pilih Pengguna:");
        userLabel.setFont(new Font("Arial", Font.ITALIC, 18));
        JComboBox<String> userComboBox = new JComboBox<>();
        for (User user : dataStorage.getUsers()) {
            userComboBox.addItem(user.getName());
        }
        userComboBox.setFont(new Font("Arial", Font.PLAIN, 18));

        JLabel bookLabel = new JLabel("Pilih Buku:");
        bookLabel.setFont(new Font("Arial", Font.ITALIC, 18));
        JComboBox<String> bookComboBox = new JComboBox<>();
        List<Book> availableBooks = dataStorage.getBooks().stream()
                .filter(b -> !b.isBorrowed())
                .collect(Collectors.toList());
        for (Book book : availableBooks) {
            bookComboBox.addItem(book.getTitle());
        }
        bookComboBox.setFont(new Font("Arial", Font.PLAIN, 18));

        JButton borrowButton = new JButton("Pinjam Buku");
        borrowButton.setFont(new Font("Arial", Font.BOLD, 18));
        borrowButton.setBackground(new Color(0, 123, 255));
        borrowButton.setForeground(Color.WHITE);

        gbc.gridx = 0;
        gbc.gridy = 0;
        inputPanel.add(userLabel, gbc);
        gbc.gridx = 1;
        inputPanel.add(userComboBox, gbc);
        gbc.gridx = 0;
        gbc.gridy = 1;
        inputPanel.add(bookLabel, gbc);
        gbc.gridx = 1;
        inputPanel.add(bookComboBox, gbc);
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        inputPanel.add(borrowButton, gbc);

        add(inputPanel, BorderLayout.SOUTH);

        updateTable();

        borrowButton.addActionListener(e -> {
            String userName = (String) userComboBox.getSelectedItem();
            String bookTitle = (String) bookComboBox.getSelectedItem();
            if (userName != null && bookTitle != null) {
                User user = dataStorage.getUsers().stream()
                        .filter(u -> u.getName().equals(userName))
                        .findFirst()
                        .orElse(null);
                Book book = dataStorage.getBooks().stream()
                        .filter(b -> b.getTitle().equals(bookTitle))
                        .findFirst()
                        .orElse(null);
                if (user != null && book != null && !book.isBorrowed()) {
                    Date borrowDate = new Date();
                    Date dueDate = new Date(borrowDate.getTime() + 14L * 24 * 60 * 60 * 1000); // 14 hari
                    Transaction transaction = new Transaction(book.getId(), user.getId(), borrowDate, dueDate);
                    book.setBorrowed(true);
                    book.setBorrowDate(borrowDate);
                    book.setDueDate(dueDate);
                    dataStorage.addTransaction(transaction);
                    updateTable();
                    JOptionPane.showMessageDialog(this, "Buku berhasil dipinjam oleh " + userName);
                    // Refresh book combo box
                    bookComboBox.removeAllItems();
                    availableBooks = dataStorage.getBooks().stream()
                            .filter(b -> !b.isBorrowed())
                            .collect(Collectors.toList());
                    for (Book b : availableBooks) {
                        bookComboBox.addItem(b.getTitle());
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "Gagal meminjam buku. Pastikan buku tersedia.");
                }
            }
        });
    }

    private void updateTable() {
        tableModel.setRowCount(0);
        for (Transaction transaction : dataStorage.getTransactions()) {
            String userName = dataStorage.getUsers().stream()
                    .filter(u -> u.getId().equals(transaction.getUserId()))
                    .findFirst()
                    .map(User::getName)
                    .orElse("Unknown");
            String bookTitle = dataStorage.getBooks().stream()
                    .filter(b -> b.getId().equals(transaction.getBookId()))
                    .findFirst()
                    .map(Book::getTitle)
                    .orElse("Unknown");
            tableModel.addRow(new Object[]{
                "TRX" + transaction.getBookId(),
                userName,
                bookTitle,
                transaction.getBorrowDate(),
                transaction.getDueDate(),
                transaction.getFine() > 0 ? "Rp " + transaction.getFine() : "Tidak Ada"
            });
        }
    }
}=
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