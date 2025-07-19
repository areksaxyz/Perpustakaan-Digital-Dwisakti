package ui;

import model.Book;
import storage.DataStorage;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class PhysicalBookPanel extends JPanel {
    private DataStorage dataStorage;
    private DefaultTableModel tableModel;
    private JTable table;
    private JTextField idField;
    private JTextField titleField;
    private JTextField authorField;
    private JTextField publicationYearField;
    private JTextField subjectField;
    private CardLayout cardLayout;
    private JPanel cardPanel;

    private JButton addButton, updateButton, deleteButton, clearButton;

    public PhysicalBookPanel(DataStorage dataStorage, CardLayout cardLayout, JPanel cardPanel) {
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
                GradientPaint gp = new GradientPaint(0, 0, new Color(76, 175, 80), getWidth(), 0, new Color(102, 187, 106));
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        headerPanel.setPreferredSize(new Dimension(getWidth(), 80));
        headerPanel.setLayout(new BorderLayout());
        JLabel titleLabel = new JLabel("Manajemen Buku Fisik", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titleLabel.setForeground(Color.WHITE);
        headerPanel.add(titleLabel, BorderLayout.CENTER);
        add(headerPanel, BorderLayout.NORTH);

        // Form input
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(new Color(245, 247, 250));
        formPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)), "Tambah Buku Fisik", 0, 0, new Font("Segoe UI", Font.BOLD, 16), new Color(76, 175, 80)));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Label dan field
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
        publicationYearField = new JTextField(15);
        publicationYearField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        publicationYearField.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 1, true));

        JLabel subjectLabel = new JLabel("Subjek:");
        subjectLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subjectField = new JTextField(15);
        subjectField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subjectField.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 1, true));

        addButton = createStyledButton("Tambah Buku", new Color(76, 175, 80));
        updateButton = createStyledButton("Update", new Color(33, 150, 243));
        deleteButton = createStyledButton("Hapus", new Color(244, 67, 54));
        clearButton = createStyledButton("Clear", new Color(96, 125, 139));

        // Layout form
        gbc.gridx = 0; gbc.gridy = 0; formPanel.add(idLabel, gbc);
        gbc.gridx = 1; formPanel.add(idField, gbc);

        gbc.gridx = 0; gbc.gridy = 1; formPanel.add(titleLabel2, gbc);
        gbc.gridx = 1; formPanel.add(titleField, gbc);

        gbc.gridx = 0; gbc.gridy = 2; formPanel.add(authorLabel, gbc);
        gbc.gridx = 1; formPanel.add(authorField, gbc);

        gbc.gridx = 0; gbc.gridy = 3; formPanel.add(yearLabel, gbc);
        gbc.gridx = 1; formPanel.add(publicationYearField, gbc);

        gbc.gridx = 0; gbc.gridy = 4; formPanel.add(subjectLabel, gbc);
        gbc.gridx = 1; formPanel.add(subjectField, gbc);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        buttonPanel.setOpaque(false);
        buttonPanel.add(addButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(clearButton);

        gbc.gridx = 0; gbc.gridy = 5; gbc.gridwidth = 2;
        formPanel.add(buttonPanel, gbc);

        // Panel tengah dengan form dan tabel
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBackground(new Color(245, 247, 250));
        centerPanel.add(formPanel, BorderLayout.NORTH);

        // Tabel buku fisik dengan kolom kustom
        String[] columns = {"No", "ID", "Judul", "Penulis", "Tahun Terbit", "Subjek", "Status Pinjam"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table = new JTable(tableModel);
        table.setFillsViewportHeight(true);
        table.setRowHeight(35);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.setSelectionBackground(new Color(200, 230, 201));
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
        tableHeader.setBackground(new Color(76, 175, 80));
        tableHeader.setForeground(Color.WHITE);
        tableHeader.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));
        centerPanel.add(scrollPane, BorderLayout.CENTER);

        add(centerPanel, BorderLayout.CENTER);

        // Aksi tombol Tambah Buku
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String id = idField.getText().trim();
                String title = titleField.getText().trim();
                String author = authorField.getText().trim();
                String year = publicationYearField.getText().trim();
                String subject = subjectField.getText().trim();

                if (id.isEmpty() || title.isEmpty() || author.isEmpty() || year.isEmpty()) {
                    JOptionPane.showMessageDialog(PhysicalBookPanel.this, "Semua field harus diisi!", "Peringatan", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                // Validasi ID hanya mengandung angka setelah "BOOK"
                if (!id.matches("BOOK\\d+")) {
                    JOptionPane.showMessageDialog(PhysicalBookPanel.this, "ID harus dalam format 'BOOK' diikuti angka (contoh: BOOK1)!", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                try {
                    int publicationYear = Integer.parseInt(year);
                    Book book = new Book(id, title, author, publicationYear, "Fisik", "", subject);
                    if (dataStorage.getBooks().stream().noneMatch(b -> b.getId().equals(id))) {
                        dataStorage.saveBook(book);
                        refresh();
                        clearFields();
                        JOptionPane.showMessageDialog(PhysicalBookPanel.this, "Buku fisik berhasil ditambahkan!", "Sukses", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(PhysicalBookPanel.this, "ID buku sudah ada!", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(PhysicalBookPanel.this, "Tahun harus berupa angka!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // Aksi tombol Update Buku
        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String id = idField.getText().trim();
                String title = titleField.getText().trim();
                String author = authorField.getText().trim();
                String year = publicationYearField.getText().trim();
                String subject = subjectField.getText().trim();

                if (id.isEmpty()) {
                    JOptionPane.showMessageDialog(PhysicalBookPanel.this, "Pilih buku untuk diupdate!", "Peringatan", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                Book bookToUpdate = dataStorage.getBooks().stream()
                        .filter(b -> b.getId().equals(id))
                        .findFirst()
                        .orElse(null);

                if (bookToUpdate == null) {
                    JOptionPane.showMessageDialog(PhysicalBookPanel.this, "Buku dengan ID tersebut tidak ditemukan!", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                try {
                    bookToUpdate.setTitle(title);
                    bookToUpdate.setAuthor(author);
                    bookToUpdate.setPublicationYear(year); // Konsisten dengan String
                    bookToUpdate.setSubject(subject);

                    dataStorage.saveBook(bookToUpdate);
                    refresh();
                    clearFields();
                    JOptionPane.showMessageDialog(PhysicalBookPanel.this, "Buku fisik berhasil diperbarui!", "Sukses", JOptionPane.INFORMATION_MESSAGE);
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(PhysicalBookPanel.this, "Tahun harus berupa angka!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // Aksi tombol Delete Buku
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String id = idField.getText().trim();
                
                if (id.isEmpty()) {
                    JOptionPane.showMessageDialog(PhysicalBookPanel.this, "Pilih buku untuk dihapus!", "Peringatan", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                int confirm = JOptionPane.showConfirmDialog(PhysicalBookPanel.this, "Apakah Anda yakin ingin menghapus buku ini?", "Konfirmasi Hapus", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    Book bookToDelete = dataStorage.getBooks().stream()
                        .filter(b -> b.getId().equals(id))
                        .findFirst()
                        .orElse(null);
                    
                    if (bookToDelete != null) {
                        if (bookToDelete.isBorrowed()) {
                            JOptionPane.showMessageDialog(PhysicalBookPanel.this, "Buku sedang dipinjam dan tidak dapat dihapus.", "Peringatan", JOptionPane.WARNING_MESSAGE);
                        } else {
                            if (dataStorage.deleteBook(id)) {
                                refresh();
                                clearFields();
                                JOptionPane.showMessageDialog(PhysicalBookPanel.this, "Buku fisik berhasil dihapus!", "Sukses", JOptionPane.INFORMATION_MESSAGE);
                                ((LibraryUI) SwingUtilities.getWindowAncestor(PhysicalBookPanel.this)).updateAllPanels();
                            } else {
                                JOptionPane.showMessageDialog(PhysicalBookPanel.this, "Gagal menghapus buku dari database.", "Error", JOptionPane.ERROR_MESSAGE);
                            }
                        }
                    } else {
                        JOptionPane.showMessageDialog(PhysicalBookPanel.this, "Buku dengan ID tersebut tidak ditemukan!", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });

        // Aksi tombol Clear
        clearButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clearFields();
            }
        });

        // Memuat semua buku saat panel dibuka
        refresh();
        System.out.println("Tabel buku fisik dimuat dengan " + tableModel.getRowCount() + " buku.");
    }

    private JButton createStyledButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Roboto", Font.BOLD, 14));
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(bgColor.darker());
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(bgColor);
            }
        });
        return button;
    }

    public void refresh() {
        tableModel.setRowCount(0);
        int rowNumber = 1;
        List<Book> physicalBooks = dataStorage.getBooks().stream()
                .filter(b -> "Fisik".equals(b.getType()))
                .collect(java.util.stream.Collectors.toList());

        for (Book book : physicalBooks) {
            tableModel.addRow(new Object[]{rowNumber++, book.getId(), book.getTitle(), book.getAuthor(), book.getPublicationYear(), book.getSubject().isEmpty() ? "-" : book.getSubject(), book.isBorrowed() ? "Dipinjam" : "Tersedia"});
        }
        revalidate();
        repaint();
    }

    private void clearFields() {
        idField.setText("");
        titleField.setText("");
        authorField.setText("");
        publicationYearField.setText("");
        subjectField.setText("");
        table.clearSelection();
    }
}