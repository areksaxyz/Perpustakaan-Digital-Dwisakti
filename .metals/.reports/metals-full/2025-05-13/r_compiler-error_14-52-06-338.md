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
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class LibraryUI extends JFrame {
    private DataStorage dataStorage;
    private JPanel contentPanel;

    public LibraryUI() {
        dataStorage = new DataStorage();
        setTitle("Perpustakaan Digital - Muhammadiyah Arga Reskaptati");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 600);
        setLayout(new BorderLayout());

        // Sidebar
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBackground(new Color(248, 249, 250));
        sidebar.setPreferredSize(new Dimension(250, getHeight()));
        sidebar.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));

        JLabel menuLabel = new JLabel("MENU UTAMA");
        menuLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        menuLabel.setForeground(new Color(33, 37, 41));
        menuLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        sidebar.add(menuLabel);
        sidebar.add(Box.createVerticalStrut(20));

        String[] menuItems = {"Beranda", "Katalog Buku", "Buku", "Peminjaman", "Riwayat & Statistik", "Manajemen Denda"};
        for (String item : menuItems) {
            JButton button = new JButton(item);
            button.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            button.setBackground(Color.WHITE);
            button.setForeground(new Color(33, 37, 41));
            button.setFocusPainted(false);
            button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
            button.setAlignmentX(Component.CENTER_ALIGNMENT);
            button.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    button.setBackground(new Color(0, 123, 255));
                    button.setForeground(Color.WHITE);
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    button.setBackground(Color.WHITE);
                    button.setForeground(new Color(33, 37, 41));
                }
            });
            button.addActionListener(e -> switchPanel(item));
            sidebar.add(button);
            sidebar.add(Box.createVerticalStrut(10));
        }

        // Top Bar
        JPanel topBar = new JPanel();
        topBar.setLayout(new BorderLayout());
        topBar.setBackground(Color.WHITE);
        topBar.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(200, 200, 200)));
        JLabel titleLabel = new JLabel("Perpustakaan Digital - Muhammadiyah Arga Reskaptati");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        JButton startButton = new JButton("Mulai Presensi");
        startButton.setBackground(new Color(0, 123, 255));
        startButton.setForeground(Color.WHITE);
        startButton.setFocusPainted(false);
        startButton.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
        startButton.setFont(new Font("Segoe UI", Font.BOLD, 12));
        topBar.add(titleLabel, BorderLayout.WEST);
        topBar.add(startButton, BorderLayout.EAST);

        // Content Area
        contentPanel = new JPanel();
        contentPanel.setLayout(new BorderLayout());
        contentPanel.setBackground(Color.WHITE);

        // Default Beranda
        switchPanel("Beranda");

        add(sidebar, BorderLayout.WEST);
        add(topBar, BorderLayout.NORTH);
        add(contentPanel, BorderLayout.CENTER);

        setVisible(true);
    }

    public void switchPanel(String panelName) {
        contentPanel.removeAll();
        switch (panelName) {
            case "Beranda":
                JPanel berandaPanel = new JPanel();
                berandaPanel.setLayout(new BoxLayout(berandaPanel, BoxLayout.Y_AXIS));
                berandaPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
                JLabel welcomeLabel = new JLabel("Selamat Datang di Perpustakaan Digital");
                welcomeLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
                welcomeLabel.setForeground(new Color(33, 37, 41));
                welcomeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
                berandaPanel.add(welcomeLabel);
                berandaPanel.add(Box.createVerticalStrut(10));
                JLabel subtitleLabel = new JLabel("Buku adalah jendela dunia.");
                subtitleLabel.setFont(new Font("Segoe UI", Font.ITALIC, 16));
                subtitleLabel.setForeground(new Color(108, 117, 125));
                subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
                berandaPanel.add(subtitleLabel);
                berandaPanel.add(Box.createVerticalStrut(30));
                JPanel cardsPanel = new JPanel();
                cardsPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 20));
                cardsPanel.setBackground(Color.WHITE);
                String[] cardTexts = {
                    "Membaca adalah kunci untuk membuka pintu pengetahuan.",
                    "Setiap buku adalah petualangan baru yang menanti."
                };
                for (String text : cardTexts) {
                    JPanel card = new JPanel();
                    card.setPreferredSize(new Dimension(250, 200));
                    card.setBackground(Color.WHITE);
                    card.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(new Color(200, 200, 200)),
                        BorderFactory.createEmptyBorder(15, 15, 15, 15)
                    ));
                    card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
                    JLabel cardLabel = new JLabel("<html><div style='text-align: center;'>" + text + "</div></html>");
                    cardLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
                    cardLabel.setForeground(new Color(108, 117, 125));
                    cardLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
                    card.add(cardLabel);
                    cardsPanel.add(card);
                }
                berandaPanel.add(cardsPanel);
                contentPanel.add(berandaPanel, BorderLayout.CENTER);
                break;

            case "Katalog Buku":
                CatalogPanel catalogPanel = new CatalogPanel(dataStorage);
                contentPanel.add(catalogPanel, BorderLayout.CENTER);
                break;

            case "Buku":
                BookPanel bookPanel = new BookPanel(dataStorage);
                contentPanel.add(bookPanel, BorderLayout.CENTER);
                break;

            case "Peminjaman":
                BorrowPanel borrowPanel = new BorrowPanel(dataStorage);
                contentPanel.add(borrowPanel, BorderLayout.CENTER);
                break;

            case "Riwayat & Statistik":
                StatsPanel statsPanel = new StatsPanel(dataStorage);
                contentPanel.add(statsPanel, BorderLayout.CENTER);
                break;

            case "Manajemen Denda":
                FinesPanel finesPanel = new FinesPanel(dataStorage);
                contentPanel.add(finesPanel, BorderLayout.CENTER);
                break;

            default:
                JLabel errorLabel = new JLabel("Halaman tidak ditemukan");
                errorLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
                errorLabel.setForeground(new Color(108, 117, 125));
                contentPanel.add(errorLabel, BorderLayout.CENTER);
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