file://<WORKSPACE>/src/ui/LibraryUI.java
### java.util.NoSuchElementException: next on empty iterator

occurred in the presentation compiler.

presentation compiler configuration:


action parameters:
offset: 898
uri: file://<WORKSPACE>/src/ui/LibraryUI.java
text:
```scala
package model;

import java.util.Date;

public class Book {
    private String id;
    private String title;
    private String author;
    private String subject;
    private String url;
    private boolean isBorrowed;
    private boolean isDigital;
    private Date borrowDate;
    private Date dueDate;

    public Book(String id, String title, String author, String subject, String url, boolean isDigital) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.subject = subject;
        this.url = url;
        this.isDigital = isDigital;
        this.isBorrowed = false;
    }

    public String getId() { return id; }
    public String getTitle() { return title; }
    public String getAuthor() { return author; }
    public String getSubject() { return subject; }
    public String getUrl() { return url; }
    public boolean isBorrowed() { return is@@Borrowed; }
    public boolean isDigital() { return isDigital; }
    public Date getBorrowDate() { return borrowDate; }
    public Date getDueDate() { return dueDate; }

    public void setTitle(String title) { this.title = title; }
    public void setAuthor(String author) { this.author = author; }
    public void setSubject(String subject) { this.subject = subject; }
    public void setUrl(String url) { this.url = url; }
    public void setBorrowed(boolean borrowed) { this.isBorrowed = borrowed; }
    public void setBorrowDate(Date borrowDate) { this.borrowDate = borrowDate; }
    public void setDueDate(Date dueDate) { this.dueDate = dueDate; }
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