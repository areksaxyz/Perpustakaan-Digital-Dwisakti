package ui;

import model.Book;
import model.Loan;
import model.User;
import storage.DataStorage;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.UUID;
import java.util.EventObject;

public class BorrowPanel extends JPanel {
    private DataStorage dataStorage;
    private DefaultTableModel tableModel;
    private JTable table;
    private JComboBox<Book> bookComboBox;
    private JTextField borrowerNameField;
    private JTextField classField;
    private JTextField nimField;
    private boolean showUnreturnedOnly = true;
    private LibraryUI libraryUI;
    private String currentUsername;

    public BorrowPanel(DataStorage dataStorage, Book selectedBook, LibraryUI libraryUI, String currentUsername) {
        this.dataStorage = dataStorage;
        this.libraryUI = libraryUI;
        this.currentUsername = currentUsername;
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
        borrowerNameField.setEditable(false);

        JLabel classLabel = new JLabel("Kelas:");
        classLabel.setFont(new Font("Roboto", Font.PLAIN, 14));
        classField = new JTextField(15);
        classField.setFont(new Font("Roboto", Font.PLAIN, 14));
        classField.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 1, true));
        classField.setEditable(false);

        JLabel nimLabel = new JLabel("NIM:");
        nimLabel.setFont(new Font("Roboto", Font.PLAIN, 14));
        nimField = new JTextField(15);
        nimField.setFont(new Font("Roboto", Font.PLAIN, 14));
        nimField.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 1, true));
        nimField.setEditable(false);

        User currentUser = dataStorage.getUserByUsername(currentUsername);
        if (currentUser != null) {
            borrowerNameField.setText(currentUser.getFullName());
            classField.setText(currentUser.getClassName());
            nimField.setText(currentUser.getNim());
        }

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
            if (bookComboBox.getSelectedItem() == null) {
                JOptionPane.showMessageDialog(BorrowPanel.this, "Pilih buku terlebih dahulu!", "Peringatan", JOptionPane.WARNING_MESSAGE);
                return;
            }

            Book bookToBorrow = (Book) bookComboBox.getSelectedItem();
            String borrowerName = borrowerNameField.getText().trim();
            String className = classField.getText().trim();
            String nim = nimField.getText().trim();

            if (bookToBorrow.isBorrowed()) {
                JOptionPane.showMessageDialog(BorrowPanel.this, "Buku ini sudah dipinjam!", "Peringatan", JOptionPane.WARNING_MESSAGE);
                return;
            }

            String dateStr = LocalDate.now().format(DateTimeFormatter.ofPattern("ddMMyy"));
            String loanId = bookToBorrow.getId() + "-" + dateStr + "-" + UUID.randomUUID().toString().substring(0, 8);

            LocalDate loanDate = LocalDate.now();
            Loan loan = new Loan(loanId, bookToBorrow, borrowerName, className, nim, loanDate, null, false);
            bookToBorrow.setBorrowed(true);

            dataStorage.getLoans().add(loan);
            dataStorage.saveLoan(loan);
            dataStorage.saveBooks();

            clearFields();
            updateTable();

            JOptionPane.showMessageDialog(BorrowPanel.this, "Buku berhasil dipinjam!", "Sukses", JOptionPane.INFORMATION_MESSAGE);
            if (libraryUI != null) {
                libraryUI.updateAllPanels();
            }
        });

        JButton deleteButton = new JButton("Hapus Peminjaman");
        deleteButton.setFont(new Font("Roboto", Font.BOLD, 14));
        deleteButton.setBackground(new Color(244, 67, 54));
        deleteButton.setForeground(Color.WHITE);
        deleteButton.setFocusPainted(false);
        deleteButton.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        deleteButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        deleteButton.setEnabled("admin".equals(dataStorage.getUserRole(currentUsername)));
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
            if (selectedRow >= 0) {
                String loanId = (String) tableModel.getValueAt(selectedRow, 1);
                Loan loan = dataStorage.getLoans().stream()
                        .filter(l -> l.getLoanId().equals(loanId))
                        .findFirst()
                        .orElse(null);
                if (loan != null) {
                    if (!loan.isReturned()) {
                         int confirm = JOptionPane.showConfirmDialog(BorrowPanel.this, "Peminjaman ini belum dikembalikan. Yakin ingin menghapus? Status buku akan diubah menjadi Tersedia.", "Konfirmasi Hapus", JOptionPane.YES_NO_OPTION);
                         if (confirm == JOptionPane.YES_OPTION) {
                             loan.getBook().setBorrowed(false);
                             dataStorage.saveBooks();
                             boolean deleted = dataStorage.deleteLoan(loanId);
                             if (deleted) {
                                 JOptionPane.showMessageDialog(BorrowPanel.this, "Peminjaman berhasil dihapus! Status buku diperbarui.", "Sukses", JOptionPane.INFORMATION_MESSAGE);
                             } else {
                                 JOptionPane.showMessageDialog(BorrowPanel.this, "Gagal menghapus peminjaman dari database.", "Error", JOptionPane.ERROR_MESSAGE);
                                 loan.getBook().setBorrowed(true);
                                 dataStorage.saveBooks();
                             }
                             updateTable();
                             if (libraryUI != null) libraryUI.updateAllPanels();
                         }
                    } else { // Loan is already returned
                        int confirm = JOptionPane.showConfirmDialog(BorrowPanel.this, "Peminjaman ini sudah dikembalikan. Apakah Anda yakin ingin menghapusnya dari riwayat?", "Konfirmasi Hapus Riwayat", JOptionPane.YES_NO_OPTION);
                        if (confirm == JOptionPane.YES_OPTION) {
                            boolean deleted = dataStorage.deleteLoan(loanId);
                            if (deleted) {
                                JOptionPane.showMessageDialog(BorrowPanel.this, "Peminjaman riwayat berhasil dihapus!", "Sukses", JOptionPane.INFORMATION_MESSAGE);
                            } else {
                                JOptionPane.showMessageDialog(BorrowPanel.this, "Gagal menghapus peminjaman dari database.", "Error", JOptionPane.ERROR_MESSAGE);
                            }
                            updateTable();
                            if (libraryUI != null) libraryUI.updateAllPanels();
                        }
                    }
                } else {
                    JOptionPane.showMessageDialog(BorrowPanel.this, "Peminjaman tidak ditemukan! Mungkin data tidak sinkron.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(BorrowPanel.this, "Pilih peminjaman untuk dihapus!", "Peringatan", JOptionPane.WARNING_MESSAGE);
            }
        });

        // Konfigurasi tombol berdasarkan role
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 5));
        buttonPanel.setBackground(new Color(245, 247, 250));
        String userRole = dataStorage.getUserRole(currentUsername);
        if ("user".equals(userRole)) {
            buttonPanel.add(borrowButton);
            deleteButton.setVisible(false);
        } else if ("admin".equals(userRole)) {
            buttonPanel.add(deleteButton);
            borrowButton.setVisible(false);
        }

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

        // Tentukan kolom berdasarkan peran
        String[] columns;
        if ("admin".equals(dataStorage.getUserRole(currentUsername))) {
            columns = new String[]{"No", "ID Peminjaman", "Judul Buku", "Tipe Buku", "Nama Peminjam", "Kelas", "NIM", "Tanggal Pinjam", "Tanggal Harus Dikembalikan", "Status", "Aksi 1", "Aksi 2"};
        } else {
            columns = new String[]{"No", "ID Peminjaman", "Nama Buku", "Tipe Buku", "Tanggal Peminjam", "Tanggal Harus Dikembalikan", "Status"};
        }
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                if ("admin".equals(dataStorage.getUserRole(currentUsername))) {
                    int colCount = tableModel.getColumnCount();
                    return (column == colCount - 2 || column == colCount - 1) && // Aksi 1 atau Aksi 2
                           tableModel.getRowCount() > 0 && row >= 0 && row < tableModel.getRowCount();
                }
                return false;
            }
        };
        table = new JTable(tableModel);
        table.setFillsViewportHeight(true);
        table.setRowHeight(35);
        table.setFont(new Font("Roboto", Font.PLAIN, 14));
        table.setSelectionBackground(new Color(255, 204, 128));
        table.setSelectionForeground(Color.BLACK);
        table.setGridColor(new Color(220, 220, 200));
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

        if ("admin".equals(dataStorage.getUserRole(currentUsername))) {
            // Renderer dan Editor untuk Kolom "Aksi 1" (Kembalikan)
            table.getColumnModel().getColumn(table.getColumnCount() - 2).setCellRenderer(new ButtonRenderer("Kembalikan", new Color(255, 152, 0)));
            table.getColumnModel().getColumn(table.getColumnCount() - 2).setCellEditor(new ButtonEditor(new JCheckBox(), "Kembalikan") {
                private String currentLoanId;
                private int currentRow;

                @Override
                public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
                    currentRow = row;
                    JButton editorButton = (JButton) super.getTableCellEditorComponent(table, value, isSelected, row, column);

                    String status = (String) table.getModel().getValueAt(row, table.getColumnCount() - 3);
                    if ("Sudah Dikembalikan".equals(status)) {
                        editorButton.setEnabled(false);
                        editorButton.setBackground(new Color(150, 150, 150));
                    } else {
                        editorButton.setEnabled(true);
                        editorButton.setBackground(new Color(255, 152, 0));
                    }
                    currentLoanId = (String) table.getModel().getValueAt(row, 1);

                    editorButton.addActionListener(e -> {
                        if (editorButton.isEnabled()) {
                            System.out.println("Tombol Kembalikan diklik pada baris: " + currentRow);
                            System.out.println("Loan ID yang dipilih: " + currentLoanId);

                            Loan loan = dataStorage.getLoans().stream()
                                    .filter(l -> l.getLoanId().equals(currentLoanId))
                                    .findFirst()
                                    .orElse(null);

                            if (loan != null) {
                                System.out.println("Loan ditemukan: " + loan);
                                if (!loan.isReturned()) {
                                    loan.setReturnDate(LocalDate.now());
                                    loan.setReturned(true);
                                    loan.getBook().setBorrowed(false);

                                    dataStorage.updateLoan(loan);

                                    if (libraryUI != null && libraryUI.getFineManagementPanel() != null) {
                                        dataStorage.deleteFineFromDatabase(currentLoanId);
                                    }

                                    boolean deleted = dataStorage.deleteLoan(currentLoanId);
                                    if (deleted) {
                                        System.out.println("Peminjaman " + currentLoanId + " berhasil dihapus dari database setelah dikembalikan.");
                                    } else {
                                        System.err.println("Gagal menghapus peminjaman " + currentLoanId + " dari database setelah dikembalikan.");
                                    }

                                    SwingUtilities.invokeLater(() -> {
                                        JOptionPane.showMessageDialog(BorrowPanel.this, "Buku berhasil dikembalikan dan peminjaman dihapus!", "Sukses", JOptionPane.INFORMATION_MESSAGE);
                                        updateTable();
                                        if (libraryUI != null) {
                                            libraryUI.updateAllPanels();
                                        }
                                    });
                                } else {
                                    JOptionPane.showMessageDialog(BorrowPanel.this, "Peminjaman ini sudah dikembalikan sebelumnya!", "Peringatan", JOptionPane.WARNING_MESSAGE);
                                }
                            } else {
                                JOptionPane.showMessageDialog(BorrowPanel.this, "Peminjaman tidak ditemukan untuk loanId: " + currentLoanId, "Error", JOptionPane.ERROR_MESSAGE);
                            }
                        }
                        stopCellEditing();
                    });

                    return editorButton;
                }
            });

            // Renderer dan Editor untuk Kolom "Aksi 2" (Denda)
            table.getColumnModel().getColumn(table.getColumnCount() - 1).setCellRenderer(new ButtonRenderer("Denda", new Color(244, 67, 54)));
            table.getColumnModel().getColumn(table.getColumnCount() - 1).setCellEditor(new ButtonEditor(new JCheckBox(), "Denda") {
                private String currentLoanId;
                private int currentRow;

                @Override
                public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
                    currentRow = row;
                    JButton editorButton = (JButton) super.getTableCellEditorComponent(table, value, isSelected, row, column);

                    String status = (String) table.getModel().getValueAt(row, table.getColumnCount() - 3);
                    if ("Sudah Dikembalikan".equals(status)) {
                        editorButton.setEnabled(false);
                        editorButton.setBackground(new Color(150, 150, 150));
                    } else {
                        editorButton.setEnabled(true);
                        editorButton.setBackground(new Color(244, 67, 54));
                    }
                    currentLoanId = (String) table.getModel().getValueAt(row, 1);

                    editorButton.addActionListener(e -> {
                        if (editorButton.isEnabled()) {
                            System.out.println("Tombol Denda diklik pada baris: " + currentRow);
                            System.out.println("Loan ID untuk denda: " + currentLoanId);

                            Loan loan = dataStorage.getLoans().stream()
                                    .filter(l -> l.getLoanId().equals(currentLoanId))
                                    .findFirst()
                                    .orElse(null);

                            if (loan != null) {
                                if (!loan.isReturned()) {
                                    if (libraryUI != null && libraryUI.getFineManagementPanel() != null) {
                                        libraryUI.getFineManagementPanel().addFine(loan, 0);
                                        libraryUI.getFineManagementPanel().refresh();
                                        libraryUI.showCard("FineManagementPanel");
                                        JOptionPane.showMessageDialog(BorrowPanel.this, "Peminjaman " + loan.getBook().getTitle() + " ditambahkan ke Manajemen Denda.", "Denda Ditambahkan", JOptionPane.INFORMATION_MESSAGE);
                                    }
                                } else {
                                    JOptionPane.showMessageDialog(BorrowPanel.this, "Buku ini sudah dikembalikan, tidak bisa dikenakan denda!", "Peringatan", JOptionPane.WARNING_MESSAGE);
                                }
                            } else {
                                JOptionPane.showMessageDialog(BorrowPanel.this, "Peminjaman tidak ditemukan! Mungkin data tidak sinkron.", "Error", JOptionPane.ERROR_MESSAGE);
                            }
                        }
                        stopCellEditing();
                    });

                    return editorButton;
                }
            });
        }

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));
        centerPanel.add(scrollPane, BorderLayout.CENTER);

        add(centerPanel, BorderLayout.CENTER);

        updateTable();
        System.out.println("Tabel peminjaman dimuat dengan " + tableModel.getRowCount() + " peminjaman.");
        System.out.println("Isi dataStorage.getLoans(): " + dataStorage.getLoans());
    }

    public void updateTable() {
        System.out.println("Memperbarui tabel...");
        System.out.println("Isi dataStorage.getLoans() sebelum update: " + dataStorage.getLoans());
        tableModel.setRowCount(0);
        int rowNumber = 1;
        User currentUser = dataStorage.getUserByUsername(currentUsername);
        String borrowerFullName = (currentUser != null) ? currentUser.getFullName() : "";

        dataStorage.loadDataFromDatabase();
        dataStorage.loadFinesFromDatabase();

        if ("admin".equals(dataStorage.getUserRole(currentUsername))) {
            for (Loan loan : dataStorage.getLoans()) {
                LocalDate returnExpectedDate = loan.getLoanDate().plusDays(7);
                tableModel.addRow(new Object[]{
                    rowNumber++,
                    loan.getLoanId(),
                    loan.getBook().getTitle(),
                    loan.getBook().getType(),
                    loan.getBorrowerName(),
                    loan.getClassName(),
                    loan.getNim(),
                    loan.getLoanDate().toString(),
                    returnExpectedDate.toString(),
                    loan.isReturned() ? "Sudah Dikembalikan" : "Belum Dikembalikan",
                    "", // Kolom Aksi 1 (untuk Kembalikan)
                    ""  // Kolom Aksi 2 (untuk Denda)
                });
            }
        } else {
            for (Loan loan : dataStorage.getLoans()) {
                if (loan.getBorrowerName().equalsIgnoreCase(borrowerFullName) && (!showUnreturnedOnly || !loan.isReturned())) {
                    LocalDate returnExpectedDate = loan.getLoanDate().plusDays(7);
                    tableModel.addRow(new Object[]{
                        rowNumber++,
                        loan.getLoanId(),
                        loan.getBook().getTitle(),
                        loan.getBook().getType(),
                        loan.getLoanDate().toString(),
                        returnExpectedDate.toString(),
                        loan.isReturned() ? "Sudah Dikembalikan" : "Belum Dikembalikan"
                    });
                }
            }
        }

        bookComboBox.removeAllItems();
        dataStorage.loadDataFromDatabase();
        for (Book book : dataStorage.getBooks()) {
            if (!book.isBorrowed()) {
                bookComboBox.addItem(book);
            }
        }

        System.out.println("Tabel diperbarui dengan " + tableModel.getRowCount() + " peminjaman.");
        table.revalidate();
        table.repaint();
    }

    public void updateUserTable() {
        System.out.println("Memperbarui tabel pengguna...");
        updateTable();
    }

    private void clearFields() {
        bookComboBox.setSelectedIndex(-1);
    }

    // Class ButtonRenderer and ButtonEditor for JTable (pindahkan ke luar sebagai top-level class jika tidak statis)
    // Dibuat sebagai static inner class agar dapat diakses tanpa instance BorrowPanel
    static class ButtonRenderer extends JButton implements javax.swing.table.TableCellRenderer {
        private String buttonText;
        private Color bgColor;

        public ButtonRenderer(String text, Color bgColor) {
            this.buttonText = text;
            this.bgColor = bgColor;
            setText(text);
            setBackground(bgColor);
            setForeground(Color.WHITE);
            setOpaque(true);
            setFont(new Font("Roboto", Font.BOLD, 12));
            setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
            setFocusPainted(false);
            setCursor(new Cursor(Cursor.HAND_CURSOR));
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            setText(buttonText);
            if (isSelected) {
                setBackground(bgColor.darker());
            } else {
                setBackground(bgColor);
            }
            return this;
        }
    }

    static class ButtonEditor extends DefaultCellEditor {
        protected JButton button;
        private String label;
        private boolean isPushed;

        public ButtonEditor(JCheckBox checkBox, String buttonText) {
            super(checkBox);
            button = new JButton();
            button.setOpaque(true);
            button.setFont(new Font("Roboto", Font.BOLD, 12));
            button.setForeground(Color.WHITE);
            button.setBackground(new Color(255, 152, 0)); // Default background, akan diubah oleh getTableCellEditorComponent
            button.setFocusPainted(false);
            button.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
            button.setCursor(new Cursor(Cursor.HAND_CURSOR));

            button.addActionListener(e -> fireEditingStopped());
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            if (isSelected) {
                button.setBackground(button.getBackground().darker());
            } else {
                // Warna background akan diset di bawah
            }
            label = (value == null) ? "" : value.toString();
            button.setText(label);
            isPushed = true;
            return button;
        }

        @Override
        public Object getCellEditorValue() {
            if (isPushed) {
                // Aksi akan dilakukan oleh ActionListener di kelas BorrowPanel
            }
            isPushed = false;
            return label;
        }

        @Override
        public boolean stopCellEditing() {
            isPushed = false;
            return super.stopCellEditing();
        }

        @Override
        public boolean shouldSelectCell(EventObject anEvent) {
            if (anEvent instanceof MouseEvent) {
                MouseEvent e = (MouseEvent) anEvent;
                return e.getID() == MouseEvent.MOUSE_PRESSED;
            }
            return true;
        }
    }
}