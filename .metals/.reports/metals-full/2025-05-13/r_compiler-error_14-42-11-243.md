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
import utils.DateUtils;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.Date;

public class BorrowPanel extends JPanel {
    private DataStorage dataStorage;

    public BorrowPanel(DataStorage dataStorage) {
        this.dataStorage = dataStorage;
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Form
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel userLabel = new JLabel("Nama Peminjam:");
        userLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        JComboBox<String> userBox = new JComboBox<>();
        for (User user : dataStorage.getUsers()) {
            userBox.addItem(user.getName());
        }
        userBox.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        JLabel bookLabel = new JLabel("Judul Buku:");
        bookLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        JComboBox<String> bookBox = new JComboBox<>();
        for (Book book : dataStorage.getBooks()) {
            if (!book.isBorrowed()) {
                bookBox.addItem(book.getTitle());
            }
        }
        bookBox.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        JLabel classLabel = new JLabel("Kelas:");
        classLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        JTextField classField = new JTextField(20);
        classField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        JLabel nimLabel = new JLabel("NIM:");
        nimLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        JTextField nimField = new JTextField(20);
        nimField.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(userLabel, gbc);
        gbc.gridx = 1;
        formPanel.add(userBox, gbc);
        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(bookLabel, gbc);
        gbc.gridx = 1;
        formPanel.add(bookBox, gbc);
        gbc.gridx = 0;
        gbc.gridy = 2;
        formPanel.add(classLabel, gbc);
        gbc.gridx = 1;
        formPanel.add(classField, gbc);
        gbc.gridx = 0;
        gbc.gridy = 3;
        formPanel.add(nimLabel, gbc);
        gbc.gridx = 1;
        formPanel.add(nimField, gbc);

        JButton borrowButton = new JButton("Pinjam");
        borrowButton.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        borrowButton.setBackground(new Color(0, 123, 255));
        borrowButton.setForeground(Color.WHITE);
        borrowButton.setFocusPainted(false);
        JButton returnButton = new JButton("Kembalikan");
        returnButton.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        returnButton.setBackground(new Color(0, 123, 255));
        returnButton.setForeground(Color.WHITE);
        returnButton.setFocusPainted(false);
        JButton extendButton = new JButton("Belum Dikembalikan");
        extendButton.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        extendButton.setBackground(new Color(220, 53, 69));
        extendButton.setForeground(Color.WHITE);
        extendButton.setFocusPainted(false);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        buttonPanel.add(borrowButton);
        buttonPanel.add(returnButton);
        buttonPanel.add(extendButton);

        // Table
        String[] columnNames = {"ID Peminjaman", "Judul Buku", "Peminjam", "Kelas", "NIM", "Tanggal Pinjam", "Tanggal Kembali", "Status", "Aksi"};
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

        // Populate initial data
        for (Transaction transaction : dataStorage.getTransactions()) {
            tableModel.addRow(new Object[]{
                "LOAN" + transaction.getBookId(),
                dataStorage.getBooks().stream().filter(b -> b.getId().equals(transaction.getBookId())).findFirst().map(Book::getTitle).orElse(""),
                dataStorage.getUsers().stream().filter(u -> u.getId().equals(transaction.getUserId())).findFirst().map(User::getName).orElse(""),
                "TIF 24 C", // Placeholder
                "24552011234", // Placeholder
                transaction.getBorrowDate().toString(),
                transaction.getDueDate().toString(),
                transaction.getReturnDate() == null ? "Dipinjam" : "Dikembalikan",
                "Perpanjang"
            });
        }

        borrowButton.addActionListener(e -> {
            String userName = (String) userBox.getSelectedItem();
            String bookTitle = (String) bookBox.getSelectedItem();
            User selectedUser = dataStorage.getUsers().stream()
                    .filter(u -> u.getName().equals(userName))
                    .findFirst()
                    .orElse(null);
            Book selectedBook = dataStorage.getBooks().stream()
                    .filter(b -> b.getTitle().equals(bookTitle))
                    .findFirst()
                    .orElse(null);
            if (selectedUser != null && selectedBook != null && !selectedBook.isBorrowed()) {
                Date borrowDate = new Date();
                Date dueDate = DateUtils.addDays(borrowDate, 14);
                Transaction transaction = new Transaction(selectedBook.getId(), selectedUser.getId(), borrowDate, dueDate);
                selectedBook.setBorrowed(true);
                selectedUser.addTransaction(transaction);
                dataStorage.addTransaction(transaction);
                tableModel.addRow(new Object[]{
                    "LOAN" + selectedBook.getId(),
                    selectedBook.getTitle(),
                    selectedUser.getName(),
                    "TIF 24 C",
                    "24552011234",
                    borrowDate.toString(),
                    dueDate.toString(),
                    "Dipinjam",
                    "Perpanjang"
                });
                bookBox.removeItem(bookTitle);
            }
        });

        returnButton.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow >= 0) {
                String bookId = dataStorage.getTransactions().get(selectedRow).getBookId();
                Book book = dataStorage.getBooks().stream()
                        .filter(b -> b.getId().equals(bookId))
                        .findFirst()
                        .orElse(null);
                if (book != null) {
                    book.setBorrowed(false);
                    dataStorage.getTransactions().get(selectedRow).setReturnDate(new Date());
                    tableModel.setValueAt("Dikembalikan", selectedRow, 7);
                    bookBox.addItem(book.getTitle());
                }
            }
        });

        extendButton.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "Fungsi perpanjangan belum diimplementasikan.");
        });

        add(formPanel, BorderLayout.NORTH);
        add(buttonPanel, BorderLayout.CENTER);
        add(scrollPane, BorderLayout.SOUTH);
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