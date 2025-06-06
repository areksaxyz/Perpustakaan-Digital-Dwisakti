file://<WORKSPACE>/src/ui/BorrowPanel.java
### java.util.NoSuchElementException: next on empty iterator

occurred in the presentation compiler.

presentation compiler configuration:


action parameters:
offset: 13174
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
        setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));
        setBackground(new Color(248, 249, 250));

        // Form
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel userLabel = new JLabel("Nama Peminjam:");
        userLabel.setFont(new Font("Arial", Font.ITALIC, 18));
        JComboBox<String> userBox = new JComboBox<>();
        for (User user : dataStorage.getUsers()) {
            userBox.addItem(user.getName());
        }
        userBox.setFont(new Font("Arial", Font.PLAIN, 18));
        JLabel bookLabel = new JLabel("Judul Buku:");
        bookLabel.setFont(new Font("Arial", Font.ITALIC, 18));
        bookBox = new JComboBox<>();
        updateBookBox();
        bookBox.setFont(new Font("Arial", Font.PLAIN, 18));
        JLabel classLabel = new JLabel("Kelas:");
        classLabel.setFont(new Font("Arial", Font.ITALIC, 18));
        JTextField classField = new JTextField(20);
        classField.setFont(new Font("Arial", Font.PLAIN, 18));
        JLabel nimLabel = new JLabel("NIM (Angka Saja):");
        nimLabel.setFont(new Font("Arial", Font.ITALIC, 18));
        JTextField nimField = new JTextField(20);
        nimField.setFont(new Font("Arial", Font.PLAIN, 18));
        JLabel jurusanLabel = new JLabel("Jurusan:");
        jurusanLabel.setFont(new Font("Arial", Font.ITALIC, 18));
        JComboBox<String> jurusanBox = new JComboBox<>(new String[]{"Teknik Informatika", "Industri", "Bisnis Digital"});
        jurusanBox.setFont(new Font("Arial", Font.PLAIN, 18));

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
        gbc.gridx = 0;
        gbc.gridy = 4;
        formPanel.add(jurusanLabel, gbc);
        gbc.gridx = 1;
        formPanel.add(jurusanBox, gbc);

        JButton borrowButton = new JButton("Pinjam");
        borrowButton.setFont(new Font("Arial", Font.BOLD, 18));
        borrowButton.setBackground(new Color(0, 123, 255));
        borrowButton.setForeground(Color.WHITE);
        borrowButton.setFocusPainted(false);
        JButton returnButton = new JButton("Kembalikan");
        returnButton.setFont(new Font("Arial", Font.BOLD, 18));
        returnButton.setBackground(new Color(0, 123, 255));
        returnButton.setForeground(Color.WHITE);
        returnButton.setFocusPainted(false);
        JButton extendButton = new JButton("Perpanjang");
        extendButton.setFont(new Font("Arial", Font.BOLD, 18));
        extendButton.setBackground(new Color(0, 123, 255));
        extendButton.setForeground(Color.WHITE);
        extendButton.setFocusPainted(false);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.add(borrowButton);
        buttonPanel.add(returnButton);
        buttonPanel.add(extendButton);

        // Table
        String[] columnNames = {"ID Peminjaman", "Judul Buku", "Peminjam", "Kelas", "NIM", "Jurusan", "Tanggal Pinjam", "Tanggal Kembali", "Status", "Aksi"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 9;
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

        // Populate initial data
        for (Transaction transaction : dataStorage.getTransactions()) {
            tableModel.addRow(new Object[]{
                "LOAN" + transaction.getBookId(),
                dataStorage.getBooks().stream().filter(b -> b.getId().equals(transaction.getBookId())).findFirst().map(Book::getTitle).orElse(""),
                dataStorage.getUsers().stream().filter(u -> u.getId().equals(transaction.getUserId())).findFirst().map(User::getName).orElse(""),
                "TIF 24 C",
                "24552011234",
                "Teknik Informatika",
                transaction.getBorrowDate().toString(),
                transaction.getDueDate().toString(),
                transaction.getReturnDate() == null ? "Dipinjam" : "Dikembalikan",
                transaction.getReturnDate() == null ? "Belum Dikembalikan" : ""
            });
        }

        borrowButton.addActionListener(e -> {
            String userName = (String) userBox.getSelectedItem();
            String bookTitle = (String) bookBox.getSelectedItem();
            String kelas = classField.getText();
            String nim = nimField.getText();
            String jurusan = (String) jurusanBox.getSelectedItem();
            if (kelas.isEmpty() || nim.isEmpty() || !nim.matches("\\d+")) {
                JOptionPane.showMessageDialog(this, "Kelas dan NIM (angka saja) harus diisi!");
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
                    jurusan,
                    borrowDate.toString(),
                    dueDate.toString(),
                    "Dipinjam",
                    "Belum Dikembalikan"
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
                    tableModel.setValueAt("Dikembalikan", selectedRow, 8);
                    tableModel.setValueAt("", selectedRow, 9);
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
                    tableModel.setValueAt(newDueDate.toString(), selectedRow, 7);
                    JOptionPane.showMessageDialog(this, "Tanggal pengembalian diperpanjang hingga " + newDueDate);
                } else {
                    JOptionPane.showMessageDialog(this, "Buku sudah dikembalikan!");
                }
            } else {
                JOptionPane.showMessageDialog(this, "Pilih transaksi untuk diperpanjang!");
            }
        });

        table.getColumnModel().getColumn(9).setCellRenderer(new ActionRenderer());
        table.getColumnModel().getColumn(9).setCellEditor(new ActionEditor(new JCheckBox(), dataStorage, table));

        add(formPanel, BorderLayout.NORTH);
        add(buttonPanel, BorderLayout.CENTER);
        add(scrollPane, BorderLayout.SOUTH);

        updateBookBox();
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

class ActionRenderer extends JLabel implements TableCellRenderer {
    public ActionRenderer() {
        setOpaque(true);
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        if (value instanceof String && ((String) value).equals("Belum Dikembalikan")) {
            JButton button = new JButton("Belum Dikembalikan");
            button.setFont(new Font("Arial", Font.BOLD, 18));
            button.setBackground(new Color(220, 53, 69));
            button.setForeground(Color.WHITE);
            button.setFocusPainted(false);
            return button;
        }
        setText(value != null ? value.toString() : "");
        setFont(new Font("Arial", Font.PLAIN, 18));
        return this;
    }
}

class ActionEditor extends DefaultCellEditor {
    private String label;
    private JButton button;
    private boolean isPushed;
    private DataStorage dataStorage;
    private JTable table;

    public ActionEditor(JCheckBox checkBox, DataStorage dataStorage, JTable table) {
        super(checkBox);
        this.dataStorage = dataStorage;
        this.table = table;
        button = new JButton();
        button.setOpaque(true);
        button.addActionListener(e -> fireEditingStopped());
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        if (value instanceof String && ((String) value).equals("Belum Dikembalikan")) {
            label = (String) value;
            button.setText(label);
            button.setFont(new Font("Arial", Font.BOLD, 18));
            button.setBackground(new Color(220, 53, 69));
            button.setForeground(Color.WHITE);
            button.setFocusPainted(false);
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
                Transaction transaction = dataStorage.getTransactions().get(row);
                Date now = new Date();
                if (now.after(transaction.getDueDate()) && transaction.getReturnDate() == null) {
                    long overdueDays = DateUtils.daysBetween(transaction.getDueDate(), now);
                    double fine = overdueDays * 500.0;
                    transaction.setFine(fine);
                    dataStorage.getFines().add(transaction); // Asumsi ada metode untuk menambahkan ke fines
                    JOptionPane.showMessa@@geDialog(null, "Transaksi ditambahkan ke Manajemen Denda.");
                } else {
                    JOptionPane.showMessageDialog(null, "Buku masih dalam batas waktu atau sudah dikembalikan.");
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
	dotty.tools.pc.HoverProvider$.hover(HoverProvider.scala:40)
	dotty.tools.pc.ScalaPresentationCompiler.hover$$anonfun$1(ScalaPresentationCompiler.scala:389)
```
#### Short summary: 

java.util.NoSuchElementException: next on empty iterator