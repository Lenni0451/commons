package net.lenni0451.commons.swing.components;

import javax.swing.*;
import java.awt.*;

/**
 * An invisible panel with a fixed size.<br>
 * Can be used to add empty space between components.
 */
public class InvisiblePanel extends JPanel {

    private final int width;
    private final int height;

    public InvisiblePanel(final int width, final int height) {
        this.width = width;
        this.height = height;

        this.setOpaque(false);
    }

    @Override
    public int getWidth() {
        return this.width;
    }

    @Override
    public int getHeight() {
        return this.height;
    }

    @Override
    public Dimension getSize(Dimension rv) {
        return new Dimension(this.width, this.height);
    }

    @Override
    public Dimension getSize() {
        return new Dimension(this.width, this.height);
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(this.width, this.height);
    }

    @Override
    protected void paintComponent(Graphics g) {
    }

}
