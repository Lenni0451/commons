package net.lenni0451.commons.collections.pool;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FixedObjectPoolTest {

    @Test
    void basicLogic() {
        ObjectPool<String> pool = new FixedObjectPool<String>(5) {
            @Override
            protected String create() {
                return String.valueOf(this.size());
            }
        };

        assertEquals(5, pool.size());
        assertEquals(5, pool.freeSize());
        assertEquals(0, pool.usedSize());

        String obj1 = pool.borrowObject();
        assertEquals(4, pool.freeSize());
        assertEquals(1, pool.usedSize());

        String obj2 = pool.borrowObject();
        assertEquals(3, pool.freeSize());
        assertEquals(2, pool.usedSize());

        pool.returnObject(obj1);
        assertEquals(4, pool.freeSize());
        assertEquals(1, pool.usedSize());

        pool.returnObject(obj2);
        assertEquals(5, pool.freeSize());
        assertEquals(0, pool.usedSize());
    }

    @Test
    void removeMoreThanAvailable() {
        FixedObjectPool<String> pool = new FixedObjectPool<String>(2) {
            @Override
            protected String create() {
                return String.valueOf(this.size());
            }
        };

        String obj1 = pool.borrowObject();
        String obj2 = pool.borrowObject();
        assertThrows(IllegalStateException.class, pool::borrowObject);
        assertNull(pool.tryBorrowObject());
        pool.returnObject(obj1);
        pool.returnObject(obj2);
        assertNotNull(pool.borrowObject());
    }

    @Test
    void returnUnknownObject() {
        ObjectPool<String> pool = new FixedObjectPool<String>(1) {
            @Override
            protected String create() {
                return String.valueOf(this.size());
            }
        };

        String obj = pool.borrowObject();
        assertThrows(IllegalArgumentException.class, () -> pool.returnObject("100"));
        assertDoesNotThrow(() -> pool.returnObject(obj));
    }

}
