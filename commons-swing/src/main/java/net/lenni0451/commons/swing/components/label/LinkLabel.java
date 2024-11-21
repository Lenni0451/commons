package net.lenni0451.commons.swing.components.label;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URI;

/**
 * A JLabel that opens a link in the default browser when clicked.<br>
 * It has the correct cursor, color and underlining for a link.<br>
 * If an exception is thrown while opening the link, it will be passed to the {@link #exceptionCaught(Exception)} method which can be overridden.
 */
public class LinkLabel extends JLabel {

    public LinkLabel(final String text, final String url) {
        super("<html><a href=\"" + url + "\">" + text + "</a></html>");
        this.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(final MouseEvent e) {
                try {
                    Desktop.getDesktop().browse(new URI(url));
                } catch (Exception ex) {
                    LinkLabel.this.exceptionCaught(ex);
                }
            }
        });
    }

    protected void exceptionCaught(final Exception e) {
        e.printStackTrace();
    }

}
