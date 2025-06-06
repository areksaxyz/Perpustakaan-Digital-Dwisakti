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
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import java.util.stream.Collectors;

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

        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel titleLabel = new JLabel("Judul:");
        titleLabel.setFont(new Font("Arial", Font.ITALIC, 18));
        JTextField titleField = new JTextField(20);
        titleField.setFont(new Font("Arial", Font.PLAIN, 18));
        JLabel authorLabel = new JLabel("Penulis:");
        authorLabel.setFont(new Font("Arial", Font.ITALIC, 18));
        JTextField authorField = new JTextField(20);
        authorField.setFont(new Font("Arial", Font.PLAIN, 18));
        JLabel yearLabel = new JLabel("Tahun Terbit:");
        yearLabel.setFont(new Font("Arial", Font.ITALIC, 18));
        JTextField yearField = new JTextField(20);
        yearField.setFont(new Font("Arial", Font.PLAIN, 18));
        JLabel subjectLabel = new JLabel("Subjek:");
        subjectLabel.setFont(new Font("Arial", Font.ITALIC, 18));
        JTextField subjectField = new JTextField(20);
        subjectField.setFont(new Font("Arial", Font.PLAIN, 18));
        JLabel typeLabel = new JLabel("Tipe Buku:");
        typeLabel.setFont(new Font("Arial", Font.ITALIC, 18));
        JComboBox<String> typeBox = new JComboBox<>(new String[]{"Digital", "Fisik"});
        typeBox.setFont(new Font("Arial", Font.PLAIN, 18));
        JLabel urlLabel = new JLabel("URL (Jika Digital):");
        urlLabel.setFont(new Font("Arial", Font.ITALIC, 18));
        JTextField urlField = new JTextField(20);
        urlField.setFont(new Font("Arial", Font.PLAIN, 18));

        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(titleLabel, gbc);
        gbc.gridx = 1;
        formPanel.add(titleField, gbc);
        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(authorLabel, gbc);
        gbc.gridx = 1;
        formPanel.add(authorField, gbc);
        gbc.gridx = 0;
        gbc.gridy = 2;
        formPanel.add(yearLabel, gbc);
        gbc.gridx = 1;
        formPanel.add(yearField, gbc);
        gbc.gridx = 0;
        gbc.gridy = 3;
        formPanel.add(subjectLabel, gbc);
        gbc.gridx = 1;
        formPanel.add(subjectField, gbc);
        gbc.gridx = 0;
        gbc.gridy = 4;
        formPanel.add(typeLabel, gbc);
        gbc.gridx = 1;
        formPanel.add(typeBox, gbc);
        gbc.gridx = 0;
        gbc.gridy = 5;
        formPanel.add(urlLabel, gbc);
        gbc.gridx = 1;
        formPanel.add(urlField, gbc);

        JButton addButton = new JButton("Tambah Buku");
        addButton.setFont(new Font("Arial", Font.BOLD, 18));
        addButton.setBackground(new Color(0, 123, 255));
        addButton.setForeground(Color.WHITE);
        addButton.setFocusPainted(false);
        JButton deleteButton = new JButton("Hapus Buku");
        deleteButton.setFont(new Font("Arial", Font.BOLD, 18));
        deleteButton.setBackground(new Color(220, 53, 69));
        deleteButton.setForeground(Color.WHITE);
        deleteButton.setFocusPainted(false);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.add(addButton);
        buttonPanel.add(deleteButton);

        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Arial", Font.BOLD, 20));

        String[] digitalColumns = {"Judul", "Penulis", "Tahun Terbit", "Status", "Aksi", "Edit"};
        digitalTableModel = new DefaultTableModel(digitalColumns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 4 || column == 5;
            }
        };
        digitalTable = new JTable(digitalTableModel);
        digitalTable.setFillsViewportHeight(true);
        digitalTable.setRowHeight(40);
        digitalTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 20));
        digitalTable.getTableHeader().setBackground(new Color(0, 123, 255));
        digitalTable.getTableHeader().setForeground(Color.WHITE);
        digitalTable.setFont(new Font("Arial", Font.PLAIN, 18));
        digitalTable.setGridColor(new Color(200, 200, 200));
        JScrollPane digitalScrollPane = new JScrollPane(digitalTable);
        tabbedPane.addTab("Buku Digital", digitalScrollPane);

        String[] physicalColumns = {"Judul", "Penulis", "Tahun Terbit", "Status", "Edit"};
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
        physicalTable.getTableHeader().setBackground(new Color(0, 123, 255));
        physicalTable.getTableHeader().setForeground(Color.WHITE);
        physicalTable.setFont(new Font("Arial", Font.PLAIN, 18));
        physicalTable.setGridColor(new Color(200, 200, 200));
        JScrollPane physicalScrollPane = new JScrollPane(physicalTable);
        tabbedPane.addTab("Buku Fisik", physicalScrollPane);

        updateTables();

        addButton.addActionListener(e -> {
            String title = titleField.getText();
            String author = authorField.getText();
            String year = yearField.getText();
            String subject = subjectField.getText();
            String type = (String) typeBox.getSelectedItem();
            String url = urlField.getText();
            if (!title.isEmpty() && !author.isEmpty() && !subject.isEmpty()) {
                String id = String.valueOf(dataStorage.getBooks().size() + 1);
                Book newBook = new Book(id, title, author, subject, type.equals("Digital"), type.equals("Digital") ? url : "");
                dataStorage.addBook(newBook);
                if (newBook.isDigital()) {
                    digitalTableModel.addRow(new Object[]{
                        newBook.getTitle(),
                        newBook.getAuthor(),
                        year.isEmpty() ? "2020" : year,
                        newBook.isBorrowed() ? "Dipinjam" : "Tersedia",
                        !newBook.getUrl().isEmpty() ? "Baca" : "",
                        "Edit"
                    });
                } else {
                    physicalTableModel.addRow(new Object[]{
                        newBook.getTitle(),
                        newBook.getAuthor(),
                        year.isEmpty() ? "2020" : year,
                        newBook.isBorrowed() ? "Dipinjam" : "Tersedia",
                        "Edit"
                    });
                }
                titleField.setText("");
                authorField.setText("");
                yearField.setText("");
                subjectField.setText("");
                urlField.setText("");
            } else {
                JOptionPane.showMessageDialog(this, "Judul, Penulis, dan Subjek harus diisi!");
            }
        });

        deleteButton.addActionListener(e -> {
            int selectedTab = tabbedPane.getSelectedIndex();
            if (selectedTab == 0) {
                int selectedRow = digitalTable.getSelectedRow();
                if (selectedRow >= 0) {
                    String bookTitle = (String) digitalTableModel.getValueAt(selectedRow, 0);
                    dataStorage.getBooks().removeIf(book -> book.getTitle().equals(bookTitle));
                    digitalTableModel.removeRow(selectedRow);
                } else {
                    JOptionPane.showMessageDialog(this, "Pilih buku digital yang ingin dihapus!");
                }
            } else {
                int selectedRow = physicalTable.getSelectedRow();
                if (selectedRow >= 0) {
                    String bookTitle = (String) physicalTableModel.getValueAt(selectedRow, 0);
                    dataStorage.getBooks().removeIf(book -> book.getTitle().equals(bookTitle));
                    physicalTableModel.removeRow(selectedRow);
                } else {
                    JOptionPane.showMessageDialog(this, "Pilih buku fisik yang ingin dihapus!");
                }
            }
        });

        add(formPanel, BorderLayout.NORTH);
        add(buttonPanel, BorderLayout.CENTER);
        add(tabbedPane, BorderLayout.SOUTH);

        if (digitalTableModel.getRowCount() > 0) {
            digitalTable.getColumnModel().getColumn(4).setCellRenderer(new ButtonComponents.ButtonRenderer());
            digitalTable.getColumnModel().getColumn(4).setCellEditor(new ButtonComponents.ButtonEditor(new JCheckBox(), dataStorage, digitalTable));
            digitalTable.getColumnModel().getColumn(5).setCellRenderer(new ButtonComponents.EditButtonRenderer());
            digitalTable.getColumnModel().getColumn(5).setCellEditor(new ButtonComponents.EditButtonEditor(new JCheckBox(), dataStorage, digitalTable, digitalTableModel));
        }
        if (physicalTableModel.getRowCount() > 0) {
            physicalTable.getColumnModel().getColumn(4).setCellRenderer(new ButtonComponents.EditButtonRenderer());
            physicalTable.getColumnModel().getColumn(4).setCellEditor(new ButtonComponents.EditButtonEditor(new JCheckBox(), dataStorage, physicalTable, physicalTableModel));
        }
    }

    private void updateTables() {
        digitalTableModel.setRowCount(0);
        physicalTableModel.setRowCount(0);

        for (Book book : dataStorage.getBooks()) {
            if (book.isDigital()) {
                digitalTableModel.addRow(new Object[]{
                    book.getTitle(),
                    book.getAuthor(),
                    "2020",
                    book.isBorrowed() ? "Dipinjam" : "Tersedia",
                    !book.getUrl().isEmpty() ? "Baca" : "",
                    "Edit"
                });
            } else {
                physicalTableModel.addRow(new Object[]{
                    book.getTitle(),
                    book.getAuthor(),
                    "2020",
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