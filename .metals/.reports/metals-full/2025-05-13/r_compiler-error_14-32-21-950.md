file://<WORKSPACE>/src/ui/CatalogPanel.java
### java.util.NoSuchElementException: next on empty iterator

occurred in the presentation compiler.

presentation compiler configuration:


action parameters:
uri: file://<WORKSPACE>/src/ui/CatalogPanel.java
text:
```scala
package ui;

import model.Book;
import storage.DataStorage;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.stream.Collectors;

public class CatalogPanel extends JPanel {
    private DataStorage dataStorage;

    public CatalogPanel(DataStorage dataStorage) {
        this.dataStorage = dataStorage;
        setLayout(new BorderLayout());

        // Search bar
        JPanel searchPanel = new JPanel();
        JTextField searchField = new JTextField(20);
        JComboBox<String> filterBox = new JComboBox<>(new String[]{"Judul", "Penulis", "Subjek"});
        JButton searchButton = new JButton("Cari");
        searchPanel.add(searchField);
        searchPanel.add(filterBox);
        searchPanel.add(searchButton);

        // Book list
        DefaultListModel<String> bookListModel = new DefaultListModel<>();
        JList<String> bookList = new JList<>(bookListModel);
        for (Book book : dataStorage.getBooks()) {
            bookListModel.addElement(book.toString() + (book.isDigital() ? " [Digital]" : " [Fisik]"));
        }

        searchButton.addActionListener(e -> {
            String query = searchField.getText().toLowerCase();
            String filter = (String) filterBox.getSelectedItem();
            bookListModel.clear();
            List<Book> filteredBooks = dataStorage.getBooks().stream()
                    .filter(book -> {
                        if (filter.equals("Judul")) return book.getTitle().toLowerCase().contains(query);
                        if (filter.equals("Penulis")) return book.getAuthor().toLowerCase().contains(query);
                        return book.getSubject().toLowerCase().contains(query);
                    })
                    .collect(Collectors.toList());
            for (Book book : filteredBooks) {
                bookListModel.addElement(book.toString() + (book.isDigital() ? " [Digital]" : " [Fisik]"));
            }
        });

        add(searchPanel, BorderLayout.NORTH);
        add(new JScrollPane(bookList), BorderLayout.CENTER);
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