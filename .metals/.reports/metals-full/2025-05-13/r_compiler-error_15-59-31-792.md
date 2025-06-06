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

        // Buku Digital (dengan link PDF)
        books.add(new Book("1", "Algoritma dan Pemrograman", "Penulis Algoritma", "Pemrograman", true, "https://digilib.stekom.ac.id/assets/dokumen/ebook/feb_3671f11f1337ceebc687b6e6a0fa3f7fa796347f_1642059468.pdf"));
        books.add(new Book("2", "Pemrograman Python", "Penulis Python", "Pemrograman", true, "https://repository.unikom.ac.id/65984/1/E-Book_Belajar_Pemrograman_Python_Dasar.pdf"));
        books.add(new Book("3", "Pemrograman Java", "Penulis Java", "Pemrograman", true, "https://digilib.stekom.ac.id/assets/dokumen/ebook/feb_BMuBPtvpXwUkhZqdyUPA7LyV7948c7ZdhjGj8z2EkAjSpNgD_njQSpM_1656322622.pdf"));
        // Buku Fisik (tanpa link)
        books.add(new Book("4", "Matematika Diskrit", "Penulis Matematika", "Matematika", false, ""));
        books.add(new Book("5", "Fisika", "Penulis Fisika", "Sains", false, ""));
        books.add(new Book("6", "Bahasa Inggris", "Penulis Bahasa", "Bahasa", false, ""));

        // Pengguna
        users.add(new User("U1", "Arga"));
        users.add(new User("U2", "Bob"));

        // Transaksi contoh (opsional)
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