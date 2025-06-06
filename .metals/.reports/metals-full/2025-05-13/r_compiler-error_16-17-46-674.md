file://<WORKSPACE>/src/ui/StatsPanel.java
### java.util.NoSuchElementException: next on empty iterator

occurred in the presentation compiler.

presentation compiler configuration:


action parameters:
uri: file://<WORKSPACE>/src/ui/StatsPanel.java
text:
```scala
package ui;

import model.Transaction;
import storage.DataStorage;
import javax.swing.*;
import java.awt.*;

public class StatsPanel extends JPanel {
    private DataStorage dataStorage;

    public StatsPanel(DataStorage dataStorage) {
        this.dataStorage = dataStorage;
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));
        setBackground(new Color(248, 249, 250));

        JPanel statsPanel = new JPanel(new GridLayout(2, 2, 20, 20));
        statsPanel.setBackground(new Color(248, 249, 250));

        JPanel totalBooksPanel = new JPanel(new BorderLayout());
        totalBooksPanel.setBackground(Color.WHITE);
        totalBooksPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));
        JLabel totalBooksLabel = new JLabel("Total Buku: " + dataStorage.getBooks().size());
        totalBooksLabel.setFont(new Font("Arial", Font.BOLD, 20));
        totalBooksLabel.setForeground(new Color(51, 51, 51));
        totalBooksPanel.add(totalBooksLabel, BorderLayout.CENTER);
        statsPanel.add(totalBooksPanel);

        JPanel totalUsersPanel = new JPanel(new BorderLayout());
        totalUsersPanel.setBackground(Color.WHITE);
        totalUsersPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));
        JLabel totalUsersLabel = new JLabel("Total Pengguna: " + dataStorage.getUsers().size());
        totalUsersLabel.setFont(new Font("Arial", Font.BOLD, 20));
        totalUsersLabel.setForeground(new Color(51, 51, 51));
        totalUsersPanel.add(totalUsersLabel, BorderLayout.CENTER);
        statsPanel.add(totalUsersPanel);

        JPanel totalTransactionsPanel = new JPanel(new BorderLayout());
        totalTransactionsPanel.setBackground(Color.WHITE);
        totalTransactionsPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));
        JLabel totalTransactionsLabel = new JLabel("Total Peminjaman: " + dataStorage.getTransactions().size());
        totalTransactionsLabel.setFont(new Font("Arial", Font.BOLD, 20));
        totalTransactionsLabel.setForeground(new Color(51, 51, 51));
        totalTransactionsPanel.add(totalTransactionsLabel, BorderLayout.CENTER);
        statsPanel.add(totalTransactionsPanel);

        JPanel totalFinesPanel = new JPanel(new BorderLayout());
        totalFinesPanel.setBackground(Color.WHITE);
        totalFinesPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));
        JLabel totalFinesLabel = new JLabel("Total Denda: Rp " + dataStorage.getFines().stream().mapToDouble(Transaction::getFine).sum());
        totalFinesLabel.setFont(new Font("Arial", Font.BOLD, 20));
        totalFinesLabel.setForeground(new Color(51, 51, 51));
        totalFinesPanel.add(totalFinesLabel, BorderLayout.CENTER);
        statsPanel.add(totalFinesPanel);

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