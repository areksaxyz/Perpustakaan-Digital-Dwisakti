package model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter; // Tambahkan import ini jika ingin format string


public class Loan {
    private String loanId;
    private Book book;
    private String borrowerName;
    private String className;
    private String nim;
    private LocalDate loanDate; // Ubah ke LocalDate
    private LocalDate returnDate; // Ubah ke LocalDate
    private boolean isReturned;

    // Constructor untuk membuat peminjaman baru
    public Loan(String loanId, Book book, String borrowerName, String className, String nim) {
        this.loanId = loanId;
        this.book = book;
        this.borrowerName = borrowerName;
        this.className = className;
        this.nim = nim;
        this.loanDate = LocalDate.now();
        this.returnDate = null;
        this.isReturned = false;
    }

    // Constructor untuk memuat peminjaman (misalnya dari database/file)
    public Loan(String loanId, Book book, String borrowerName, String className, String nim, LocalDate loanDate, LocalDate returnDate, boolean isReturned) {
        this.loanId = loanId;
        this.book = book;
        this.borrowerName = borrowerName;
        this.className = className;
        this.nim = nim;
        this.loanDate = loanDate;
        this.returnDate = returnDate;
        this.isReturned = isReturned;
    }

    // Getters
    public String getLoanId() { return loanId; }
    public Book getBook() { return book; }
    public String getBorrowerName() { return borrowerName; }
    public String getClassName() { return className; }
    public String getNim() { return nim; }

    public LocalDate getLoanDate() { return loanDate; } // Sekarang mengembalikan LocalDate
    public LocalDate getReturnDate() { return returnDate; } // Sekarang mengembalikan LocalDate
    public boolean isReturned() { return isReturned; }

    // Setters
    public void setLoanId(String loanId) { this.loanId = loanId; }
    public void setBook(Book book) { this.book = book; }
    public void setBorrowerName(String borrowerName) { this.borrowerName = borrowerName; }
    public void setClassName(String className) { this.className = className; }
    public void setNim(String nim) { this.nim = nim; }

    public void setLoanDate(LocalDate loanDate) { this.loanDate = loanDate; } // Sekarang menerima LocalDate
    public void setReturnDate(LocalDate returnDate) { this.returnDate = returnDate; } // Sekarang menerima LocalDate
    public void setReturned(boolean returned) { this.isReturned = returned; }

    // Metode untuk mendapatkan tanggal dalam format string
    public String getFormattedLoanDate() {
        return loanDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }

    public String getFormattedReturnDate() {
        // Cek jika returnDate tidak null sebelum memformatnya
        return returnDate != null ? returnDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) : "";
    }

    @Override
    public String toString() {
        return "Loan{" +
                "loanId='" + loanId + '\'' +
                ", book=" + (book != null ? book.getTitle() : "null") +
                ", borrowerName='" + borrowerName + '\'' +
                ", className='" + className + '\'' +
                ", nim='" + nim + '\'' +
                ", loanDate=" + loanDate + // LocalDate sudah memiliki toString() yang baik
                ", returnDate=" + returnDate + // LocalDate sudah memiliki toString() yang baik
                ", returned=" + isReturned +
                '}';
    }
}