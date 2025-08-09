package net.lenni0451.commons.collections.iterators;

import net.lenni0451.commons.collections.Lists;
import org.junit.jupiter.api.Test;

import java.util.Iterator;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

class DelegateIteratorTest {

    @Test
    void test() {
        Iterator<String> it = new DelegateIterator<>(Lists.arrayList("a", "b", "c").iterator());
        assertTrue(it.hasNext());
        assertEquals("a", it.next());
        assertTrue(it.hasNext());
        assertEquals("b", it.next());
        assertDoesNotThrow(it::remove);
        assertTrue(it.hasNext());
        assertEquals("c", it.next());
        assertFalse(it.hasNext());
        assertThrows(NoSuchElementException.class, it::next);
    }

}
