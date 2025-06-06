package ui;

import model.Loan;
import storage.DataStorage;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Map;
import java.util.stream.Collectors;

public class StatisticsPanel extends JPanel {
    private DataStorage dataStorage;
    private JLabel totalLabel;
    private JLabel activeLabel;
    private JLabel returnedLabel;

    public StatisticsPanel(DataStorage dataStorage) {
        this.dataStorage = dataStorage;
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
                GradientPaint gp = new GradientPaint(0, 0, new Color(156, 39, 176), getWidth(), 0, new Color(186, 104, 200));
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        headerPanel.setPreferredSize(new Dimension(getWidth(), 80));
        headerPanel.setLayout(new BorderLayout());
        JLabel titleLabel = new JLabel("Statistik Peminjaman", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titleLabel.setForeground(Color.WHITE);
        headerPanel.add(titleLabel, BorderLayout.CENTER);
        add(headerPanel, BorderLayout.NORTH);

        // Panel statistik ringkas
        JPanel statsPanel = new JPanel(new GridLayout(1, 3, 10, 10));
        statsPanel.setBackground(new Color(245, 247, 250));
        statsPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

        long totalLoans = dataStorage.getLoans().size();
        long activeLoans = dataStorage.getLoans().stream().filter(loan -> !loan.isReturned()).count();
        long returnedLoans = totalLoans - activeLoans;

        // Total Peminjaman
        JPanel totalPanel = new JPanel(new BorderLayout());
        totalPanel.setBackground(new Color(156, 39, 176));
        totalLabel = new JLabel("Total Peminjaman: " + totalLoans, SwingConstants.CENTER);
        totalLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        totalLabel.setForeground(Color.WHITE);
        totalLabel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)));
        totalLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        totalLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                showLoanDetails(dataStorage.getLoans(), "Semua Peminjaman");
            }
            @Override
            public void mouseEntered(MouseEvent e) {
                totalLabel.setForeground(new Color(255, 255, 255));
                totalPanel.setBackground(new Color(186, 104, 200));
            }
            @Override
            public void mouseExited(MouseEvent e) {
                totalLabel.setForeground(Color.WHITE);
                totalPanel.setBackground(new Color(156, 39, 176));
            }
        });
        totalPanel.add(totalLabel, BorderLayout.CENTER);
        statsPanel.add(totalPanel);

        // Sedang Dipinjam
        JPanel activePanel = new JPanel(new BorderLayout());
        activePanel.setBackground(new Color(156, 39, 176));
        activeLabel = new JLabel("Sedang Dipinjam: " + activeLoans, SwingConstants.CENTER);
        activeLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        activeLabel.setForeground(Color.WHITE);
        activeLabel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)));
        activeLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        activeLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                showLoanDetails(dataStorage.getLoans().stream().filter(loan -> !loan.isReturned()).toList(), "Peminjaman Aktif");
            }
            @Override
            public void mouseEntered(MouseEvent e) {
                activeLabel.setForeground(new Color(255, 255, 255));
                activePanel.setBackground(new Color(186, 104, 200));
            }
            @Override
            public void mouseExited(MouseEvent e) {
                activeLabel.setForeground(Color.WHITE);
                activePanel.setBackground(new Color(156, 39, 176));
            }
        });
        activePanel.add(activeLabel, BorderLayout.CENTER);
        statsPanel.add(activePanel);

        // Sudah Dikembalikan
        JPanel returnedPanel = new JPanel(new BorderLayout());
        returnedPanel.setBackground(new Color(156, 39, 176));
        returnedLabel = new JLabel("Sudah Dikembalikan: " + returnedLoans, SwingConstants.CENTER);
        returnedLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        returnedLabel.setForeground(Color.WHITE);
        returnedLabel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)));
        returnedLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        returnedLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                showLoanDetails(dataStorage.getLoans().stream().filter(Loan::isReturned).toList(), "Peminjaman Selesai");
            }
            @Override
            public void mouseEntered(MouseEvent e) {
                returnedLabel.setForeground(new Color(255, 255, 255));
                returnedPanel.setBackground(new Color(186, 104, 200));
            }
            @Override
            public void mouseExited(MouseEvent e) {
                returnedLabel.setForeground(Color.WHITE);
                returnedPanel.setBackground(new Color(156, 39, 176));
            }
        });
        returnedPanel.add(returnedLabel, BorderLayout.CENTER);
        statsPanel.add(returnedPanel);

        // Tabbed Pane untuk statistik terperinci
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Buku Populer", createPopularBooksPanel());
        tabbedPane.addTab("Peminjaman Aktif", createActiveLoansPanel());
        tabbedPane.addTab("Rekam Jejak Siswa", createStudentHistoryPanel());

        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBackground(new Color(245, 247, 250));
        centerPanel.add(statsPanel, BorderLayout.NORTH);
        centerPanel.add(tabbedPane, BorderLayout.CENTER);

        add(centerPanel, BorderLayout.CENTER);

        System.out.println("Panel statistik dimuat.");
    }

    private void showLoanDetails(java.util.List<Loan> loans, String title) {
        if (loans.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Tidak ada data peminjaman.", "Info", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        String[] columns = {"No", "ID Peminjaman", "Judul Buku", "Tipe Buku", "Peminjam", "Tanggal Pinjam", "Tanggal Kembali"};
        DefaultTableModel tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        JTable table = new JTable(tableModel);
        table.setFillsViewportHeight(true);
        table.setRowHeight(35);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.setSelectionBackground(new Color(225, 190, 231));
        table.setSelectionForeground(Color.BLACK);
        table.setGridColor(new Color(220, 220, 220));
        table.setShowGrid(true);

        JTableHeader tableHeader = table.getTableHeader();
        tableHeader.setFont(new Font("Segoe UI", Font.BOLD, 15));
        tableHeader.setBackground(new Color(156, 39, 176));
        tableHeader.setForeground(Color.WHITE);
        tableHeader.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));

        int rowNumber = 1;
        for (Loan loan : loans) {
            tableModel.addRow(new Object[]{
                    rowNumber++,
                    loan.getLoanId(),
                    loan.getBook().getTitle(),
                    loan.getBook().getType(),
                    loan.getBorrowerName(),
                    loan.getLoanDate().toString(),
                    loan.getReturnDate() != null ? loan.getReturnDate().toString() : "-"
            });
        }

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setPreferredSize(new Dimension(600, 400));
        JOptionPane.showMessageDialog(this, scrollPane, title, JOptionPane.INFORMATION_MESSAGE);
    }

    private JPanel createPopularBooksPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(245, 247, 250));

        String[] columns = {"No", "Judul Buku", "Jumlah Peminjaman"};
        DefaultTableModel tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        JTable table = new JTable(tableModel);
        table.setFillsViewportHeight(true);
        table.setRowHeight(35);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.setSelectionBackground(new Color(225, 190, 231));
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
        tableHeader.setFont(new Font("Segoe UI", Font.BOLD, 15));
        tableHeader.setBackground(new Color(156, 39, 176));
        tableHeader.setForeground(Color.WHITE);
        tableHeader.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));
        panel.add(scrollPane, BorderLayout.CENTER);

        // Hitung buku populer berdasarkan jumlah peminjaman
        Map<String, Long> bookLoanCount = dataStorage.getLoans().stream()
                .collect(Collectors.groupingBy(
                        loan -> loan.getBook().getTitle(),
                        Collectors.counting()
                ));

        // Gunakan loop tradisional untuk menghindari masalah lambda
        java.util.List<Map.Entry<String, Long>> entries = new ArrayList<>(bookLoanCount.entrySet());
        entries.sort(Map.Entry.<String, Long>comparingByValue().reversed());
        int rowNumber = 1;
        for (int i = 0; i < Math.min(5, entries.size()); i++) {
            Map.Entry<String, Long> entry = entries.get(i);
            tableModel.addRow(new Object[]{rowNumber++, entry.getKey(), entry.getValue()});
        }

        return panel;
    }

    private JPanel createActiveLoansPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(245, 247, 250));

        String[] columns = {"No", "ID Peminjaman", "Judul Buku", "Tipe Buku", "Peminjam", "Tanggal Pinjam"};
        DefaultTableModel tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        JTable table = new JTable(tableModel);
        table.setFillsViewportHeight(true);
        table.setRowHeight(35);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.setSelectionBackground(new Color(225, 190, 231));
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
        tableHeader.setFont(new Font("Segoe UI", Font.BOLD, 15));
        tableHeader.setBackground(new Color(156, 39, 176));
        tableHeader.setForeground(Color.WHITE);
        tableHeader.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));
        panel.add(scrollPane, BorderLayout.CENTER);

        int rowNumber = 1;
        for (Loan loan : dataStorage.getLoans()) {
            if (!loan.isReturned()) {
                tableModel.addRow(new Object[]{
                        rowNumber++,
                        loan.getLoanId(),
                        loan.getBook().getTitle(),
                        loan.getBook().getType(),
                        loan.getBorrowerName(),
                        loan.getLoanDate().toString()
                });
            }
        }

        return panel;
    }

    private JPanel createStudentHistoryPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(245, 247, 250));

        String[] columns = {"No", "Nama Peminjam", "Jumlah Peminjaman"};
        DefaultTableModel tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        JTable table = new JTable(tableModel);
        table.setFillsViewportHeight(true);
        table.setRowHeight(35);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.setSelectionBackground(new Color(225, 190, 231));
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
        tableHeader.setFont(new Font("Segoe UI", Font.BOLD, 15));
        tableHeader.setBackground(new Color(156, 39, 176));
        tableHeader.setForeground(Color.WHITE);
        tableHeader.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));
        panel.add(scrollPane, BorderLayout.CENTER);

        // Hitung rekam jejak siswa berdasarkan jumlah peminjaman
        Map<String, Long> studentLoanCount = dataStorage.getLoans().stream()
                .collect(Collectors.groupingBy(
                        loan -> loan.getBorrowerName(),
                        Collectors.counting()
                ));

        // Gunakan loop tradisional untuk menghindari masalah lambda
        java.util.List<Map.Entry<String, Long>> entries = new ArrayList<>(studentLoanCount.entrySet());
        entries.sort(Map.Entry.<String, Long>comparingByValue().reversed());
        int rowNumber = 1;
        for (Map.Entry<String, Long> entry : entries) {
            tableModel.addRow(new Object[]{rowNumber++, entry.getKey(), entry.getValue()});
        }

        return panel;
    }

    public void refresh() {
        long totalLoans = dataStorage.getLoans().size();
        long activeLoans = dataStorage.getLoans().stream().filter(loan -> !loan.isReturned()).count();
        long returnedLoans = totalLoans - activeLoans;

        totalLabel.setText("Total Peminjaman: " + totalLoans);
        activeLabel.setText("Sedang Dipinjam: " + activeLoans);
        returnedLabel.setText("Sudah Dikembalikan: " + returnedLoans);

        revalidate();
        repaint();
    }
}