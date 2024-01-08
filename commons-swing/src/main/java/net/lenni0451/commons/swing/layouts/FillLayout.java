package net.lenni0451.commons.swing.layouts;

import java.awt.*;

/**
 * A layout that fills the whole parent container.
 */
public class FillLayout implements LayoutManager {

    @Override
    public void addLayoutComponent(String name, Component comp) {
    }

    @Override
    public void removeLayoutComponent(Component comp) {
    }

    @Override
    public Dimension preferredLayoutSize(Container parent) {
        return parent.getSize();
    }

    @Override
    public Dimension minimumLayoutSize(Container parent) {
        return parent.getMinimumSize();
    }

    @Override
    public void layoutContainer(Container parent) {
        for (Component component : parent.getComponents()) {
            if (!component.isVisible()) continue;
            component.setBounds(0, 0, parent.getWidth(), parent.getHeight());
        }
    }

}
