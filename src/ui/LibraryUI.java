package ui;

import model.Book;
import model.Loan;
import storage.DataStorage;
import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class LibraryUI extends JFrame {
    private DataStorage dataStorage;
    private CardLayout cardLayout;
    private JPanel cardPanel;
    private DigitalBookPanel digitalBookPanel;
    private PhysicalBookPanel physicalBookPanel;
    private DigitalCatalogPanel digitalCatalogPanel;
    private PhysicalCatalogPanel physicalCatalogPanel;
    private FineManagementPanel fineManagementPanel;
    private BorrowPanel borrowPanel; // Tambahkan variabel untuk menyimpan referensi BorrowPanel

    public LibraryUI() {
        super("Perpustakaan Digital");
        dataStorage = new DataStorage();
        initializeUI();
    }

    private void initializeUI() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 700);
        setLocationRelativeTo(null);

        setLayout(new BorderLayout());

        // Navigasi bar di sisi kiri
        JPanel navPanel = new JPanel();
        navPanel.setPreferredSize(new Dimension(200, getHeight()));
        navPanel.setBackground(new Color(44, 62, 80)); // #2C3E50
        navPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 0, 10, 0);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.CENTER;

        // Tombol Navigasi dengan ikon
        JButton homeButton = createNavButton("🏠 Beranda", new Color(52, 73, 94), Color.WHITE);
        JButton digitalBookButton = createNavButton("📚 Buku Digital", new Color(52, 73, 94), Color.WHITE);
        JButton physicalBookButton = createNavButton("📖 Buku Fisik", new Color(52, 73, 94), Color.WHITE);
        JButton digitalCatalogButton = createNavButton("📂 Katalog Digital", new Color(52, 73, 94), Color.WHITE);
        JButton physicalCatalogButton = createNavButton("📁 Katalog Fisik", new Color(52, 73, 94), Color.WHITE);
        JButton borrowButton = createNavButton("📤 Peminjaman", new Color(52, 73, 94), Color.WHITE);
        JButton statsButton = createNavButton("📊 Riwayat & Statistik", new Color(52, 73, 94), Color.WHITE);
        JButton finesButton = createNavButton("💸 Manajemen Denda", new Color(52, 73, 94), Color.WHITE);

        gbc.gridx = 0;
        gbc.gridy = 0;
        navPanel.add(homeButton, gbc);
        gbc.gridy++;
        navPanel.add(digitalBookButton, gbc);
        gbc.gridy++;
        navPanel.add(physicalBookButton, gbc);
        gbc.gridy++;
        navPanel.add(digitalCatalogButton, gbc);
        gbc.gridy++;
        navPanel.add(physicalCatalogButton, gbc);
        gbc.gridy++;
        navPanel.add(borrowButton, gbc);
        gbc.gridy++;
        navPanel.add(statsButton, gbc);
        gbc.gridy++;
        navPanel.add(finesButton, gbc);

        // Inisialisasi CardLayout untuk konten panel
        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);

        // Inisialisasi semua panel dengan error handling
        try {
            cardPanel.add(new HomePanel(dataStorage), "HomePanel");
            System.out.println("HomePanel berhasil diinisialisasi.");
        } catch (Exception e) {
            System.err.println("Gagal menginisialisasi HomePanel: " + e.getMessage());
            e.printStackTrace();
        }

        try {
            digitalBookPanel = new DigitalBookPanel(dataStorage, cardLayout, cardPanel);
            cardPanel.add(digitalBookPanel, "DigitalBookPanel");
            System.out.println("DigitalBookPanel berhasil diinisialisasi.");
        } catch (Exception e) {
            System.err.println("Gagal menginisialisasi DigitalBookPanel: " + e.getMessage());
            e.printStackTrace();
        }

        try {
            physicalBookPanel = new PhysicalBookPanel(dataStorage, cardLayout, cardPanel);
            cardPanel.add(physicalBookPanel, "PhysicalBookPanel");
            System.out.println("PhysicalBookPanel berhasil diinisialisasi.");
        } catch (Exception e) {
            System.err.println("Gagal menginisialisasi PhysicalBookPanel: " + e.getMessage());
            e.printStackTrace();
        }

        try {
            digitalCatalogPanel = new DigitalCatalogPanel(dataStorage, cardLayout, cardPanel);
            cardPanel.add(digitalCatalogPanel, "DigitalCatalogPanel");
            System.out.println("DigitalCatalogPanel berhasil diinisialisasi.");
        } catch (Exception e) {
            System.err.println("Gagal menginisialisasi DigitalCatalogPanel: " + e.getMessage());
            e.printStackTrace();
        }

        try {
            physicalCatalogPanel = new PhysicalCatalogPanel(dataStorage, cardLayout, cardPanel);
            cardPanel.add(physicalCatalogPanel, "PhysicalCatalogPanel");
            System.out.println("PhysicalCatalogPanel berhasil diinisialisasi.");
        } catch (Exception e) {
            System.err.println("Gagal menginisialisasi PhysicalCatalogPanel: " + e.getMessage());
            e.printStackTrace();
        }

        try {
            borrowPanel = new BorrowPanel(dataStorage, null, this); // Simpan referensi ke borrowPanel
            cardPanel.add(borrowPanel, "BorrowPanel");
            System.out.println("BorrowPanel berhasil diinisialisasi.");
        } catch (Exception e) {
            System.err.println("Gagal menginisialisasi BorrowPanel: " + e.getMessage());
            e.printStackTrace();
        }

        try {
            StatisticsPanel statsPanel = new StatisticsPanel(dataStorage);
            cardPanel.add(statsPanel, "StatisticsPanel");
            System.out.println("StatisticsPanel berhasil diinisialisasi.");
        } catch (Exception e) {
            System.err.println("Gagal menginisialisasi StatisticsPanel: " + e.getMessage());
            e.printStackTrace();
        }

        try {
            fineManagementPanel = new FineManagementPanel(dataStorage, this); // Oper this ke FineManagementPanel
            cardPanel.add(fineManagementPanel, "FineManagementPanel");
            System.out.println("FineManagementPanel berhasil diinisialisasi.");
        } catch (Exception e) {
            System.err.println("Gagal menginisialisasi FineManagementPanel: " + e.getMessage());
            e.printStackTrace();
        }

        add(navPanel, BorderLayout.WEST);
        add(cardPanel, BorderLayout.CENTER);

        // Action listener untuk navigasi
        homeButton.addActionListener(e -> {
            System.out.println("Navigasi ke HomePanel");
            cardLayout.show(cardPanel, "HomePanel");
        });

        digitalBookButton.addActionListener(e -> {
            System.out.println("Navigasi ke DigitalBookPanel");
            cardLayout.show(cardPanel, "DigitalBookPanel");
            digitalBookPanel.refresh();
        });

        physicalBookButton.addActionListener(e -> {
            System.out.println("Navigasi ke PhysicalBookPanel");
            cardLayout.show(cardPanel, "PhysicalBookPanel");
            physicalBookPanel.refresh();
        });

        digitalCatalogButton.addActionListener(e -> {
            System.out.println("Navigasi ke DigitalCatalogPanel");
            cardLayout.show(cardPanel, "DigitalCatalogPanel");
            digitalCatalogPanel.refresh();
        });

        physicalCatalogButton.addActionListener(e -> {
            System.out.println("Navigasi ke PhysicalCatalogPanel");
            cardLayout.show(cardPanel, "PhysicalCatalogPanel");
            physicalCatalogPanel.refresh();
        });

        borrowButton.addActionListener(e -> {
            System.out.println("Navigasi ke BorrowPanel");
            for (Component comp : cardPanel.getComponents()) {
                if (comp instanceof BorrowPanel) {
                    cardPanel.remove(comp);
                    break;
                }
            }
            borrowPanel = new BorrowPanel(dataStorage, null, this); // Buat ulang BorrowPanel dengan referensi baru
            cardPanel.add(borrowPanel, "BorrowPanel");
            cardLayout.show(cardPanel, "BorrowPanel");
        });

        statsButton.addActionListener(e -> {
            System.out.println("Navigasi ke StatisticsPanel");
            cardLayout.show(cardPanel, "StatisticsPanel");
            for (Component comp : cardPanel.getComponents()) {
                if (comp instanceof StatisticsPanel) {
                    ((StatisticsPanel) comp).refresh();
                    break;
                }
            }
        });

        finesButton.addActionListener(e -> {
            System.out.println("Navigasi ke FineManagementPanel");
            cardLayout.show(cardPanel, "FineManagementPanel");
            fineManagementPanel.refresh();
        });

        // Tambahkan data dummy jika belum ada di database
        addDummyDataIfNeeded();
        // Pastikan manajemen denda diperbarui saat aplikasi dimulai
        if (fineManagementPanel != null) {
            fineManagementPanel.refresh();
        }

        // Tampilkan panel Beranda sebagai default
        System.out.println("Menampilkan HomePanel sebagai default");
        cardLayout.show(cardPanel, "HomePanel");
    }

    private void addDummyDataIfNeeded() {
        if (dataStorage.getBooks().isEmpty()) {
            addBookIfNotExists(new Book("BOOK1", "Algoritma Pemrograman", "Irwana Kautsar, Ph.D", "2020", "Digital", "http://eprints.umsida.ac.id/9873/5/BE1-ALPO-BukuAjar.pdf"), "Algoritma");
            addBookIfNotExists(new Book("BOOK2", "Belajar Pemrograman Python Dasar", "Penulis Python", "2020", "Digital", "https://repository.unikom.ac.id/65984/1/E-Book_Belajar_Pemrograman_Python_Dasar.pdf"), "Pemrograman");
            addBookIfNotExists(new Book("BOOK3", "Pemrograman Java", "Penulis Java", "2020", "Digital", "https://digilib.stekom.ac.id/assets/dokumen/ebook/feb_BMuBPtvpXwUkhZqdyUPA7LyV7948c7ZdhjGj8z2EkAjSpNgD_njQSpM_1656322622.pdf"), "Pemrograman");

            addBookIfNotExists(new Book("BOOK4", "Pemrograman Java", "Penulis Java", "2019", "Fisik", ""), "Pemrograman");
            addBookIfNotExists(new Book("BOOK5", "Pemrograman Python", "Penulis Python", "2022", "Fisik", ""), "Pemrograman");
            addBookIfNotExists(new Book("BOOK6", "Pendidikan Agama Islam", "Ahmad Syarif", "2023", "Fisik", ""), "Agama");
            addBookIfNotExists(new Book("BOOK7", "Fisika Dasar", "Budi Santoso", "2021", "Fisik", ""), "Fisika");
            addBookIfNotExists(new Book("BOOK8", "Matematika Diskrit", "Siti Aminah", "2020", "Fisik", ""), "Matematika Diskrit");
            addBookIfNotExists(new Book("BOOK9", "English for Beginners", "John Smith", "2019", "Fisik", ""), "Bahasa Inggris");
            addBookIfNotExists(new Book("BOOK10", "Pendidikan Kewarganegaraan", "Dewi Kartika", "2022", "Fisik", ""), "Kewarganegaraan");

            Book book4 = dataStorage.getBooks().stream()
                    .filter(b -> b.getId().equals("BOOK4"))
                    .findFirst()
                    .orElse(null);
            if (book4 != null) {
                book4.setBorrowed(true);
                long pastDate = System.currentTimeMillis() - (1 * 24 * 60 * 60 * 1000L); // 1 hari yang lalu
                String loanId = "BOOK4-" + LocalDate.now().format(DateTimeFormatter.ofPattern("ddMMyy"));
                dataStorage.getLoans().add(new Loan(loanId, book4, "John Doe", "XII IPA 1", "123456", new Date(pastDate), null, false));
                dataStorage.saveLoan(dataStorage.getLoans().get(0));
                // Tambahkan denda otomatis untuk peminjaman terlambat
                if (fineManagementPanel != null) {
                    FineManagementPanel.addFine(dataStorage.getLoans().get(0), 1); // 1 hari terlambat
                    fineManagementPanel.refresh();
                }
            }
        }
    }

    private void addBookIfNotExists(Book book, String subject) {
        if (dataStorage.getBooks().stream().noneMatch(b -> b.getId().equals(book.getId()))) {
            book.setSubject(subject);
            dataStorage.getBooks().add(book);
            dataStorage.saveBook(book);
            System.out.println("Buku dummy ditambahkan: " + book.getTitle());
        } else {
            System.out.println("Buku dummy dengan ID " + book.getId() + " sudah ada, tidak ditambahkan.");
        }
    }

    private JButton createNavButton(String text, Color bgColor, Color fgColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 16));
        button.setBackground(bgColor);
        button.setForeground(fgColor);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(70, 86, 102)),
                BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(41, 58, 71)); // Efek hover
            }
            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(bgColor);
            }
            @Override
            public void mousePressed(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(34, 48, 60)); // Efek klik
            }
            @Override
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(41, 58, 71));
            }
        });
        return button;
    }

    // Getter untuk fineManagementPanel
    public FineManagementPanel getFineManagementPanel() {
        return fineManagementPanel;
    }

    // Getter untuk borrowPanel
    public BorrowPanel getBorrowPanel() {
        return borrowPanel;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel("com.formdev.flatlaf.FlatLightLaf");
            } catch (Exception e) {
                e.printStackTrace();
            }
            new LibraryUI().setVisible(true);
        });
    }
}