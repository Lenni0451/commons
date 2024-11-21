package net.lenni0451.commons.swing.components;

import net.lenni0451.commons.swing.utils.ComponentUtils;

import javax.swing.*;

/**
 * A JFileChooser that displays the files in a vertical list instead of a horizontal list.
 */
public class VerticalFileChooser extends JFileChooser {

    public VerticalFileChooser() {
        ComponentUtils.iterate(this, component -> {
            if (component instanceof JList<?>) {
                JList<?> list = (JList<?>) component;
                list.setLayoutOrientation(JList.VERTICAL);
            }
        });
    }

}
