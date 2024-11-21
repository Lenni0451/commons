package net.lenni0451.commons.swing.utils;

import lombok.experimental.UtilityClass;

import java.awt.*;
import java.util.function.Consumer;

@UtilityClass
public class ComponentUtils {

    /**
     * Recursively iterate over all components of the given component and all its children.<br>
     * The root component will be included in the iteration.
     *
     * @param component The component to start the iteration from
     * @param consumer  The consumer that will be called for each component
     */
    public static void iterate(final Component component, final Consumer<Component> consumer) {
        consumer.accept(component);
        if (component instanceof Container) {
            Container container = (Container) component;
            for (Component c : container.getComponents()) {
                iterate(c, consumer);
            }
        }
    }

}
