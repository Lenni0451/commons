package net.lenni0451.commons.swing.components;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.MouseAdapter;

/**
 * A panel that captures all mouse and keyboard events and paints a background color.<br>
 * The background color can be translucent to make the underlying components visible.<br>
 * Intended for use as an overlay/JFrame glass pane.
 */
public abstract class OverlayPanel extends JPanel {

    private static final MouseAdapter VOID_MOUSE_ADAPTER = new MouseAdapter() {
    };
    private static final KeyAdapter VOID_KEY_ADAPTER = new KeyAdapter() {
    };
    private static final FocusAdapter STEALING_FOCUS_ADAPTER = new FocusAdapter() {
        @Override
        public void focusLost(FocusEvent e) {
            e.getComponent().requestFocus();
        }
    };


    private final Color backgroundColor;

    public OverlayPanel(final Color backgroundColor) {
        this.backgroundColor = backgroundColor;

        this.setOpaque(false);
    }

    @Override
    public void addNotify() {
        super.addNotify();

        this.addMouseListener(VOID_MOUSE_ADAPTER);
        this.addMouseMotionListener(VOID_MOUSE_ADAPTER);
        this.addMouseWheelListener(VOID_MOUSE_ADAPTER);
        this.addKeyListener(VOID_KEY_ADAPTER);
        this.addFocusListener(STEALING_FOCUS_ADAPTER);
    }

    @Override
    public void removeNotify() {
        super.removeNotify();

        this.removeMouseListener(VOID_MOUSE_ADAPTER);
        this.removeMouseMotionListener(VOID_MOUSE_ADAPTER);
        this.removeMouseWheelListener(VOID_MOUSE_ADAPTER);
        this.removeKeyListener(VOID_KEY_ADAPTER);
        this.removeFocusListener(STEALING_FOCUS_ADAPTER);
    }

    @Override
    protected void paintComponent(Graphics g) {
        g.setColor(this.backgroundColor);
        g.fillRect(0, 0, this.getWidth(), this.getHeight());
        this.paintOverlay(g);
        super.paintComponent(g);
    }

    /**
     * Paints the overlay on top of the background color.
     *
     * @param g The graphics object to paint on
     */
    protected abstract void paintOverlay(Graphics g);

}
