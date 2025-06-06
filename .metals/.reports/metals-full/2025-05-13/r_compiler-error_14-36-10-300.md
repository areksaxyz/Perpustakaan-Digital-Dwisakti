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
import java.awt.*;
import java.util.Date;

public class FinesPanel extends JPanel {
    private DataStorage dataStorage;

    public FinesPanel(DataStorage dataStorage) {
        this.dataStorage = dataStorage;
        setLayout(new BorderLayout());

        DefaultListModel<String> finesListModel = new DefaultListModel<>();
        JList<String> finesList = new JList<>(finesListModel);

        for (Transaction transaction : dataStorage.getTransactions()) {
            if (transaction.getReturnDate() == null) {
                Date now = new Date();
                if (now.after(transaction.getDueDate())) {
                    long overdueDays = DateUtils.daysBetween(transaction.getDueDate(), now);
                    double fine = overdueDays * 500.0; // 500 per day
                    transaction.setFine(fine);
                    finesListModel.addElement("Book: " + transaction.getBookId() + ", User: " + transaction.getUserId() + ", Fine: Rp " + fine);
                }
            }
        }

        add(new JScrollPane(finesList), BorderLayout.CENTER);
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