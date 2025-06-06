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
import java.awt.*;
import java.util.Date;

public class BorrowPanel extends JPanel {
    private DataStorage dataStorage;

    public BorrowPanel(DataStorage dataStorage) {
        this.dataStorage = dataStorage;
        setLayout(new BorderLayout());

        // User and book selection
        JPanel selectionPanel = new JPanel();
        JComboBox<String> userBox = new JComboBox<>();
        JComboBox<String> bookBox = new JComboBox<>();
        for (User user : dataStorage.getUsers()) {
            userBox.addItem(user.getName());
        }
        for (Book book : dataStorage.getBooks()) {
            if (!book.isBorrowed()) {
                bookBox.addItem(book.getTitle());
            }
        }
        selectionPanel.add(new JLabel("Pengguna:"));
        selectionPanel.add(userBox);
        selectionPanel.add(new JLabel("Buku:"));
        selectionPanel.add(bookBox);

        // Buttons
        JPanel buttonPanel = new JPanel();
        JButton borrowButton = new JButton("Pinjam");
        JButton returnButton = new JButton("Kembalikan");
        JButton extendButton = new JButton("Perpanjang");
        buttonPanel.add(borrowButton);
        buttonPanel.add(returnButton);
        buttonPanel.add(extendButton);

        // Transaction list
        DefaultListModel<String> transactionListModel = new DefaultListModel<>();
        JList<String> transactionList = new JList<>(transactionListModel);
        for (Transaction transaction : dataStorage.getTransactions()) {
            transactionListModel.addElement("User: " + transaction.getUserId() + ", Book: " + transaction.getBookId());
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
                Date dueDate = DateUtils.addDays(borrowDate, 14); // 2 weeks
                Transaction transaction = new Transaction(selectedBook.getId(), selectedUser.getId(), borrowDate, dueDate);
                selectedBook.setBorrowed(true);
                selectedUser.addTransaction(transaction);
                dataStorage.addTransaction(transaction);
                transactionListModel.addElement("User: " + selectedUser.getName() + ", Book: " + selectedBook.getTitle());
                bookBox.removeItem(bookTitle);
            }
        });

        returnButton.addActionListener(e -> {
            // Simplified return logic
            JOptionPane.showMessageDialog(this, "Fungsi pengembalian akan diimplementasikan.");
        });

        extendButton.addActionListener(e -> {
            // Simplified extend logic
            JOptionPane.showMessageDialog(this, "Fungsi perpanjangan akan diimplementasikan.");
        });

        add(selectionPanel, BorderLayout.NORTH);
        add(buttonPanel, BorderLayout.CENTER);
        add(new JScrollPane(transactionList), BorderLayout.SOUTH);
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