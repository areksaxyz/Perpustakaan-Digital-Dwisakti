file://<WORKSPACE>/src/ui/BorrowPanel.java
### java.util.NoSuchElementException: next on empty iterator

occurred in the presentation compiler.

presentation compiler configuration:


action parameters:
offset: 5547
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
    private DefaultTableModel tableModel;
    private JComboBox<String> bookBox;

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
        bookBox = new JComboBox<>();
        updateBookBox();
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
        JButton extendButton = new JButton("Perpanjang");
        extendButton.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        extendButton.setBackground(new Color(0, 123, 255));
        extendButton.setForeground(Color.WHITE);
        extendButton.setFocusPainted(false);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        buttonPanel.add(borrowButton);
        buttonPanel.add(returnButton);
        buttonPanel.add(extendButton);

        // Table
        String[] columnNames = {"ID Peminjaman", "Judul Buku", "Peminjam", "Kelas", "NIM", "Tanggal Pinjam", "Tanggal Kembali", "Status", "Aksi"};
        tableModel = new DefaultTableModel(columnNames, 0) {
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
            String kelas = classField.getText();
            String nim = nimField.getText();
            if (kelas.isEmpty() || nim.isEmpty()) {
                JOptionPane.show@@MessageDialog(this, "Kelas dan NIM harus diisi!");
                return;
            }
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
                    kelas,
                    nim,
                    borrowDate.toString(),
                    dueDate.toString(),
                    "Dipinjam",
                    "Perpanjang"
                });
                bookBox.removeItem(bookTitle);
                classField.setText("");
                nimField.setText("");
            } else {
                JOptionPane.showMessageDialog(this, "Gagal meminjam buku. Pastikan buku tersedia!");
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
                if (book != null && book.isBorrowed()) {
                    book.setBorrowed(false);
                    dataStorage.getTransactions().get(selectedRow).setReturnDate(new Date());
                    tableModel.setValueAt("Dikembalikan", selectedRow, 7);
                    updateBookBox();
                } else {
                    JOptionPane.showMessageDialog(this, "Buku sudah dikembalikan atau tidak valid!");
                }
            } else {
                JOptionPane.showMessageDialog(this, "Pilih transaksi untuk dikembalikan!");
            }
        });

        extendButton.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow >= 0) {
                Transaction transaction = dataStorage.getTransactions().get(selectedRow);
                if (transaction.getReturnDate() == null) {
                    Date newDueDate = DateUtils.addDays(transaction.getDueDate(), 7);
                    transaction.setDueDate(newDueDate);
                    tableModel.setValueAt(newDueDate.toString(), selectedRow, 6);
                    JOptionPane.showMessageDialog(this, "Tanggal pengembalian diperpanjang hingga " + newDueDate);
                } else {
                    JOptionPane.showMessageDialog(this, "Buku sudah dikembalikan!");
                }
            } else {
                JOptionPane.showMessageDialog(this, "Pilih transaksi untuk diperpanjang!");
            }
        });

        add(formPanel, BorderLayout.NORTH);
        add(buttonPanel, BorderLayout.CENTER);
        add(scrollPane, BorderLayout.SOUTH);
    }

    private void updateBookBox() {
        bookBox.removeAllItems();
        for (Book book : dataStorage.getBooks()) {
            if (!book.isBorrowed()) {
                bookBox.addItem(book.getTitle());
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