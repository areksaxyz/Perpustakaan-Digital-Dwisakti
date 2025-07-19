package ui;

import storage.DataStorage;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.ActionListener;

public class LoginPanel extends JPanel {
    private LibraryUI mainFrame;
    private DataStorage dataStorage;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton createAccountButton;
    private JRadioButton adminRadio, userRadio;
    private JLabel titleLabel; // Dideklarasikan di scope kelas

    public LoginPanel(LibraryUI frame) {
        this.mainFrame = frame;
        this.dataStorage = mainFrame.getDataStorage(); // Inisialisasi dataStorage
        initializeUI();
    }

    private void initializeUI() {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15);
        setBackground(new Color(187, 222, 251)); // Nuansa biru muda (#BBDEFB)

        // Panel logo dengan gradien
        JPanel logoPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                GradientPaint gp = new GradientPaint(0, 0, new Color(187, 222, 251), 0, getHeight(), new Color(144, 202, 249));
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());
                super.paintComponent(g);
            }
        };
        logoPanel.setOpaque(false);
        JLabel logoLabel = new JLabel("📚 Perpustakaan DwiSakti");
        logoLabel.setFont(new Font("Segoe UI", Font.BOLD, 32));
        logoLabel.setForeground(new Color(33, 150, 243)); // Biru tua (#2196F3)
        logoPanel.add(logoLabel);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        add(logoPanel, gbc);

        // Pilihan peran (Admin/User) dengan panel stylish
        JPanel rolePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        rolePanel.setOpaque(false);
        adminRadio = new JRadioButton("Admin");
        userRadio = new JRadioButton("User");
        adminRadio.setForeground(new Color(33, 150, 243));
        userRadio.setForeground(new Color(33, 150, 243));
        adminRadio.setOpaque(false);
        userRadio.setOpaque(false);
        ButtonGroup roleGroup = new ButtonGroup();
        roleGroup.add(adminRadio);
        roleGroup.add(userRadio);
        userRadio.setSelected(true); // Default ke User
        rolePanel.add(adminRadio);
        rolePanel.add(userRadio);
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        add(rolePanel, gbc);

        // Judul dinamis (sekarang sudah di scope kelas)
        titleLabel = new JLabel("Login sebagai User"); // Default ke User
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titleLabel.setForeground(new Color(33, 150, 243));
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        add(titleLabel, gbc);

        // Panel form dengan border halus
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(144, 202, 249), 2),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)));
        GridBagConstraints formGbc = new GridBagConstraints();
        formGbc.insets = new Insets(10, 10, 10, 10);
        formGbc.anchor = GridBagConstraints.WEST;

        // Username
        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        usernameLabel.setForeground(new Color(33, 150, 243));
        usernameField = new JTextField(20);
        usernameField.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        usernameField.setBackground(new Color(245, 245, 245));
        usernameField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(144, 202, 249), 1),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        formGbc.gridx = 0;
        formGbc.gridy = 0;
        formPanel.add(usernameLabel, formGbc);
        formGbc.gridx = 1;
        formPanel.add(usernameField, formGbc);

        // Password
        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        passwordLabel.setForeground(new Color(33, 150, 243));
        passwordField = new JPasswordField(20);
        passwordField.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        passwordField.setBackground(new Color(245, 245, 245));
        passwordField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(144, 202, 249), 1),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        formGbc.gridy = 1;
        formGbc.gridx = 0;
        formPanel.add(passwordLabel, formGbc);
        formGbc.gridx = 1;
        formPanel.add(passwordField, formGbc);

        gbc.gridy = 3;
        gbc.gridwidth = 2;
        add(formPanel, gbc);

        // Tombol Login
        loginButton = new JButton("Login");
        loginButton.setFont(new Font("Segoe UI", Font.BOLD, 16));
        loginButton.setBackground(new Color(33, 150, 243)); // Biru tua (#2196F3)
        loginButton.setForeground(Color.WHITE);
        loginButton.setFocusPainted(false);
        loginButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        loginButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        loginButton.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                loginButton.setBackground(new Color(25, 118, 210)); // Hover effect
            }
            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                loginButton.setBackground(new Color(33, 150, 243));
            }
        });
        loginButton.addActionListener(e -> authenticateUser()); 
        gbc.gridy = 4;
        gbc.gridx = 0;
        gbc.gridwidth = 1;
        add(loginButton, gbc);

        // Tombol Create New Account
        createAccountButton = new JButton("Create New Account");
        createAccountButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        createAccountButton.setBackground(new Color(76, 175, 80)); // Hijau (#4CAF50)
        createAccountButton.setForeground(Color.WHITE);
        createAccountButton.setFocusPainted(false);
        createAccountButton.setBorder(BorderFactory.createEmptyBorder(8, 20, 8, 20));
        createAccountButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        createAccountButton.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                createAccountButton.setBackground(new Color(56, 142, 60)); // Hover effect
            }
            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                createAccountButton.setBackground(new Color(76, 175, 80));
            }
        });
        createAccountButton.addActionListener(e -> mainFrame.getCardLayout().show(mainFrame.getCardPanel(), "CreateAccountPanel"));
        gbc.gridx = 1;
        add(createAccountButton, gbc);

        // Dukungan untuk tombol Enter
        KeyAdapter enterListener = new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    loginButton.doClick(); // Panggil aksi tombol login
                }
            }
        };
        usernameField.addKeyListener(enterListener);
        passwordField.addKeyListener(enterListener);

        // Action listener untuk peran radio button
        ActionListener roleListener = e -> {
            if (adminRadio.isSelected()) {
                titleLabel.setText("Login sebagai Admin");
                loginButton.setBackground(new Color(244, 67, 54)); // Merah untuk admin (#F44336)
            } else if (userRadio.isSelected()) {
                titleLabel.setText("Login sebagai User");
                loginButton.setBackground(new Color(33, 150, 243)); // Biru untuk user (#2196F3)
            }
        };
        adminRadio.addActionListener(roleListener);
        userRadio.addActionListener(roleListener);
    }

    private void authenticateUser() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword()).trim();

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Username dan password tidak boleh kosong!", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        if (!dataStorage.isUserExists(username)) {
            JOptionPane.showMessageDialog(this, "Username tidak ditemukan!", "Login Gagal", JOptionPane.ERROR_MESSAGE);
            System.out.println("Gagal login untuk " + username + ". Username tidak ditemukan.");
            return;
        }

        if (dataStorage.authenticateUser(username, password)) {
            String actualRole = dataStorage.getUserRole(username); // Ambil role sebenarnya dari database
            System.out.println("Login berhasil untuk " + username + " dengan role " + actualRole);
            JOptionPane.showMessageDialog(this, "Login berhasil sebagai " + (actualRole.equals("admin") ? "Admin" : "User " + username) + "!");
            mainFrame.onLoginSuccess(username, actualRole); // Kirim role yang sebenarnya
        } else {
            JOptionPane.showMessageDialog(this, "Password salah!", "Login Gagal", JOptionPane.ERROR_MESSAGE);
            System.out.println("Gagal login untuk " + username + ". Password salah.");
        }
        usernameField.setText("");
        passwordField.setText("");
    }
}