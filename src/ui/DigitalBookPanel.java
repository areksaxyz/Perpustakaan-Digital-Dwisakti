package ui;

import model.Book;
import storage.DataStorage;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

public class DigitalBookPanel extends JPanel {
    private DataStorage dataStorage;
    private DefaultTableModel tableModel;
    private JTable table;
    private JTextField idField;
    private JTextField titleField;
    private JTextField authorField;
    private JTextField yearField;
    private JTextField urlField;
    private JTextField subjectField;
    private CardLayout cardLayout;
    private JPanel cardPanel;

    public DigitalBookPanel(DataStorage dataStorage, CardLayout cardLayout, JPanel cardPanel) {
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
        JLabel titleLabel = new JLabel("Manajemen Buku Digital", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titleLabel.setForeground(Color.WHITE);
        headerPanel.add(titleLabel, BorderLayout.CENTER);
        add(headerPanel, BorderLayout.NORTH);

        // Form input
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(new Color(245, 247, 250));
        formPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)), "Tambah Buku Digital", 0, 0, new Font("Segoe UI", Font.BOLD, 16), new Color(33, 150, 243)));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Label dan field (dengan ID)
        JLabel idLabel = new JLabel("ID Buku:");
        idLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        idField = new JTextField(15);
        idField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        idField.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 1, true));

        JLabel titleLabel2 = new JLabel("Judul:");
        titleLabel2.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        titleField = new JTextField(15);
        titleField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        titleField.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 1, true));

        JLabel authorLabel = new JLabel("Penulis:");
        authorLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        authorField = new JTextField(15);
        authorField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        authorField.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 1, true));

        JLabel yearLabel = new JLabel("Tahun Terbit:");
        yearLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        yearField = new JTextField(15);
        yearField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        yearField.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 1, true));

        JLabel urlLabel = new JLabel("URL:");
        urlLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        urlField = new JTextField(15);
        urlField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        urlField.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 1, true));

        JLabel subjectLabel = new JLabel("Subjek:");
        subjectLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subjectField = new JTextField(15);
        subjectField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subjectField.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 1, true));

        JButton addButton = new JButton("Tambah Buku");
        addButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        addButton.setBackground(new Color(33, 150, 243));
        addButton.setForeground(Color.WHITE);
        addButton.setFocusPainted(false);
        addButton.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        addButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        addButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                addButton.setBackground(new Color(66, 165, 245));
            }
            @Override
            public void mouseExited(MouseEvent e) {
                addButton.setBackground(new Color(33, 150, 243));
            }
        });

        // Posisi komponen dalam form (dengan ID)
        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(idLabel, gbc);
        gbc.gridx = 1;
        formPanel.add(idField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(titleLabel2, gbc);
        gbc.gridx = 1;
        formPanel.add(titleField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        formPanel.add(authorLabel, gbc);
        gbc.gridx = 1;
        formPanel.add(authorField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        formPanel.add(yearLabel, gbc);
        gbc.gridx = 1;
        formPanel.add(yearField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        formPanel.add(urlLabel, gbc);
        gbc.gridx = 1;
        formPanel.add(urlField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 5;
        formPanel.add(subjectLabel, gbc);
        gbc.gridx = 1;
        formPanel.add(subjectField, gbc);

        gbc.gridx = 1;
        gbc.gridy = 6;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.EAST;
        formPanel.add(addButton, gbc);

        // Panel tengah dengan form dan tabel
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBackground(new Color(245, 247, 250));
        centerPanel.add(formPanel, BorderLayout.NORTH);

        // Tabel buku digital dengan kolom "No", "Baca", dan "Hapus"
        String[] columns = {"No", "ID", "Judul", "Penulis", "Tahun Terbit", "Subjek", "URL", "Baca", "Hapus"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 7 || column == 8; // Kolom "Baca" dan "Hapus" yang dapat diedit
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
                if (row >= 0 && row < tableModel.getRowCount()) {
                    table.setRowSelectionInterval(row, row);
                } else {
                    table.clearSelection();
                }
            }
        });

        // Kolom Baca
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
                if (row < 0 || row >= tableModel.getRowCount()) return new JLabel();
                JButton button = new JButton("Baca");
                button.setFont(new Font("Segoe UI", Font.BOLD, 12));
                button.setBackground(new Color(33, 150, 243));
                button.setForeground(Color.WHITE);
                button.setFocusPainted(false);
                button.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
                button.setCursor(new Cursor(Cursor.HAND_CURSOR));
                button.addActionListener(e -> {
                    // Hentikan pengeditan sebelum mengakses data
                    if (table.getCellEditor() != null) {
                        table.getCellEditor().stopCellEditing();
                    }
                    // Pastikan baris masih valid setelah penghentian pengeditan
                    if (row < 0 || row >= tableModel.getRowCount()) return;
                    String bookId = (String) table.getValueAt(row, 1);
                    Book book = dataStorage.getBooks().stream()
                            .filter(b -> b.getId().equals(bookId))
                            .findFirst()
                            .orElse(null);
                    if (book != null && "Digital".equals(book.getType()) && !book.getUrl().isEmpty()) {
                        JDialog loadingDialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(DigitalBookPanel.this), "Memuat...", false);
                        loadingDialog.setLayout(new BorderLayout());
                        JLabel loadingLabel = new JLabel("Memuat dokumen, harap tunggu...", SwingConstants.CENTER);
                        loadingDialog.add(loadingLabel, BorderLayout.CENTER);
                        loadingDialog.setSize(300, 100);
                        loadingDialog.setLocationRelativeTo(DigitalBookPanel.this);
                        loadingDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

                        new Thread(() -> {
                            try {
                                System.out.println("Mengakses URL: " + book.getUrl());
                                URL url = new URL(book.getUrl());
                                URLConnection connection = url.openConnection();
                                connection.setConnectTimeout(10000);
                                connection.setReadTimeout(10000);
                                InputStream in = connection.getInputStream();
                                PDDocument document = PDDocument.load(in);
                                System.out.println("PDF berhasil dimuat, jumlah halaman: " + document.getNumberOfPages());
                                SwingUtilities.invokeLater(() -> {
                                    PDFViewerFrame viewer = new PDFViewerFrame(book.getTitle(), document, in);
                                    viewer.setVisible(true);
                                    loadingDialog.dispose();
                                });
                            } catch (Exception ex) {
                                System.err.println("Gagal membuka atau merender PDF: " + ex.getClass().getName() + " - " + ex.getMessage());
                                ex.printStackTrace();
                                SwingUtilities.invokeLater(() -> {
                                    JOptionPane.showMessageDialog(DigitalBookPanel.this, "Gagal membuka atau merender PDF: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                                    loadingDialog.dispose();
                                });
                            }
                        }).start();

                        loadingDialog.setVisible(true);
                    } else {
                        JOptionPane.showMessageDialog(DigitalBookPanel.this, "URL tidak tersedia atau buku bukan digital!", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                });
                return button;
            }
        });

        // Kolom Hapus
        table.getColumnModel().getColumn(8).setCellRenderer((table, value, isSelected, hasFocus, row, column) -> {
            JButton button = new JButton("Hapus");
            button.setFont(new Font("Segoe UI", Font.BOLD, 12));
            button.setBackground(new Color(255, 82, 82));
            button.setForeground(Color.WHITE);
            button.setFocusPainted(false);
            button.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
            button.setCursor(new Cursor(Cursor.HAND_CURSOR));
            button.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    button.setBackground(new Color(255, 99, 99));
                }
                @Override
                public void mouseExited(MouseEvent e) {
                    button.setBackground(new Color(255, 82, 82));
                }
            });
            return button;
        });

        table.getColumnModel().getColumn(8).setCellEditor(new DefaultCellEditor(new JCheckBox()) {
            @Override
            public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
                if (row < 0 || row >= tableModel.getRowCount()) return new JLabel();
                JButton button = new JButton("Hapus");
                button.setFont(new Font("Segoe UI", Font.BOLD, 12));
                button.setBackground(new Color(255, 82, 82));
                button.setForeground(Color.WHITE);
                button.setFocusPainted(false);
                button.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
                button.setCursor(new Cursor(Cursor.HAND_CURSOR));
                button.addActionListener(e -> {
                    // Hentikan pengeditan sebelum mengakses data
                    if (table.getCellEditor() != null) {
                        table.getCellEditor().stopCellEditing();
                    }
                    // Pastikan baris masih valid setelah penghentian pengeditan
                    if (row < 0 || row >= tableModel.getRowCount()) return;
                    String bookId = (String) table.getValueAt(row, 1);

                    // Dialog untuk memasukkan password
                    JPasswordField passwordField = new JPasswordField();
                    int option = JOptionPane.showConfirmDialog(null, 
                            new Object[]{"Masukkan Password:", passwordField}, 
                            "Autentikasi Penghapusan", 
                            JOptionPane.OK_CANCEL_OPTION);
                    if (option == JOptionPane.OK_OPTION) {
                        String inputPassword = new String(passwordField.getPassword());
                        if ("admin1234".equals(inputPassword)) {
                            if (JOptionPane.showConfirmDialog(null, "Apakah Anda yakin ingin menghapus buku ini?", "Konfirmasi Hapus", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                                dataStorage.getBooks().removeIf(book -> book.getId().equals(bookId));
                                dataStorage.saveBooks();
                                updateTable();
                            }
                        } else {
                            JOptionPane.showMessageDialog(null, "Password salah! Penghapusan dibatalkan.", "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                });
                return button;
            }
        });

        JTableHeader tableHeader = table.getTableHeader();
        tableHeader.setFont(new Font("Segoe UI", Font.BOLD, 15));
        tableHeader.setBackground(new Color(33, 150, 243));
        tableHeader.setForeground(Color.WHITE);
        tableHeader.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));
        centerPanel.add(scrollPane, BorderLayout.CENTER);

        add(centerPanel, BorderLayout.CENTER);

        // Memuat semua buku saat panel dibuka
        updateTable();
        System.out.println("Tabel buku digital dimuat dengan " + tableModel.getRowCount() + " buku.");

        // Aksi tombol Tambah Buku
        addButton.addActionListener(e -> {
            String id = idField.getText().trim();
            String title = titleField.getText().trim();
            String author = authorField.getText().trim();
            String year = yearField.getText().trim();
            String url = urlField.getText().trim();
            String subject = subjectField.getText().trim();

            if (id.isEmpty() || title.isEmpty() || author.isEmpty() || year.isEmpty() || url.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Semua kolom wajib diisi!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Validasi ID unik
            if (dataStorage.getBooks().stream().anyMatch(book -> book.getId().equals(id))) {
                JOptionPane.showMessageDialog(this, "ID Buku sudah ada! Gunakan ID lain.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                int publicationYear = Integer.parseInt(year);
                Book book = new Book(id, title, author, publicationYear, "Digital", url, subject);
                dataStorage.getBooks().add(book);
                dataStorage.saveBooks();
                updateTable();
                clearFields();
                JOptionPane.showMessageDialog(this, "Buku digital berhasil ditambahkan dengan ID: " + id, "Sukses", JOptionPane.INFORMATION_MESSAGE);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Tahun harus berupa angka!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    public void refresh() {
        System.out.println("Menyegarkan DigitalBookPanel...");
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
                        book.getUrl(),
                        "Baca",
                        "Hapus"
                });
            }
        }
    }

    private void clearFields() {
        idField.setText("");
        titleField.setText("");
        authorField.setText("");
        yearField.setText("");
        urlField.setText("");
        subjectField.setText("");
    }

    // Kelas internal untuk menampilkan PDF dengan gambar halaman nyata
    private static class PDFViewerFrame extends JFrame {
        private PDDocument document;
        private PDFRenderer renderer;
        private JLabel pageLabel;
        private JScrollPane scrollPane;
        private int currentPage;
        private int totalPages;
        private JButton prevButton;
        private JButton nextButton;
        private JButton zoomInButton;
        private JButton zoomOutButton;
        private JButton goButton;
        private JTextField pageInputField;
        private JButton closeButton;
        private Map<Integer, BufferedImage> pageCache;
        private double zoomLevel;
        private InputStream inputStream;
        private AtomicBoolean isDocumentClosed;

        public PDFViewerFrame(String title, PDDocument document, InputStream inputStream) {
            this.document = document;
            this.renderer = new PDFRenderer(document);
            this.isDocumentClosed = new AtomicBoolean(false);
            this.pageCache = new HashMap<>();
            this.currentPage = 1;
            this.totalPages = document.getNumberOfPages();
            this.zoomLevel = 1.0;
            this.inputStream = inputStream;

            System.out.println("Membuat PDFViewerFrame untuk: " + title);

            setTitle(title);
            setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            setLayout(new BorderLayout());

            // Panel header biru untuk navigasi dan kontrol
            JPanel headerPanel = new JPanel();
            headerPanel.setBackground(new Color(33, 150, 243));
            headerPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
            headerPanel.setPreferredSize(new Dimension(getWidth(), 40));

            JLabel pageInfoLabel = new JLabel("Halaman " + currentPage + " / " + totalPages);
            pageInfoLabel.setForeground(Color.WHITE);
            pageInfoLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));

            prevButton = new JButton("Sebelumnya");
            prevButton.setFont(new Font("Segoe UI", Font.BOLD, 12));
            prevButton.setBackground(new Color(255, 255, 0));
            prevButton.setForeground(Color.BLACK);
            prevButton.setFocusPainted(false);
            prevButton.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
            prevButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
            prevButton.addActionListener(e -> {
                if (currentPage > 1) {
                    currentPage--;
                    updateContent();
                }
            });
            prevButton.setEnabled(false);

            nextButton = new JButton("Selanjutnya");
            nextButton.setFont(new Font("Segoe UI", Font.BOLD, 12));
            nextButton.setBackground(new Color(255, 255, 0));
            nextButton.setForeground(Color.BLACK);
            nextButton.setFocusPainted(false);
            nextButton.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
            nextButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
            nextButton.addActionListener(e -> {
                if (currentPage < totalPages) {
                    currentPage++;
                    updateContent();
                }
            });
            nextButton.setEnabled(totalPages > 1);

            zoomInButton = new JButton("Zoom In");
            zoomInButton.setFont(new Font("Segoe UI", Font.BOLD, 12));
            zoomInButton.setBackground(new Color(255, 255, 0));
            zoomInButton.setForeground(Color.BLACK);
            zoomInButton.setFocusPainted(false);
            zoomInButton.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
            zoomInButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
            zoomInButton.addActionListener(e -> {
                zoomLevel += 0.2;
                updateContent();
            });

            zoomOutButton = new JButton("Zoom Out");
            zoomOutButton.setFont(new Font("Segoe UI", Font.BOLD, 12));
            zoomOutButton.setBackground(new Color(255, 255, 0));
            zoomOutButton.setForeground(Color.BLACK);
            zoomOutButton.setFocusPainted(false);
            zoomOutButton.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
            zoomOutButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
            zoomOutButton.addActionListener(e -> {
                if (zoomLevel > 0.2) {
                    zoomLevel -= 0.2;
                    updateContent();
                }
            });

            pageInputField = new JTextField(5);
            pageInputField.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            pageInputField.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 1, true));

            goButton = new JButton("Go");
            goButton.setFont(new Font("Segoe UI", Font.BOLD, 12));
            goButton.setBackground(new Color(255, 255, 0));
            goButton.setForeground(Color.BLACK);
            goButton.setFocusPainted(false);
            goButton.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
            goButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
            goButton.addActionListener(e -> {
                try {
                    int pageNum = Integer.parseInt(pageInputField.getText());
                    if (pageNum >= 1 && pageNum <= totalPages) {
                        currentPage = pageNum;
                        updateContent();
                    } else {
                        JOptionPane.showMessageDialog(this, "Nomor halaman harus antara 1 dan " + totalPages + "!", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "Masukkan nomor halaman yang valid!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            });

            closeButton = new JButton("Tutup");
            closeButton.setFont(new Font("Segoe UI", Font.BOLD, 12));
            closeButton.setBackground(new Color(255, 255, 0));
            closeButton.setForeground(Color.BLACK);
            closeButton.setFocusPainted(false);
            closeButton.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
            closeButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
            closeButton.addActionListener(e -> dispose());

            headerPanel.add(pageInfoLabel);
            headerPanel.add(prevButton);
            headerPanel.add(nextButton);
            headerPanel.add(zoomInButton);
            headerPanel.add(zoomOutButton);
            headerPanel.add(new JLabel("Ke Halaman:"));
            headerPanel.add(pageInputField);
            headerPanel.add(goButton);
            headerPanel.add(closeButton);
            add(headerPanel, BorderLayout.NORTH);

            // ScrollPane untuk halaman (gambar)
            pageLabel = new JLabel("Memuat halaman...");
            pageLabel.setHorizontalAlignment(SwingConstants.CENTER);
            scrollPane = new JScrollPane(pageLabel);
            scrollPane.setBackground(Color.WHITE);
            scrollPane.getVerticalScrollBar().setUnitIncrement(16); // Muluskan scroll
            scrollPane.getHorizontalScrollBar().setUnitIncrement(16); // Muluskan scroll
            add(scrollPane, BorderLayout.CENTER);

            // Render halaman pertama langsung
            renderPage(currentPage);
            setPreferredSize(new Dimension(800, 600)); // Ukuran awal jendela
            pack();
            setLocationRelativeTo(null);
        }

        private void renderPage(int page) {
            try {
                BufferedImage image = renderer.renderImage(page - 1, 1.5f); // DPI 1.5 untuk ukuran lebih besar
                pageCache.put(page - 1, image);
                updateContent();
            } catch (IOException e) {
                System.err.println("Gagal merender halaman " + page + ": " + e.getMessage());
                pageLabel.setText("Gagal memuat halaman " + page);
            }
        }

        private void updateContent() {
            prevButton.setEnabled(currentPage > 1);
            nextButton.setEnabled(currentPage < totalPages);
            BufferedImage pageImage = pageCache.get(currentPage - 1);
            if (pageImage == null) {
                renderPage(currentPage);
                return;
            }
            int width = (int)(pageImage.getWidth() * zoomLevel);
            int height = (int)(pageImage.getHeight() * zoomLevel);
            Image scaled = pageImage.getScaledInstance(width, height, Image.SCALE_SMOOTH);
            BufferedImage scaledImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2d = scaledImage.createGraphics();
            g2d.drawImage(scaled, 0, 0, null);
            g2d.dispose();
            pageLabel.setIcon(new ImageIcon(scaledImage));
            pageLabel.setText(null);
            ((JLabel) ((JPanel) getContentPane().getComponent(0)).getComponent(0)).setText("Halaman " + currentPage + " / " + totalPages);
            // Reset posisi scroll ke atas
            scrollPane.getViewport().setViewPosition(new Point(0, 0));
            revalidate();
            repaint();
            pack();
        }

        @Override
        public void dispose() {
            System.out.println("PDFViewerFrame ditutup");
            if (!isDocumentClosed.get()) {
                try {
                    if (document != null) {
                        document.close();
                    }
                    if (inputStream != null) {
                        inputStream.close();
                    }
                    isDocumentClosed.set(true);
                    System.out.println("Dokumen PDF dan InputStream ditutup");
                } catch (IOException e) {
                    System.err.println("Gagal menutup dokumen PDF atau InputStream: " + e.getMessage());
                }
            }
            super.dispose();
        }
    }
}