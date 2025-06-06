package ui;

import model.Loan;
import storage.DataStorage;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.*;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class FineManagementPanel extends JPanel {
    private DataStorage dataStorage;
    private DefaultTableModel tableModel;
    private JTable table;
    private List<Fine> fines;
    private LibraryUI libraryUI; // Referensi ke LibraryUI untuk mengakses BorrowPanel

    public FineManagementPanel(DataStorage dataStorage, LibraryUI libraryUI) {
        this.dataStorage = dataStorage;
        this.libraryUI = libraryUI;
        this.fines = new ArrayList<>();
        setLayout(new BorderLayout());
        setBackground(new Color(245, 247, 250));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Header dengan gradien
        JPanel headerPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                GradientPaint gp = new GradientPaint(0, 0, new Color(244, 67, 54), getWidth(), 0, new Color(239, 83, 80));
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        headerPanel.setPreferredSize(new Dimension(getWidth(), 80));
        headerPanel.setLayout(new BorderLayout());
        JLabel titleLabel = new JLabel("Manajemen Denda", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Roboto", Font.BOLD, 28));
        titleLabel.setForeground(Color.WHITE);
        headerPanel.add(titleLabel, BorderLayout.CENTER);
        add(headerPanel, BorderLayout.NORTH);

        // Tabel denda dengan kolom "No" dan "Aksi"
        String[] columns = {"No", "ID Peminjaman", "Judul Buku", "Tipe Buku", "Peminjam", "Tanggal Pinjam", "Hari Terlambat", "Denda (Rp)", "Aksi"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 8 && tableModel.getRowCount() > 0 && row >= 0 && row < tableModel.getRowCount();
            }
        };
        table = new JTable(tableModel);
        table.setFillsViewportHeight(true);
        table.setRowHeight(35);
        table.setFont(new Font("Roboto", Font.PLAIN, 14));
        table.setSelectionBackground(new Color(255, 205, 210));
        table.setSelectionForeground(Color.BLACK);
        table.setGridColor(new Color(220, 220, 220));
        table.setShowGrid(true);

        // Efek hover pada baris
        table.addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                int row = table.rowAtPoint(e.getPoint());
                if (row >= 0 && row < tableModel.getRowCount()) {
                    table.setRowSelectionInterval(row, row);
                } else {
                    table.clearSelection();
                }
            }
        });

        JTableHeader tableHeader = table.getTableHeader();
        tableHeader.setFont(new Font("Roboto", Font.BOLD, 15));
        tableHeader.setBackground(new Color(244, 67, 54));
        tableHeader.setForeground(Color.WHITE);
        tableHeader.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));

        // Renderer untuk kolom "Aksi"
        table.getColumnModel().getColumn(8).setCellRenderer((table, value, isSelected, hasFocus, row, column) -> {
            // Jika tabel kosong, kembalikan label kosong
            if (tableModel.getRowCount() == 0) {
                JLabel emptyLabel = new JLabel("");
                emptyLabel.setBackground(new Color(245, 247, 250));
                return emptyLabel;
            }
            // Jika baris tidak valid, kembalikan label kosong
            if (row < 0 || row >= tableModel.getRowCount()) {
                JLabel emptyLabel = new JLabel("");
                emptyLabel.setBackground(new Color(245, 247, 250));
                return emptyLabel;
            }
            JButton button = new JButton("Tandai Lunas");
            button.setFont(new Font("Roboto", Font.BOLD, 12));
            button.setBackground(new Color(76, 175, 80));
            button.setForeground(Color.WHITE);
            button.setFocusPainted(false);
            button.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
            button.setCursor(new Cursor(Cursor.HAND_CURSOR));
            button.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    button.setBackground(new Color(67, 160, 71));
                }
                @Override
                public void mouseExited(MouseEvent e) {
                    button.setBackground(new Color(76, 175, 80));
                }
            });
            return button;
        });

        // Editor untuk kolom "Aksi"
        table.getColumnModel().getColumn(8).setCellEditor(new DefaultCellEditor(new JCheckBox()) {
            @Override
            public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
                // Jika tabel kosong atau baris tidak valid, kembalikan label kosong
                if (tableModel.getRowCount() == 0 || row < 0 || row >= tableModel.getRowCount()) {
                    return new JLabel("");
                }
                JButton button = new JButton("Tandai Lunas");
                button.addActionListener(e -> {
                    // Pastikan baris masih valid sebelum menghapus
                    if (tableModel.getRowCount() > 0 && row >= 0 && row < tableModel.getRowCount()) {
                        String loanId = (String) tableModel.getValueAt(row, 1);
                        if (loanId != null) {
                            // Cari Loan yang sesuai
                            Loan loan = dataStorage.getLoans().stream()
                                    .filter(l -> l.getLoanId().equals(loanId))
                                    .findFirst()
                                    .orElse(null);
                            if (loan != null) {
                                // Tandai buku sebagai dikembalikan
                                loan.setReturnDate(new Date());
                                loan.setReturned(true);
                                loan.getBook().setBorrowed(false);
                                dataStorage.updateLoan(loan);

                                // Hapus Loan dari database dan DataStorage
                                deleteLoanFromDatabase(loanId);
                                dataStorage.getLoans().remove(loan);
                            }

                            // Hapus denda
                            deleteFineFromDatabase(loanId);

                            // Hentikan pengeditan sebelum refresh
                            stopCellEditing();
                            SwingUtilities.invokeLater(() -> {
                                refresh();
                                // Segarkan BorrowPanel untuk memperbarui tabel peminjaman dan bookComboBox
                                if (libraryUI != null && libraryUI.getBorrowPanel() != null) {
                                    libraryUI.getBorrowPanel().updateTable();
                                }
                                JOptionPane.showMessageDialog(FineManagementPanel.this, "Denda untuk peminjaman " + loanId + " ditandai lunas, buku dikembalikan, dan peminjaman dihapus!", "Sukses", JOptionPane.INFORMATION_MESSAGE);
                            });
                        }
                    }
                });
                button.setFont(new Font("Roboto", Font.BOLD, 12));
                button.setBackground(new Color(76, 175, 80));
                button.setForeground(Color.WHITE);
                button.setFocusPainted(false);
                button.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
                button.setCursor(new Cursor(Cursor.HAND_CURSOR));
                return button;
            }

            @Override
            public boolean stopCellEditing() {
                return super.stopCellEditing();
            }
        });

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));
        add(scrollPane, BorderLayout.CENTER);

        // Memuat semua denda dari database saat panel dibuka
        loadFinesFromDatabase();
        updateTable();
        System.out.println("Tabel manajemen denda dimuat dengan " + tableModel.getRowCount() + " peminjaman.");
    }

    public static void addFine(Loan loan, long additionalDays) {
        // Hitung hari terlambat dari tanggal pinjam sampai sekarang
        Date loanDate = loan.getLoanDate();
        Date currentDate = new Date();
        long daysLate = ChronoUnit.DAYS.between(
                Instant.ofEpochMilli(loanDate.getTime()),
                Instant.ofEpochMilli(currentDate.getTime())
        ) + additionalDays;

        // Denda awal Rp5.000, ditambah Rp5.000 per hari terlambat atau per klik "Belum Dikembalikan"
        long fineAmount = 5000 + (daysLate * 5000);

        Fine fine = new Fine(loan.getLoanId(), loan.getBook().getTitle(), loan.getBook().getType(),
                loan.getBorrowerName(), loanDate, daysLate, fineAmount);
        saveFineToDatabase(fine);
        System.out.println("Denda ditambahkan untuk peminjaman " + loan.getLoanId() + ": " + fineAmount + " (Hari Terlambat: " + daysLate + ")");
    }

    private void loadFinesFromDatabase() {
        fines.clear();
        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:library.db")) {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM fines");
            while (rs.next()) {
                Fine fine = new Fine(
                        rs.getString("loan_id"),
                        rs.getString("book_title"),
                        rs.getString("book_type"),
                        rs.getString("borrower_name"),
                        new Date(rs.getLong("loan_date")),
                        rs.getLong("days_late"),
                        rs.getLong("fine_amount")
                );
                fines.add(fine);
            }
            rs.close();
            stmt.close();
            System.out.println("Memuat denda dari database: " + fines.size() + " denda ditemukan.");
        } catch (SQLException e) {
            System.err.println("Gagal memuat denda dari database: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void checkAndAddOverdueFines() {
        // Tidak diperlukan karena denda ditambahkan secara manual
        System.out.println("Metode checkAndAddOverdueFines tidak digunakan lagi.");
    }

    private static void saveFineToDatabase(Fine fine) {
        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:library.db")) {
            // Cek apakah denda dengan loan_id sudah ada
            PreparedStatement checkStmt = conn.prepareStatement("SELECT COUNT(*) FROM fines WHERE loan_id = ?");
            checkStmt.setString(1, fine.getLoanId());
            ResultSet rs = checkStmt.executeQuery();
            boolean exists = rs.getInt(1) > 0;
            rs.close();
            checkStmt.close();

            if (exists) {
                // Update denda yang sudah ada dengan penambahan
                PreparedStatement updateStmt = conn.prepareStatement(
                        "UPDATE fines SET book_title = ?, book_type = ?, borrower_name = ?, loan_date = ?, days_late = ?, fine_amount = ? WHERE loan_id = ?");
                updateStmt.setString(1, fine.getBookTitle());
                updateStmt.setString(2, fine.getBookType());
                updateStmt.setString(3, fine.getBorrowerName());
                updateStmt.setLong(4, fine.getLoanDate().getTime());
                updateStmt.setLong(5, fine.getDaysLate());
                updateStmt.setLong(6, fine.getFineAmount());
                updateStmt.setString(7, fine.getLoanId());
                updateStmt.executeUpdate();
                updateStmt.close();
                System.out.println("Denda untuk peminjaman " + fine.getLoanId() + " diperbarui di database: " + fine.getFineAmount());
            } else {
                // Tambah denda baru
                PreparedStatement insertStmt = conn.prepareStatement(
                        "INSERT INTO fines (loan_id, book_title, book_type, borrower_name, loan_date, days_late, fine_amount) VALUES (?, ?, ?, ?, ?, ?, ?)");
                insertStmt.setString(1, fine.getLoanId());
                insertStmt.setString(2, fine.getBookTitle());
                insertStmt.setString(3, fine.getBookType());
                insertStmt.setString(4, fine.getBorrowerName());
                insertStmt.setLong(5, fine.getLoanDate().getTime());
                insertStmt.setLong(6, fine.getDaysLate());
                insertStmt.setLong(7, fine.getFineAmount());
                insertStmt.executeUpdate();
                insertStmt.close();
                System.out.println("Denda baru untuk peminjaman " + fine.getLoanId() + " ditambahkan ke database: " + fine.getFineAmount());
            }
        } catch (SQLException e) {
            System.err.println("Gagal menyimpan denda ke database: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void deleteFineFromDatabase(String loanId) {
        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:library.db")) {
            PreparedStatement pstmt = conn.prepareStatement("DELETE FROM fines WHERE loan_id = ?");
            pstmt.setString(1, loanId);
            int rowsAffected = pstmt.executeUpdate();
            pstmt.close();
            System.out.println("Menghapus denda untuk peminjaman " + loanId + ": " + rowsAffected + " baris dihapus.");
        } catch (SQLException e) {
            System.err.println("Gagal menghapus denda dari database: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void deleteLoanFromDatabase(String loanId) {
        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:library.db")) {
            PreparedStatement pstmt = conn.prepareStatement("DELETE FROM loans WHERE loan_id = ?");
            pstmt.setString(1, loanId);
            int rowsAffected = pstmt.executeUpdate();
            pstmt.close();
            System.out.println("Menghapus peminjaman " + loanId + " dari database: " + rowsAffected + " baris dihapus.");
        } catch (SQLException e) {
            System.err.println("Gagal menghapus peminjaman dari database: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void updateTable() {
        tableModel.setRowCount(0); // Bersihkan tabel sebelum mengisi ulang
        loadFinesFromDatabase(); // Muat data terbaru dari database
        int rowNumber = 1;
        for (Fine fine : fines) {
            tableModel.addRow(new Object[]{
                    rowNumber++,
                    fine.getLoanId(),
                    fine.getBookTitle(),
                    fine.getBookType(),
                    fine.getBorrowerName(),
                    fine.getLoanDate().toString(),
                    fine.getDaysLate(),
                    fine.getFineAmount(),
                    "Tandai Lunas"
            });
        }
        System.out.println("Tabel denda diperbarui dengan " + tableModel.getRowCount() + " entri.");
    }

    public void refresh() {
        SwingUtilities.invokeLater(() -> {
            System.out.println("Menyegarkan FineManagementPanel...");
            updateTable();
            table.clearSelection(); // Bersihkan seleksi untuk mencegah rendering tombol lama
            revalidate();
            repaint();
        });
    }

    private static class Fine {
        private String loanId;
        private String bookTitle;
        private String bookType;
        private String borrowerName;
        private Date loanDate;
        private long daysLate;
        private long fineAmount;

        public Fine(String loanId, String bookTitle, String bookType, String borrowerName, Date loanDate, long daysLate, long fineAmount) {
            this.loanId = loanId;
            this.bookTitle = bookTitle;
            this.bookType = bookType;
            this.borrowerName = borrowerName;
            this.loanDate = loanDate;
            this.daysLate = daysLate;
            this.fineAmount = fineAmount;
        }

        public String getLoanId() { return loanId; }
        public String getBookTitle() { return bookTitle; }
        public String getBookType() { return bookType; }
        public String getBorrowerName() { return borrowerName; }
        public Date getLoanDate() { return loanDate; }
        public long getDaysLate() { return daysLate; }
        public long getFineAmount() { return fineAmount; }
    }
}