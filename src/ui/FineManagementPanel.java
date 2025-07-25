package ui;

import model.Loan;
import model.Book;
import storage.DataStorage;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.*; // Keep this import
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

public class FineManagementPanel extends JPanel {
    private DataStorage dataStorage;
    private DefaultTableModel tableModel;
    private JTable table;
    private List<Fine> fines; // Local list, should be consistent with DB
    private LibraryUI libraryUI;

    public FineManagementPanel(DataStorage dataStorage, LibraryUI libraryUI) {
        this.dataStorage = dataStorage;
        this.libraryUI = libraryUI;
        this.fines = new ArrayList<>(); // Initialize local list
        setLayout(new BorderLayout());
        setBackground(new Color(245, 247, 250));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

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

        String[] columns = {"No", "Nama Peminjam", "Kelas", "NIM", "ID Peminjaman", "Judul Buku", "Tipe Buku", "Tanggal Pinjam", "Hari Terlambat", "Denda (Rp)", "Aksi"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 10 && tableModel.getRowCount() > 0 && row >= 0 && row < tableModel.getRowCount();
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

        table.getColumnModel().getColumn(10).setCellRenderer((table, value, isSelected, hasFocus, row, column) -> {
            if (tableModel.getRowCount() == 0 || row < 0 || row >= tableModel.getRowCount()) {
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

        table.getColumnModel().getColumn(10).setCellEditor(new DefaultCellEditor(new JCheckBox()) {
            @Override
            public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
                if (tableModel.getRowCount() == 0 || row < 0 || row >= tableModel.getRowCount()) {
                    return new JLabel("");
                }
                JButton button = new JButton("Tandai Lunas");
                button.addActionListener(e -> {
                    int selectedRow = table.getSelectedRow();
                    if (selectedRow >= 0 && selectedRow < tableModel.getRowCount()) {
                        String loanId = (String) tableModel.getValueAt(selectedRow, 4); // Kolom ID Peminjaman
                        if (loanId != null) {
                            // Temukan Loan dari dataStorage, bukan hanya dari list fines lokal
                            Loan loan = dataStorage.getLoans().stream()
                                    .filter(l -> l.getLoanId().equals(loanId))
                                    .findFirst()
                                    .orElse(null);

                            if (loan != null) {
                                // 1. Tandai buku kembali di DataStorage dan database
                                loan.setReturnDate(LocalDate.now());
                                loan.setReturned(true);
                                loan.getBook().setBorrowed(false); 
                                dataStorage.updateLoan(loan); 

                                // 2. Hapus denda dari database
                                dataStorage.deleteFineFromDatabase(loanId); 
                                dataStorage.deleteLoan(loanId); 

                                SwingUtilities.invokeLater(() -> {
                                    // Hapus baris dari tabel model lokal
                                    DefaultTableModel model = (DefaultTableModel) table.getModel();
                                    // Pastikan selectedRow masih valid sebelum remove
                                    if (selectedRow < model.getRowCount()) {
                                        model.removeRow(selectedRow);
                                    }
                                    
                                    // Perbarui tampilan di BorrowPanel juga
                                    if (libraryUI != null && libraryUI.getBorrowPanel() != null) {
                                        libraryUI.getBorrowPanel().updateTable();
                                    }
                                    JOptionPane.showMessageDialog(FineManagementPanel.this, "Denda untuk peminjaman " + loanId + " ditandai lunas, buku dikembalikan, dan peminjaman dihapus!", "Sukses", JOptionPane.INFORMATION_MESSAGE);
                                    updateTable(); // Perbarui tabel denda
                                    libraryUI.updateAllPanels(); // Perbarui semua panel
                                });
                            } else {
                                JOptionPane.showMessageDialog(FineManagementPanel.this, "Peminjaman tidak ditemukan! Mungkin data tidak sinkron.", "Error", JOptionPane.ERROR_MESSAGE);
                            }
                        }
                    } else {
                        JOptionPane.showMessageDialog(FineManagementPanel.this, "Pilih denda yang akan ditandai lunas.", "Peringatan", JOptionPane.WARNING_MESSAGE);
                    }
                    stopCellEditing();
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

        updateTable(); // Panggil updateTable untuk mengisi data awal
        System.out.println("Tabel manajemen denda dimuat dengan " + tableModel.getRowCount() + " peminjaman.");
    }

    // Metode untuk menambahkan denda. Kini memanggil saveFineToDatabase di DataStorage
    public void addFine(Loan loan, long additionalDays) {
        LocalDate loanDate = loan.getLoanDate();
        LocalDate currentDate = LocalDate.now();
        // Pastikan daysLate tidak negatif
        long daysLate = Math.max(0, ChronoUnit.DAYS.between(loanDate, currentDate) - 7); 
        daysLate += additionalDays; // Tambahan hari jika ada

        long fineAmount = 5000 + (daysLate * 5000); // 5000 + (hari terlambat * 5000)

        Fine fine = new Fine(
            loan.getLoanId(),
            loan.getBook().getTitle(),
            loan.getBook().getType(),
            loan.getBorrowerName(),
            loan.getClassName(),
            loan.getNim(),
            loanDate,
            daysLate,
            fineAmount
        );
        dataStorage.saveFineToDatabase(fine); // Panggil metode DataStorage
        System.out.println("Denda ditambahkan untuk peminjaman " + loan.getLoanId() + ": " + fineAmount + " (Hari Terlambat: " + daysLate + ")");
        updateTable(); // Perbarui tampilan tabel setelah menambahkan denda
    }

    // Ubah loadFinesFromDatabase untuk menggunakan DataStorage
    private void loadFinesFromDatabase() {
        fines.clear();
        // Ambil daftar denda dari DataStorage
        fines.addAll(dataStorage.getFines());
        System.out.println("Memuat denda dari DataStorage: " + fines.size() + " denda ditemukan.");
    }

  
    private void updateTable() {
        tableModel.setRowCount(0);
        loadFinesFromDatabase(); // Muat denda terbaru
        int rowNumber = 1;
        for (Fine fine : fines) {
            tableModel.addRow(new Object[]{
                rowNumber++,
                fine.getBorrowerName(),
                fine.getClassName() != null ? fine.getClassName() : "N/A",
                fine.getNim() != null ? fine.getNim() : "N/A",
                fine.getLoanId(),
                fine.getBookTitle(),
                fine.getBookType(),
                fine.getLoanDate().toString(),
                fine.getDaysLate(),
                fine.getFineAmount(),
                "Tandai Lunas"
            });
        }
        System.out.println("Tabel denda diperbarui dengan " + tableModel.getRowCount() + " entri.");
        table.revalidate();
        table.repaint();
    }

    public void refresh() {
        SwingUtilities.invokeLater(() -> {
            System.out.println("Menyegarkan FineManagementPanel...");
            updateTable();
            table.clearSelection();
            revalidate();
            repaint();
        });
    }

    // Kelas Fine tetap sama
    public static class Fine {
        private String loanId;
        private String bookTitle;
        private String bookType;
        private String borrowerName;
        private String className;
        private String nim;
        private LocalDate loanDate;
        private long daysLate;
        private long fineAmount;

        public Fine(String loanId, String bookTitle, String bookType, String borrowerName, String className, String nim, LocalDate loanDate, long daysLate, long fineAmount) {
            this.loanId = loanId;
            this.bookTitle = bookTitle;
            this.bookType = bookType;
            this.borrowerName = borrowerName;
            this.className = className;
            this.nim = nim;
            this.loanDate = loanDate;
            this.daysLate = daysLate;
            this.fineAmount = fineAmount;
        }

        public String getLoanId() { return loanId; }
        public String getBookTitle() { return bookTitle; }
        public String getBookType() { return bookType; }
        public String getBorrowerName() { return borrowerName; }
        public String getClassName() { return className; }
        public String getNim() { return nim; }
        public LocalDate getLoanDate() { return loanDate; }
        public long getDaysLate() { return daysLate; }
        public long getFineAmount() { return fineAmount; }
    }
}
