package ui;

import storage.DataStorage;
import javax.swing.*;
import java.awt.*;

public class HomePanel extends JPanel {
    private DataStorage dataStorage;

    public HomePanel(DataStorage dataStorage) {
        this.dataStorage = dataStorage;
        setLayout(new BorderLayout());
        setBackground(new Color(245, 247, 250)); // Background abu-abu terang
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // Header dengan gradien, ikon, dan efek shadow
        JPanel headerPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                GradientPaint gp = new GradientPaint(0, 0, new Color(48, 79, 254), getWidth(), 0, new Color(121, 134, 203));
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());
                // Efek shadow di bawah header
                g2d.setColor(new Color(0, 0, 0, 60));
                g2d.fillRect(0, getHeight() - 5, getWidth(), 5);
            }
        };
        headerPanel.setPreferredSize(new Dimension(getWidth(), 100));
        headerPanel.setLayout(new BorderLayout());
        headerPanel.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 20));

        JLabel titleLabel = new JLabel("Beranda Perpustakaan Digital");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 32));
        titleLabel.setForeground(Color.WHITE);
        // Tambahkan ikon di sebelah judul (pastikan file ikon ada)
        try {
            titleLabel.setIcon(new ImageIcon(new ImageIcon("src/resources/library-icon.png").getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH)));
        } catch (Exception e) {
            System.err.println("Ikon library-icon.png tidak ditemukan: " + e.getMessage());
        }
        headerPanel.add(titleLabel, BorderLayout.WEST);
        add(headerPanel, BorderLayout.NORTH);

        // Panel utama hanya dengan foto
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(245, 247, 250));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));

        // Panel untuk foto
        JPanel photoPanel = new JPanel(new GridLayout(1, 2, 20, 0));
        photoPanel.setBackground(new Color(245, 247, 250));

        // Foto pertama (library.jpg)
        JLabel photoLabel1;
        try {
            ImageIcon imageIcon1 = new ImageIcon("src/resources/library.jpg");
            Image image1 = imageIcon1.getImage().getScaledInstance(450, 300, Image.SCALE_SMOOTH);
            photoLabel1 = new JLabel(new ImageIcon(image1));
        } catch (Exception e) {
            System.err.println("Foto library.jpg tidak ditemukan: " + e.getMessage());
            photoLabel1 = new JLabel("Foto 1 Tidak Tersedia", SwingConstants.CENTER);
            photoLabel1.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        }
        photoLabel1.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        photoLabel1.setBackground(Color.WHITE);
        photoLabel1.setOpaque(true);
        // Efek shadow pada foto
        photoLabel1.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 5, 5, new Color(0, 0, 0, 60)),
                photoLabel1.getBorder()));

        // Foto kedua (library2.jpg)
        JLabel photoLabel2;
        try {
            ImageIcon imageIcon2 = new ImageIcon("src/resources/library2.jpg");
            Image image2 = imageIcon2.getImage().getScaledInstance(450, 300, Image.SCALE_SMOOTH);
            photoLabel2 = new JLabel(new ImageIcon(image2));
        } catch (Exception e) {
            System.err.println("Foto library2.jpg tidak ditemukan: " + e.getMessage());
            photoLabel2 = new JLabel("Foto 2 Tidak Tersedia", SwingConstants.CENTER);
            photoLabel2.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        }
        photoLabel2.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        photoLabel2.setBackground(Color.WHITE);
        photoLabel2.setOpaque(true);
        // Efek shadow pada foto
        photoLabel2.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 5, 5, new Color(0, 0, 0, 60)),
                photoLabel2.getBorder()));

        photoPanel.add(photoLabel1);
        photoPanel.add(photoLabel2);

        mainPanel.add(photoPanel, BorderLayout.CENTER);

        add(mainPanel, BorderLayout.CENTER);

        // Footer sederhana
        JPanel footerPanel = new JPanel();
        footerPanel.setBackground(new Color(48, 79, 254));
        footerPanel.setPreferredSize(new Dimension(getWidth(), 40));
        JLabel footerLabel = new JLabel("© 2025 Perpustakaan Digital - All Rights Reserved");
        footerLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        footerLabel.setForeground(Color.WHITE);
        footerPanel.add(footerLabel);
        add(footerPanel, BorderLayout.SOUTH);

        System.out.println("HomePanel dimuat.");
    }

    public void refresh() {
        revalidate();
        repaint();
    }
}