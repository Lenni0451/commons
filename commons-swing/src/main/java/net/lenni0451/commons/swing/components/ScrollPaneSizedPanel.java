package net.lenni0451.commons.swing.components;

import javax.swing.*;
import java.awt.*;

/**
 * A {@link JPanel} that returns the width of a {@link JScrollPane} viewport as its preferred width.<br>
 * Using this panel in a {@link JScrollPane} prevents a horizontal scrollbar from appearing when the preferred width of the panel is larger than the width of the viewport.
 */
public class ScrollPaneSizedPanel extends JPanel {

    private final JScrollPane scrollPane;

    public ScrollPaneSizedPanel(final JScrollPane scrollPane) {
        this.scrollPane = scrollPane;
    }

    @Override
    public Dimension getPreferredSize() {
        Dimension preferred = super.getPreferredSize();
        JViewport viewport = this.scrollPane.getViewport();
        preferred.width = Math.min(preferred.width, viewport.getWidth());
//        preferred.height = Math.min(preferred.height, viewport.getHeight());
        return preferred;
    }

}
