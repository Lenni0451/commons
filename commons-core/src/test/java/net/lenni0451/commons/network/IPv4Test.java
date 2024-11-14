package net.lenni0451.commons.network;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

class IPv4Test {

    private static final int i0_0_0_0 = 0;
    private static final int i255_255_255_255 = 0xFFFFFFFF;
    private static final int i192_168_0_1 = (192 << 24) | (168 << 16) | (0 << 8) | (1 << 0);

    @Test
    void toInt() {
        assertEquals(i0_0_0_0, IPv4.toInt("0.0.0.0"));
        assertEquals(i255_255_255_255, IPv4.toInt("255.255.255.255"));
        assertEquals(i192_168_0_1, IPv4.toInt("192.168.0.1"));
    }

    @Test
    void testToString() {
        assertEquals("0.0.0.0", IPv4.toString(i0_0_0_0));
        assertEquals("255.255.255.255", IPv4.toString(i255_255_255_255));
        assertEquals("192.168.0.1", IPv4.toString(i192_168_0_1));
    }

    @Test
    void expandSubnetString() {
        assertArrayEquals(new int[]{i192_168_0_1}, IPv4.expandSubnet("192.168.0.1/32"));
        assertArrayEquals(new int[]{i192_168_0_1 - 1, i192_168_0_1}, IPv4.expandSubnet("192.168.0.1/31"));
    }

    @Test
    void expandSubnetInt() {
        assertArrayEquals(new int[]{i192_168_0_1}, IPv4.expandSubnet(i192_168_0_1, 32));
        assertArrayEquals(new int[]{i192_168_0_1 - 1, i192_168_0_1}, IPv4.expandSubnet(i192_168_0_1, 31));
    }

}
