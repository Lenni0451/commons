package net.lenni0451.commons.swing.layouts;

import java.awt.*;

/**
 * A layout that aligns all components vertically.<br>
 * Optionally a padding and a gap between the components can be specified.
 */
public class VerticalLayout implements LayoutManager {

    private final int padding;
    private final int gap;

    public VerticalLayout() {
        this(0, 0);
    }

    public VerticalLayout(final int padding, final int gap) {
        this.padding = padding;
        this.gap = gap;
    }

    /**
     * @return The padding between the components and the parent container
     */
    public int getPadding() {
        return this.padding;
    }

    /**
     * @return The gap between the components
     */
    public int getGap() {
        return this.gap;
    }

    @Override
    public void addLayoutComponent(String name, Component comp) {
    }

    @Override
    public void removeLayoutComponent(Component comp) {
    }

    @Override
    public Dimension preferredLayoutSize(Container parent) {
        int width = 0;
        int height = 0;
        for (Component component : parent.getComponents()) {
            if (!component.isVisible()) continue;
            Dimension preferredSize = component.getPreferredSize();
            width = Math.max(width, preferredSize.width);
            height += this.gap + preferredSize.height;
        }
        if (height > 0) height -= this.gap;

        Insets insets = parent.getInsets();
        width += insets.left + insets.right;
        height += insets.top + insets.bottom;
        return new Dimension(width + this.padding * 2, height + this.padding * 2);
    }

    @Override
    public Dimension minimumLayoutSize(Container parent) {
        return this.preferredLayoutSize(parent);
    }

    @Override
    public void layoutContainer(Container parent) {
        Insets insets = parent.getInsets();
        int width = parent.getWidth() - insets.left - insets.right - this.padding * 2;
        int height = insets.top + this.padding;
        for (Component component : parent.getComponents()) {
            if (!component.isVisible()) continue;
            component.setBounds(insets.left + this.padding, height, width, component.getPreferredSize().height);
            height += component.getSize().height + this.gap;
        }
    }

}
