package net.lenni0451.commons.network;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

class IPv4Test {

    private static final int I0_0_0_0 = 0;
    private static final int I255_255_255_255 = 0xFFFFFFFF;
    private static final int I192_168_0_1 = (192 << 24) | (168 << 16) | (0 << 8) | (1 << 0);

    @Test
    void toInt() {
        assertEquals(I0_0_0_0, IPv4.toInt("0.0.0.0"));
        assertEquals(I255_255_255_255, IPv4.toInt("255.255.255.255"));
        assertEquals(I192_168_0_1, IPv4.toInt("192.168.0.1"));
    }

    @Test
    void testToString() {
        assertEquals("0.0.0.0", IPv4.toString(I0_0_0_0));
        assertEquals("255.255.255.255", IPv4.toString(I255_255_255_255));
        assertEquals("192.168.0.1", IPv4.toString(I192_168_0_1));
    }

    @Test
    void expandSubnetString() {
        assertArrayEquals(new int[]{I192_168_0_1}, IPv4.expandSubnet("192.168.0.1/32"));
        assertArrayEquals(new int[]{I192_168_0_1 - 1, I192_168_0_1}, IPv4.expandSubnet("192.168.0.1/31"));
    }

    @Test
    void expandSubnetInt() {
        assertArrayEquals(new int[]{I192_168_0_1}, IPv4.expandSubnet(I192_168_0_1, 32));
        assertArrayEquals(new int[]{I192_168_0_1 - 1, I192_168_0_1}, IPv4.expandSubnet(I192_168_0_1, 31));
    }

}
