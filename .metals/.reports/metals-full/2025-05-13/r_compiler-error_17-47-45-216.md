file://<WORKSPACE>/src/storage/DataStorage.java
### java.util.NoSuchElementException: next on empty iterator

occurred in the presentation compiler.

presentation compiler configuration:


action parameters:
offset: 1138
uri: file://<WORKSPACE>/src/storage/DataStorage.java
text:
```scala
package storage;

import model.Book;
import java.util.ArrayList;
import java.util.List;

public class DataStorage {
    private List<Book> books;
    private List<model.Transaction> transactions;
    private List<model.User> users;

    public DataStorage() {
        books = new ArrayList<>();
        transactions = new ArrayList<>();
        users = new ArrayList<>();
        initializeSampleData();
    }

    private void initializeSampleData() {
        // Contoh data buku dengan path PDF dari <WORKSPACE>/ebooks
        books.add(new Book("B001", "Algoritma Pemrograman", "John Doe", "Komputer", "<WORKSPACE>/ebooks/algoritma.pdf", true));
        books.add(new Book("B002", "Fisika Dasar", "Jane Smith", "Sains", "", false));
    }

    public List<Book> getBooks() { return books; }
    public List<model.Transaction> getTransactions() { return transactions; }
    public List<model.User> getUsers() { return users; }
    public List<model.Transaction> getFines() {
        return transactions.stream().filter(t -> t.getFine() > 0 && !t.isFinePaid()).collect(Collectors.toList());
    }
}c@@
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
	dotty.tools.pc.completions.CompletionProvider.completions(CompletionProvider.scala:72)
	dotty.tools.pc.ScalaPresentationCompiler.complete$$anonfun$1(ScalaPresentationCompiler.scala:150)
```
#### Short summary: 

java.util.NoSuchElementException: next on empty iterator