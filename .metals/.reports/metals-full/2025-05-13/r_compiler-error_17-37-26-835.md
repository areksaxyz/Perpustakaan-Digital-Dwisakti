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

        // Teks Selamat Datang di Atas
        JPanel headerPanel = new JPanel(new GridBagLayout());
        headerPanel.setBackground(new Color(248, 249, 250));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        JLabel welcomeLabel = new JLabel("Selamat Datang di Perpustakaan Digital");
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 28));
        welcomeLabel.setForeground(new Color(0, 123, 255));
        gbc.gridx = 0;
        gbc.gridy = 0;
        headerPanel.add(welcomeLabel, gbc);

        JLabel mottoLabel = new JLabel("Buku adalah jendela dunia.");
        mottoLabel.setFont(new Font("Arial", Font.ITALIC, 20));
        mottoLabel.setForeground(new Color(51, 51, 51));
        gbc.gridy = 1;
        headerPanel.add(mottoLabel, gbc);

        add(headerPanel, BorderLayout.NORTH);

        // Panel untuk 2 Ilustrasi
        JPanel imagePanel = new JPanel(new GridLayout(1, 2, 20, 20));
        imagePanel.setBackground(new Color(248, 249, 250));

        // Ilustrasi Kiri (Placeholder dengan Ikon dan Teks)
        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.setBackground(Color.WHITE);
        leftPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));
        JLabel leftIcon = new JLabel("📖 Membaca adalah kunci untuk membuka pintu pengetahuan.");
        leftIcon.setFont(new Font("Arial", Font.PLAIN, 18));
        leftIcon.setHorizontalAlignment(JLabel.CENTER);
        leftPanel.add(leftIcon, BorderLayout.CENTER);
        imagePanel.add(leftPanel);

        // Ilustrasi Kanan (Placeholder dengan Ikon dan Teks)
        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.setBackground(Color.WHITE);
        rightPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));
        JLabel rightIcon = new JLabel("📚 Setiap buku adalah petualangan baru yang menanti.");
        rightIcon.setFont(new Font("Arial", Font.PLAIN, 18));
        rightIcon.setHorizontalAlignment(JLabel.CENTER);
        rightPanel.add(rightIcon, BorderLayout.CENTER);
        imagePanel.add(rightPanel);

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