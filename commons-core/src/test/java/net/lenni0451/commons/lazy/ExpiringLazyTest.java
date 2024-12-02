package net.lenni0451.commons.lazy;

import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

class ExpiringLazyTest {

    @Test
    void testInitialize() {
        int[] i = {0};
        Lazy<int[]> lazy = Lazy.of(() -> {
            i[0] = 1;
            return i;
        });
        assertEquals(0, i[0]);
        assertEquals(1, lazy.get()[0]);
        assertEquals(1, i[0]);
    }

    @RepeatedTest(5)
    void testThreadSafety() throws InterruptedException {
        AtomicInteger initCount = new AtomicInteger(0);
        AtomicInteger runCount = new AtomicInteger(0);
        Lazy<int[]> lazy = Lazy.of(() -> {
            initCount.incrementAndGet();
            return new int[]{1};
        });

        int threadCount = Runtime.getRuntime().availableProcessors();
        CountDownLatch latch = new CountDownLatch(1);
        List<Thread> threads = new ArrayList<>();
        for (int i = 0; i < threadCount; i++) { //Start threads and let them wait for the latch
            Thread t = new Thread(() -> {
                try {
                    latch.await();
                    lazy.get();
                    runCount.incrementAndGet();
                } catch (Throwable ignored) {
                }
            });
            t.setDaemon(true);
            t.start();
            threads.add(t);
        }

        //Make sure all threads are waiting
        assertEquals(0, initCount.get());
        assertEquals(0, runCount.get());
        latch.countDown(); //Release all threads
        for (Thread thread : threads) { //Wait for all threads to finish
            thread.join(1000);
            if (thread.isAlive()) fail("Thread did not finish in time");
        }
        //Make sure only one thread initialized the object
        assertEquals(1, initCount.get());
        assertEquals(threadCount, runCount.get());
    }

    @Test
    void testWriteExpiration() throws InterruptedException {
        AtomicInteger initCount = new AtomicInteger(0);
        ExpiringLazy<int[]> lazy = ExpiringLazy.of(ExpiringLazy.ExpirationType.WRITE, 1000, () -> {
            initCount.incrementAndGet();
            return new int[]{1};
        });

        assertEquals(0, initCount.get());
        assertEquals(1, lazy.get()[0]);
        assertEquals(1, initCount.get());

        Thread.sleep(500); //The value should not have expired yet
        assertEquals(1, initCount.get());
        assertEquals(1, lazy.get()[0]);
        assertEquals(1, initCount.get());

        Thread.sleep(600); //Now it should have expired
        assertEquals(1, initCount.get());
        assertEquals(1, lazy.get()[0]);
        assertEquals(2, initCount.get());
    }

    @Test
    void testReadExpiration() throws InterruptedException {
        AtomicInteger initCount = new AtomicInteger(0);
        ExpiringLazy<int[]> lazy = ExpiringLazy.of(ExpiringLazy.ExpirationType.READ, 1000, () -> {
            initCount.incrementAndGet();
            return new int[]{1};
        });

        assertEquals(0, initCount.get());
        assertEquals(1, lazy.get()[0]);
        assertEquals(1, initCount.get());

        Thread.sleep(500); //The value should not have expired yet
        assertEquals(1, initCount.get());
        assertEquals(1, lazy.get()[0]);
        assertEquals(1, initCount.get());

        Thread.sleep(600); //The value should not have expired yet
        assertEquals(1, initCount.get());
        assertEquals(1, lazy.get()[0]);
        assertEquals(1, initCount.get());

        Thread.sleep(1100); //Now it should have expired
        assertEquals(1, initCount.get());
        assertEquals(1, lazy.get()[0]);
        assertEquals(2, initCount.get());
    }

}
