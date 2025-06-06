file://<WORKSPACE>/src/ui/HomePanel.java
### java.util.NoSuchElementException: next on empty iterator

occurred in the presentation compiler.

presentation compiler configuration:


action parameters:
uri: file://<WORKSPACE>/src/ui/HomePanel.java
text:
```scala
package ui;

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

        JPanel imagePanel = new JPanel(new GridLayout(1, 2, 20, 0));
        imagePanel.setBackground(new Color(248, 249, 250));

        // Placeholder gambar kiri (gunakan file PNG)
        ImageIcon leftIcon = new ImageIcon("<WORKSPACE>/images/left_image.png");
        Image leftImageResized = leftIcon.getImage().getScaledInstance(200, 300, Image.SCALE_SMOOTH);
        JLabel leftImage = new JLabel(new ImageIcon(leftImageResized));
        leftImage.setHorizontalAlignment(JLabel.CENTER);
        imagePanel.add(leftImage);

        // Placeholder gambar kanan (gunakan file PNG)
        ImageIcon rightIcon = new ImageIcon("<WORKSPACE>/images/right_image.png");
        Image rightImageResized = rightIcon.getImage().getScaledInstance(200, 300, Image.SCALE_SMOOTH);
        JLabel rightImage = new JLabel(new ImageIcon(rightImageResized));
        rightImage.setHorizontalAlignment(JLabel.CENTER);
        imagePanel.add(rightImage);

        add(imagePanel, BorderLayout.CENTER);
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