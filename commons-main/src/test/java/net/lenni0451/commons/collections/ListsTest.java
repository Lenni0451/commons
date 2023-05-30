package net.lenni0451.commons.collections;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

class ListsTest {

    @Test
    void moveToBottom() {
        List<String> list = new ArrayList<>();
        list.add("A");
        list.add("B");
        list.add("C");
        Lists.moveToBottom(list, "B");
        assertEquals("A", list.get(0));
        assertEquals("C", list.get(1));
        assertEquals("B", list.get(2));
    }

    @Test
    void arrayList() {
        List<String> list = Lists.arrayList("A", "B", "C");
        assertInstanceOf(ArrayList.class, list);
        assertEquals("A", list.get(0));
        assertEquals("B", list.get(1));
        assertEquals("C", list.get(2));
    }

    @Test
    void copyOnWriteArrayList() {
        List<String> list = Lists.copyOnWriteArrayList("A", "B", "C");
        assertInstanceOf(CopyOnWriteArrayList.class, list);
        assertEquals("A", list.get(0));
        assertEquals("B", list.get(1));
        assertEquals("C", list.get(2));
    }

}
