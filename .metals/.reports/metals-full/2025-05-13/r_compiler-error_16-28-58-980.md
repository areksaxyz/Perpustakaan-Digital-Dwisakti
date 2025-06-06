file://<WORKSPACE>/src/ui/BerandaPanel.java
### java.util.NoSuchElementException: next on empty iterator

occurred in the presentation compiler.

presentation compiler configuration:


action parameters:
uri: file://<WORKSPACE>/src/ui/BerandaPanel.java
text:
```scala
package ui;

import storage.DataStorage;
import javax.swing.*;
import java.awt.*;

public class BerandaPanel extends JPanel {
    public BerandaPanel(DataStorage dataStorage) {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));
        setBackground(new Color(248, 249, 250));

        JLabel welcomeLabel = new JLabel("Selamat Datang di Perpustakaan Digital");
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 28));
        welcomeLabel.setForeground(new Color(51, 51, 51));
        welcomeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(welcomeLabel);
        add(Box.createVerticalStrut(20));

        JLabel subtitleLabel = new JLabel("Buku adalah jendela dunia.");
        subtitleLabel.setFont(new Font("Arial", Font.ITALIC, 20));
        subtitleLabel.setForeground(new Color(108, 117, 125));
        subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(subtitleLabel);
        add(Box.createVerticalStrut(40));

        JPanel cardsPanel = new JPanel();
        cardsPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 30, 30));
        cardsPanel.setBackground(new Color(248, 249, 250));
        String[] cardTexts = {
            "Membaca adalah kunci untuk membuka pintu pengetahuan.",
            "Setiap buku adalah petualangan baru yang menanti."
        };
        for (String text : cardTexts) {
            JPanel card = new JPanel();
            card.setPreferredSize(new Dimension(300, 250));
            card.setBackground(Color.WHITE);
            card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)
            ));
            card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
            JLabel cardLabel = new JLabel("<html><div style='text-align: center;'>" + text + "</div></html>");
            cardLabel.setFont(new Font("Arial", Font.PLAIN, 18));
            cardLabel.setForeground(new Color(108, 117, 125));
            cardLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            card.add(cardLabel);
            cardsPanel.add(card);
        }
        add(cardsPanel);
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