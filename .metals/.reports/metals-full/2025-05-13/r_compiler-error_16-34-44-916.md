file://<WORKSPACE>/src/ui/LibraryUI.java
### java.util.NoSuchElementException: next on empty iterator

occurred in the presentation compiler.

presentation compiler configuration:


action parameters:
offset: 5329
uri: file://<WORKSPACE>/src/ui/LibraryUI.java
text:
```scala
package ui;

import com.formdev.flatlaf.FlatLightLaf;
import storage.DataStorage;

import javax.swing.*;
import java.awt.*;

public class LibraryUI extends JFrame {
    private DataStorage dataStorage;
    private JPanel currentPanel;

    public LibraryUI() {
        dataStorage = new DataStorage();
        setupLookAndFeel();
        initializeUI();
    }

    private void setupLookAndFeel() {
        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initializeUI() {
        setTitle("Sistem Perpustakaan");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1280, 720);
        setLocationRelativeTo(null);

        setLayout(new BorderLayout());

        JPanel sidebarPanel = new JPanel();
        sidebarPanel.setLayout(new BoxLayout(sidebarPanel, BoxLayout.Y_AXIS));
        sidebarPanel.setBackground(new Color(33, 37, 41));
        sidebarPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel titleLabel = new JLabel("📚 Perpustakaan");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        sidebarPanel.add(titleLabel);
        sidebarPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        JButton homeButton = createSidebarButton("🏠 Beranda");
        JButton catalogButton = createSidebarButton("📖 Katalog Buku");
        JButton bookButton = createSidebarButton("📚 Buku");
        JButton borrowButton = createSidebarButton("📅 Peminjaman");
        JButton statsButton = createSidebarButton("📊 Riwayat & Statistik");
        JButton finesButton = createSidebarButton("💸 Manajemen Denda");

        sidebarPanel.add(homeButton);
        sidebarPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        sidebarPanel.add(catalogButton);
        sidebarPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        sidebarPanel.add(bookButton);
        sidebarPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        sidebarPanel.add(borrowButton);
        sidebarPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        sidebarPanel.add(statsButton);
        sidebarPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        sidebarPanel.add(finesButton);

        homeButton.addActionListener(e -> switchPanel("Beranda"));
        catalogButton.addActionListener(e -> switchPanel("Katalog Buku"));
        bookButton.addActionListener(e -> switchPanel("Buku"));
        borrowButton.addActionListener(e -> switchPanel("Peminjaman"));
        statsButton.addActionListener(e -> switchPanel("Riwayat & Statistik"));
        finesButton.addActionListener(e -> switchPanel("Manajemen Denda"));

        add(sidebarPanel, BorderLayout.WEST);

        switchPanel("Beranda");
    }

    private JButton createSidebarButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.PLAIN, 18));
        button.setForeground(Color.WHITE);
        button.setBackground(new Color(52, 58, 64));
        button.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setMaximumSize(new Dimension(Integer.MAX_VALUE, button.getMinimumSize().height));
        button.setFocusPainted(false);
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(73, 80, 87));
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(52, 58, 64));
            }
        });
        return button;
    }

    private void switchPanel(String panelName) {
        if (currentPanel != null) {
            remove(currentPanel);
        }

        switch (panelName) {
            case "Beranda":
                currentPanel = new JPanel();
                currentPanel.setLayout(new BorderLayout());
                JLabel welcomeLabel = new JLabel("Selamat Datang di Sistem Perpustakaan!", SwingConstants.CENTER);
                welcomeLabel.setFont(new Font("Arial", Font.BOLD, 30));
                currentPanel.add(welcomeLabel, BorderLayout.CENTER);
                break;
            case "Katalog Buku":
                currentPanel = new CatalogPanel(dataStorage);
                break;
            case "Buku":
                currentPanel = new BookPanel(dataStorage);
                break;
            case "Peminjaman":
                currentPanel = new BorrowPanel(dataStorage);
                break;
            case "Riwayat & Statistik":
                currentPanel = new StatsPanel(dataStorage);
                break;
            case "Manajemen Denda":
                currentPanel = new FinesPanel(dataStorage);
                break;
            default:
                currentPanel = new JPanel();
                currentPanel.add(new JLabel("Halaman tidak ditemukan"));
                break;
        }

        add(currentPanel, BorderLayout.CENTER);
        revalidate();
        repaint();
    }

    public static void main(String[] args) {
        SwingUtilities.invok@@eLater(() -> new LibraryUI().setVisible(true));
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
	dotty.tools.pc.HoverProvider$.hover(HoverProvider.scala:40)
	dotty.tools.pc.ScalaPresentationCompiler.hover$$anonfun$1(ScalaPresentationCompiler.scala:389)
```
#### Short summary: 

java.util.NoSuchElementException: next on empty iterator