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
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));
        setBackground(new Color(248, 249, 250));

        // Teks Motivasi di Atas
        JLabel motivasiLabel = new JLabel("🌟 Baca buku untuk membuka pintu dunia pengetahuan! 🌟", SwingConstants.CENTER);
        motivasiLabel.setFont(new Font("Arial", Font.BOLD, 28));
        motivasiLabel.setForeground(new Color(51, 51, 51));
        add(motivasiLabel, BorderLayout.NORTH);

        // Panel untuk 2 Foto
        JPanel imagePanel = new JPanel(new GridLayout(1, 2, 20, 20));
        imagePanel.setBackground(new Color(248, 249, 250));

        // Foto Kiri (Placeholder, ganti dengan path gambar Anda)
        JLabel leftImage = new JLabel(new ImageIcon("<WORKSPACE>/left_book.jpg"));
        leftImage.setHorizontalAlignment(JLabel.CENTER);
        imagePanel.add(leftImage);

        // Foto Kanan (Placeholder, ganti dengan path gambar Anda)
        JLabel rightImage = new JLabel(new ImageIcon("<WORKSPACE>/right_book.jpg"));
        rightImage.setHorizontalAlignment(JLabel.CENTER);
        imagePanel.add(rightImage);

        add(imagePanel, BorderLayout.CENTER);

        // Pastikan gambar ada di direktori yang ditentukan
        if (!new java.io.File("<WORKSPACE>/left_book.jpg").exists() ||
            !new java.io.File("<WORKSPACE>/right_book.jpg").exists()) {
            JOptionPane.showMessageDialog(this, "Gambar tidak ditemukan. Silakan tambahkan file 'left_book.jpg' dan 'right_book.jpg' di <WORKSPACE>/");
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