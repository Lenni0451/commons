package net.lenni0451.commons.collections.lists;

import net.lenni0451.commons.collections.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import static org.junit.jupiter.api.Assertions.*;

class DelegateListenerListTest {

    private List<String> list;

    @BeforeEach
    void setUp() {
        this.list = Lists.arrayList("Test1", "Test2", "Test3");
    }

    @Test
    void iterator() {
        Iterator<String> it = new DelegateListenerList<>(this.list, a -> {
            fail("Iterator should not be called");
        }, r -> {
            fail("Iterator should not be called");
        }).iterator();
        assertThrows(UnsupportedOperationException.class, it::remove);
    }

    @Test
    void add() {
        int[] callCount = {0};
        new DelegateListenerList<>(this.list, a -> {
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
        new DelegateListenerList<>(this.list, a -> {
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
        List<String> additionalList = Lists.arrayList("Test4", "Test5");
        new DelegateListenerList<>(this.list, a -> {
            if (!a.equals("Test4") && !a.equals("Test5")) {
                fail("AddAll should only be called with Test4 or Test5, but was: " + a);
            }
            callCount[0]++;
        }, r -> {
            fail("AddAll should not be called");
        }).addAll(additionalList);
        assertEquals(2, callCount[0], "AddAll should be called exactly twice");
    }

    @Test
    void retainAll() {
        int[] callCount = {0};
        List<String> retainList = Lists.arrayList("Test1", "Test2");
        new DelegateListenerList<>(this.list, a -> {
            fail("RetainAll should not be called");
        }, r -> {
            assertEquals("Test3", r);
            callCount[0]++;
        }).retainAll(retainList);
        assertEquals(1, callCount[0], "RetainAll should be called exactly once");
    }

    @Test
    void removeAll() {
        int[] callCount = {0};
        List<String> removeList = Lists.arrayList("Test2", "Test3");
        new DelegateListenerList<>(this.list, a -> {
            fail("RemoveAll should not be called");
        }, r -> {
            if (!r.equals("Test2") && !r.equals("Test3")) {
                fail("RemoveAll should only be called with Test2 or Test3, but was: " + r);
            }
            callCount[0]++;
        }).removeAll(removeList);
        assertEquals(2, callCount[0], "RemoveAll should be called exactly twice");
    }

    @Test
    void clear() {
        int[] callCount = {0};
        new DelegateListenerList<>(this.list, a -> {
            fail("Clear should not be called");
        }, r -> {
            callCount[0]++;
        }).clear();
        assertEquals(3, callCount[0], "Clear should be called exactly once");
        assertTrue(this.list.isEmpty(), "List should be empty after clear");
    }

    @Test
    void listIterator() {
        int[] addCallCount = {0};
        int[] removeCallCount = {0};
        DelegateListenerList<String> delegateList = new DelegateListenerList<>(this.list, a -> {
            addCallCount[0]++;
        }, r -> {
            removeCallCount[0]++;
        });
        ListIterator<String> it = delegateList.listIterator();
        assertTrue(it.hasNext());
        assertEquals("Test1", it.next());
        assertThrows(UnsupportedOperationException.class, it::remove);
        assertThrows(UnsupportedOperationException.class, () -> it.set("TestX"));
        it.add("Test4");
        assertEquals(1, addCallCount[0], "Add should be called exactly once via listIterator");
        assertEquals(0, removeCallCount[0], "Remove should not be called via listIterator");
    }

    @Test
    void subList() {
        int[] addCallCount = {0};
        int[] removeCallCount = {0};
        DelegateListenerList<String> delegateList = new DelegateListenerList<>(this.list, a -> {
            addCallCount[0]++;
        }, r -> {
            removeCallCount[0]++;
        });
        List<String> sub = delegateList.subList(1, 3); // ["Test2", "Test3"]
        sub.add("Test5");
        assertEquals(1, addCallCount[0], "Add should be called exactly once via subList");
        sub.remove("Test2");
        assertEquals(1, removeCallCount[0], "Remove should be called exactly once via subList");
    }

}
