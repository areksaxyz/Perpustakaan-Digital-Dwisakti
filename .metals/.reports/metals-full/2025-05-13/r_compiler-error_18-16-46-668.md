file://<WORKSPACE>/src/ui/HomePanel.java
### java.util.NoSuchElementException: next on empty iterator

occurred in the presentation compiler.

presentation compiler configuration:


action parameters:
offset: 0
uri: file://<WORKSPACE>/src/ui/HomePanel.java
text:
```scala
@@package ui;

import storage.DataStorage;
import javax.swing.*;
import java.awt.*;

public class HomePanel extends JPanel {
    private DataStorage dataStorage;

    public HomePanel(DataStorage dataStorage) {
        this.dataStorage = dataStorage;
        setLayout(new BorderLayout());
        setBackground(new Color(248, 249, 250));
        setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));

        JLabel titleLabel = new JLabel("Selamat Datang di Sistem Perpustakaan", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        add(titleLabel, BorderLayout.NORTH);

        JPanel infoPanel = new JPanel(new GridLayout(3, 1, 10, 10));
        infoPanel.setBackground(new Color(248, 249, 250));

        JLabel bookCountLabel = new JLabel("Jumlah Buku: " + dataStorage.getBooks().size());
        bookCountLabel.setFont(new Font("Arial", Font.PLAIN, 18));
        JLabel userCountLabel = new JLabel("Jumlah Pengguna: " + dataStorage.getUsers().size());
        userCountLabel.setFont(new Font("Arial", Font.PLAIN, 18));
        JLabel transactionCountLabel = new JLabel("Jumlah Transaksi: " + dataStorage.getTransactions().size());
        transactionCountLabel.setFont(new Font("Arial", Font.PLAIN, 18));

        infoPanel.add(bookCountLabel);
        infoPanel.add(userCountLabel);
        infoPanel.add(transactionCountLabel);

        add(infoPanel, BorderLayout.CENTER);
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