package ui;

import storage.DataStorage;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class CreateAccountPanel extends JPanel {
    private LibraryUI libraryUI;
    private DataStorage dataStorage;
    private JTextField fullNameField;
    private JTextField nimField;
    private JTextField classField;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JPasswordField confirmPasswordField;

    public CreateAccountPanel(LibraryUI libraryUI, DataStorage dataStorage) {
        this.libraryUI = libraryUI;
        this.dataStorage = dataStorage;
        setLayout(new GridBagLayout());
        setBackground(new Color(245, 247, 250));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Panel utama untuk form pendaftaran
        JPanel registerFormPanel = new JPanel(new GridBagLayout());
        registerFormPanel.setBackground(Color.WHITE);
        registerFormPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));

        JLabel titleLabel = new JLabel("Buat Akun Baru", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Roboto", Font.BOLD, 24));
        titleLabel.setForeground(new Color(25, 118, 210));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        registerFormPanel.add(titleLabel, gbc);

        // Field Nama Lengkap
        gbc.gridwidth = 1;
        gbc.gridy = 1;
        registerFormPanel.add(new JLabel("Nama Lengkap:"), gbc);
        gbc.gridx = 1;
        fullNameField = new JTextField(20);
        registerFormPanel.add(fullNameField, gbc);

        // Field NIM
        gbc.gridx = 0;
        gbc.gridy = 2;
        registerFormPanel.add(new JLabel("NIM:"), gbc);
        gbc.gridx = 1;
        nimField = new JTextField(20);
        nimField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                if (!Character.isDigit(e.getKeyChar())) {
                    e.consume(); // Hanya angka
                }
            }
        });
        registerFormPanel.add(nimField, gbc);

        // Field Kelas
        gbc.gridx = 0;
        gbc.gridy = 3;
        registerFormPanel.add(new JLabel("Kelas:"), gbc);
        gbc.gridx = 1;
        classField = new JTextField(20);
        registerFormPanel.add(classField, gbc);

        // Field Username
        gbc.gridx = 0;
        gbc.gridy = 4;
        registerFormPanel.add(new JLabel("Username:"), gbc);
        gbc.gridx = 1;
        usernameField = new JTextField(20);
        registerFormPanel.add(usernameField, gbc);

        // Field Password
        gbc.gridx = 0;
        gbc.gridy = 5;
        registerFormPanel.add(new JLabel("Password:"), gbc);
        gbc.gridx = 1;
        passwordField = new JPasswordField(20);
        registerFormPanel.add(passwordField, gbc);

        // Field Konfirmasi Password
        gbc.gridx = 0;
        gbc.gridy = 6;
        registerFormPanel.add(new JLabel("Konfirmasi Password:"), gbc);
        gbc.gridx = 1;
        confirmPasswordField = new JPasswordField(20);
        registerFormPanel.add(confirmPasswordField, gbc);

        // Tombol Daftar
        JButton registerButton = new JButton("Daftar");
        registerButton.setFont(new Font("Roboto", Font.BOLD, 16));
        registerButton.setBackground(new Color(76, 175, 80));
        registerButton.setForeground(Color.WHITE);
        registerButton.setFocusPainted(false);
        registerButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        registerButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        registerButton.addActionListener(e -> registerUser());
        
        JButton backButton = new JButton("Kembali ke Login");
        backButton.setFont(new Font("Roboto", Font.BOLD, 16));
        backButton.setBackground(new Color(25, 118, 210));
        backButton.setForeground(Color.WHITE);
        backButton.setFocusPainted(false);
        backButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        backButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        backButton.addActionListener(e -> libraryUI.getCardLayout().show(libraryUI.getCardPanel(), "LoginPanel"));

        gbc.gridx = 0;
        gbc.gridy = 7;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(20, 10, 10, 10);
        registerFormPanel.add(registerButton, gbc);

        gbc.gridy = 8;
        gbc.insets = new Insets(0, 10, 20, 10);
        registerFormPanel.add(backButton, gbc);

        add(registerFormPanel, new GridBagConstraints());
    }

    private void registerUser() {
        String fullName = fullNameField.getText().trim();
        String nim = nimField.getText().trim();
        String className = classField.getText().trim();
        String username = usernameField.getText().trim();
        char[] passwordChars = passwordField.getPassword();
        char[] confirmPasswordChars = confirmPasswordField.getPassword();

        String password = new String(passwordChars);
        String confirmPassword = new String(confirmPasswordChars);

        if (fullName.isEmpty() || nim.isEmpty() || className.isEmpty() || username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Semua kolom harus diisi.", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (!password.equals(confirmPassword)) {
            JOptionPane.showMessageDialog(this, "Password dan konfirmasi password tidak cocok.", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (dataStorage.isUserExists(username)) {
            JOptionPane.showMessageDialog(this, "Username sudah ada. Pilih username lain.", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Panggil metode saveUser yang sudah diperbarui di DataStorage
        dataStorage.saveUser(username, password, "user", fullName, className, nim);
        
        JOptionPane.showMessageDialog(this, "Akun berhasil dibuat! Silakan login.", "Sukses", JOptionPane.INFORMATION_MESSAGE);
        
        // Kosongkan field setelah pendaftaran berhasil
        fullNameField.setText("");
        nimField.setText("");
        classField.setText("");
        usernameField.setText("");
        passwordField.setText("");
        confirmPasswordField.setText("");

        // Refresh panel data mahasiswa di admin jika admin sedang login
        libraryUI.updateAllStudentPanels(); 

        libraryUI.getCardLayout().show(libraryUI.getCardPanel(), "LoginPanel");
    }
}