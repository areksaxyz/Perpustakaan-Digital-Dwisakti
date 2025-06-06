package ui;

import model.Book;
import storage.DataStorage;

import javax.swing.*;
import java.awt.*;

public class ReaderPanel extends JPanel {
    private DataStorage dataStorage;

    public ReaderPanel(DataStorage dataStorage) {
        this.dataStorage = dataStorage;
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JComboBox<String> bookBox = new JComboBox<>();
        for (Book book : dataStorage.getBooks()) {
            if (book.isDigital() && !book.getUrl().isEmpty() && !book.isBorrowed()) {
                bookBox.addItem(book.getTitle());
            }
        }
        JButton readButton = new JButton("Baca Sekarang");
        readButton.setBackground(new Color(0, 123, 255));
        readButton.setForeground(Color.WHITE);
        readButton.setFocusPainted(false);

        readButton.addActionListener(e -> {
            String selectedBook = (String) bookBox.getSelectedItem();
            Book book = dataStorage.getBooks().stream()
                    .filter(b -> b.getTitle().equals(selectedBook))
                    .findFirst()
                    .orElse(null);
            if (book != null && !book.getUrl().isEmpty()) {
                try {
                    java.awt.Desktop.getDesktop().browse(new java.net.URI(book.getUrl()));
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Gagal membuka PDF: " + ex.getMessage());
                }
            }
        });

        JPanel selectionPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        selectionPanel.add(new JLabel("Pilih Buku Digital:"));
        selectionPanel.add(bookBox);
        selectionPanel.add(readButton);

        add(selectionPanel, BorderLayout.NORTH);
        JTextArea infoArea = new JTextArea("Pilih buku digital di atas untuk membaca.\nBuku fisik tidak dapat dibaca di sini.");
        infoArea.setEditable(false);
        infoArea.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        add(new JScrollPane(infoArea), BorderLayout.CENTER);
    }
}