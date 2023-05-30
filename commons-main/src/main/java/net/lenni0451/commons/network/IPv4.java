package net.lenni0451.commons.network;

public class IPv4 {

    /**
     * Convert an IPv4 address to an int.
     *
     * @param ip The IPv4 address to convert
     * @return The int representation of the IPv4 address
     */
    public static int toInt(final String ip) {
        char[] chars = ip.toCharArray();
        int ipInt = 0;
        int cur = 0;
        int shift = 3;
        for (char c : chars) {
            if (c == '.') {
                ipInt |= cur << (8 * shift);
                cur = 0;
                shift--;
            } else {
                cur = (cur * 10) + (c - '0');
            }
        }
        ipInt |= cur << (8 * shift);
        return ipInt;
    }

    /**
     * Convert an int to an IPv4 address.
     *
     * @param ip The int to convert
     * @return The IPv4 address representation of the int
     */
    public static String toString(final int ip) {
        StringBuilder sb = new StringBuilder();
        for (int i = 3; i >= 0; i--) {
            sb.append((ip >> (8 * i)) & 0xFF);
            if (i > 0) sb.append('.');
        }
        return sb.toString();
    }

    /**
     * Expand a subnet to all possible addresses.
     *
     * @param ipAndSubnet The IP and subnet in the format of {@code 192.168.0.1/24}
     * @return All possible addresses in the subnet
     */
    public static int[] expandSubnet(final String ipAndSubnet) {
        String[] parts = ipAndSubnet.split("/");
        String ip = parts[0];
        int subnet = Integer.parseInt(parts[1]);
        return expandSubnet(toInt(ip), subnet);
    }

    /**
     * Expand a subnet to all possible addresses.
     *
     * @param ip     The IP to expand
     * @param subnet The subnet to expand
     * @return All possible addresses in the subnet
     */
    public static int[] expandSubnet(final int ip, final int subnet) {
        int subnetCount = 32 - subnet;
        int subnetMask = 0xFFFFFFFF << subnetCount;
        int[] addresses = new int[(int) Math.pow(2, subnetCount)];

        int maskedIP = ip & subnetMask;
        for (int i = 0; i < addresses.length; i++) addresses[i] = maskedIP | i;
        return addresses;
    }

}
