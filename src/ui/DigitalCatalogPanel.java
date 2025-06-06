package ui;

import model.Book;
import storage.DataStorage;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class DigitalCatalogPanel extends JPanel {
    private DataStorage dataStorage;
    private DefaultTableModel tableModel;
    private JTable table;
    private CardLayout cardLayout;
    private JPanel cardPanel;
    private JDialog readDialog;
    private JTextArea textArea;
    private int currentPage = 1;
    private final int totalPages = 10; // Simulasi jumlah halaman

    public DigitalCatalogPanel(DataStorage dataStorage, CardLayout cardLayout, JPanel cardPanel) {
        this.dataStorage = dataStorage;
        this.cardLayout = cardLayout;
        this.cardPanel = cardPanel;
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
                GradientPaint gp = new GradientPaint(0, 0, new Color(33, 150, 243), getWidth(), 0, new Color(66, 165, 245));
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        headerPanel.setPreferredSize(new Dimension(getWidth(), 80));
        headerPanel.setLayout(new BorderLayout());
        JLabel titleLabel = new JLabel("Katalog Buku Digital", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titleLabel.setForeground(Color.WHITE);
        headerPanel.add(titleLabel, BorderLayout.CENTER);
        add(headerPanel, BorderLayout.NORTH);

        // Tabel katalog digital dengan kolom "No"
        String[] columns = {"No", "ID", "Judul", "Penulis", "Tahun Terbit", "Subjek", "Status", "Aksi"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 7; // Hanya kolom "Aksi" yang dapat diedit
            }
        };
        table = new JTable(tableModel);
        table.setFillsViewportHeight(true);
        table.setRowHeight(35);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.setSelectionBackground(new Color(200, 220, 255));
        table.setSelectionForeground(Color.BLACK);
        table.setGridColor(new Color(220, 220, 220));
        table.setShowGrid(true);

        // Efek hover pada baris
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
        tableHeader.setBackground(new Color(33, 150, 243));
        tableHeader.setForeground(Color.WHITE);
        tableHeader.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));

        // Kolom Aksi dengan tombol "Baca"
        table.getColumnModel().getColumn(7).setCellRenderer((table, value, isSelected, hasFocus, row, column) -> {
            JButton button = new JButton("Baca");
            button.setFont(new Font("Segoe UI", Font.BOLD, 12));
            button.setBackground(new Color(33, 150, 243));
            button.setForeground(Color.WHITE);
            button.setFocusPainted(false);
            button.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
            button.setCursor(new Cursor(Cursor.HAND_CURSOR));
            button.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    button.setBackground(new Color(66, 165, 245));
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    button.setBackground(new Color(33, 150, 243));
                }
            });
            return button;
        });

        table.getColumnModel().getColumn(7).setCellEditor(new DefaultCellEditor(new JCheckBox()) {
            @Override
            public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
                JButton button = new JButton("Baca");
                button.setFont(new Font("Segoe UI", Font.BOLD, 12));
                button.setBackground(new Color(33, 150, 243));
                button.setForeground(Color.WHITE);
                button.setFocusPainted(false);
                button.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
                button.setCursor(new Cursor(Cursor.HAND_CURSOR));
                button.addActionListener(e -> {
                    String bookTitle = (String) table.getValueAt(row, 2);
                    showReadMode(bookTitle);
                });
                return button;
            }
        });

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));
        add(scrollPane, BorderLayout.CENTER);

        // Memuat semua buku saat panel dibuka
        updateTable();
        System.out.println("Tabel katalog digital dimuat dengan " + tableModel.getRowCount() + " buku.");
    }

    public void refresh() {
        System.out.println("Menyegarkan DigitalCatalogPanel...");
        updateTable();
        revalidate();
        repaint();
    }

    private void updateTable() {
        tableModel.setRowCount(0);
        int rowNumber = 1;
        for (Book book : dataStorage.getBooks()) {
            if (book.isDigital()) {
                tableModel.addRow(new Object[]{
                        rowNumber++,
                        book.getId(),
                        book.getTitle(),
                        book.getAuthor(),
                        book.getPublicationYear(),
                        book.getSubject().isEmpty() ? "-" : book.getSubject(),
                        book.isBorrowed() ? "Dipinjam" : "Tersedia",
                        "Baca"
                });
            }
        }
    }

    private void showReadMode(String bookTitle) {
        if (readDialog == null) {
            readDialog = new JDialog((JFrame) SwingUtilities.getWindowAncestor(this), "Mode Baca - " + bookTitle, true);
            readDialog.setSize(900, 600);
            readDialog.setLocationRelativeTo(this);

            JPanel readPanel = new JPanel(new BorderLayout());
            readPanel.setBackground(new Color(245, 247, 250));
            readPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

            // Header dengan gradien
            JPanel readHeaderPanel = new JPanel() {
                @Override
                protected void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    Graphics2D g2d = (Graphics2D) g;
                    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    GradientPaint gp = new GradientPaint(0, 0, new Color(33, 150, 243), getWidth(), 0, new Color(66, 165, 245));
                    g2d.setPaint(gp);
                    g2d.fillRect(0, 0, getWidth(), getHeight());
                }
            };
            readHeaderPanel.setPreferredSize(new Dimension(getWidth(), 60));
            readHeaderPanel.setLayout(new BorderLayout());
            JLabel readTitleLabel = new JLabel("Membaca: " + bookTitle, SwingConstants.CENTER);
            readTitleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
            readTitleLabel.setForeground(Color.WHITE);
            readHeaderPanel.add(readTitleLabel, BorderLayout.CENTER);

            // Konten baca (simulasi teks buku)
            textArea = new JTextArea();
            textArea.setFont(new Font("Times New Roman", Font.PLAIN, 16));
            textArea.setEditable(false);
            textArea.setBackground(new Color(255, 255, 240));
            textArea.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(200, 200, 200)),
                    BorderFactory.createEmptyBorder(15, 15, 15, 15)));
            textArea.setLineWrap(true);
            textArea.setWrapStyleWord(true);
            updatePageContent();

            JScrollPane scrollPane = new JScrollPane(textArea);
            scrollPane.setBorder(BorderFactory.createEmptyBorder());

            // Panel navigasi halaman
            JPanel navPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
            navPanel.setBackground(new Color(245, 247, 250));

            JButton prevButton = new JButton("Sebelumnya");
            prevButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
            prevButton.setBackground(new Color(33, 150, 243));
            prevButton.setForeground(Color.WHITE);
            prevButton.setFocusPainted(false);
            prevButton.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
            prevButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
            prevButton.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    prevButton.setBackground(new Color(66, 165, 245));
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    prevButton.setBackground(new Color(33, 150, 243));
                }
            });
            prevButton.addActionListener(e -> {
                if (currentPage > 1) {
                    currentPage--;
                    updatePageContent();
                }
            });

            JLabel pageLabel = new JLabel("Halaman " + currentPage + " dari " + totalPages);
            pageLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));

            JButton nextButton = new JButton("Berikutnya");
            nextButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
            nextButton.setBackground(new Color(33, 150, 243));
            nextButton.setForeground(Color.WHITE);
            nextButton.setFocusPainted(false);
            nextButton.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
            nextButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
            nextButton.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    nextButton.setBackground(new Color(66, 165, 245));
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    nextButton.setBackground(new Color(33, 150, 243));
                }
            });
            nextButton.addActionListener(e -> {
                if (currentPage < totalPages) {
                    currentPage++;
                    updatePageContent();
                }
            });

            navPanel.add(prevButton);
            navPanel.add(pageLabel);
            navPanel.add(nextButton);

            JButton closeButton = new JButton("Tutup");
            closeButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
            closeButton.setBackground(new Color(33, 150, 243));
            closeButton.setForeground(Color.WHITE);
            closeButton.setFocusPainted(false);
            closeButton.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
            closeButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
            closeButton.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    closeButton.setBackground(new Color(66, 165, 245));
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    closeButton.setBackground(new Color(33, 150, 243));
                }
            });
            closeButton.addActionListener(e -> readDialog.dispose());

            readPanel.add(readHeaderPanel, BorderLayout.NORTH);
            readPanel.add(scrollPane, BorderLayout.CENTER);
            readPanel.add(navPanel, BorderLayout.SOUTH);
            readPanel.add(closeButton, BorderLayout.SOUTH);

            readDialog.add(readPanel);
        }
        readDialog.setVisible(true);
    }

    private void updatePageContent() {
        StringBuilder content = new StringBuilder();
        content.append("** ").append(currentPage).append(". Bab ").append(currentPage).append(": Pengenalan **\n\n");
        content.append("Ini adalah simulasi isi buku digital. Berikut adalah teks placeholder untuk halaman ").append(currentPage).append(":\n\n");
        for (int i = 0; i < 5; i++) {
            content.append("Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. ")
                   .append("Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.\n\n");
        }
        textArea.setText(content.toString());
        textArea.setCaretPosition(0); // Kembali ke atas
    }
}