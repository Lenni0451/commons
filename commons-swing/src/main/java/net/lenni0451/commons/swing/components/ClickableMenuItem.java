package net.lenni0451.commons.swing.components;

import javax.swing.*;
import java.awt.event.ActionListener;

/**
 * A JMenuItem which accepts a click listener in the constructor.
 */
public class ClickableMenuItem extends JMenuItem {

    public ClickableMenuItem(final String text, final Runnable onClick) {
        super(text);
        this.addActionListener(e -> onClick.run());
    }

    public ClickableMenuItem(final String text, final ActionListener onClick) {
        super(text);
        this.addActionListener(onClick);
    }

}
