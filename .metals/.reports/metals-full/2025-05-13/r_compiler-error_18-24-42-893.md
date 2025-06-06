file://<WORKSPACE>/src/model/Transaction.java
### java.util.NoSuchElementException: next on empty iterator

occurred in the presentation compiler.

presentation compiler configuration:


action parameters:
uri: file://<WORKSPACE>/src/model/Transaction.java
text:
```scala
package model;

import java.util.Date;

public class Transaction {
    private String bookId;
    private String userId;
    private Date borrowDate;
    private Date dueDate;
    private Date returnDate;
    private long fine;
    private boolean finePaid;

    public Transaction(String bookId, String userId, Date borrowDate, Date dueDate) {
        this.bookId = bookId;
        this.userId = userId;
        this.borrowDate = borrowDate;
        this.dueDate = dueDate;
        this.fine = calculateFine();
        this.finePaid = false;
    }

    public String getBookId() { return bookId; }
    public String getUserId() { return userId; }
    public Date getBorrowDate() { return borrowDate; }
    public Date getDueDate() { return dueDate; }
    public Date getReturnDate() { return returnDate; }
    public long getFine() { return fine; }
    public boolean isFinePaid() { return finePaid; }

    public void setReturnDate(Date returnDate) {
        this.returnDate = returnDate;
        this.fine = calculateFine();
    }

    public void setFinePaid(boolean finePaid) { this.finePaid = finePaid; }

    private long calculateFine() {
        if (returnDate == null) {
            Date now = new Date();
            if (now.after(dueDate)) {
                long diff = now.getTime() - dueDate.getTime();
                long daysLate = diff / (1000 * 60 * 60 * 24);
                return daysLate * 1000; // Denda Rp 1000 per hari
            }
        }
        return 0;
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