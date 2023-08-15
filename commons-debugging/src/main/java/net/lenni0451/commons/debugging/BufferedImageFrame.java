package net.lenni0451.commons.debugging;

import javax.annotation.Nullable;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * A simple JFrame that displays a BufferedImage.
 */
public class BufferedImageFrame extends JFrame {

    private BufferedImage image;

    public BufferedImageFrame(@Nullable final BufferedImage image) {
        this.image = image;

        this.setTitle("BufferedImage Frame");
        this.setSize(500, 500);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.initPanes();
        this.setVisible(true);
    }

    private void initPanes() {
        JPanel panel = new JPanel() {
            @Override
            public void paint(Graphics g) {
                if (BufferedImageFrame.this.image == null) {
                    g.setColor(Color.WHITE);
                    g.fillRect(0, 0, this.getWidth(), this.getHeight());
                    g.setColor(Color.BLACK);
                    g.drawString("No Image", 0, 10);
                } else {
                    g.drawImage(BufferedImageFrame.this.image, 0, 0, this.getWidth(), this.getHeight(), null);
                }
            }
        };
        this.add(panel);
    }

    public void setImage(@Nullable final BufferedImage image) {
        this.image = image;
        this.repaint();
    }

}
