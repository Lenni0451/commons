package net.lenni0451.commons.swing.utils;

import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;

import javax.swing.*;

@UtilityClass
public class SwingTheming {

    /**
     * Set the system look and feel for the current application.
     *
     * @throws UnsupportedLookAndFeelException If the system look and feel is not supported
     * @throws ClassNotFoundException          If the system look and feel class could not be found
     * @throws InstantiationException          If a new instance of the system look and feel could not be created
     * @throws IllegalAccessException          If the system look and feel class is not accessible
     */
    @SneakyThrows
    public static void setSystemLookAndFeel() {
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
    }

}
