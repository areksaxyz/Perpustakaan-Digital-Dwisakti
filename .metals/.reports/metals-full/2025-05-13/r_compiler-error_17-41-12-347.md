file://<WORKSPACE>/src/ui/FinesPanel.java
### java.util.NoSuchElementException: next on empty iterator

occurred in the presentation compiler.

presentation compiler configuration:


action parameters:
uri: file://<WORKSPACE>/src/ui/FinesPanel.java
text:
```scala
package ui;

import model.Book;
import model.Transaction;
import model.User;
import storage.DataStorage;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import java.awt.*;

public class FinesPanel extends JPanel {
    private DataStorage dataStorage;
    private DefaultTableModel tableModel;
    private JTable table;

    public FinesPanel(DataStorage dataStorage) {
        this.dataStorage = dataStorage;
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));
        setBackground(new Color(248, 249, 250));

        JLabel titleLabel = new JLabel("Manajemen Denda", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(new Color(51, 51, 51));
        add(titleLabel, BorderLayout.NORTH);

        String[] columnNames = {"ID Transaksi", "Nama Peminjam", "Judul Buku", "Jumlah Denda", "Status", "Aksi"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 5;
            }
        };
        table = new JTable(tableModel);
        table.setFillsViewportHeight(true);
        table.setRowHeight(40);
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 20));
        table.getTableHeader().setBackground(new Color(0, 123, 255));
        table.getTableHeader().setForeground(Color.WHITE);
        table.setFont(new Font("Arial", Font.PLAIN, 18));
        table.setGridColor(new Color(200, 200, 200));
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        updateTable();

        table.getColumnModel().getColumn(5).setCellRenderer(new FineActionRenderer());
        table.getColumnModel().getColumn(5).setCellEditor(new FineActionEditor(new JCheckBox(), dataStorage, table, tableModel));
    }

    private void updateTable() {
        tableModel.setRowCount(0);
        for (Transaction transaction : dataStorage.getFines()) {
            String userName = dataStorage.getUsers().stream()
                    .filter(u -> u.getId().equals(transaction.getUserId()))
                    .findFirst()
                    .map(User::getName)
                    .orElse("Unknown");
            String bookTitle = dataStorage.getBooks().stream()
                    .filter(b -> b.getId().equals(transaction.getBookId()))
                    .findFirst()
                    .map(Book::getTitle)
                    .orElse("Unknown");
            tableModel.addRow(new Object[]{
                "FINE" + transaction.getBookId(),
                userName,
                bookTitle,
                "Rp " + transaction.getFine(),
                transaction.isFinePaid() ? "Sudah Dibayar" : "Belum Dibayar",
                transaction.isFinePaid() ? "Hapus" : "Bayar"
            });
        }
    }
}

class FineActionRenderer extends JLabel implements TableCellRenderer {
    public FineActionRenderer() {
        setOpaque(true);
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        String action = (String) value;
        JButton button = new JButton(action);
        button.setFont(new Font("Arial", Font.BOLD, 18));
        if (action.equals("Bayar")) {
            button.setBackground(new Color(0, 123, 255));
        } else {
            button.setBackground(new Color(220, 53, 69));
        }
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        return button;
    }
}

class FineActionEditor extends DefaultCellEditor {
    private String label;
    private JButton button;
    private boolean isPushed;
    private DataStorage dataStorage;
    private JTable table;
    private DefaultTableModel tableModel;

    public FineActionEditor(JCheckBox checkBox, DataStorage dataStorage, JTable table, DefaultTableModel tableModel) {
        super(checkBox);
        this.dataStorage = dataStorage;
        this.table = table;
        this.tableModel = tableModel;
        button = new JButton();
        button.setOpaque(true);
        button.addActionListener(e -> fireEditingStopped());
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        label = (String) value;
        button.setText(label);
        button.setFont(new Font("Arial", Font.BOLD, 18));
        if (label.equals("Bayar")) {
            button.setBackground(new Color(0, 123, 255));
        } else {
            button.setBackground(new Color(220, 53, 69));
        }
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        isPushed = true;
        return button;
    }

    @Override
    public Object getCellEditorValue() {
        if (isPushed) {
            int row = this.table.getEditingRow();
            if (row >= 0) {
                Transaction transaction = dataStorage.getFines().get(row);
                if (label.equals("Bayar")) {
                    transaction.setFinePaid(true);
                    tableModel.setValueAt("Sudah Dibayar", row, 4);
                    tableModel.setValueAt("Hapus", row, 5);
                    JOptionPane.showMessageDialog(null, "Denda berhasil dibayar.");
                } else if (label.equals("Hapus")) {
                    dataStorage.getFines().remove(row);
                    tableModel.removeRow(row);
                    JOptionPane.showMessageDialog(null, "Denda berhasil dihapus.");
                }
            }
        }
        isPushed = false;
        return label;
    }

    @Override
    public boolean stopCellEditing() {
        isPushed = false;
        return super.stopCellEditing();
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