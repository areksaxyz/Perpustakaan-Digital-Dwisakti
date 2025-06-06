package model;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Loan {
    private String loanId;
    private Book book;
    private String borrowerName;
    private String className;
    private String nim;
    private Date loanDate;
    private Date returnDate;
    private boolean returned;

    public Loan(String loanId, Book book, String borrowerName, String className, String nim) {
        this.loanId = loanId;
        this.book = book;
        this.borrowerName = borrowerName;
        this.className = className;
        this.nim = nim;
        this.loanDate = new Date();
        this.returnDate = null;
        this.returned = false;
    }

    // Konstruktor baru untuk memuat data dari database
    public Loan(String loanId, Book book, String borrowerName, String className, String nim, Date loanDate, Date returnDate, boolean returned) {
        this.loanId = loanId;
        this.book = book;
        this.borrowerName = borrowerName;
        this.className = className;
        this.nim = nim;
        this.loanDate = loanDate;
        this.returnDate = returnDate;
        this.returned = returned;
    }

    public String getLoanId() { return loanId; }
    public Book getBook() { return book; }
    public String getBorrowerName() { return borrowerName; }
    public String getClassName() { return className; }
    public String getNim() { return nim; }
    public Date getLoanDate() { return loanDate; }
    public Date getReturnDate() { return returnDate; }
    public void setReturnDate(Date returnDate) { this.returnDate = returnDate; }
    public boolean isReturned() { return returned; }
    public void setReturned(boolean returned) { this.returned = returned; }

    public String getFormattedLoanDate() {
        return new SimpleDateFormat("yyyy-MM-dd").format(loanDate);
    }

    public String getFormattedReturnDate() {
        return returnDate != null ? new SimpleDateFormat("yyyy-MM-dd").format(returnDate) : "";
    }

    @Override
    public String toString() {
        return "Loan{" +
                "loanId='" + loanId + '\'' +
                ", book=" + (book != null ? book.getTitle() : "null") +
                ", borrowerName='" + borrowerName + '\'' +
                ", className='" + className + '\'' +
                ", nim='" + nim + '\'' +
                ", loanDate=" + getFormattedLoanDate() +
                ", returnDate=" + getFormattedReturnDate() +
                ", returned=" + returned +
                '}';
    }
}