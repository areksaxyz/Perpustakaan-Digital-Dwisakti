package ui;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class PDFViewer extends JPanel {
    private PDDocument document;
    private PDFRenderer pdfRenderer;
    private JLabel pdfLabel;
    private int currentPage = 0;
    private int totalPages;

    public PDFViewer(String title, PDDocument document) throws IOException {
        this.document = document;
        this.pdfRenderer = new PDFRenderer(document);
        this.totalPages = document.getNumberOfPages();
        setLayout(new BorderLayout());

        pdfLabel = new JLabel();
        JScrollPane scrollPane = new JScrollPane(pdfLabel);
        add(scrollPane, BorderLayout.CENTER);

        JPanel navPanel = new JPanel(new FlowLayout());
        JButton prevButton = new JButton("Halaman Sebelumnya");
        prevButton.setFont(new Font("Arial", Font.BOLD, 18));
        prevButton.setBackground(new Color(0, 123, 255));
        prevButton.setForeground(Color.WHITE);
        JButton nextButton = new JButton("Halaman Berikutnya");
        nextButton.setFont(new Font("Arial", Font.BOLD, 18));
        nextButton.setBackground(new Color(0, 123, 255));
        nextButton.setForeground(Color.WHITE);
        JLabel pageLabel = new JLabel("Halaman: 1 / " + totalPages);
        pageLabel.setFont(new Font("Arial", Font.PLAIN, 18));

        navPanel.add(prevButton);
        navPanel.add(pageLabel);
        navPanel.add(nextButton);
        add(navPanel, BorderLayout.SOUTH);

        prevButton.addActionListener(e -> {
            if (currentPage > 0) {
                currentPage--;
                updatePage(pageLabel);
            }
        });

        nextButton.addActionListener(e -> {
            if (currentPage < totalPages - 1) {
                currentPage++;
                updatePage(pageLabel);
            }
        });

        updatePage(pageLabel);
    }

    private void updatePage(JLabel pageLabel) {
        try {
            BufferedImage image = pdfRenderer.renderImageWithDPI(currentPage, 100);
            pdfLabel.setIcon(new ImageIcon(image));
            pageLabel.setText("Halaman: " + (currentPage + 1) + " / " + totalPages);
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, "Gagal merender halaman: " + ex.getMessage());
        }
    }

    @Override
    public void removeNotify() {
        try {
            if (document != null) {
                document.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        super.removeNotify();
    }
}