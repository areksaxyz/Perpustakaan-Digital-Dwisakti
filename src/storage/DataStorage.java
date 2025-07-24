package storage;

import model.Book;
import model.Loan;
import model.User;
import ui.FineManagementPanel.Fine;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class DataStorage {
    private List<Book> books;
    private List<Loan> loans;
    private List<User> users;
    private List<Fine> fines;

    static {
        try {
            Class.forName("org.sqlite.JDBC");
            System.out.println("SQLite JDBC Driver berhasil dimuat.");
        } catch (ClassNotFoundException e) {
            System.err.println("Gagal memuat SQLite JDBC Driver: " + e.getMessage());
        }
    }

    public DataStorage() {
        books = new ArrayList<>();
        loans = new ArrayList<>();
        users = new ArrayList<>();
        fines = new ArrayList<>();
        initializeDatabase();
        loadDataFromDatabase();
        loadFinesFromDatabase();
    }

    private void initializeDatabase() {
        String url = "jdbc:sqlite:library.db";

        try (Connection conn = DriverManager.getConnection(url);
             Statement stmt = conn.createStatement()) {

            stmt.execute("CREATE TABLE IF NOT EXISTS books (" +
                    "book_id VARCHAR(50) PRIMARY KEY, " +
                    "title VARCHAR(200) NOT NULL, " +
                    "author VARCHAR(100) NOT NULL, " +
                    "publication_year VARCHAR(10), " +
                    "type VARCHAR(20), " +
                    "url VARCHAR(255), " +
                    "subject VARCHAR(50), " +
                    "is_borrowed BOOLEAN DEFAULT FALSE)");

            stmt.execute("CREATE TABLE IF NOT EXISTS loans (" +
                    "loan_id VARCHAR(50) PRIMARY KEY, " +
                    "book_id VARCHAR(50), " +
                    "borrower_name VARCHAR(100), " +
                    "class_name VARCHAR(50), " +
                    "nim VARCHAR(20), " +
                    "loan_date DATE, " +
                    "return_date DATE, " +
                    "is_returned BOOLEAN DEFAULT FALSE, " +
                    "FOREIGN KEY (book_id) REFERENCES books(book_id) ON DELETE SET NULL)");

            stmt.execute("CREATE TABLE IF NOT EXISTS fines (" +
                    "loan_id VARCHAR(50) PRIMARY KEY, " +
                    "book_title VARCHAR(100) NOT NULL, " +
                    "book_type VARCHAR(20) NOT NULL, " +
                    "borrower_name VARCHAR(100) NOT NULL, " +
                    "class_name VARCHAR(50), " +
                    "nim VARCHAR(20), " +
                    "loan_date DATE NOT NULL, " +
                    "days_late INTEGER NOT NULL, " +
                    "fine_amount INTEGER NOT NULL)");

            stmt.execute("CREATE TABLE IF NOT EXISTS users (" +
                    "username VARCHAR(50) PRIMARY KEY, " +
                    "password VARCHAR(50), " +
                    "role VARCHAR(20), " +
                    "full_name VARCHAR(100), " +
                    "class_name VARCHAR(50), " +
                    "nim VARCHAR(20))");

            stmt.executeUpdate("INSERT OR IGNORE INTO books (book_id, title, author, publication_year, type, subject, is_borrowed) VALUES " +
                    "('BOOKFISIK1', 'Algoritma dan Pemograman 2', 'Ahsani Takwim, S.Kom., M.Kom', '2025', 'Fisik', 'Teknik Informatika', FALSE), " +
                    "('BOOKFISIK2', 'Bahasa Inggris', 'Titania Sari, S.S., M.Hum.', '2025', 'Fisik', 'Teknik Informatika', FALSE), " +
                    "('BOOKFISIK3', 'Kewarganegaraan', 'Dr. Yayat Hidayat, M.Pd.', '2025', 'Fisik', 'Teknik Informatika', FALSE), " +
                    "('BOOKFISIK4', 'Literasi Digital', 'Jack Febrian Rusdi, ST., MT.', '2025', 'Fisik', 'Teknik Informatika', FALSE), " +
                    "('BOOKFISIK5', 'Matematika Informatika', 'Farida Nurmala Sihotang, S.Si., M.Si.', '2025', 'Fisik', 'Teknik Informatika', FALSE), " +
                    "('BOOKFISIK6', 'Metode Numerik', 'Diyah Wijayati, S.Si., M.Si.', '2025', 'Fisik', 'Teknik Informatika', FALSE), " +
                    "('BOOKFISIK7', 'Organisasi & Arsitektur Komputer', 'Muhamad Malik Mutoffar, ST., MM.', '2025', 'Fisik', 'Teknik Informatika', FALSE), " +
                    "('BOOKFISIK8', 'Sistem Basis Data', 'Tarsinah Sumarni, S.Kom., M.Kom.', '2025', 'Fisik', 'Teknik Informatika', FALSE)");

            stmt.executeUpdate("INSERT OR IGNORE INTO books (book_id, title, author, publication_year, type, url, subject, is_borrowed) VALUES " +
                    "('BOOKDIGITAL1', 'Algoritma Pemograman', 'Irwan A. Kautsar, S.Kom., M.Kom., Ph.D', '2020', 'Digital', 'http://eprints.umsida.ac.id/9873/5/BE1-ALPO-BukuAjar.pdf', 'Teknik Informatika', FALSE), " +
                    "('BOOKDIGITAL2', 'Pemograman Java', 'Unikom', '2020', 'Digital', 'https://repository.unikom.ac.id/61988/1/Modul%201.pdf', 'Teknik Informatika', FALSE), " +
                    "('BOOKDIGITAL3', 'Pemograman C++', 'Alfin Ma''arif', '2020', 'Digital', 'https://eprints.uad.ac.id/32726/1/Dasar%20Pemrograman%20Bahasa%20C%2B%2B.pdf', 'Teknik Informatika', FALSE), " +
                    "('BOOKDIGITAL4', 'Pemograman Python', 'Ridwan Fajar Septian', '2020', 'Digital', 'https://repository.unikom.ac.id/65984/1/E-Book_Belajar_Pemrograman_Python_Dasar.pdf', 'Teknik Informatika', FALSE), " +
                    "('BOOKDIGITAL5', 'Pemograman JavaScript', 'Muhammad Sholikhan, S.Kom., M.Kom', '2022', 'Digital', 'https://digilib.stekom.ac.id/assets/dokumen/ebook/feb_B8yDP9PqWAcqjJqdyULO5LyV7948c7lfhDCs9TeAmAjSp9wD-HzTTJs_1662348035.pdf', 'Teknik Informatika', FALSE)");

            stmt.executeUpdate("INSERT OR IGNORE INTO users (username, password, role, full_name, class_name, nim) VALUES " +
                    "('admin', 'admin123', 'admin', 'Administrator', 'N/A', 'N/A'), " +
                    "('arga', 'arga123', 'user', 'Arga Aditama', 'TI 2023 A', '2023001'), " +
                    "('userb', 'userb123', 'user', 'Budi Santoso', 'SI 2023 B', '2023002'), " +
                    "('windah_basudara', 'windah123', 'user', 'Windah Basudara', 'TI 2023 A', '2023006')");

            System.out.println("Database inisialisasi berhasil (SQLite) dengan file library.db.");
        } catch (SQLException e) {
            System.err.println("Gagal menginisialisasi database SQLite: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public synchronized void loadDataFromDatabase() {
        String url = "jdbc:sqlite:library.db";

        try (Connection conn = DriverManager.getConnection(url);
             Statement stmt = conn.createStatement()) {

            ResultSet rs = stmt.executeQuery("SELECT * FROM books");
            books.clear();
            int bookCount = 0;
            while (rs.next()) {
                Book book = new Book(
                        rs.getString("book_id"),
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
            }
            rs.close();

            rs = stmt.executeQuery("SELECT * FROM loans");
            loans.clear();
            while (rs.next()) {
                String bookId = rs.getString("book_id");
                Book book = books.stream()
                        .filter(b -> b.getId().equals(bookId))
                        .findFirst()
                        .orElse(null);
                if (book != null) {
                    LocalDate loanDate = rs.getDate("loan_date") != null ? rs.getDate("loan_date").toLocalDate() : null;
                    LocalDate returnDate = rs.getDate("return_date") != null ? rs.getDate("return_date").toLocalDate() : null;
                    Loan loan = new Loan(
                            rs.getString("loan_id"),
                            book,
                            rs.getString("borrower_name"),
                            rs.getString("class_name"),
                            rs.getString("nim"),
                            loanDate,
                            returnDate,
                            rs.getBoolean("is_returned")
                    );
                    loans.add(loan);
                }
            }
            rs.close();

            rs = stmt.executeQuery("SELECT * FROM users");
            users.clear();
            while (rs.next()) {
                User user = new User(
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getString("role"),
                        rs.getString("full_name"),
                        rs.getString("class_name"),
                        rs.getString("nim")
                );
                users.add(user);
            }
            rs.close();

            System.out.println("Data dimuat: " + bookCount + " buku, " + loans.size() + " peminjaman, " + users.size() + " pengguna.");
        } catch (SQLException e) {
            System.err.println("Gagal memuat data dari database SQLite: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public synchronized void loadFinesFromDatabase() {
        fines.clear();
        String url = "jdbc:sqlite:library.db";

        try (Connection conn = DriverManager.getConnection(url);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM fines")) {
            while (rs.next()) {
                LocalDate loanDate = rs.getDate("loan_date") != null ? rs.getDate("loan_date").toLocalDate() : null;
                Fine fine = new Fine(
                    rs.getString("loan_id"),
                    rs.getString("book_title"),
                    rs.getString("book_type"),
                    rs.getString("borrower_name"),
                    rs.getString("class_name"),
                    rs.getString("nim"),
                    loanDate,
                    rs.getLong("days_late"),
                    rs.getLong("fine_amount")
                );
                fines.add(fine);
            }
            System.out.println("Memuat denda dari database (SQLite) ke DataStorage: " + fines.size() + " denda ditemukan.");
        } catch (SQLException e) {
            System.err.println("Gagal memuat denda dari database (SQLite) ke DataStorage: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public synchronized void saveFineToDatabase(Fine fine) {
        String url = "jdbc:sqlite:library.db";

        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement pstmt = conn.prepareStatement(
                     "INSERT OR REPLACE INTO fines (loan_id, book_title, book_type, borrower_name, class_name, nim, loan_date, days_late, fine_amount) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)")) {
            pstmt.setString(1, fine.getLoanId());
            pstmt.setString(2, fine.getBookTitle());
            pstmt.setString(3, fine.getBookType());
            pstmt.setString(4, fine.getBorrowerName());
            pstmt.setString(5, fine.getClassName());
            pstmt.setString(6, fine.getNim());
            pstmt.setDate(7, fine.getLoanDate() != null ? java.sql.Date.valueOf(fine.getLoanDate()) : null);
            pstmt.setLong(8, fine.getDaysLate());
            pstmt.setLong(9, fine.getFineAmount());
            pstmt.executeUpdate();
            System.out.println("Denda untuk peminjaman " + fine.getLoanId() + " disimpan/diperbarui di database melalui DataStorage (SQLite): " + fine.getFineAmount());

            boolean found = false;
            for (int i = 0; i < fines.size(); i++) {
                if (fines.get(i).getLoanId().equals(fine.getLoanId())) {
                    fines.set(i, fine);
                    found = true;
                    break;
                }
            }
            if (!found) {
                fines.add(fine);
            }
        } catch (SQLException e) {
            System.err.println("Gagal menyimpan denda ke database melalui DataStorage (SQLite): " + e.getMessage());
            e.printStackTrace();
        }
    }

    public synchronized void deleteFineFromDatabase(String loanId) {
        String url = "jdbc:sqlite:library.db";

        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement pstmt = conn.prepareStatement("DELETE FROM fines WHERE loan_id = ?")) {
            pstmt.setString(1, loanId);
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                fines.removeIf(fine -> fine.getLoanId().equals(loanId));
                System.out.println("Denda untuk peminjaman " + loanId + " dihapus dari database melalui DataStorage (SQLite): " + rowsAffected + " baris dihapus.");
            }
        } catch (SQLException e) {
            System.err.println("Gagal menghapus denda dari database melalui DataStorage (SQLite): " + e.getMessage());
            e.printStackTrace();
        }
    }

    public synchronized void saveBook(Book book) {
        String url = "jdbc:sqlite:library.db";

        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement pstmt = conn.prepareStatement(
                     "INSERT OR REPLACE INTO books (book_id, title, author, publication_year, type, url, subject, is_borrowed) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?)")) {

            pstmt.setString(1, book.getId());
            pstmt.setString(2, book.getTitle());
            pstmt.setString(3, book.getAuthor());
            pstmt.setString(4, book.getPublicationYear());
            pstmt.setString(5, book.getType());
            pstmt.setString(6, book.getUrl());
            pstmt.setString(7, book.getSubject());
            pstmt.setBoolean(8, book.isBorrowed());
            pstmt.executeUpdate();

            if (books.stream().noneMatch(b -> b.getId().equals(book.getId()))) {
                books.add(book);
            } else {
                books.stream()
                        .filter(b -> b.getId().equals(book.getId()))
                        .findFirst()
                        .ifPresent(existingBook -> {
                            existingBook.setTitle(book.getTitle());
                            existingBook.setAuthor(book.getAuthor());
                            existingBook.setPublicationYear(book.getPublicationYear());
                            existingBook.setType(book.getType());
                            existingBook.setUrl(book.getUrl());
                            existingBook.setSubject(book.getSubject());
                            existingBook.setBorrowed(book.isBorrowed());
                        });
            }
            System.out.println("Buku disimpan: " + book.getTitle());
        } catch (SQLException e) {
            System.err.println("Gagal menyimpan buku ke database SQLite: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public synchronized void saveLoan(Loan loan) {
        String url = "jdbc:sqlite:library.db";

        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement pstmt = conn.prepareStatement(
                     "INSERT OR REPLACE INTO loans (loan_id, book_id, borrower_name, class_name, nim, loan_date, return_date, is_returned) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?)");
             PreparedStatement pstmtBook = conn.prepareStatement("UPDATE books SET is_borrowed = ? WHERE book_id = ?")) {

            pstmt.setString(1, loan.getLoanId());
            pstmt.setString(2, loan.getBook().getId());
            pstmt.setString(3, loan.getBorrowerName());
            pstmt.setString(4, loan.getClassName());
            pstmt.setString(5, loan.getNim());
            pstmt.setDate(6, loan.getLoanDate() != null ? Date.valueOf(loan.getLoanDate()) : null);
            pstmt.setDate(7, loan.getReturnDate() != null ? Date.valueOf(loan.getReturnDate()) : null);
            pstmt.setBoolean(8, loan.isReturned());
            pstmt.executeUpdate();

            pstmtBook.setBoolean(1, loan.getBook().isBorrowed());
            pstmtBook.setString(2, loan.getBook().getId());
            pstmtBook.executeUpdate();

            if (loans.stream().noneMatch(l -> l.getLoanId().equals(loan.getLoanId()))) {
                loans.add(loan);
            } else {
                loans.stream()
                        .filter(l -> l.getLoanId().equals(loan.getLoanId()))
                        .findFirst()
                        .ifPresent(existingLoan -> {
                            existingLoan.setReturnDate(loan.getReturnDate());
                            existingLoan.setReturned(loan.isReturned());
                        });
            }
            System.out.println("Peminjaman disimpan: " + loan.getLoanId());
        } catch (SQLException e) {
            System.err.println("Gagal menyimpan peminjaman ke database SQLite: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Gagal menyimpan peminjaman: " + e.getMessage());
        }
    }

    public synchronized void updateLoan(Loan loan) {
        String url = "jdbc:sqlite:library.db";

        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement pstmt = conn.prepareStatement(
                     "UPDATE loans SET return_date = ?, is_returned = ? WHERE loan_id = ?");
             PreparedStatement pstmtBook = conn.prepareStatement("UPDATE books SET is_borrowed = ? WHERE book_id = ?")) {

            pstmt.setDate(1, loan.getReturnDate() != null ? Date.valueOf(loan.getReturnDate()) : null);
            pstmt.setBoolean(2, loan.isReturned());
            pstmt.setString(3, loan.getLoanId().trim());
            int rowsUpdated = pstmt.executeUpdate();
            System.out.println("Peminjaman diperbarui: " + loan.getLoanId() + " (Rows affected: " + rowsUpdated + ")");

            pstmtBook.setBoolean(1, loan.getBook().isBorrowed());
            pstmtBook.setString(2, loan.getBook().getId());
            pstmtBook.executeUpdate();

            loans.stream()
                    .filter(l -> l.getLoanId().equals(loan.getLoanId()))
                    .findFirst()
                    .ifPresent(existingLoan -> {
                        existingLoan.setReturnDate(loan.getReturnDate());
                        existingLoan.setReturned(loan.isReturned());
                    });

        } catch (SQLException e) {
            System.err.println("Gagal memperbarui peminjaman ke database SQLite: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public synchronized boolean deleteLoan(String loanId) {
        String url = "jdbc:sqlite:library.db";

        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement pstmt = conn.prepareStatement("DELETE FROM loans WHERE loan_id = ?")) {

            System.out.println("Mencoba menghapus loan_id: '" + loanId + "'");
            pstmt.setString(1, loanId.trim());
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                loans.removeIf(loan -> loan.getLoanId().equals(loanId));
                System.out.println("Peminjaman dihapus dari database: " + loanId + " (Rows affected: " + rowsAffected + ")");
                return true;
            } else {
                System.out.println("Gagal menghapus peminjaman: Tidak ada baris yang cocok untuk loan_id: " + loanId);
                return false;
            }
        } catch (SQLException e) {
            System.err.println("Gagal menghapus peminjaman dari database SQLite: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public synchronized void saveUser(String username, String password, String role, String fullName, String className, String nim) {
        String url = "jdbc:sqlite:library.db";

        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement checkStmt = conn.prepareStatement("SELECT COUNT(*) FROM users WHERE username = ?")) {

            checkStmt.setString(1, username.trim());
            ResultSet rs = checkStmt.executeQuery();
            boolean exists = rs.next() && rs.getInt(1) > 0;
            rs.close();

            try (PreparedStatement pstmt = conn.prepareStatement(
                    exists ? "UPDATE users SET password = ?, role = ?, full_name = ?, class_name = ?, nim = ? WHERE username = ?"
                           : "INSERT INTO users (username, password, role, full_name, class_name, nim) VALUES (?, ?, ?, ?, ?, ?)")) {

                if (exists) {
                    pstmt.setString(1, password.trim());
                    pstmt.setString(2, role);
                    pstmt.setString(3, fullName);
                    pstmt.setString(4, className);
                    pstmt.setString(5, nim);
                    pstmt.setString(6, username.trim());
                } else {
                    pstmt.setString(1, username.trim());
                    pstmt.setString(2, password.trim());
                    pstmt.setString(3, role);
                    pstmt.setString(4, fullName);
                    pstmt.setString(5, className);
                    pstmt.setString(6, nim);
                }

                int rowsAffected = pstmt.executeUpdate();
                System.out.println("User " + username + " disimpan ke database. Baris terpengaruh: " + rowsAffected);

                if (!exists) {
                    User newUser = new User(username.trim(), password.trim(), role, fullName, className, nim);
                    users.add(newUser);
                    System.out.println("User " + username + " ditambahkan ke DataStorage.");
                }
            }
        } catch (SQLException e) {
            System.err.println("Gagal menyimpan user ke database SQLite: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public synchronized boolean deleteUser(String username) {
        String url = "jdbc:sqlite:library.db";

        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement pstmt = conn.prepareStatement("DELETE FROM users WHERE username = ?")) {

            System.out.println("Mencoba menghapus user: '" + username + "'");
            pstmt.setString(1, username.trim());
            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected > 0) {
                users.removeIf(user -> user.getUsername().equals(username));
                loadDataFromDatabase(); // Memuat ulang data untuk memastikan konsistensi
                System.out.println("User dengan username " + username + " dihapus dari database (Rows affected: " + rowsAffected + ")");
                return true;
            } else {
                System.out.println("Gagal menghapus user: Tidak ada user dengan username " + username);
                return false;
            }
        } catch (SQLException e) {
            System.err.println("Gagal menghapus user dari database SQLite: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public synchronized boolean deleteBook(String bookId) {
        String url = "jdbc:sqlite:library.db";

        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement pstmt = conn.prepareStatement("DELETE FROM books WHERE book_id = ?")) {

            pstmt.setString(1, bookId);
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                books.removeIf(book -> book.getId().equals(bookId));
                System.out.println("Buku dengan ID " + bookId + " dihapus dari database (Rows affected: " + rowsAffected + ")");
                return true;
            } else {
                System.out.println("Gagal menghapus buku: Tidak ada buku dengan ID " + bookId);
                return false;
            }
        } catch (SQLException e) {
            System.err.println("Gagal menghapus buku dari database SQLite: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public List<String[]> getAllUsersData() {
        List<String[]> userData = new ArrayList<>();
        String url = "jdbc:sqlite:library.db";

        try (Connection conn = DriverManager.getConnection(url);
             Statement stmt = conn.createStatement()) {

            ResultSet rs = stmt.executeQuery("SELECT username, full_name, class_name, nim FROM users WHERE role = 'user'");
            while (rs.next()) {
                userData.add(new String[]{
                        rs.getString("username"),
                        rs.getString("full_name"),
                        rs.getString("class_name"),
                        rs.getString("nim")
                });
            }
            rs.close();
        } catch (SQLException e) {
            System.err.println("Gagal memuat data user dari database SQLite: " + e.getMessage());
            e.printStackTrace();
        }
        return userData;
    }

    public String[] getUserData(String username) {
        String[] userData = null;
        String url = "jdbc:sqlite:library.db";

        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement pstmt = conn.prepareStatement("SELECT full_name, class_name, nim FROM users WHERE username = ?")) {

            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                userData = new String[]{
                        rs.getString("full_name"),
                        rs.getString("class_name"),
                        rs.getString("nim")
                };
            }
            rs.close();
        } catch (SQLException e) {
            System.err.println("Gagal mengambil data user dari database SQLite: " + e.getMessage());
            e.printStackTrace();
        }
        return userData;
    }

    public boolean isUserExists(String username) {
        String url = "jdbc:sqlite:library.db";

        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement pstmt = conn.prepareStatement("SELECT COUNT(*) FROM users WHERE username = ?")) {

            pstmt.setString(1, username.trim());
            ResultSet rs = pstmt.executeQuery();
            boolean exists = rs.next() && rs.getInt(1) > 0;
            rs.close();
            System.out.println("Memeriksa user " + username + ": " + (exists ? "Ada" : "Tidak ada"));
            return exists;
        } catch (SQLException e) {
            System.err.println("Gagal memeriksa user dari database SQLite: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public boolean authenticateUser(String username, String password) {
        String url = "jdbc:sqlite:library.db";

        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement pstmt = conn.prepareStatement("SELECT password FROM users WHERE username = ?")) {

            pstmt.setString(1, username.trim());
            ResultSet rs = pstmt.executeQuery();
            boolean authenticated = rs.next() && rs.getString("password").equals(password.trim());
            rs.close();
            System.out.println("Autentikasi user " + username + ": " + (authenticated ? "Berhasil" : "Gagal"));
            return authenticated;
        } catch (SQLException e) {
            System.err.println("Gagal autentikasi user dari database SQLite: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public String getUserRole(String username) {
        String url = "jdbc:sqlite:library.db";

        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement pstmt = conn.prepareStatement("SELECT role FROM users WHERE username = ?")) {

            pstmt.setString(1, username.trim());
            ResultSet rs = pstmt.executeQuery();
            String role = rs.next() ? rs.getString("role") : null;
            rs.close();
            System.out.println("Role user " + username + ": " + (role != null ? role : "null"));
            return role != null ? role : "user";
        } catch (SQLException e) {
            System.err.println("Gagal mengambil role user dari database SQLite: " + e.getMessage());
            e.printStackTrace();
            return "user";
        }
    }

    public User getUserByUsername(String username) {
        String url = "jdbc:sqlite:library.db";

        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement pstmt = conn.prepareStatement(
                     "SELECT username, password, role, full_name, class_name, nim FROM users WHERE username = ?")) {

            pstmt.setString(1, username.trim());
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return new User(
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getString("role"),
                        rs.getString("full_name"),
                        rs.getString("class_name"),
                        rs.getString("nim")
                );
            }
            rs.close();
        } catch (SQLException e) {
            System.err.println("Gagal mengambil user oleh username dari database SQLite: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    public List<User> getUsers() {
        return users;
    }

    public List<Book> getBooks() {
        return books;
    }

    public List<Loan> getLoans() {
        return loans;
    }

    public List<Fine> getFines() {
        return fines;
    }

    public synchronized void saveBooks() {
        String url = "jdbc:sqlite:library.db";

        try (Connection conn = DriverManager.getConnection(url)) {
            conn.setAutoCommit(false);

            try (PreparedStatement pstmt = conn.prepareStatement(
                    "INSERT OR REPLACE INTO books (book_id, title, author, publication_year, type, url, subject, is_borrowed) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?)")) {

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
                conn.commit();
                System.out.println("Semua buku disimpan ke database.");
            } catch (SQLException e) {
                conn.rollback();
                System.err.println("Gagal menyimpan buku ke database SQLite: " + e.getMessage());
                e.printStackTrace();
            }
        } catch (SQLException e) {
            System.err.println("Gagal terhubung ke database untuk menyimpan buku: " + e.getMessage());
            e.printStackTrace();
        }
    }
}