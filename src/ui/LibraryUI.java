package ui;

import model.Book;
import model.Loan;
import model.User;
import storage.DataStorage;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class LibraryUI extends JFrame {
    private DataStorage dataStorage;
    private CardLayout cardLayout;
    private JPanel cardPanel;
    private DigitalBookPanel digitalBookPanel;
    private PhysicalBookPanel physicalBookPanel;
    private DigitalCatalogPanel digitalCatalogPanel;
    private PhysicalCatalogPanel physicalCatalogPanel;
    private FineManagementPanel fineManagementPanel;
    private BorrowPanel borrowPanel;
    private LoginPanel loginPanel;
    private CreateAccountPanel createAccountPanel;
    private JPanel navPanel;
    private JPanel logoutPanel;
    private StudentDataPanel studentDataPanel;
    private StatisticsPanel statsPanel;
    private String currentUserRole;
    private String currentUsername;
    private User currentUser;

    public LibraryUI() {
        super("Perpustakaan");
        dataStorage = new DataStorage();
        initializeUI();
    }

    private void initializeUI() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 700);
        setLocationRelativeTo(null);

        setLayout(new BorderLayout());

        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);

        loginPanel = new LoginPanel(this);
        cardPanel.add(loginPanel, "LoginPanel");
        
        createAccountPanel = new CreateAccountPanel(this, dataStorage);
        cardPanel.add(createAccountPanel, "CreateAccountPanel");

        try {
            cardPanel.add(new HomePanel(dataStorage), "HomePanel");
            System.out.println("HomePanel berhasil diinisialisasi.");
        } catch (Exception e) {
            System.err.println("Gagal menginisialisasi HomePanel: " + e.getMessage());
            e.printStackTrace();
        }

        try {
            digitalBookPanel = new DigitalBookPanel(dataStorage, cardLayout, cardPanel, null);
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
            statsPanel = new StatisticsPanel(dataStorage);
            cardPanel.add(statsPanel, "StatisticsPanel");
            System.out.println("StatisticsPanel berhasil diinisialisasi.");
        } catch (Exception e) {
            System.err.println("Gagal menginisialisasi StatisticsPanel: " + e.getMessage());
            e.printStackTrace();
        }

        try {
            fineManagementPanel = new FineManagementPanel(dataStorage, this);
            cardPanel.add(fineManagementPanel, "FineManagementPanel");
            System.out.println("FineManagementPanel berhasil diinisialisasi.");
        } catch (Exception e) {
            System.err.println("Gagal menginisialisasi FineManagementPanel: " + e.getMessage());
            e.printStackTrace();
        }

        add(cardPanel, BorderLayout.CENTER);

        addDummyDataIfNeeded();
        if (fineManagementPanel != null) {
            fineManagementPanel.refresh();
        }

        cardLayout.show(cardPanel, "LoginPanel");
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

            // Tambahkan pengguna hanya jika belum ada di database
            java.util.List<User> existingUsers = dataStorage.getUsers();
            if (!existingUsers.stream().anyMatch(u -> u.getUsername().equals("windah_basudara"))) {
                dataStorage.saveUser("windah_basudara", "password123", "user", "Windah Basudara", "XII IPA 1", "123456");
            }
            if (!existingUsers.stream().anyMatch(u -> u.getUsername().equals("userb"))) {
                dataStorage.saveUser("userb", "password456", "user", "User B", "XII IPA 2", "123457");
            }
            // Tambahkan user lain jika perlu, dengan pengecekan serupa

            Book book4 = dataStorage.getBooks().stream()
                    .filter(b -> b.getId().equals("BOOK4"))
                    .findFirst()
                    .orElse(null);
            if (book4 != null) {
                String loanIdToCheck = "BOOK4-" + LocalDate.now().format(DateTimeFormatter.ofPattern("ddMMyy"));
                boolean loanExists = dataStorage.getLoans().stream()
                        .anyMatch(l -> l.getLoanId().equals(loanIdToCheck));

                if (!loanExists) {
                    book4.setBorrowed(true);
                    LocalDate pastDate = LocalDate.now().minusDays(2);
                    String loanId = "BOOK4-" + LocalDate.now().format(DateTimeFormatter.ofPattern("ddMMyy"));
                    Loan newLoan = new Loan(loanId, book4, "John Doe", "XII IPA 1", "123456", pastDate, null, false);
                    dataStorage.saveLoan(newLoan);

                    if (fineManagementPanel != null) {
                        fineManagementPanel.addFine(newLoan, 2);
                        fineManagementPanel.refresh();
                    }
                }
            }
        }
    }

    private void addBookIfNotExists(Book book, String subject) {
        book.setSubject(subject);
        dataStorage.saveBook(book);
        System.out.println("Buku dummy ditambahkan/diperbarui: " + book.getTitle());
    }

    private JButton createNavButton(String text, Color bgColor, Color fgColor) {
        JButton button = new JButton(text);
        try {
            button.setFont(new Font("Segoe UI Emoji", Font.BOLD, 16));
        } catch (Exception e) {
            button.setFont(new Font("Segoe UI", Font.BOLD, 16));
            System.err.println("Font Segoe UI Emoji tidak tersedia, menggunakan Segoe UI. Ikon mungkin tidak muncul.");
        }
        button.setBackground(bgColor);
        button.setForeground(fgColor);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(207, 216, 220)),
                BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent evt) {
                button.setBackground(new Color(224, 224, 224));
            }
            @Override
            public void mouseExited(MouseEvent evt) {
                button.setBackground(bgColor);
            }
            @Override
            public void mousePressed(MouseEvent evt) {
                button.setBackground(new Color(189, 189, 189));
            }
            @Override
            public void mouseReleased(MouseEvent evt) {
                button.setBackground(new Color(224, 224, 224));
            }
        });
        return button;
    }

    public void onLoginSuccess(String username, String role) {
        this.currentUserRole = role;
        this.currentUsername = username;

        currentUser = dataStorage.getUserByUsername(username);
        if (currentUser == null) {
            JOptionPane.showMessageDialog(this, "Data pengguna tidak ditemukan!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (navPanel == null) {
            navPanel = new JPanel();
            navPanel.setPreferredSize(new Dimension(250, getHeight()));
            navPanel.setBackground(new Color(250, 250, 250));
            navPanel.setLayout(new GridBagLayout());
            add(navPanel, BorderLayout.WEST);
        } else {
            navPanel.removeAll();
        }

        if (logoutPanel == null) {
            logoutPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            logoutPanel.setPreferredSize(new Dimension(getWidth(), 50));
            logoutPanel.setBackground(new Color(255, 255, 255));
            add(logoutPanel, BorderLayout.NORTH);
        } else {
            logoutPanel.removeAll();
        }

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 70, 15, 15);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.gridx = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;

        JLabel logoLabel = new JLabel();
        ImageIcon logoIcon = new ImageIcon("src/resources/logo.png");
        if (logoIcon.getImageLoadStatus() == MediaTracker.COMPLETE) {
            Image img = logoIcon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
            logoIcon = new ImageIcon(img);
            logoLabel.setIcon(logoIcon);
        } else {
            System.out.println("Gagal memuat logo.png dari src/resources/. Cek path atau file. Debugging: " + new java.io.File("src/resources/logo.png").getAbsolutePath());
            logoLabel.setText("Logo Tidak Ditemukan");
        }
        gbc.gridy = 0;
        navPanel.add(logoLabel, gbc);

        JLabel navTitle = new JLabel("");
        navTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        navTitle.setForeground(new Color(25, 118, 210));
        gbc.gridy = 1;
        gbc.insets = new Insets(0, 70, 15, 15);
        navPanel.add(navTitle, gbc);

        JButton homeButton = createNavButton("🏠 Beranda", new Color(255, 255, 255), new Color(25, 118, 210));
        JButton digitalCatalogButton = createNavButton("📂 Katalog Digital", new Color(255, 255, 255), new Color(25, 118, 210));
        JButton physicalCatalogButton = createNavButton("📁 Katalog Fisik", new Color(255, 255, 255), new Color(25, 118, 210));
        JButton studentDataButton = createNavButton("👤 Data Mahasiswa", new Color(255, 255, 255), new Color(25, 118, 210));
        JButton borrowButton = createNavButton("📤 Peminjaman", new Color(255, 255, 255), new Color(25, 118, 210));

        if ("admin".equals(role)) {
            JButton digitalBookButton = createNavButton("📚 Buku Digital", new Color(255, 255, 255), new Color(25, 118, 210));
            JButton physicalBookButton = createNavButton("📖 Buku Fisik", new Color(255, 255, 255), new Color(25, 118, 210));
            JButton statsButton = createNavButton("📊 Riwayat & Statistik", new Color(255, 255, 255), new Color(25, 118, 210));
            JButton finesButton = createNavButton("💸 Manajemen Denda", new Color(255, 255, 255), new Color(25, 118, 210));

            gbc.gridy = 2;
            gbc.insets = new Insets(0, 15, 15, 15);
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
            gbc.gridy++;
            navPanel.add(studentDataButton, gbc);

            digitalBookButton.addActionListener(e -> {
                cardLayout.show(cardPanel, "DigitalBookPanel");
                if (digitalBookPanel != null) digitalBookPanel.refresh();
            });
            physicalBookButton.addActionListener(e -> {
                cardLayout.show(cardPanel, "PhysicalBookPanel");
                if (physicalBookPanel != null) physicalBookPanel.refresh();
            });
            borrowButton.addActionListener(e -> {
                for (Component comp : cardPanel.getComponents()) {
                    if (comp instanceof BorrowPanel) {
                        cardPanel.remove(comp);
                        break;
                    }
                }
                borrowPanel = new BorrowPanel(dataStorage, null, this, currentUsername);
                cardPanel.add(borrowPanel, "BorrowPanel");
                cardLayout.show(cardPanel, "BorrowPanel");
            });
            statsButton.addActionListener(e -> {
                cardLayout.show(cardPanel, "StatisticsPanel");
                if (statsPanel != null) statsPanel.refresh();
            });
            finesButton.addActionListener(e -> {
                cardLayout.show(cardPanel, "FineManagementPanel");
                if (fineManagementPanel != null) fineManagementPanel.refresh();
            });
        } else if ("user".equals(role)) {
            gbc.gridy = 2;
            gbc.insets = new Insets(0, 15, 15, 15);
            navPanel.add(homeButton, gbc);
            gbc.gridy++;
            navPanel.add(digitalCatalogButton, gbc);
            gbc.gridy++;
            navPanel.add(physicalCatalogButton, gbc);
            gbc.gridy++;
            navPanel.add(borrowButton, gbc);
            gbc.gridy++;
            navPanel.add(studentDataButton, gbc);
        }

        homeButton.addActionListener(e -> cardLayout.show(cardPanel, "HomePanel"));
        digitalCatalogButton.addActionListener(e -> {
            cardLayout.show(cardPanel, "DigitalCatalogPanel");
            if (digitalCatalogPanel != null) digitalCatalogPanel.refresh();
        });
        physicalCatalogButton.addActionListener(e -> {
            cardLayout.show(cardPanel, "PhysicalCatalogPanel");
            if (physicalCatalogPanel != null) physicalCatalogPanel.refresh();
        });
        borrowButton.addActionListener(e -> {
            for (Component comp : cardPanel.getComponents()) {
                if (comp instanceof BorrowPanel) {
                    cardPanel.remove(comp);
                    break;
                }
            }
            borrowPanel = new BorrowPanel(dataStorage, null, this, currentUsername);
            cardPanel.add(borrowPanel, "BorrowPanel");
            cardLayout.show(cardPanel, "BorrowPanel");
        });
        studentDataButton.addActionListener(e -> {
            if (studentDataPanel == null || currentUsername == null) {
                studentDataPanel = new StudentDataPanel(dataStorage, this, currentUsername);
                cardPanel.add(studentDataPanel, "StudentDataPanel");
            }
            cardLayout.show(cardPanel, "StudentDataPanel");
            if (studentDataPanel != null && currentUsername != null) {
                System.out.println("Refreshing with username: " + currentUsername + ", role: " + currentUserRole);
                studentDataPanel.refresh(currentUsername);
            } else {
                System.out.println("Error: currentUsername is null or studentDataPanel not initialized.");
            }
        });

        JButton logoutButton = createNavButton("🚪 Logout", new Color(211, 47, 47), Color.WHITE);
        logoutButton.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(183, 28, 28), 1),
                BorderFactory.createEmptyBorder(8, 15, 8, 15)
        ));
        logoutButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent evt) {
                logoutButton.setBackground(new Color(183, 28, 28));
            }
            @Override
            public void mouseExited(MouseEvent evt) {
                logoutButton.setBackground(new Color(211, 47, 47));
            }
            @Override
            public void mousePressed(MouseEvent evt) {
                logoutButton.setBackground(new Color(165, 25, 25));
            }
            @Override
            public void mouseReleased(MouseEvent evt) {
                logoutButton.setBackground(new Color(183, 28, 28));
            }
        });
        logoutPanel.add(logoutButton);

        navPanel.revalidate();
        navPanel.repaint();
        logoutPanel.revalidate();
        logoutPanel.repaint();

        logoutButton.addActionListener(e -> {
            System.out.println("Logout berhasil untuk " + currentUsername);
            navPanel.removeAll();
            logoutPanel.removeAll();
            remove(navPanel);
            remove(logoutPanel);
            navPanel = null;
            logoutPanel = null;
            currentUserRole = null;
            currentUsername = null;
            currentUser = null;
            if (studentDataPanel != null) {
                cardPanel.remove(studentDataPanel);
                studentDataPanel = null;
            }
            if (borrowPanel != null) {
                cardPanel.remove(borrowPanel);
                borrowPanel = null;
            }
            // Reset semua panel yang mungkin ada
            digitalBookPanel = new DigitalBookPanel(dataStorage, cardLayout, cardPanel, null);
            cardPanel.add(digitalBookPanel, "DigitalBookPanel");
            physicalBookPanel = new PhysicalBookPanel(dataStorage, cardLayout, cardPanel);
            cardPanel.add(physicalBookPanel, "PhysicalBookPanel");
            digitalCatalogPanel = new DigitalCatalogPanel(dataStorage, cardLayout, cardPanel);
            cardPanel.add(digitalCatalogPanel, "DigitalCatalogPanel");
            physicalCatalogPanel = new PhysicalCatalogPanel(dataStorage, cardLayout, cardPanel);
            cardPanel.add(physicalCatalogPanel, "PhysicalCatalogPanel");
            statsPanel = new StatisticsPanel(dataStorage);
            cardPanel.add(statsPanel, "StatisticsPanel");
            fineManagementPanel = new FineManagementPanel(dataStorage, this);
            cardPanel.add(fineManagementPanel, "FineManagementPanel");

            cardLayout.show(cardPanel, "LoginPanel");
            revalidate();
            repaint();
        });

        // Perbarui DigitalBookPanel dengan currentUsername setelah login
        if (digitalBookPanel != null && currentUsername != null) {
            digitalBookPanel = new DigitalBookPanel(dataStorage, cardLayout, cardPanel, currentUsername);
            cardPanel.add(digitalBookPanel, "DigitalBookPanel");
        }

        cardLayout.show(cardPanel, "HomePanel");
    }
    
    public void showCard(String cardName) {
        if (cardLayout != null && cardPanel != null) {
            cardLayout.show(cardPanel, cardName);
            System.out.println("Menampilkan card: " + cardName);

            Component currentComponent = null;
            for (Component comp : cardPanel.getComponents()) {
                if (comp.isVisible() && comp.getName() != null && comp.getName().equals(cardName)) {
                    currentComponent = comp;
                    break;
                }
                if (cardName.equals("HomePanel") && comp instanceof HomePanel) {
                    currentComponent = comp;
                } else if (cardName.equals("DigitalBookPanel") && comp instanceof DigitalBookPanel) {
                    currentComponent = comp;
                } else if (cardName.equals("PhysicalBookPanel") && comp instanceof PhysicalBookPanel) {
                    currentComponent = comp;
                } else if (cardName.equals("DigitalCatalogPanel") && comp instanceof DigitalCatalogPanel) {
                    currentComponent = comp;
                } else if (cardName.equals("PhysicalCatalogPanel") && comp instanceof PhysicalCatalogPanel) {
                    currentComponent = comp;
                } else if (cardName.equals("StatisticsPanel") && comp instanceof StatisticsPanel) {
                    currentComponent = comp;
                } else if (cardName.equals("FineManagementPanel") && comp instanceof FineManagementPanel) {
                    currentComponent = comp;
                } else if (cardName.equals("BorrowPanel") && comp instanceof BorrowPanel) {
                    currentComponent = comp;
                } else if (cardName.equals("StudentDataPanel") && comp instanceof StudentDataPanel) {
                    currentComponent = comp;
                }
            }

            if (currentComponent != null) {
                if (currentComponent instanceof HomePanel) {
                    ((HomePanel) currentComponent).refresh();
                } else if (currentComponent instanceof DigitalBookPanel) {
                    ((DigitalBookPanel) currentComponent).refresh();
                } else if (currentComponent instanceof PhysicalBookPanel) {
                    ((PhysicalBookPanel) currentComponent).refresh();
                } else if (currentComponent instanceof DigitalCatalogPanel) {
                    ((DigitalCatalogPanel) currentComponent).refresh();
                } else if (currentComponent instanceof PhysicalCatalogPanel) {
                    ((PhysicalCatalogPanel) currentComponent).refresh();
                } else if (currentComponent instanceof StatisticsPanel) {
                    ((StatisticsPanel) currentComponent).refresh();
                } else if (currentComponent instanceof FineManagementPanel) {
                    ((FineManagementPanel) currentComponent).refresh();
                } else if (currentComponent instanceof BorrowPanel) {
                    ((BorrowPanel) currentComponent).updateTable();
                } else if (currentComponent instanceof StudentDataPanel && currentUsername != null) {
                    ((StudentDataPanel) currentComponent).refresh(currentUsername);
                }
            } else {
                System.out.println("Komponen tidak ditemukan atau tidak perlu di-refresh: " + cardName);
            }
        } else {
            System.err.println("Error: cardLayout atau cardPanel belum diinisialisasi di LibraryUI.");
        }
    }

    public void updateAllPanels() {
        for (Component comp : cardPanel.getComponents()) {
            try {
                if (comp instanceof BorrowPanel) {
                    ((BorrowPanel) comp).updateTable();
                    ((BorrowPanel) comp).updateUserTable();
                } else if (comp instanceof StudentDataPanel && currentUsername != null) {
                    ((StudentDataPanel) comp).refresh(currentUsername);
                } else if (comp instanceof DigitalBookPanel) {
                    ((DigitalBookPanel) comp).refresh();
                } else if (comp instanceof PhysicalBookPanel) {
                    ((PhysicalBookPanel) comp).refresh();
                } else if (comp instanceof DigitalCatalogPanel) {
                    ((DigitalCatalogPanel) comp).refresh();
                } else if (comp instanceof PhysicalCatalogPanel) {
                    ((PhysicalCatalogPanel) comp).refresh();
                } else if (comp instanceof StatisticsPanel) {
                    ((StatisticsPanel) comp).refresh();
                } else if (comp instanceof FineManagementPanel) {
                    ((FineManagementPanel) comp).refresh();
                }
            } catch (Exception e) {
                System.err.println("Error refreshing panel " + comp.getName() + ": " + e.getMessage());
            }
        }
        revalidate();
        repaint();
    }

    public void updateAllStudentPanels() {
        for (Component comp : cardPanel.getComponents()) {
            if (comp instanceof StudentDataPanel && currentUsername != null) {
                ((StudentDataPanel) comp).refresh(currentUsername);
            }
        }
    }

    public FineManagementPanel getFineManagementPanel() {
        return fineManagementPanel;
    }

    public BorrowPanel getBorrowPanel() {
        return borrowPanel;
    }

    public DataStorage getDataStorage() {
        return dataStorage;
    }

    public CardLayout getCardLayout() {
        return cardLayout;
    }

    public JPanel getCardPanel() {
        return cardPanel;
    }

    public User getCurrentUser() {
        return currentUser;
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