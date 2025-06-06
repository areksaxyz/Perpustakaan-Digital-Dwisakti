file://<WORKSPACE>/src/storage/DataStorage.java
### java.util.NoSuchElementException: next on empty iterator

occurred in the presentation compiler.

presentation compiler configuration:


action parameters:
offset: 794
uri: file://<WORKSPACE>/src/storage/DataStorage.java
text:
```scala
package storage;

import model.Book;
import model.Transaction;
import model.User;

import java.util.ArrayList;
import java.util.List;

public class DataStorage {
    private List<Book> books = new ArrayList<>();
    private List<User> users = new ArrayList<>();
    private List<Transaction> transactions = new ArrayList<>();
    private List<Transaction> fines = new ArrayList<>();

    public DataStorage() {
        // Inisialisasi data awal (opsional)
        books.add(new Book("1", "Java Programming", "John Doe", "Programming", true, "file.pdf"));
        users.add(new User("1", "Alice"));
        transactions.add(new Transaction("1", "1", new Date(), new Date()));
    }

    public List<Book> getBooks() { return books; }
    public List<User> getUsers() { return users; }
    public@@ List<Transaction> getTransactions() { return transactions; }
    public List<Transaction> getFines() { return fines; }

    public void addBook(Book book) { books.add(book); }
    public void addUser(User user) { users.add(user); }
    public void addTransaction(Transaction transaction) { transactions.add(transaction); }
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