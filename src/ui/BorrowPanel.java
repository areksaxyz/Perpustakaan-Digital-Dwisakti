package ui;

import model.Book;
import model.Loan;
import storage.DataStorage;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.UUID;

public class BorrowPanel extends JPanel {
    private DataStorage dataStorage;
    private DefaultTableModel tableModel;
    private JTable table;
    private JComboBox<Book> bookComboBox;
    private JTextField borrowerNameField;
    private JTextField classField;
    private JTextField nimField;
    private boolean showUnreturnedOnly = true;
    private LibraryUI libraryUI; // Referensi ke LibraryUI

    public BorrowPanel(DataStorage dataStorage, Book selectedBook, LibraryUI libraryUI) {
        this.dataStorage = dataStorage;
        this.libraryUI = libraryUI;
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
                GradientPaint gp = new GradientPaint(0, 0, new Color(255, 152, 0), getWidth(), 0, new Color(255, 167, 38));
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        headerPanel.setPreferredSize(new Dimension(getWidth(), 80));
        headerPanel.setLayout(new BorderLayout());
        JLabel titleLabel = new JLabel("Peminjaman Buku", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Roboto", Font.BOLD, 28));
        titleLabel.setForeground(Color.WHITE);
        headerPanel.add(titleLabel, BorderLayout.CENTER);
        add(headerPanel, BorderLayout.NORTH);

        // Form peminjaman
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(new Color(245, 247, 250));
        formPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)), "Form Peminjaman", 0, 0, new Font("Roboto", Font.BOLD, 16), new Color(255, 152, 0)));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel bookLabel = new JLabel("Pilih Buku:");
        bookLabel.setFont(new Font("Roboto", Font.PLAIN, 14));
        bookComboBox = new JComboBox<>();
        for (Book book : dataStorage.getBooks()) {
            if (!book.isBorrowed()) {
                bookComboBox.addItem(book);
            }
        }
        // Pre-select buku jika selectedBook tidak null dan tersedia
        if (selectedBook != null && !selectedBook.isBorrowed()) {
            bookComboBox.setSelectedItem(selectedBook);
        }
        bookComboBox.setFont(new Font("Roboto", Font.PLAIN, 14));
        bookComboBox.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Book) {
                    Book book = (Book) value;
                    setText(book.getTitle() + " (" + book.getType() + ")");
                }
                return this;
            }
        });

        JLabel borrowerLabel = new JLabel("Nama Peminjam:");
        borrowerLabel.setFont(new Font("Roboto", Font.PLAIN, 14));
        borrowerNameField = new JTextField(15);
        borrowerNameField.setFont(new Font("Roboto", Font.PLAIN, 14));
        borrowerNameField.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 1, true));

        JLabel classLabel = new JLabel("Kelas:");
        classLabel.setFont(new Font("Roboto", Font.PLAIN, 14));
        classField = new JTextField(15);
        classField.setFont(new Font("Roboto", Font.PLAIN, 14));
        classField.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 1, true));

        JLabel nimLabel = new JLabel("NIM:");
        nimLabel.setFont(new Font("Roboto", Font.PLAIN, 14));
        nimField = new JTextField(15);
        nimField.setFont(new Font("Roboto", Font.PLAIN, 14));
        nimField.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 1, true));
        nimField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                if (!Character.isDigit(c)) {
                    e.consume(); // Hanya izinkan angka
                }
            }
        });

        JButton borrowButton = new JButton("Pinjam Buku");
        borrowButton.setFont(new Font("Roboto", Font.BOLD, 14));
        borrowButton.setBackground(new Color(255, 152, 0));
        borrowButton.setForeground(Color.WHITE);
        borrowButton.setFocusPainted(false);
        borrowButton.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        borrowButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        borrowButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                borrowButton.setBackground(new Color(255, 167, 38));
            }
            @Override
            public void mouseExited(MouseEvent e) {
                borrowButton.setBackground(new Color(255, 152, 0));
            }
        });
        borrowButton.addActionListener(e -> {
            System.out.println("Tombol Pinjam Buku diklik"); // Debugging
            // Validasi input
            if (bookComboBox.getSelectedItem() == null) {
                JOptionPane.showMessageDialog(BorrowPanel.this, "Pilih buku terlebih dahulu!", "Peringatan", JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (borrowerNameField.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(BorrowPanel.this, "Nama peminjam tidak boleh kosong!", "Peringatan", JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (classField.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(BorrowPanel.this, "Kelas tidak boleh kosong!", "Peringatan", JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (nimField.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(BorrowPanel.this, "NIM tidak boleh kosong!", "Peringatan", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Ambil data dari form
            Book bookToBorrow = (Book) bookComboBox.getSelectedItem();
            String borrowerName = borrowerNameField.getText().trim();
            String className = classField.getText().trim();
            String nim = nimField.getText().trim();

            // Cek apakah buku sudah dipinjam
            if (bookToBorrow.isBorrowed()) {
                JOptionPane.showMessageDialog(BorrowPanel.this, "Buku ini sudah dipinjam!", "Peringatan", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Buat ID peminjaman (format: BOOKID-DDMMYY-RANDOM)
            String dateStr = LocalDate.now().format(DateTimeFormatter.ofPattern("ddMMyy"));
            String loanId = bookToBorrow.getId() + "-" + dateStr + "-" + UUID.randomUUID().toString().substring(0, 8);

            // Buat objek Loan
            Loan loan = new Loan(loanId, bookToBorrow, borrowerName, className, nim);
            bookToBorrow.setBorrowed(true); // Tandai buku sebagai dipinjam

            // Simpan peminjaman ke DataStorage
            dataStorage.getLoans().add(loan);
            dataStorage.saveLoan(loan);
            dataStorage.saveBooks(); // Perbarui status buku di database

            // Bersihkan form dan perbarui tabel
            clearFields();
            updateTable();

            JOptionPane.showMessageDialog(BorrowPanel.this, "Buku berhasil dipinjam!", "Sukses", JOptionPane.INFORMATION_MESSAGE);
        });

        JButton filterButton = new JButton("Tampilkan Semua");
        filterButton.setFont(new Font("Roboto", Font.BOLD, 14));
        filterButton.setBackground(new Color(255, 152, 0));
        filterButton.setForeground(Color.WHITE);
        filterButton.setFocusPainted(false);
        filterButton.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        filterButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        filterButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                filterButton.setBackground(new Color(255, 167, 38));
            }
            @Override
            public void mouseExited(MouseEvent e) {
                filterButton.setBackground(new Color(255, 152, 0));
            }
        });
        filterButton.addActionListener(e -> {
            showUnreturnedOnly = !showUnreturnedOnly;
            filterButton.setText(showUnreturnedOnly ? "Tampilkan Semua" : "Tampilkan Belum Dikembalikan");
            updateTable();
        });

        JButton deleteButton = new JButton("Hapus");
        deleteButton.setFont(new Font("Roboto", Font.BOLD, 14));
        deleteButton.setBackground(new Color(244, 67, 54));
        deleteButton.setForeground(Color.WHITE);
        deleteButton.setFocusPainted(false);
        deleteButton.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        deleteButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        deleteButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                deleteButton.setBackground(new Color(239, 83, 80));
            }
            @Override
            public void mouseExited(MouseEvent e) {
                deleteButton.setBackground(new Color(244, 67, 54));
            }
        });
        deleteButton.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow != -1) {
                String loanId = (String) table.getValueAt(selectedRow, 1);
                Loan loan = dataStorage.getLoans().stream()
                        .filter(l -> l.getLoanId().equals(loanId))
                        .findFirst()
                        .orElse(null);
                if (loan != null) {
                    JPasswordField passwordField = new JPasswordField();
                    int option = JOptionPane.showConfirmDialog(
                            BorrowPanel.this,
                            passwordField,
                            "Masukkan Password untuk Menghapus",
                            JOptionPane.OK_CANCEL_OPTION,
                            JOptionPane.PLAIN_MESSAGE
                    );
                    if (option == JOptionPane.OK_OPTION) {
                        String password = new String(passwordField.getPassword());
                        if ("admin1234".equals(password)) {
                            loan.getBook().setBorrowed(false);
                            dataStorage.getLoans().remove(loan);
                            deleteLoanFromDatabase(loanId);
                            updateTable();
                            JOptionPane.showMessageDialog(BorrowPanel.this, "Peminjaman berhasil dihapus!", "Sukses", JOptionPane.INFORMATION_MESSAGE);
                        } else {
                            JOptionPane.showMessageDialog(BorrowPanel.this, "Password salah!", "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                }
            } else {
                JOptionPane.showMessageDialog(BorrowPanel.this, "Pilih peminjaman terlebih dahulu!", "Peringatan", JOptionPane.WARNING_MESSAGE);
            }
        });

        // Panel untuk tombol agar lebih rapi
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 5));
        buttonPanel.setBackground(new Color(245, 247, 250));
        buttonPanel.add(borrowButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(filterButton);

        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(bookLabel, gbc);
        gbc.gridx = 1;
        formPanel.add(bookComboBox, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(borrowerLabel, gbc);
        gbc.gridx = 1;
        formPanel.add(borrowerNameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        formPanel.add(classLabel, gbc);
        gbc.gridx = 1;
        formPanel.add(classField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        formPanel.add(nimLabel, gbc);
        gbc.gridx = 1;
        formPanel.add(nimField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        formPanel.add(buttonPanel, gbc);

        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBackground(new Color(245, 247, 250));
        centerPanel.add(formPanel, BorderLayout.NORTH);

        String[] columns = {"No", "ID Peminjaman", "Judul Buku", "Tipe Buku", "Peminjam", "Kelas", "NIM", "Tanggal Pinjam", "Status", "Aksi 1", "Aksi 2"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 9 || column == 10;
            }
        };
        table = new JTable(tableModel);
        table.setFillsViewportHeight(true);
        table.setRowHeight(35);
        table.setFont(new Font("Roboto", Font.PLAIN, 14));
        table.setSelectionBackground(new Color(255, 204, 128));
        table.setSelectionForeground(Color.BLACK);
        table.setGridColor(new Color(220, 220, 220));
        table.setShowGrid(true);

        table.addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                int row = table.rowAtPoint(e.getPoint());
                if (row >= 0) {
                    table.setRowSelectionInterval(row, row);
                } else {
                    table.clearSelection();
                }
            }
        });

        JTableHeader tableHeader = table.getTableHeader();
        tableHeader.setFont(new Font("Roboto", Font.BOLD, 15));
        tableHeader.setBackground(new Color(255, 152, 0));
        tableHeader.setForeground(Color.WHITE);
        tableHeader.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));

        table.getColumnModel().getColumn(9).setCellRenderer((table, value, isSelected, hasFocus, row, column) -> {
            if (row >= table.getRowCount()) { // Cegah akses baris yang sudah dihapus
                return new JLabel("");
            }
            JButton button;
            String status = (String) table.getValueAt(row, 8);
            if ("Belum Dikembalikan".equals(status)) {
                button = new JButton("Kembalikan");
            } else {
                button = new JButton("Detail");
            }
            button.setFont(new Font("Roboto", Font.BOLD, 12));
            button.setBackground(new Color(255, 152, 0));
            button.setForeground(Color.WHITE);
            button.setFocusPainted(false);
            button.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
            button.setCursor(new Cursor(Cursor.HAND_CURSOR));
            button.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    button.setBackground(new Color(255, 167, 38));
                }
                @Override
                public void mouseExited(MouseEvent e) {
                    button.setBackground(new Color(255, 152, 0));
                }
            });
            return button;
        });

        table.getColumnModel().getColumn(9).setCellEditor(new DefaultCellEditor(new JCheckBox()) {
            @Override
            public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
                if (row >= table.getRowCount()) { // Cegah akses baris yang sudah dihapus
                    stopCellEditing();
                    return new JLabel("");
                }
                JButton button;
                String status = (String) table.getValueAt(row, 8);
                if ("Belum Dikembalikan".equals(status)) {
                    button = new JButton("Kembalikan");
                    button.addActionListener(e -> {
                        System.out.println("Tombol Kembalikan diklik pada baris: " + row); // Debugging
                        if (row >= 0 && row < table.getRowCount()) { // Pastikan baris valid
                            String loanId = (String) table.getValueAt(row, 1);
                            System.out.println("Loan ID yang dipilih: " + loanId); // Debugging
                            Loan loan = dataStorage.getLoans().stream()
                                    .filter(l -> l.getLoanId().equals(loanId))
                                    .findFirst()
                                    .orElse(null);
                            if (loan != null) {
                                System.out.println("Loan ditemukan: " + loan); // Debugging
                                if (!loan.isReturned()) {
                                    loan.setReturnDate(new Date());
                                    loan.setReturned(true);
                                    loan.getBook().setBorrowed(false);
                                    dataStorage.updateLoan(loan);

                                    // Hapus peminjaman dari DataStorage dan database
                                    dataStorage.getLoans().remove(loan);
                                    deleteLoanFromDatabase(loanId);

                                    // Hentikan pengeditan sebelum memperbarui tabel
                                    if (table.getCellEditor() != null) {
                                        table.getCellEditor().stopCellEditing();
                                    }
                                    table.clearSelection();

                                    // Perbarui tabel
                                    SwingUtilities.invokeLater(() -> {
                                        updateTable();
                                        JOptionPane.showMessageDialog(BorrowPanel.this, "Buku berhasil dikembalikan dan peminjaman dihapus!", "Sukses", JOptionPane.INFORMATION_MESSAGE);
                                    });
                                } else {
                                    System.out.println("Loan sudah dikembalikan sebelumnya."); // Debugging
                                    JOptionPane.showMessageDialog(BorrowPanel.this, "Peminjaman ini sudah dikembalikan sebelumnya!", "Peringatan", JOptionPane.WARNING_MESSAGE);
                                }
                            } else {
                                System.out.println("Loan tidak ditemukan untuk loanId: " + loanId); // Debugging
                                JOptionPane.showMessageDialog(BorrowPanel.this, "Peminjaman tidak ditemukan! Mungkin data tidak sinkron.", "Error", JOptionPane.ERROR_MESSAGE);
                            }
                        } else {
                            System.out.println("Baris tidak valid: " + row); // Debugging
                            JOptionPane.showMessageDialog(BorrowPanel.this, "Baris tidak valid!", "Error", JOptionPane.ERROR_MESSAGE);
                        }
                        stopCellEditing(); // Hentikan pengeditan setelah aksi
                    });
                } else {
                    button = new JButton("Detail");
                    button.addActionListener(e -> {
                        if (row >= 0 && row < table.getRowCount()) { // Pastikan baris valid
                            String loanId = (String) table.getValueAt(row, 1);
                            Loan loan = dataStorage.getLoans().stream()
                                    .filter(l -> l.getLoanId().equals(loanId))
                                    .findFirst()
                                    .orElse(null);
                            if (loan != null) {
                                JOptionPane.showMessageDialog(BorrowPanel.this,
                                        "Detail Peminjaman:\n" +
                                        "ID Peminjaman: " + loan.getLoanId() + "\n" +
                                        "Judul Buku: " + loan.getBook().getTitle() + "\n" +
                                        "Peminjam: " + loan.getBorrowerName() + "\n" +
                                        "Tanggal Pinjam: " + loan.getLoanDate() + "\n" +
                                        "Tanggal Kembali: " + (loan.getReturnDate() != null ? loan.getReturnDate() : "-"),
                                        "Detail Peminjaman", JOptionPane.INFORMATION_MESSAGE);
                            } else {
                                JOptionPane.showMessageDialog(BorrowPanel.this, "Peminjaman tidak ditemukan!", "Error", JOptionPane.ERROR_MESSAGE);
                            }
                        }
                        stopCellEditing(); // Hentikan pengeditan setelah aksi
                    });
                }
                button.setFont(new Font("Roboto", Font.BOLD, 12));
                button.setBackground(new Color(255, 152, 0));
                button.setForeground(Color.WHITE);
                button.setFocusPainted(false);
                button.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
                button.setCursor(new Cursor(Cursor.HAND_CURSOR));
                return button;
            }
        });

        table.getColumnModel().getColumn(10).setCellRenderer((table, value, isSelected, hasFocus, row, column) -> {
            if (row >= table.getRowCount()) { // Cegah akses baris yang sudah dihapus
                return new JLabel("");
            }
            JButton button = new JButton("Belum Dikembalikan");
            button.setFont(new Font("Roboto", Font.BOLD, 12));
            button.setBackground(new Color(244, 67, 54));
            button.setForeground(Color.WHITE);
            button.setFocusPainted(false);
            button.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
            button.setCursor(new Cursor(Cursor.HAND_CURSOR));
            button.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    button.setBackground(new Color(239, 83, 80));
                }
                @Override
                public void mouseExited(MouseEvent e) {
                    button.setBackground(new Color(244, 67, 54));
                }
            });
            return button;
        });

        table.getColumnModel().getColumn(10).setCellEditor(new DefaultCellEditor(new JCheckBox()) {
            @Override
            public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
                if (row >= table.getRowCount()) { // Cegah akses baris yang sudah dihapus
                    stopCellEditing();
                    return new JLabel("");
                }
                JButton button = new JButton("Belum Dikembalikan");
                button.addActionListener(e -> {
                    if (row >= 0 && row < table.getRowCount()) { // Pastikan baris valid
                        String loanId = (String) table.getValueAt(row, 1);
                        Loan loan = dataStorage.getLoans().stream()
                                .filter(l -> l.getLoanId().equals(loanId))
                                .findFirst()
                                .orElse(null);
                        if (loan != null) {
                            FineManagementPanel.addFine(loan, 0); // Tambahkan denda tanpa menghitung hari terlambat awal
                            if (libraryUI != null && libraryUI.getFineManagementPanel() != null) {
                                libraryUI.getFineManagementPanel().refresh();
                            }
                            JOptionPane.showMessageDialog(BorrowPanel.this, "Buku telah ditambahkan ke Manajemen Denda!", "Sukses", JOptionPane.INFORMATION_MESSAGE);
                            // Hentikan pengeditan sebelum memperbarui tabel
                            if (table.getCellEditor() != null) {
                                table.getCellEditor().stopCellEditing();
                            }
                            table.clearSelection();
                            updateTable();
                        } else {
                            JOptionPane.showMessageDialog(BorrowPanel.this, "Peminjaman tidak ditemukan!", "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                    stopCellEditing(); // Hentikan pengeditan setelah aksi
                });
                button.setFont(new Font("Roboto", Font.BOLD, 12));
                button.setBackground(new Color(244, 67, 54));
                button.setForeground(Color.WHITE);
                button.setFocusPainted(false);
                button.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
                button.setCursor(new Cursor(Cursor.HAND_CURSOR));
                return button;
            }
        });

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));
        centerPanel.add(scrollPane, BorderLayout.CENTER);

        add(centerPanel, BorderLayout.CENTER);

        updateTable();
        System.out.println("Tabel peminjaman dimuat dengan " + tableModel.getRowCount() + " peminjaman.");
        System.out.println("Isi dataStorage.getLoans(): " + dataStorage.getLoans()); // Debugging
    }

    private void deleteLoanFromDatabase(String loanId) {
        try {
            java.sql.Connection conn = java.sql.DriverManager.getConnection("jdbc:sqlite:library.db");
            java.sql.PreparedStatement pstmt = conn.prepareStatement("DELETE FROM loans WHERE loan_id = ?");
            pstmt.setString(1, loanId);
            pstmt.executeUpdate();
            pstmt.close();
            conn.close();
            System.out.println("Peminjaman dihapus dari database: " + loanId); // Debugging
        } catch (java.sql.SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Gagal menghapus peminjaman dari database: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Pastikan metode ini public
    public void updateTable() {
        System.out.println("Memperbarui tabel..."); // Debugging
        System.out.println("Isi dataStorage.getLoans() sebelum update: " + dataStorage.getLoans()); // Debugging
        tableModel.setRowCount(0);
        int rowNumber = 1;
        for (Loan loan : dataStorage.getLoans()) {
            if (showUnreturnedOnly && loan.isReturned()) {
                continue;
            }
            tableModel.addRow(new Object[]{
                    rowNumber++,
                    loan.getLoanId(),
                    loan.getBook().getTitle(),
                    loan.getBook().getType(),
                    loan.getBorrowerName(),
                    loan.getClassName(),
                    loan.getNim(),
                    loan.getLoanDate().toString(),
                    loan.isReturned() ? "Sudah Dikembalikan" : "Belum Dikembalikan",
                    "", // Placeholder untuk tombol Kembalikan/Detail
                    ""  // Placeholder untuk tombol Belum Dikembalikan
            });
        }

        bookComboBox.removeAllItems();
        for (Book book : dataStorage.getBooks()) {
            if (!book.isBorrowed()) {
                bookComboBox.addItem(book);
            }
        }
        System.out.println("Tabel diperbarui dengan " + tableModel.getRowCount() + " peminjaman."); // Debugging
    }

    private void clearFields() {
        bookComboBox.setSelectedIndex(-1);
        borrowerNameField.setText("");
        classField.setText("");
        nimField.setText("");
    }
}