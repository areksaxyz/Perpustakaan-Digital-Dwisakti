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
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.HashMap;
import java.util.Map;

public class CatalogPanel extends JPanel {
    private DataStorage dataStorage;
    private DefaultTableModel tableModel;
    private JTable table;
    private CardLayout cardLayout;
    private JPanel cardPanel;

    public CatalogPanel(DataStorage dataStorage, CardLayout cardLayout, JPanel cardPanel) {
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

        // Panel tengah dengan tabel
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBackground(new Color(245, 247, 250));

        // Tabel buku digital dengan kolom "No", "Judul", "Penulis", "Tahun Terbit", "Baca"
        String[] columns = {"No", "Judul", "Penulis", "Tahun Terbit", "Baca"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 4; 
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
        table.getColumnModel().getColumn(4).setCellRenderer((table, value, isSelected, hasFocus, row, column) -> {
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

        table.getColumnModel().getColumn(4).setCellEditor(new DefaultCellEditor(new JCheckBox()) {
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
                    if (table.getCellEditor() != null) {
                        table.getCellEditor().stopCellEditing();
                    }
                    if (row < 0 || row >= tableModel.getRowCount()) return;
                    String bookId = (String) table.getValueAt(row, 1); // Ambil ID dari kolom kedua
                    Book book = dataStorage.getBooks().stream()
                            .filter(b -> b.getId().equals(bookId))
                            .findFirst()
                            .orElse(null);
                    if (book != null && "Digital".equals(book.getType()) && !book.getUrl().isEmpty()) {
                        JDialog loadingDialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(CatalogPanel.this), "Memuat...", false);
                        loadingDialog.setLayout(new BorderLayout());
                        JLabel loadingLabel = new JLabel("Memuat dokumen, harap tunggu...", SwingConstants.CENTER);
                        loadingDialog.add(loadingLabel, BorderLayout.CENTER);
                        loadingDialog.setSize(300, 100);
                        loadingDialog.setLocationRelativeTo(CatalogPanel.this);
                        loadingDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

                        new Thread(() -> {
                            PDDocument document = null;
                            InputStream in = null;
                            try {
                                System.out.println("Mengakses URL: " + book.getUrl());
                                URL url = new URL(book.getUrl());
                                URLConnection connection = url.openConnection();
                                connection.setConnectTimeout(10000);
                                connection.setReadTimeout(10000);
                                in = connection.getInputStream();
                                document = PDDocument.load(in); // Gunakan PDDocument.load untuk PDFBox 2.0.27
                                System.out.println("PDF berhasil dimuat, jumlah halaman: " + document.getNumberOfPages());
                                final PDDocument finalDocument = document; // Membuat salinan final
                                final InputStream finalIn = in; // Membuat salinan final
                                SwingUtilities.invokeLater(() -> {
                                    PDFViewerFrame viewer = new PDFViewerFrame(book.getTitle(), finalDocument, finalIn);
                                    viewer.setVisible(true);
                                    loadingDialog.dispose();
                                });
                            } catch (Exception ex) {
                                System.err.println("Gagal membuka atau merender PDF: " + ex.getClass().getName() + " - " + ex.getMessage());
                                ex.printStackTrace();
                                final PDDocument errorDocument = document; // Salinan untuk error handling
                                final InputStream errorIn = in; // Salinan untuk error handling
                                SwingUtilities.invokeLater(() -> {
                                    JOptionPane.showMessageDialog(CatalogPanel.this, "Gagal membuka atau merender PDF: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                                    if (errorDocument != null) try { errorDocument.close(); } catch (IOException ioe) {}
                                    if (errorIn != null) try { errorIn.close(); } catch (IOException ioe) {}
                                    loadingDialog.dispose();
                                });
                            }
                        }).start();

                        loadingDialog.setVisible(true);
                    } else {
                        JOptionPane.showMessageDialog(CatalogPanel.this, "URL tidak tersedia atau buku bukan digital!", "Error", JOptionPane.ERROR_MESSAGE);
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
        System.out.println("CatalogPanel berhasil diinisialisasi.");
    }

    public void refresh() {
        System.out.println("Menyegarkan CatalogPanel...");
        updateTable();
        revalidate();
        repaint();
    }

    private void updateTable() {
        tableModel.setRowCount(0);
        int rowNumber = 1;
        List<Book> digitalBooks = dataStorage.getBooks().stream()
                .filter(book -> "Digital".equalsIgnoreCase(book.getType()))
                .toList();
        for (Book book : digitalBooks) {
            tableModel.addRow(new Object[]{
                    rowNumber++,
                    book.getId(),
                    book.getTitle(),
                    book.getAuthor(),
                    book.getPublicationYear(),
                    "Baca"
            });
        }
        System.out.println("Tabel katalog dimuat dengan " + tableModel.getRowCount() + " buku.");
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
            scrollPane.getVerticalScrollBar().setUnitIncrement(16);
            scrollPane.getHorizontalScrollBar().setUnitIncrement(16);
            add(scrollPane, BorderLayout.CENTER);

            // Render halaman pertama langsung
            renderPage(currentPage);
            setPreferredSize(new Dimension(800, 600));
            pack();
            setLocationRelativeTo(null);
        }

        private void renderPage(int page) {
            try {
                BufferedImage image = renderer.renderImage(page - 1, 1.5f);
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