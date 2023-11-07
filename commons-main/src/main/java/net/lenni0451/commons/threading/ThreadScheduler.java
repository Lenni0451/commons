package net.lenni0451.commons.threading;

import java.util.concurrent.*;

/**
 * A thread scheduler that can execute tasks with a delay or repeat them.<br>
 * Internally a {@link ScheduledExecutorService} is used.
 */
public class ThreadScheduler {

    private final boolean taskThreads;
    private final ThreadFactory threadFactory;
    private final ScheduledExecutorService executor;

    /**
     * Create a new task scheduler.
     *
     * @param daemon      Whether the scheduler should use daemon threads
     * @param taskThreads Whether the scheduler should use a new thread for each task
     */
    public ThreadScheduler(final boolean daemon, final boolean taskThreads) {
        this.taskThreads = taskThreads;
        this.threadFactory = ThreadFactoryImpl.of("ThreadScheduler #").daemon(daemon);
        this.executor = Executors.newSingleThreadScheduledExecutor(this.threadFactory);
    }

    /**
     * Directly execute a task.
     *
     * @param task The task to execute
     * @return The thread that is executing the task
     */
    public Thread execute(final Runnable task) {
        Thread thread = this.threadFactory.newThread(task);
        thread.start();
        return thread;
    }

    /**
     * Schedule a task to be executed after a delay.
     *
     * @param task  The task to execute
     * @param delay The delay in milliseconds
     * @return The {@link ScheduledFuture} of the task
     */
    public ScheduledFuture<?> schedule(final Runnable task, final long delay) {
        return this.schedule(task, delay, TimeUnit.MILLISECONDS);
    }

    /**
     * Schedule a task to be executed after a delay.
     *
     * @param task     The task to execute
     * @param delay    The delay
     * @param timeUnit The time unit of the delay
     * @return The {@link ScheduledFuture} of the task
     */
    public ScheduledFuture<?> schedule(final Runnable task, final long delay, final TimeUnit timeUnit) {
        return this.executor.schedule(this.wrapExecute(task), delay, timeUnit);
    }

    /**
     * Repeat a task with a fixed delay.
     *
     * @param task        The task to execute
     * @param repeatDelay The delay between each execution in milliseconds
     * @return The {@link ScheduledFuture} of the task
     */
    public ScheduledFuture<?> repeat(final Runnable task, final long repeatDelay) {
        return this.repeat(task, repeatDelay, repeatDelay);
    }

    /**
     * Repeat a task with a fixed delay.
     *
     * @param task        The task to execute
     * @param startDelay  The delay before the first execution in milliseconds
     * @param repeatDelay The delay between each execution in milliseconds
     * @return The {@link ScheduledFuture} of the task
     */
    public ScheduledFuture<?> repeat(final Runnable task, final long startDelay, final long repeatDelay) {
        return this.repeat(task, startDelay, repeatDelay, TimeUnit.MILLISECONDS);
    }

    /**
     * Repeat a task with a fixed delay.
     *
     * @param task        The task to execute
     * @param repeatDelay The delay between each execution
     * @param timeUnit    The time unit of the delay
     * @return The {@link ScheduledFuture} of the task
     */
    public ScheduledFuture<?> repeat(final Runnable task, final long repeatDelay, final TimeUnit timeUnit) {
        return this.repeat(task, repeatDelay, repeatDelay, timeUnit);
    }

    /**
     * Repeat a task with a fixed delay.
     *
     * @param task        The task to execute
     * @param startDelay  The delay before the first execution
     * @param repeatDelay The delay between each execution
     * @param timeUnit    The time unit of the delay
     * @return The {@link ScheduledFuture} of the task
     */
    public ScheduledFuture<?> repeat(final Runnable task, final long startDelay, final long repeatDelay, final TimeUnit timeUnit) {
        return this.executor.scheduleAtFixedRate(this.wrapExecute(task), startDelay, repeatDelay, timeUnit);
    }


    private Runnable wrapExecute(final Runnable task) {
        if (!this.taskThreads) return task;
        return () -> this.execute(task);
    }

}
