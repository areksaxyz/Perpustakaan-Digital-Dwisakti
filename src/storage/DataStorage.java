package storage;

import model.Book;
import model.ReadHistory;
import model.Loan;
import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DataStorage {
    private List<Book> books;
    private List<ReadHistory> readHistory;
    private List<Loan> loans;
    private Connection conn;

    public DataStorage() {
        books = new ArrayList<>();
        readHistory = new ArrayList<>();
        loans = new ArrayList<>();
        initializeDatabase();
        loadBooksFromDatabase();
        // loadReadHistoryFromDatabase(); // Dinonaktifkan sementara
        loadLoansFromDatabase();
    }

    private void initializeDatabase() {
        try {
            conn = DriverManager.getConnection("jdbc:sqlite:library.db");
            Statement stmt = conn.createStatement();

            // Tabel books
            stmt.execute("CREATE TABLE IF NOT EXISTS books (" +
                    "id TEXT PRIMARY KEY, " +
                    "title TEXT, " +
                    "author TEXT, " +
                    "publication_year TEXT, " +
                    "type TEXT, " +
                    "url TEXT, " +
                    "subject TEXT, " +
                    "is_borrowed BOOLEAN)");

            // Tabel read_history (dinonaktifkan sementara)
            /*
            stmt.execute("CREATE TABLE IF NOT EXISTS read_history (" +
                    "id TEXT PRIMARY KEY, " +
                    "book_id TEXT, " +
                    "FOREIGN KEY(book_id) REFERENCES books(id))");
            */

            // Tabel loans
            stmt.execute("CREATE TABLE IF NOT EXISTS loans (" +
                    "loan_id TEXT PRIMARY KEY, " +
                    "book_id TEXT, " +
                    "borrower_name TEXT, " +
                    "class_name TEXT, " +
                    "nim TEXT, " +
                    "loan_date INTEGER, " +
                    "return_date INTEGER, " +
                    "is_returned BOOLEAN, " +
                    "FOREIGN KEY(book_id) REFERENCES books(id))");

            // Tabel fines
            stmt.execute("CREATE TABLE IF NOT EXISTS fines (" +
                    "loan_id TEXT PRIMARY KEY, " +
                    "book_title TEXT NOT NULL, " +
                    "book_type TEXT NOT NULL, " +
                    "borrower_name TEXT NOT NULL, " +
                    "loan_date INTEGER NOT NULL, " +
                    "days_late INTEGER NOT NULL, " +
                    "fine_amount INTEGER NOT NULL)");

            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void loadBooksFromDatabase() {
        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM books");
            int bookCount = 0;
            while (rs.next()) {
                Book book = new Book(
                        rs.getString("id"),
                        rs.getString("title"),
                        rs.getString("author"),
                        rs.getString("publication_year"),
                        rs.getString("type"),
                        rs.getString("url")
                );
                book.setSubject(rs.getString("subject"));
                book.setBorrowed(rs.getBoolean("is_borrowed"));
                books.add(book);
                bookCount++;
                System.out.println("Memuat buku dari database: " + book.getTitle()); // Debugging
            }
            rs.close();
            stmt.close();
            System.out.println("Jumlah buku dimuat dari database: " + bookCount);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void loadLoansFromDatabase() {
        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM loans");
            loans.clear(); // Bersihkan daftar loans sebelum memuat ulang
            while (rs.next()) {
                String bookId = rs.getString("book_id");
                Book book = books.stream()
                        .filter(b -> b.getId().equals(bookId))
                        .findFirst()
                        .orElse(null);
                if (book != null) {
                    Date loanDate = new Date(rs.getLong("loan_date"));
                    Long returnDateMillis = rs.wasNull() ? null : rs.getLong("return_date");
                    Date returnDate = returnDateMillis != null ? new Date(returnDateMillis) : null;
                    boolean isReturned = rs.getBoolean("is_returned");
                    Loan loan = new Loan(
                            rs.getString("loan_id"),
                            book,
                            rs.getString("borrower_name"),
                            rs.getString("class_name"),
                            rs.getString("nim"),
                            loanDate,
                            returnDate,
                            isReturned
                    );
                    loans.add(loan);
                    System.out.println("Memuat peminjaman dari database: " + loan); // Debugging
                } else {
                    System.out.println("Buku tidak ditemukan untuk bookId: " + bookId + ", peminjaman diabaikan.");
                }
            }
            rs.close();
            stmt.close();
            System.out.println("Jumlah peminjaman dimuat dari database: " + loans.size()); // Debugging
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Gagal memuat peminjaman dari database: " + e.getMessage());
        }
    }

    public void saveBook(Book book) {
        try {
            PreparedStatement pstmt = conn.prepareStatement(
                    "INSERT OR REPLACE INTO books (id, title, author, publication_year, type, url, subject, is_borrowed) VALUES (?, ?, ?, ?, ?, ?, ?, ?)");
            pstmt.setString(1, book.getId());
            pstmt.setString(2, book.getTitle());
            pstmt.setString(3, book.getAuthor());
            pstmt.setString(4, book.getPublicationYear());
            pstmt.setString(5, book.getType());
            pstmt.setString(6, book.getUrl());
            pstmt.setString(7, book.getSubject());
            pstmt.setBoolean(8, book.isBorrowed());
            pstmt.executeUpdate();
            pstmt.close();
            System.out.println("Buku disimpan ke database: " + book.getTitle()); // Debugging
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void saveBooks() {
        try {
            // Nonaktifkan constraint sementara untuk menghindari konflik
            Statement stmt = conn.createStatement();
            stmt.execute("PRAGMA foreign_keys = OFF");

            // Hapus semua data buku dari tabel
            stmt.executeUpdate("DELETE FROM books");

            // Simpan ulang semua buku
            PreparedStatement pstmt = conn.prepareStatement(
                    "INSERT INTO books (id, title, author, publication_year, type, url, subject, is_borrowed) VALUES (?, ?, ?, ?, ?, ?, ?, ?)");
            for (Book book : books) {
                pstmt.setString(1, book.getId());
                pstmt.setString(2, book.getTitle());
                pstmt.setString(3, book.getAuthor());
                pstmt.setString(4, book.getPublicationYear());
                pstmt.setString(5, book.getType());
                pstmt.setString(6, book.getUrl());
                pstmt.setString(7, book.getSubject());
                pstmt.setBoolean(8, book.isBorrowed());
                pstmt.addBatch();
            }
            pstmt.executeBatch();
            pstmt.close();

            // Aktifkan kembali constraint
            stmt.execute("PRAGMA foreign_keys = ON");
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void saveLoan(Loan loan) {
        try {
            PreparedStatement pstmt = conn.prepareStatement(
                    "INSERT OR REPLACE INTO loans (loan_id, book_id, borrower_name, class_name, nim, loan_date, return_date, is_returned) VALUES (?, ?, ?, ?, ?, ?, ?, ?)");
            pstmt.setString(1, loan.getLoanId());
            pstmt.setString(2, loan.getBook().getId());
            pstmt.setString(3, loan.getBorrowerName());
            pstmt.setString(4, loan.getClassName());
            pstmt.setString(5, loan.getNim());
            pstmt.setLong(6, loan.getLoanDate().getTime());
            pstmt.setObject(7, loan.getReturnDate() != null ? loan.getReturnDate().getTime() : null);
            pstmt.setBoolean(8, loan.isReturned());
            pstmt.executeUpdate();
            pstmt.close();

            // Perbarui status is_borrowed pada buku
            pstmt = conn.prepareStatement(
                    "UPDATE books SET is_borrowed = ? WHERE id = ?");
            pstmt.setBoolean(1, loan.getBook().isBorrowed());
            pstmt.setString(2, loan.getBook().getId());
            pstmt.executeUpdate();
            pstmt.close();
            System.out.println("Peminjaman disimpan ke database: " + loan); // Debugging
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Gagal menyimpan peminjaman: " + e.getMessage());
        }
    }

    public void updateLoan(Loan loan) {
        try {
            PreparedStatement pstmt = conn.prepareStatement(
                    "UPDATE loans SET return_date = ?, is_returned = ? WHERE loan_id = ?");
            pstmt.setObject(1, loan.getReturnDate() != null ? loan.getReturnDate().getTime() : null);
            pstmt.setBoolean(2, loan.isReturned());
            pstmt.setString(3, loan.getLoanId());
            pstmt.executeUpdate();
            pstmt.close();

            // Perbarui status is_borrowed pada buku
            pstmt = conn.prepareStatement(
                    "UPDATE books SET is_borrowed = ? WHERE id = ?");
            pstmt.setBoolean(1, loan.getBook().isBorrowed());
            pstmt.setString(2, loan.getBook().getId());
            pstmt.executeUpdate();
            pstmt.close();
            System.out.println("Peminjaman diperbarui di database: " + loan); // Debugging
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Gagal memperbarui peminjaman di database: " + e.getMessage());
        }
    }

    public List<Book> getBooks() {
        return books;
    }

    public List<ReadHistory> getReadHistory() {
        return readHistory;
    }

    public List<Loan> getLoans() {
        return loans;
    }

    public void close() {
        try {
            if (conn != null && !conn.isClosed()) {
                conn.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}