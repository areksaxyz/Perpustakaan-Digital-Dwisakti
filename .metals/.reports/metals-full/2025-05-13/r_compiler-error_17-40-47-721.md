file://<WORKSPACE>/src/ui/ButtonComponents.java
### java.util.NoSuchElementException: next on empty iterator

occurred in the presentation compiler.

presentation compiler configuration:


action parameters:
uri: file://<WORKSPACE>/src/ui/ButtonComponents.java
text:
```scala
package ui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import java.awt.*;

public class ButtonComponents {
    public static class ButtonRenderer extends JLabel implements TableCellRenderer {
        public ButtonRenderer() {
            setOpaque(true);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            if (value instanceof String) {
                JButton button = new JButton((String) value);
                button.setFont(new Font("Arial", Font.BOLD, 18));
                if (((String) value).equals("Baca")) {
                    button.setBackground(new Color(0, 123, 255));
                } else if (((String) value).equals("Edit")) {
                    button.setBackground(new Color(108, 117, 125));
                }
                button.setForeground(Color.WHITE);
                button.setFocusPainted(false);
                return button;
            }
            setText(value != null ? value.toString() : "");
            setFont(new Font("Arial", Font.PLAIN, 18));
            return this;
        }
    }

    public static class ButtonEditor extends DefaultCellEditor {
        private String label;
        private JButton button;
        private boolean isPushed;
        private JTable table;

        public ButtonEditor(JCheckBox checkBox, JTable table) {
            super(checkBox);
            this.table = table;
            button = new JButton();
            button.setOpaque(true);
            button.addActionListener(e -> fireEditingStopped());
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            if (value instanceof String) {
                label = (String) value;
                button.setText(label);
                button.setFont(new Font("Arial", Font.BOLD, 18));
                if (label.equals("Baca")) {
                    button.setBackground(new Color(0, 123, 255));
                } else if (label.equals("Edit")) {
                    button.setBackground(new Color(108, 117, 125));
                }
                button.setForeground(Color.WHITE);
                button.setFocusPainted(false);
                isPushed = true;
                return button;
            }
            return null;
        }

        @Override
        public Object getCellEditorValue() {
            if (isPushed) {
                // Tambahkan logika untuk "Baca" atau "Edit" jika diperlukan
                JOptionPane.showMessageDialog(null, "Tombol " + label + " diklik pada baris " + table.getEditingRow());
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