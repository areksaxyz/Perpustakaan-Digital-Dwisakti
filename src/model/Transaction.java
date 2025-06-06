package model;

import java.util.Date;

public class Transaction {
    private String id;
    private String borrower;
    private String kelas;
    private String nim;
    private Book book;
    private Date borrowDate;
    private Date returnDate;
    private long fine;

    public Transaction(String borrower, String kelas, String nim, Book book, Date borrowDate, Date returnDate) {
        this.id = "TRX-" + System.currentTimeMillis();
        this.borrower = borrower;
        this.kelas = kelas;
        this.nim = nim;
        this.book = book;
        this.borrowDate = borrowDate;
        this.returnDate = returnDate;
        this.fine = 0;
    }

    public String getId() {
        return id;
    }

    public String getBorrower() {
        return borrower;
    }

    public String getKelas() {
        return kelas;
    }

    public String getNim() {
        return nim;
    }

    public Book getBook() {
        return book;
    }

    public Date getBorrowDate() {
        return borrowDate;
    }

    public Date getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(Date returnDate) {
        this.returnDate = returnDate;
    }

    public long getFine() {
        return fine;
    }

    public void setFine(long fine) {
        this.fine = fine;
    }
}