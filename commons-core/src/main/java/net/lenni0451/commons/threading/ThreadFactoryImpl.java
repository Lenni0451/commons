package net.lenni0451.commons.threading;

import javax.annotation.Nonnull;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * A simple thread factory implementation.
 */
public class ThreadFactoryImpl implements ThreadFactory {

    /**
     * Create a new thread factory.
     *
     * @return The thread factory
     */
    public static ThreadFactoryImpl of() {
        return new ThreadFactoryImpl();
    }

    /**
     * Create a new thread factory with a name.
     *
     * @param name The name of the threads
     * @return The thread factory
     */
    public static ThreadFactoryImpl of(final String name) {
        return new ThreadFactoryImpl().name(name);
    }

    /**
     * Create a new thread factory with a daemon state.
     *
     * @param daemon Whether the threads should be daemon threads
     * @return The thread factory
     */
    public static ThreadFactoryImpl of(final boolean daemon) {
        return new ThreadFactoryImpl().daemon(daemon);
    }

    /**
     * Create a new thread factory with a priority.
     *
     * @param priority The priority of the threads
     * @return The thread factory
     */
    public static ThreadFactoryImpl of(final int priority) {
        return new ThreadFactoryImpl().priority(priority);
    }

    /**
     * Create a new thread factory with an exception handler.
     *
     * @param exceptionHandler The exception handler for the threads
     * @return The thread factory
     */
    public static ThreadFactoryImpl of(final Thread.UncaughtExceptionHandler exceptionHandler) {
        return new ThreadFactoryImpl().exceptionHandler(exceptionHandler);
    }


    private final AtomicInteger threadId = new AtomicInteger(0);
    private String name;
    private Boolean daemon;
    private Integer priority;
    private Thread.UncaughtExceptionHandler exceptionHandler;

    private ThreadFactoryImpl() {
    }

    /**
     * Set the name of the threads.<br>
     * A thread id will be appended to the name.
     *
     * @param name The name of the threads
     * @return This instance
     */
    public ThreadFactoryImpl name(final String name) {
        this.name = name;
        return this;
    }

    /**
     * Set the daemon state of the threads.
     *
     * @param daemon Whether the threads should be daemon threads
     * @return This instance
     */
    public ThreadFactoryImpl daemon(final boolean daemon) {
        this.daemon = daemon;
        return this;
    }

    /**
     * Set the priority of the threads.
     *
     * @param priority The priority of the threads
     * @return This instance
     */
    public ThreadFactoryImpl priority(final int priority) {
        this.priority = priority;
        return this;
    }

    /**
     * Set the exception handler of the threads.
     *
     * @param exceptionHandler The exception handler of the threads
     * @return This instance
     */
    public ThreadFactoryImpl exceptionHandler(final Thread.UncaughtExceptionHandler exceptionHandler) {
        this.exceptionHandler = exceptionHandler;
        return this;
    }

    @Override
    public Thread newThread(@Nonnull Runnable r) {
        Thread thread;
        if (this.name != null) thread = new Thread(r, this.name + this.threadId.getAndIncrement());
        else thread = new Thread(r);

        if (this.daemon != null) thread.setDaemon(this.daemon);
        if (this.priority != null) thread.setPriority(this.priority);
        if (this.exceptionHandler != null) thread.setUncaughtExceptionHandler(this.exceptionHandler);
        return thread;
    }

}
