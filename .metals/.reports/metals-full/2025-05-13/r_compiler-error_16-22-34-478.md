file://<WORKSPACE>/src/storage/DataStorage.java
### java.util.NoSuchElementException: next on empty iterator

occurred in the presentation compiler.

presentation compiler configuration:


action parameters:
offset: 840
uri: file://<WORKSPACE>/src/storage/DataStorage.java
text:
```scala
package storage;

import model.Book;
import model.Transaction;
import model.User;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DataStorage {
    private List<Book> books;
    private List<User> users;
    private List<Transaction> transactions;
    private List<Transaction> fines;

    public DataStorage() {
        books = new ArrayList<>();
        users = new ArrayList<>();
        transactions = new ArrayList<>();
        fines = new ArrayList<>();

        // Buku Digital (ganti URL dengan path file lokal)
        books.add(new Book("1", "Algoritma dan Pemrograman", "Penulis Algoritma", "Pemrograman", true, "<WORKSPACE>/algoritma.pdf"));
        books.add(new Book("2", "Pemrograman Python", "Penulis Python", "Pemrograman", true, "<WORKSPACE>/p@@ython.pdf"));
        books.add(new Book("3", "Pemrograman Java", "Penulis Java", "Pemrograman", true, "<WORKSPACE>/java.pdf"));
        // Buku Fisik (tanpa link)
        books.add(new Book("4", "Matematika Diskrit", "Penulis Matematika", "Matematika", false, ""));
        books.add(new Book("5", "Fisika", "Penulis Fisika", "Sains", false, ""));
        books.add(new Book("6", "Bahasa Inggris", "Penulis Bahasa", "Bahasa", false, ""));

        // Pengguna
        users.add(new User("U1", "Arga"));
        users.add(new User("U2", "Bob"));

        // Transaksi contoh
        transactions.add(new Transaction("1", "U1", new Date(), new Date()));
    }

    public List<Book> getBooks() { return books; }
    public List<User> getUsers() { return users; }
    public List<Transaction> getTransactions() { return transactions; }
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