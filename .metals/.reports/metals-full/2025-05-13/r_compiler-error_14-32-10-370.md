file://<WORKSPACE>/src/ui/LibraryUI.java
### java.util.NoSuchElementException: next on empty iterator

occurred in the presentation compiler.

presentation compiler configuration:


action parameters:
uri: file://<WORKSPACE>/src/ui/LibraryUI.java
text:
```scala
package ui;

import storage.DataStorage;
import javax.swing.*;
import java.awt.*;

public class LibraryUI extends JFrame {
    private DataStorage dataStorage;
    private JPanel contentPanel;

    public LibraryUI() {
        dataStorage = new DataStorage();
        setTitle("Perpustakaan Digital - Muhammadiyah Argas Reska");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLayout(new BorderLayout());

        // Sidebar (Menu Utama)
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBackground(new Color(240, 240, 240));
        sidebar.setPreferredSize(new Dimension(200, getHeight()));

        JLabel menuLabel = new JLabel("MENU UTAMA");
        menuLabel.setFont(new Font("Arial", Font.BOLD, 16));
        menuLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        sidebar.add(menuLabel);
        sidebar.add(Box.createVerticalStrut(20));

        String[] menuItems = {"Beranda", "Katalog Buku", "Buku", "Peminjaman", "Riwayat & Statistik", "Manajemen Denda"};
        for (String item : menuItems) {
            JButton button = new JButton(item);
            button.setAlignmentX(Component.CENTER_ALIGNMENT);
            button.setMaximumSize(new Dimension(180, 40));
            button.addActionListener(e -> switchPanel(item));
            sidebar.add(button);
            sidebar.add(Box.createVerticalStrut(10));
        }

        // Main content area
        contentPanel = new JPanel();
        contentPanel.setLayout(new BorderLayout());

        // Default Beranda panel
        JPanel berandaPanel = new JPanel();
        berandaPanel.setLayout(new BoxLayout(berandaPanel, BoxLayout.Y_AXIS));
        JLabel welcomeLabel = new JLabel("Selamat Datang di Perpustakaan Digital");
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 24));
        welcomeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        berandaPanel.add(Box.createVerticalStrut(50));
        berandaPanel.add(welcomeLabel);
        berandaPanel.add(Box.createVerticalStrut(30));

        JPanel cardsPanel = new JPanel();
        cardsPanel.setLayout(new FlowLayout());
        JPanel card1 = new JPanel();
        card1.setBackground(Color.WHITE);
        card1.setPreferredSize(new Dimension(200, 200));
        card1.add(new JLabel("Membaca adalah kunci untuk membuka pintu pengetahuan."));
        JPanel card2 = new JPanel();
        card2.setBackground(Color.WHITE);
        card2.setPreferredSize(new Dimension(200, 200));
        card2.add(new JLabel("Setiap buku adalah petualangan baru yang menanti."));
        cardsPanel.add(card1);
        cardsPanel.add(card2);
        berandaPanel.add(cardsPanel);

        contentPanel.add(berandaPanel, BorderLayout.CENTER);

        // Add components to frame
        add(sidebar, BorderLayout.WEST);
        add(contentPanel, BorderLayout.CENTER);

        // Top bar (like in the image)
        JPanel topBar = new JPanel();
        topBar.setLayout(new BorderLayout());
        JLabel titleLabel = new JLabel("Perpustakaan Digital - Muhammadiyah Argas Reska");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        topBar.add(titleLabel, BorderLayout.WEST);
        JButton startButton = new JButton("Mulai Presensi");
        startButton.setBackground(new Color(0, 120, 215));
        startButton.setForeground(Color.WHITE);
        topBar.add(startButton, BorderLayout.EAST);
        add(topBar, BorderLayout.NORTH);
    }

    private void switchPanel(String panelName) {
        contentPanel.removeAll();
        switch (panelName) {
            case "Beranda":
                JPanel berandaPanel = new JPanel();
                berandaPanel.setLayout(new BoxLayout(berandaPanel, BoxLayout.Y_AXIS));
                JLabel welcomeLabel = new JLabel("Selamat Datang di Perpustakaan Digital");
                welcomeLabel.setFont(new Font("Arial", Font.BOLD, 24));
                welcomeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
                berandaPanel.add(Box.createVerticalStrut(50));
                berandaPanel.add(welcomeLabel);
                contentPanel.add(berandaPanel, BorderLayout.CENTER);
                break;
            case "Katalog Buku":
                contentPanel.add(new CatalogPanel(dataStorage), BorderLayout.CENTER);
                break;
            case "Buku":
                contentPanel.add(new ReaderPanel(), BorderLayout.CENTER);
                break;
            case "Peminjaman":
                contentPanel.add(new BorrowPanel(dataStorage), BorderLayout.CENTER);
                break;
            case "Riwayat & Statistik":
                contentPanel.add(new StatsPanel(dataStorage), BorderLayout.CENTER);
                break;
            case "Manajemen Denda":
                contentPanel.add(new FinesPanel(dataStorage), BorderLayout.CENTER);
                break;
        }
        contentPanel.revalidate();
        contentPanel.repaint();
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