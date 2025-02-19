package net.lenni0451.commons.logging;

import net.lenni0451.commons.logging.impl.SysoutLogger;

import java.lang.reflect.Constructor;

/**
 * The global logger factory to create new loggers.<br>
 * It is recommended to use this factory instead of manually creating new loggers.
 * This allows users to easily change the logger implementation.
 */
public class LoggerFactory {

    /**
     * The system property to set the global logger builder.<br>
     * If the class cannot be found or an exception occurs, the default builder will be used.
     */
    public static final String SYSTEM_PROPERTY = "net.lenni0451.commons.logging.LoggerFactory";
    private static Builder builder;

    static {
        try {
            String builderName = System.getProperty(SYSTEM_PROPERTY);
            if (builderName == null) {
                //If no builder is set, use the default builder
                builder = new DefaultBuilder();
            } else {
                //Try to create the builder
                Class<?> builderClass = Class.forName(builderName);
                Constructor<?> constructor = builderClass.getDeclaredConstructor();
                constructor.setAccessible(true);
                builder = (Builder) constructor.newInstance();
            }
        } catch (Throwable t) {
            //If an exception occurs, just use the default builder
            builder = new DefaultBuilder();
        }
    }

    /**
     * Set the global logger builder.<br>
     * This will be used to create new loggers.
     *
     * @param builder The new logger builder
     */
    public static void setBuilder(final Builder builder) {
        LoggerFactory.builder = builder;
    }

    /**
     * Get a new logger with the given name.
     *
     * @param name The name of the logger
     * @return The new logger
     */
    public static Logger getLogger(final String name) {
        return builder.build(name);
    }

    /**
     * Get a new logger for the given class.
     *
     * @param clazz The class to create the logger for
     * @return The new logger
     */
    public static Logger getLogger(final Class<?> clazz) {
        return builder.build(clazz);
    }


    /**
     * The builder to create new loggers.
     */
    public interface Builder {
        /**
         * Build a new logger with the given name.
         *
         * @param name The name of the logger
         * @return The new logger
         */
        Logger build(final String name);

        /**
         * Build a new logger for the given class.
         *
         * @param clazz The class to create the logger for
         * @return The new logger
         */
        default Logger build(final Class<?> clazz) {
            return this.build(clazz.getSimpleName());
        }
    }

    private static class DefaultBuilder implements Builder {
        @Override
        public Logger build(final String name) {
            return SysoutLogger.builder().name(name).build();
        }
    }

}
