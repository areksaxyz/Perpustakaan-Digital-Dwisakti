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
    private JPanel contentPanel; // Panel untuk menampilkan konten

    public LibraryUI() {
        super("Sistem Perpustakaan");
        dataStorage = new DataStorage();
        initializeUI();
    }

    private void initializeUI() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 600);
        setLocationRelativeTo(null);

        // Layout utama menggunakan BorderLayout
        setLayout(new BorderLayout());

        // Navigasi bar di sisi kiri
        JPanel navPanel = new JPanel();
        navPanel.setLayout(new BoxLayout(navPanel, BoxLayout.Y_AXIS));
        navPanel.setBackground(new Color(0, 123, 255));
        navPanel.setPreferredSize(new Dimension(200, getHeight()));
        navPanel.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));

        // Tombol navigasi
        JButton bookButton = createNavButton("Daftar Buku");
        JButton catalogButton = createNavButton("Katalog Buku");
        JButton borrowButton = createNavButton("Peminjaman");
        JButton finesButton = createNavButton("Manajemen Denda");

        // Tambahkan tombol ke navPanel
        navPanel.add(Box.createVerticalStrut(20));
        navPanel.add(bookButton);
        navPanel.add(Box.createVerticalStrut(10));
        navPanel.add(catalogButton);
        navPanel.add(Box.createVerticalStrut(10));
        navPanel.add(borrowButton);
        navPanel.add(Box.createVerticalStrut(10));
        navPanel.add(finesButton);

        // Panel konten di sisi kanan
        contentPanel = new JPanel();
        contentPanel.setLayout(new BorderLayout());
        contentPanel.setBackground(new Color(248, 249, 250));

        // Tambahkan panel ke frame
        add(navPanel, BorderLayout.WEST);
        add(contentPanel, BorderLayout.CENTER);

        // Action listener untuk tombol navigasi
        bookButton.addActionListener(e -> showPanel(new BookPanel(dataStorage)));
        catalogButton.addActionListener(e -> showPanel(new CatalogPanel(dataStorage)));
        borrowButton.addActionListener(e -> showPanel(new BorrowPanel(dataStorage)));
        finesButton.addActionListener(e -> showPanel(new FinesPanel(dataStorage)));

        // Tampilkan panel default (Daftar Buku) saat aplikasi dibuka
        showPanel(new BookPanel(dataStorage));
    }

    private JButton createNavButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 16));
        button.setForeground(Color.WHITE);
        button.setBackground(new Color(0, 123, 255));
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(30, 144, 255));
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(0, 123, 255));
            }
        });
        return button;
    }

    private void showPanel(JPanel panel) {
        contentPanel.removeAll();
        contentPanel.add(panel, BorderLayout.CENTER);
        contentPanel.revalidate();
        contentPanel.repaint();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            LibraryUI ui = new LibraryUI();
            ui.setVisible(true);
        });
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