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

        JTextArea statsArea = new JTextArea();
        statsArea.setEditable(false);

        StringBuilder stats = new StringBuilder();
        stats.append("Statistik Perpustakaan:\n\n");
        stats.append("Total Peminjaman: ").append(dataStorage.getTransactions().size()).append("\n");
        stats.append("Buku Populer: Tidak tersedia (perlu implementasi lebih lanjut).\n");
        stats.append("Peminjam Aktif: Tidak tersedia (perlu implementasi lebih lanjut).\n");

        statsArea.setText(stats.toString());
        add(new JScrollPane(statsArea), BorderLayout.CENTER);
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