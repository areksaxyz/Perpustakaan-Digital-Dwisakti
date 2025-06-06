file://<WORKSPACE>/src/ui/StatisticsPanel.java
### java.util.NoSuchElementException: next on empty iterator

occurred in the presentation compiler.

presentation compiler configuration:


action parameters:
offset: 1627
uri: file://<WORKSPACE>/src/ui/StatisticsPanel.java
text:
```scala
package ui;

import storage.DataStorage;
import javax.swing.*;
import java.awt.*;

public class StatisticsPanel extends JPanel {
    private DataStorage dataStorage;

    public StatisticsPanel(DataStorage dataStorage) {
        this.dataStorage = dataStorage;
        setLayout(new BorderLayout());
        setBackground(new Color(248, 249, 250));
        setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));

        JLabel titleLabel = new JLabel("Statistik Perpustakaan", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        add(titleLabel, BorderLayout.NORTH);

        JPanel statsPanel = new JPanel(new GridLayout(4, 1, 10, 10));
        statsPanel.setBackground(new Color(248, 249, 250));

        long borrowedBooks = dataStorage.getBooks().stream().filter(Book::isBorrowed).count();
        long digitalBooks = dataStorage.getBooks().stream().filter(Book::isDigital).count();
        long totalFines = dataStorage.getFines().stream().mapToLong(Transaction::getFine).sum();

        JLabel totalBooksLabel = new JLabel("Total Buku: " + dataStorage.getBooks().size());
        totalBooksLabel.setFont(new Font("Arial", Font.PLAIN, 18));
        JLabel borrowedBooksLabel = new JLabel("Buku yang Dipinjam: " + borrowedBooks);
        borrowedBooksLabel.setFont(new Font("Arial", Font.PLAIN, 18));
        JLabel digitalBooksLabel = new JLabel("Buku Digital: " + digitalBooks);
        digitalBooksLabel.setFont(new Font("Arial", Font.PLAIN, 18));
        JLabel finesLabel = new JLabel("Total Denda Belum Dibayar: Rp " + totalFines);
        finesLabel.setFont(new Font("A@@rial", Font.PLAIN, 18));

        statsPanel.add(totalBooksLabel);
        statsPanel.add(borrowedBooksLabel);
        statsPanel.add(digitalBooksLabel);
        statsPanel.add(finesLabel);

        add(statsPanel, BorderLayout.CENTER);
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