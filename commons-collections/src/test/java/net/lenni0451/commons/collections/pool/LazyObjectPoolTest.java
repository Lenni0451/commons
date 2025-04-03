package net.lenni0451.commons.collections.pool;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class LazyObjectPoolTest {

    @Test
    void basicLogic() {
        ObjectPool<String> pool = new LazyObjectPool<String>() {
            @Override
            protected String create() {
                return String.valueOf(this.size());
            }
        };

        assertEquals(0, pool.size());
        assertEquals(0, pool.freeSize());
        assertEquals(0, pool.usedSize());

        String obj1 = pool.borrowObject();
        assertEquals(1, pool.size());
        assertEquals(0, pool.freeSize());
        assertEquals(1, pool.usedSize());

        String obj2 = pool.borrowObject();
        assertEquals(2, pool.size());
        assertEquals(0, pool.freeSize());
        assertEquals(2, pool.usedSize());

        pool.returnObject(obj1);
        assertEquals(2, pool.size());
        assertEquals(1, pool.freeSize());
        assertEquals(1, pool.usedSize());

        pool.returnObject(obj2);
        assertEquals(2, pool.size());
        assertEquals(2, pool.freeSize());
        assertEquals(0, pool.usedSize());
    }

    @Test
    void cleanup() {
        LazyObjectPool<String> pool = new LazyObjectPool<String>() {
            @Override
            protected String create() {
                return String.valueOf(this.size());
            }
        };

        String obj1 = pool.borrowObject();
        String obj2 = pool.borrowObject();

        pool.cleanup(0);
        assertEquals(2, pool.size());
        assertEquals(0, pool.freeSize());
        assertEquals(2, pool.usedSize());

        pool.returnObject(obj1);
        pool.returnObject(obj2);

        pool.cleanup(1_000_000L);
        assertEquals(2, pool.size());
        assertEquals(2, pool.freeSize());
        assertEquals(0, pool.usedSize());

        pool.cleanup(0);
        assertEquals(0, pool.size());
        assertEquals(0, pool.freeSize());
        assertEquals(0, pool.usedSize());
    }

}
