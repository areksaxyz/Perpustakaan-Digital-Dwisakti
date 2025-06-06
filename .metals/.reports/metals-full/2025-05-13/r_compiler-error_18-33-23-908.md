file://<WORKSPACE>/src/ui/BookPanel.java
### java.util.NoSuchElementException: next on empty iterator

occurred in the presentation compiler.

presentation compiler configuration:


action parameters:
uri: file://<WORKSPACE>/src/ui/BookPanel.java
text:
```scala
package ui;

import model.Book;
import storage.DataStorage;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class BookPanel extends JPanel {
    private DataStorage dataStorage;
    private DefaultTableModel digitalTableModel;
    private DefaultTableModel physicalTableModel;
    private JTable digitalTable;
    private JTable physicalTable;

    public BookPanel(DataStorage dataStorage) {
        this.dataStorage = dataStorage;
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));
        setBackground(new Color(248, 249, 250));

        JLabel titleLabel = new JLabel("Daftar Buku", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        add(titleLabel, BorderLayout.NORTH);

        // Panel untuk dua tabel
        JPanel tablePanel = new JPanel(new GridLayout(1, 2, 20, 0));
        tablePanel.setBackground(new Color(248, 249, 250));

        // Tabel Buku Digital
        String[] digitalColumns = {"Judul", "Penulis", "Subjek", "Status", "Aksi"};
        digitalTableModel = new DefaultTableModel(digitalColumns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 4;
            }
        };
        digitalTable = new JTable(digitalTableModel);
        digitalTable.setFillsViewportHeight(true);
        digitalTable.setRowHeight(40);
        digitalTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 20));
        digitalTable.getTableHeader().setBackground(new Color(52, 58, 64));
        digitalTable.getTableHeader().setForeground(Color.WHITE);
        digitalTable.setFont(new Font("Arial", Font.PLAIN, 18));
        digitalTable.setGridColor(new Color(200, 200, 200));
        JScrollPane digitalScrollPane = new JScrollPane(digitalTable);
        digitalScrollPane.setBorder(BorderFactory.createTitledBorder("Buku Digital"));
        tablePanel.add(digitalScrollPane);

        // Tabel Buku Fisik
        String[] physicalColumns = {"Judul", "Penulis", "Subjek", "Status", "Aksi"};
        physicalTableModel = new DefaultTableModel(physicalColumns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 4;
            }
        };
        physicalTable = new JTable(physicalTableModel);
        physicalTable.setFillsViewportHeight(true);
        physicalTable.setRowHeight(40);
        physicalTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 20));
        physicalTable.getTableHeader().setBackground(new Color(52, 58, 64));
        physicalTable.getTableHeader().setForeground(Color.WHITE);
        physicalTable.setFont(new Font("Arial", Font.PLAIN, 18));
        physicalTable.setGridColor(new Color(200, 200, 200));
        JScrollPane physicalScrollPane = new JScrollPane(physicalTable);
        physicalScrollPane.setBorder(BorderFactory.createTitledBorder("Buku Fisik"));
        tablePanel.add(physicalScrollPane);

        add(tablePanel, BorderLayout.CENTER);

        updateTables();

        // Set renderer dan editor untuk kedua tabel
        digitalTable.getColumnModel().getColumn(4).setCellRenderer(new ButtonComponents.ButtonRenderer());
        digitalTable.getColumnModel().getColumn(4).setCellEditor(new ButtonComponents.ButtonEditor(new JCheckBox(), dataStorage, digitalTable) {
            @Override
            public Object getCellEditorValue() {
                if (isPushed && dataStorage != null && label.equals("Baca")) {
                    int row = digitalTable.getEditingRow();
                    if (row >= 0) {
                        String bookTitle = (String) digitalTable.getValueAt(row, 0);
                        Book book = dataStorage.getBooks().stream()
                                .filter(b -> b.getTitle().equals(bookTitle))
                                .findFirst()
                                .orElse(null);
                        if (book != null && book.isDigital()) {
                            try {
                                File pdfFile = new File(book.getUrl());
                                if (pdfFile.exists()) {
                                    PDDocument document = PDDocument.load(pdfFile);
                                    PDFRenderer pdfRenderer = new PDFRenderer(document);
                                    BufferedImage image = pdfRenderer.renderImage(0); // Render halaman pertama
                                    ImageIcon icon = new ImageIcon(image.getScaledInstance(600, 800, Image.SCALE_SMOOTH));
                                    JLabel pdfLabel = new JLabel(icon);
                                    JInternalFrame pdfFrame = new JInternalFrame("PDF: " + book.getTitle(), true, true, true, true);
                                    pdfFrame.setSize(620, 820);
                                    pdfFrame.add(pdfLabel);
                                    pdfFrame.setVisible(true);
                                    desktopPane.add(pdfFrame);
                                    pdfFrame.setLocation(50, 50);
                                    document.close();
                                } else {
                                    JOptionPane.showMessageDialog(null, "File PDF tidak ditemukan: " + book.getUrl());
                                }
                            } catch (IOException e) {
                                JOptionPane.showMessageDialog(null, "Gagal membuka PDF: " + e.getMessage());
                            }
                        }
                    }
                }
                isPushed = false;
                return label;
            }
        });
        physicalTable.getColumnModel().getColumn(4).setCellRenderer(new ButtonComponents.ButtonRenderer());
        physicalTable.getColumnModel().getColumn(4).setCellEditor(new ButtonComponents.EditButtonEditor(new JCheckBox(), dataStorage, physicalTable, physicalTableModel));
    }

    private JDesktopPane desktopPane = new JDesktopPane();

    private void updateTables() {
        digitalTableModel.setRowCount(0);
        physicalTableModel.setRowCount(0);

        for (Book book : dataStorage.getBooks()) {
            if (book.isDigital()) {
                digitalTableModel.addRow(new Object[]{
                    book.getTitle(),
                    book.getAuthor(),
                    book.getSubject(),
                    book.isBorrowed() ? "Dipinjam" : "Tersedia",
                    "Baca"
                });
            } else {
                physicalTableModel.addRow(new Object[]{
                    book.getTitle(),
                    book.getAuthor(),
                    book.getSubject(),
                    book.isBorrowed() ? "Dipinjam" : "Tersedia",
                    "Edit"
                });
            }
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