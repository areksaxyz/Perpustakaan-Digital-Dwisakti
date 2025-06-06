file://<WORKSPACE>/src/storage/DataStorage.java
### java.util.NoSuchElementException: next on empty iterator

occurred in the presentation compiler.

presentation compiler configuration:


action parameters:
uri: file://<WORKSPACE>/src/storage/DataStorage.java
text:
```scala
package storage;

import model.Book;
import model.Transaction;
import model.User;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class DataStorage {
    private List<Book> books;
    private List<Transaction> transactions;
    private List<User> users;

    public DataStorage() {
        books = new ArrayList<>();
        transactions = new ArrayList<>();
        users = new ArrayList<>();
        initializeSampleData();
    }

    private void initializeSampleData() {
        books.add(new Book("B001", "Algoritma Pemrograman", "John Doe", "Komputer", "<WORKSPACE>/ebooks/algoritma.pdf", true));
        books.add(new Book("B002", "Fisika Dasar", "Jane Smith", "Sains", "", false));
        users.add(new User("U001", "Alice", "alice@example.com"));
        users.add(new User("U002", "Bob", "bob@example.com"));
    }

    public List<Book> getBooks() { return books; }
    public List<Transaction> getTransactions() { return transactions; }
    public List<User> getUsers() { return users; }
    public List<Transaction> getFines() {
        return transactions.stream().filter(t -> t.getFine() > 0 && !t.isFinePaid()).collect(Collectors.toList());
    }

    public void addTransaction(Transaction transaction) {
        transactions.add(transaction);
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