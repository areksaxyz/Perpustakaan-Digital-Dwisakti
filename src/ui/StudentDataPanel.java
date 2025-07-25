package ui;

import storage.DataStorage;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import model.User;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.EventObject;

public class StudentDataPanel extends JPanel {
    private DataStorage dataStorage;
    private LibraryUI libraryUI;
    private String currentUsername;
    private String currentUserRole;
    private JTable table;
    private DefaultTableModel tableModel;

    public StudentDataPanel(DataStorage dataStorage, LibraryUI libraryUI, String currentUsername) {
        this.dataStorage = dataStorage;
        this.libraryUI = libraryUI;
        this.currentUsername = currentUsername;
        this.currentUserRole = (currentUsername != null && !currentUsername.isEmpty()) ? dataStorage.getUserRole(currentUsername) : null;
        initializeUI();
    }

    private void initializeUI() {
        setLayout(new BorderLayout());
        setBackground(new Color(245, 247, 250));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Header
        JPanel headerPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                GradientPaint gp = new GradientPaint(0, 0, new Color(0, 120, 215), getWidth(), 0, new Color(0, 150, 199));
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        headerPanel.setPreferredSize(new Dimension(getWidth(), 80));
        headerPanel.setLayout(new BorderLayout());
        JLabel titleLabel = new JLabel("Data Mahasiswa", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Roboto", Font.BOLD, 28));
        titleLabel.setForeground(Color.WHITE);
        headerPanel.add(titleLabel, BorderLayout.CENTER);
        add(headerPanel, BorderLayout.NORTH);

        if (currentUsername != null && !currentUsername.isEmpty()) {
            refresh(currentUsername);
        } else {
            JLabel noAccessLabel = new JLabel("Akses ditolak. Silakan login sebagai admin atau user.");
            noAccessLabel.setFont(new Font("Roboto", Font.PLAIN, 16));
            noAccessLabel.setForeground(Color.RED);
            noAccessLabel.setHorizontalAlignment(JLabel.CENTER);
            add(noAccessLabel, BorderLayout.CENTER);
        }
    }

    private void setupAdminView() {
        String[] columns = {"No", "Nama Lengkap", "NIM", "Kelas", "Username", "Aksi"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 5;
            }
        };
        table = new JTable(tableModel);
        table.setFillsViewportHeight(true);
        table.setRowHeight(35);
        table.setFont(new Font("Roboto", Font.PLAIN, 14));
        table.setSelectionBackground(new Color(200, 220, 255));
        table.setSelectionForeground(Color.BLACK);
        table.setGridColor(new Color(220, 220, 220));
        table.setShowGrid(true);

        JTableHeader tableHeader = table.getTableHeader();
        tableHeader.setFont(new Font("Roboto", Font.BOLD, 15));
        tableHeader.setBackground(new Color(0, 120, 215));
        tableHeader.setForeground(Color.WHITE);
        tableHeader.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));

        table.getColumnModel().getColumn(5).setCellRenderer(new ButtonRenderer("Hapus", new Color(244, 67, 54)));
        table.getColumnModel().getColumn(5).setCellEditor(new ButtonEditor(new JCheckBox(), "Hapus", dataStorage, libraryUI, currentUsername));

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));
        add(scrollPane, BorderLayout.CENTER);
    }

    private void setupUserView() {
        User user = dataStorage.getUserByUsername(currentUsername);
        if (user == null) {
            JLabel notFoundLabel = new JLabel("Data pengguna tidak ditemukan. Silakan coba login ulang.");
            notFoundLabel.setFont(new Font("Roboto", Font.PLAIN, 16));
            notFoundLabel.setForeground(Color.RED);
            notFoundLabel.setHorizontalAlignment(JLabel.CENTER);
            add(notFoundLabel, BorderLayout.CENTER);
            return;
        }

        JPanel cardHolderPanel = new JPanel(new GridBagLayout());
        cardHolderPanel.setOpaque(false);
        GridBagConstraints gbcHolder = new GridBagConstraints();
        gbcHolder.gridx = 0;
        gbcHolder.gridy = 0;
        gbcHolder.weightx = 1.0;
        gbcHolder.weighty = 1.0;
        gbcHolder.anchor = GridBagConstraints.CENTER;
        gbcHolder.fill = GridBagConstraints.NONE;

        JPanel cardPanel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                
                g2d.setColor(new Color(0,0,0,50));
                g2d.fillRoundRect(5, 5, getWidth() - 5, getHeight() - 5, 25, 25);

                GradientPaint gradient = new GradientPaint(0, 0, new Color(255, 255, 255), 0, getHeight(), new Color(230, 240, 255));
                g2d.setPaint(gradient);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 25, 25);

                g2d.setColor(new Color(0, 120, 215, 150));
                g2d.setStroke(new BasicStroke(2));
                g2d.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 25, 25);
            }
        };
        cardPanel.setPreferredSize(new Dimension(550, 400));
        cardPanel.setLayout(new BorderLayout(0, 10));
        cardPanel.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));
        cardPanel.setOpaque(false);

        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        titlePanel.setOpaque(false);
        JLabel titleLabel = new JLabel("KARTU IDENTITAS PESERTA");
        titleLabel.setFont(new Font("Roboto", Font.BOLD, 28));
        titleLabel.setForeground(new Color(0, 100, 180));
        titlePanel.add(titleLabel);
        cardPanel.add(titlePanel, BorderLayout.NORTH);

        JPanel mainInfoPanel = new JPanel(new BorderLayout(30, 0));
        mainInfoPanel.setOpaque(false);

        JPanel photoPanel = new JPanel();
        photoPanel.setOpaque(false);
        photoPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));
        
        final int TARGET_WIDTH = 180;
        final int TARGET_HEIGHT = 240; // Rasio 3:4

        BufferedImage originalImage = null;
        try {
            originalImage = ImageIO.read(getClass().getResource("/resources/cowok.png"));
        } catch (IOException e) {
            System.err.println("Gagal memuat gambar cowok.png: " + e.getMessage());
            originalImage = createPlaceholderImage(TARGET_WIDTH, TARGET_HEIGHT, "Foto Tidak Ditemukan", new Color(0, 120, 215), new Color(220, 220, 220));
        } catch (IllegalArgumentException e) {
            System.err.println("Path gambar cowok.png tidak valid. Pastikan ada di folder resources: " + e.getMessage());
            originalImage = createPlaceholderImage(TARGET_WIDTH, TARGET_HEIGHT, "Error Memuat Foto", new Color(0, 120, 215), new Color(220, 220, 220));
        }

        if (originalImage != null) {
            int originalWidth = originalImage.getWidth();
            int originalHeight = originalImage.getHeight();

            int scaledWidth = originalWidth;
            int scaledHeight = originalHeight;

            double aspectRatio = (double) originalWidth / originalHeight;
            double targetAspectRatio = (double) TARGET_WIDTH / TARGET_HEIGHT;

            if (aspectRatio > targetAspectRatio) {
                scaledWidth = TARGET_WIDTH;
                scaledHeight = (int) (TARGET_WIDTH / aspectRatio);
            } else {
                scaledHeight = TARGET_HEIGHT;
                scaledWidth = (int) (TARGET_HEIGHT * aspectRatio);
            }
            
            Image scaledImage = originalImage.getScaledInstance(scaledWidth, scaledHeight, Image.SCALE_SMOOTH);
            BufferedImage finalImage = new BufferedImage(TARGET_WIDTH, TARGET_HEIGHT, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2d = finalImage.createGraphics();
            
            g2d.setColor(Color.WHITE);
            g2d.fillRect(0, 0, TARGET_WIDTH, TARGET_HEIGHT);

            int x = (TARGET_WIDTH - scaledWidth) / 2;
            int y = (TARGET_HEIGHT - scaledHeight) / 2;
            g2d.drawImage(scaledImage, x, y, null);
            g2d.dispose();

            JLabel photoLabel = new JLabel(new ImageIcon(finalImage));
            photoLabel.setBorder(BorderFactory.createLineBorder(new Color(0, 120, 215), 2));
            photoPanel.add(photoLabel);
            photoPanel.setPreferredSize(new Dimension(TARGET_WIDTH, TARGET_HEIGHT));
        } else {
            JLabel photoLabel = new JLabel(new ImageIcon(createPlaceholderImage(TARGET_WIDTH, TARGET_HEIGHT, "Foto Tidak Ditemukan", new Color(0, 120, 215), new Color(220, 220, 220))));
            photoLabel.setBorder(BorderFactory.createLineBorder(new Color(0, 120, 215), 2));
            photoPanel.add(photoLabel);
            photoPanel.setPreferredSize(new Dimension(TARGET_WIDTH, TARGET_HEIGHT));
        }
        mainInfoPanel.add(photoPanel, BorderLayout.WEST);

        JPanel infoDetailPanel = new JPanel(new GridBagLayout());
        infoDetailPanel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 5, 8, 5);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        String fullName = user.getFullName();
        String userClass = user.getClassName();
        String nim = user.getNim();
        String memberId = "MEMBER-" + user.getUsername().toUpperCase();

        addInfoRow(infoDetailPanel, gbc, 0, "No. Anggota:", memberId, new Color(0, 120, 215), null);
        addInfoRow(infoDetailPanel, gbc, 1, "Nama Lengkap:", fullName, new Color(0, 120, 215), null);
        addInfoRow(infoDetailPanel, gbc, 2, "Kelas:", userClass, new Color(0, 120, 215), null);
        addInfoRow(infoDetailPanel, gbc, 3, "NIM:", nim, new Color(0, 120, 215), null);

        LocalDate currentDate = LocalDate.now();
        LocalDate expiryDate = currentDate.plusYears(4);
        JLabel creationDateLabel = new JLabel("Tgl. Pembuatan: " + currentDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        creationDateLabel.setFont(new Font("Roboto", Font.ITALIC, 13));
        creationDateLabel.setForeground(new Color(50, 50, 50));

        JLabel expiryDateLabel = new JLabel("Berlaku hingga: " + expiryDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        expiryDateLabel.setFont(new Font("Roboto", Font.ITALIC, 13));
        expiryDateLabel.setForeground(new Color(50, 50, 50));

        JPanel datePanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        datePanel.setOpaque(false);
        datePanel.add(creationDateLabel);
        datePanel.add(expiryDateLabel);

        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 2; gbc.anchor = GridBagConstraints.EAST;
        gbc.insets = new Insets(15, 5, 5, 5);
        infoDetailPanel.add(datePanel, gbc);

        mainInfoPanel.add(infoDetailPanel, BorderLayout.CENTER);
        cardPanel.add(mainInfoPanel, BorderLayout.CENTER);

        cardHolderPanel.add(cardPanel, gbcHolder);
        add(cardHolderPanel, BorderLayout.CENTER);
    }
    
    private BufferedImage createPlaceholderImage(int width, int height, String text, Color borderColor, Color bgColor) {
        BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = img.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setColor(bgColor);
        g2d.fillRoundRect(0, 0, width, height, 15, 15);
        g2d.setColor(borderColor);
        g2d.setStroke(new BasicStroke(2));
        g2d.drawRoundRect(0, 0, width - 1, height - 1, 15, 15);
        g2d.setColor(Color.BLACK);
        g2d.setFont(new Font("Roboto", Font.PLAIN, 16));
        FontMetrics fm = g2d.getFontMetrics();
        int x = (width - fm.stringWidth(text)) / 2;
        int y = (height - fm.getHeight()) / 2 + fm.getAscent();
        g2d.drawString(text, x, y);
        g2d.dispose();
        return img;
    }

    private void addInfoRow(JPanel panel, GridBagConstraints gbc, int gridY, String labelText, String valueText, Color labelColor, String iconPath) {
        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Roboto", Font.BOLD, 15));
        label.setForeground(labelColor);

        if (iconPath != null && !iconPath.isEmpty()) {
            try {
                ImageIcon originalIcon = new ImageIcon(getClass().getResource(iconPath));
                if (originalIcon.getImageLoadStatus() == MediaTracker.COMPLETE && originalIcon.getImage() != null) {
                    Image scaledImage = originalIcon.getImage().getScaledInstance(18, 18, Image.SCALE_SMOOTH);
                    label.setIcon(new ImageIcon(scaledImage));
                    label.setIconTextGap(8);
                } else {
                    System.err.println("Gagal memuat ikon (ImageLoadStatus != COMPLETE): " + iconPath);
                }
            } catch (Exception e) {
                System.err.println("Gagal memuat ikon: " + iconPath + " - " + e.getMessage());
            }
        }

        JLabel value = new JLabel(valueText);
        value.setFont(new Font("Roboto", Font.PLAIN, 15));
        value.setForeground(Color.BLACK);

        gbc.gridx = 0; gbc.gridy = gridY;
        panel.add(label, gbc);
        gbc.gridx = 1;
        panel.add(value, gbc);
    }

    public void refresh(String username) {
        this.currentUsername = username;
        this.currentUserRole = (username != null && !username.isEmpty()) ? dataStorage.getUserRole(username) : null;
        System.out.println("Refreshing StudentDataPanel with username: " + username + ", role: " + currentUserRole);

        removeAll();

        JPanel headerPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                GradientPaint gp = new GradientPaint(0, 0, new Color(0, 120, 215), getWidth(), 0, new Color(0, 150, 199));
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        headerPanel.setPreferredSize(new Dimension(getWidth(), 80));
        headerPanel.setLayout(new BorderLayout());
        JLabel titleLabel = new JLabel("Data Mahasiswa", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Roboto", Font.BOLD, 28));
        titleLabel.setForeground(Color.WHITE);
        headerPanel.add(titleLabel, BorderLayout.CENTER);
        add(headerPanel, BorderLayout.NORTH);

        if ("admin".equals(currentUserRole)) {
            setupAdminView();
            updateTableData();
        } else if ("user".equals(currentUserRole)) {
            setupUserView();
        } else {
            JLabel noAccessLabel = new JLabel("Akses ditolak. Silakan login sebagai admin atau user.");
            noAccessLabel.setFont(new Font("Roboto", Font.PLAIN, 16));
            noAccessLabel.setForeground(Color.RED);
            noAccessLabel.setHorizontalAlignment(JLabel.CENTER);
            add(noAccessLabel, BorderLayout.CENTER);
            System.out.println("Akses ditolak untuk role: " + currentUserRole);
        }

        revalidate();
        repaint();
    }

    private void updateTableData() {
        if (tableModel == null) {
            setupAdminView();
        }
        tableModel.setRowCount(0); // Hapus semua baris lama
        int rowNumber = 1;
        try {
            java.util.List<User> users = dataStorage.getUsers();
            for (User user : users) {
                if (!"admin".equals(user.getUsername()) && !user.getUsername().equals(currentUsername)) { // Pastikan admin dan user sendiri tidak muncul
                    tableModel.addRow(new Object[]{
                        rowNumber++,
                        user.getFullName(),
                        user.getNim(),
                        user.getClassName(),
                        user.getUsername(),
                        "Hapus"
                    });
                }
            }
            System.out.println("Tabel admin diperbarui dengan " + tableModel.getRowCount() + " pengguna.");
        } catch (Exception e) {
            System.err.println("Gagal memuat data pengguna untuk admin: " + e.getMessage());
            JOptionPane.showMessageDialog(this, "Gagal memuat data mahasiswa untuk admin.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Renderer untuk JButton di JTable
    class ButtonRenderer extends JButton implements javax.swing.table.TableCellRenderer {
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
            if (value instanceof String) {
                setText((String) value);
            }
            if (isSelected) {
                setBackground(bgColor.darker());
            } else {
                setBackground(bgColor);
            }
            
            if (table.getModel().getRowCount() > row && ("admin".equals(table.getModel().getValueAt(row, 4)) || 
                    table.getModel().getValueAt(row, 4).equals(currentUsername))) {
                setText("Tidak Bisa Dihapus");
                setEnabled(false);
                setBackground(new Color(150, 150, 150));
            } else {
                setEnabled(true);
            }
            return this;
        }
    }

    // Editor untuk JButton di JTable
    class ButtonEditor extends DefaultCellEditor {
        protected JButton button;
        private String label;
        private boolean isPushed;
        private DataStorage dataStorageRef; 
        private LibraryUI libraryUIRef;     
        private String currentUsernameRef;  

        public ButtonEditor(JCheckBox checkBox, String text, DataStorage dataStorage, LibraryUI libraryUI, String currentUsername) {
            super(checkBox);
            this.dataStorageRef = dataStorage;
            this.libraryUIRef = libraryUI;
            this.currentUsernameRef = currentUsername;

            button = new JButton();
            button.setOpaque(true);
            button.setFont(new Font("Roboto", Font.BOLD, 12));
            button.setForeground(Color.WHITE);
            button.setBackground(new Color(244, 67, 54)); 
            button.setFocusPainted(false);
            button.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
            button.setCursor(new Cursor(Cursor.HAND_CURSOR));

            button.addActionListener(e -> {
                if (isPushed) {
                    fireEditingStopped();
                }
            });
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            if (isSelected) {
                button.setBackground(button.getBackground().darker());
            } else {
                button.setBackground(new Color(244, 67, 54));
            }
            
            String usernameToActOn = (String) table.getModel().getValueAt(row, 4);
            if ("admin".equals(usernameToActOn) || usernameToActOn.equals(currentUsernameRef)) {
                button.setText("Tidak Bisa Dihapus");
                button.setEnabled(false);
                button.setBackground(new Color(150, 150, 150));
            } else {
                label = (value == null) ? "" : value.toString();
                button.setText(label);
                button.setEnabled(true);
                button.setBackground(new Color(244, 67, 54));
            }
            isPushed = true;
            return button;
        }

        @Override
        public Object getCellEditorValue() {
            isPushed = false;
            return label;
        }

        @Override
        public boolean stopCellEditing() {
            isPushed = false;
            return super.stopCellEditing();
        }

        @Override
        protected void fireEditingStopped() {
            if (isPushed && button.isEnabled()) {
                JTable table = (JTable) SwingUtilities.getAncestorOfClass(JTable.class, button);
                if (table != null) {
                    int selectedRow = table.getEditingRow();
                    if (selectedRow >= 0 && selectedRow < table.getRowCount()) {
                        String usernameFromTable = (String) table.getValueAt(selectedRow, 4);

                        int confirm = JOptionPane.showConfirmDialog(StudentDataPanel.this,
                                "Apakah Anda yakin ingin menghapus akun '" + usernameFromTable + "'?",
                                "Konfirmasi Hapus Akun",
                                JOptionPane.YES_NO_OPTION);
                        if (confirm == JOptionPane.YES_OPTION) {
                            boolean deleted = dataStorageRef.deleteUser(usernameFromTable);
                            if (deleted) {
                                JOptionPane.showMessageDialog(StudentDataPanel.this, "Akun '" + usernameFromTable + "' berhasil dihapus.", "Sukses", JOptionPane.INFORMATION_MESSAGE);
                                StudentDataPanel parentPanel = (StudentDataPanel) SwingUtilities.getAncestorOfClass(StudentDataPanel.class, button);
                                if (parentPanel != null) {
                                    parentPanel.updateTableData();
                                }
                                if (libraryUIRef != null) {
                                    libraryUIRef.updateAllPanels();
                                }
                            } else {
                                JOptionPane.showMessageDialog(StudentDataPanel.this, "Gagal menghapus akun '" + usernameFromTable + "'.", "Error", JOptionPane.ERROR_MESSAGE);
                            }
                        }
                    } else {
                        System.err.println("Indeks baris tidak valid: " + selectedRow);
                    }
                } else {
                    System.err.println("Tidak dapat menemukan JTable yang sesuai.");
                }
            }
            super.fireEditingStopped();
        }

        @Override
        public boolean isCellEditable(EventObject e) {
            return true;
        }
    }
}
