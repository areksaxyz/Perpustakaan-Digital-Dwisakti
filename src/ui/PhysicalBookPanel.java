package ui;

import model.Book;
import storage.DataStorage;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableCellEditor;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class PhysicalBookPanel extends JPanel {
    private DataStorage dataStorage;
    private DefaultTableModel tableModel;
    private JTable table;
    private JTextField idField;
    private JTextField titleField;
    private JTextField authorField;
    private JTextField yearField;
    private JTextField subjectField;
    private CardLayout cardLayout;
    private JPanel cardPanel;

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
        yearField = new JTextField(15);
        yearField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        yearField.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 1, true));

        JLabel subjectLabel = new JLabel("Subjek:");
        subjectLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subjectField = new JTextField(15);
        subjectField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subjectField.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 1, true));

        JButton addButton = new JButton("Tambah Buku");
        styleButton(addButton, new Color(76, 175, 80), Color.WHITE); // Hijau

        // Posisi komponen dalam form
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
        formPanel.add(subjectLabel, gbc);
        gbc.gridx = 1;
        formPanel.add(subjectField, gbc);

        gbc.gridx = 1;
        gbc.gridy = 5;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.EAST;
        formPanel.add(addButton, gbc);

        // Panel tengah dengan form dan tabel
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBackground(new Color(245, 247, 250));
        centerPanel.add(formPanel, BorderLayout.NORTH);

        // Tabel buku fisik dengan kolom kustom
        String[] columns = {"No", "ID", "Judul", "Penulis", "Tahun Terbit", "Subjek", "Aksi"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 6; // Hanya kolom "Aksi" yang editable
            }

            @Override
            public Class<?> getColumnClass(int columnIndex) {
                if (columnIndex == 6) return JButton.class; // Kolom "Aksi" berisi tombol
                return super.getColumnClass(columnIndex);
            }
        };
        table = new JTable(tableModel) {
            @Override
            public void changeSelection(int row, int col, boolean toggle, boolean extend) {
                super.changeSelection(row, col, toggle, extend);
                if (col == 6) {
                    editCellAt(row, col);
                    Component editor = getEditorComponent();
                    if (editor instanceof JButton) {
                        ((JButton) editor).doClick();
                    }
                }
            }
        };
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

        // Renderer untuk tombol di kolom "Aksi"
        table.getColumnModel().getColumn(6).setCellRenderer(new ButtonRenderer());
        table.getColumnModel().getColumn(6).setCellEditor(new ButtonEditor(new JCheckBox()));

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
        addButton.addActionListener(e -> {
            String id = idField.getText().trim();
            String title = titleField.getText().trim();
            String author = authorField.getText().trim();
            String year = yearField.getText().trim();
            String subject = subjectField.getText().trim();

            if (id.isEmpty() || title.isEmpty() || author.isEmpty() || year.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Semua kolom wajib diisi!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                int publicationYear = Integer.parseInt(year);
                Book book = new Book(id, title, author, publicationYear, "Fisik", "", subject);
                if (dataStorage.getBooks().stream().noneMatch(b -> b.getId().equals(id))) {
                    dataStorage.getBooks().add(book);
                    dataStorage.saveBooks();
                    updateTable();
                    clearFields();
                    JOptionPane.showMessageDialog(this, "Buku fisik berhasil ditambahkan!", "Sukses", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this, "ID buku sudah ada!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Tahun harus berupa angka!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Memuat semua buku saat panel dibuka
        updateTable();
        System.out.println("Tabel buku fisik dimuat dengan " + tableModel.getRowCount() + " buku.");
    }

    public void refresh() {
        System.out.println("Menyegarkan PhysicalBookPanel...");
        updateTable();
        revalidate();
        repaint();
    }

    private void updateTable() {
        tableModel.setRowCount(0);
        int rowNumber = 1;
        for (Book book : dataStorage.getBooks()) {
            if ("Fisik".equals(book.getType())) {
                JButton deleteButton = new JButton("Hapus");
                styleButton(deleteButton, new Color(231, 76, 60), Color.WHITE); // Merah
                deleteButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        int modelRow = table.convertRowIndexToModel(table.getSelectedRow());
                        String bookId = (String) tableModel.getValueAt(modelRow, 1);
                        showPasswordDialog(bookId);
                    }
                });
                tableModel.addRow(new Object[]{rowNumber++, book.getId(), book.getTitle(), book.getAuthor(), book.getPublicationYear(), book.getSubject().isEmpty() ? "-" : book.getSubject(), deleteButton});
            }
        }
    }

    private void styleButton(JButton button, Color bgColor, Color fgColor) {
        button.setFont(new Font("Segoe UI", Font.BOLD, 12));
        button.setBackground(bgColor);
        button.setForeground(fgColor);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                BorderFactory.createEmptyBorder(2, 5, 2, 5)
        ));
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
            @Override
            public void mousePressed(MouseEvent e) {
                button.setBackground(bgColor.darker().darker());
            }
            @Override
            public void mouseReleased(MouseEvent e) {
                button.setBackground(bgColor.darker());
            }
        });
    }

    private void showPasswordDialog(String bookId) {
        JPasswordField passwordField = new JPasswordField(20);
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.add(new JLabel("Masukkan Password:"), BorderLayout.NORTH);
        panel.add(passwordField, BorderLayout.CENTER);

        int result = JOptionPane.showConfirmDialog(this, panel, "Autentikasi Diperlukan", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result == JOptionPane.OK_OPTION) {
            String enteredPassword = new String(passwordField.getPassword());
            if ("admin1234".equals(enteredPassword)) {
                deleteBook(bookId);
            } else {
                JOptionPane.showMessageDialog(this, "Password salah!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void deleteBook(String bookId) {
        Book bookToDelete = dataStorage.getBooks().stream()
                .filter(book -> book.getId().equals(bookId))
                .findFirst()
                .orElse(null);

        if (bookToDelete != null) {
            if (bookToDelete.isBorrowed()) {
                JOptionPane.showMessageDialog(this, "Buku sedang dipinjam dan tidak dapat dihapus.", "Peringatan", JOptionPane.WARNING_MESSAGE);
            } else {
                dataStorage.getBooks().remove(bookToDelete);
                dataStorage.saveBooks();
                updateTable();
                JOptionPane.showMessageDialog(this, "Buku berhasil dihapus.", "Sukses", JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }

    private void clearFields() {
        idField.setText("");
        titleField.setText("");
        authorField.setText("");
        yearField.setText("");
        subjectField.setText("");
    }

    // Renderer untuk tombol
    private class ButtonRenderer extends JButton implements TableCellRenderer {
        public ButtonRenderer() {
            setOpaque(true);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            if (value instanceof JButton) {
                JButton button = (JButton) value;
                button.setBackground(isSelected ? button.getBackground().brighter() : button.getBackground());
                return button;
            }
            return this;
        }
    }

    // Editor untuk tombol
    private class ButtonEditor extends DefaultCellEditor {
        private JButton button;
        private String bookId;
        private boolean isPushed;

        public ButtonEditor(JCheckBox checkBox) {
            super(checkBox);
            button = new JButton();
            button.setOpaque(true);
            button.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    fireEditingStopped();
                }
            });
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            if (value instanceof JButton) {
                button = (JButton) value;
                bookId = (String) table.getValueAt(row, 1); // Ambil ID buku
                isPushed = true;
                return button;
            }
            return button;
        }

        @Override
        public Object getCellEditorValue() {
            if (isPushed) {
                showPasswordDialog(bookId);
            }
            isPushed = false;
            return button;
        }

        @Override
        public boolean stopCellEditing() {
            isPushed = false;
            return super.stopCellEditing();
        }
    }
}