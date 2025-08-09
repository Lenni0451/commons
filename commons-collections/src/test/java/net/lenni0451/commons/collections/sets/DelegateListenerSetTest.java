package net.lenni0451.commons.collections.sets;

import net.lenni0451.commons.collections.Sets;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Iterator;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class DelegateListenerSetTest {

    private Set<String> set;

    @BeforeEach
    void setUp() {
        this.set = Sets.hashSet("Test1", "Test2", "Test3");
    }

    @Test
    void iterator() {
        Iterator<String> it = new DelegateListenerSet<>(this.set, a -> {
            fail("Iterator should not be called");
        }, r -> {
            fail("Iterator should not be called");
        }).iterator();
        assertThrows(UnsupportedOperationException.class, it::remove);
    }

    @Test
    void add() {
        int[] callCount = {0};
        new DelegateListenerSet<>(this.set, a -> {
            assertEquals("Test4", a);
            callCount[0]++;
        }, r -> {
            fail("Add should not be called");
        }).add("Test4");
        assertEquals(1, callCount[0], "Add should be called exactly once");
    }

    @Test
    void remove() {
        int[] callCount = {0};
        new DelegateListenerSet<>(this.set, a -> {
            fail("Remove should not be called");
        }, r -> {
            assertEquals("Test2", r);
            callCount[0]++;
        }).remove("Test2");
        assertEquals(1, callCount[0], "Remove should be called exactly once");
    }

    @Test
    void addAll() {
        int[] callCount = {0};
        Set<String> additionalSet = Sets.hashSet("Test4", "Test5");
        new DelegateListenerSet<>(this.set, a -> {
            if (!a.equals("Test4") && !a.equals("Test5")) {
                fail("AddAll should only be called with Test4 or Test5, but was: " + a);
            }
            callCount[0]++;
        }, r -> {
            fail("AddAll should not be called");
        }).addAll(additionalSet);
        assertEquals(2, callCount[0], "AddAll should be called exactly twice");
    }

    @Test
    void retainAll() {
        int[] callCount = {0};
        Set<String> retainSet = Sets.hashSet("Test1", "Test2");
        new DelegateListenerSet<>(this.set, a -> {
            fail("RetainAll should not be called");
        }, r -> {
            assertEquals("Test3", r);
            callCount[0]++;
        }).retainAll(retainSet);
        assertEquals(1, callCount[0], "RetainAll should be called exactly once");
    }

    @Test
    void removeAll() {
        int[] callCount = {0};
        Set<String> removeSet = Sets.hashSet("Test2", "Test3");
        new DelegateListenerSet<>(this.set, a -> {
            fail("RemoveAll should not be called");
        }, r -> {
            if (!r.equals("Test2") && !r.equals("Test3")) {
                fail("RemoveAll should only be called with Test2 or Test3, but was: " + r);
            }
            callCount[0]++;
        }).removeAll(removeSet);
        assertEquals(2, callCount[0], "RemoveAll should be called exactly twice");
    }

    @Test
    void clear() {
        int[] callCount = {0};
        new DelegateListenerSet<>(this.set, a -> {
            fail("Clear should not be called");
        }, r -> {
            callCount[0]++;
        }).clear();
        assertEquals(3, callCount[0], "Clear should be called exactly once");
        assertTrue(this.set.isEmpty(), "Set should be empty after clear");
    }

}
