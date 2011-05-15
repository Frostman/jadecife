package ru.frostman.jadecife.util.hash;

/**
 * @author slukjanov aka Frostman
 */
public class Hex {
    private static final int TOP_4_BITS = 0xF0;
    private static final int BOTTOM_4_BITS = 0x0F;

    private static final char[] HEX_ALPHABET = {
            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'
    };

    public static char[] asHex(byte[] bytes) {
        final int length = bytes.length;
        char[] result = new char[2 * length];

        int pos = 0;
        for (int i = 0; i < length; i++) {
            // Alphabet character for top 4 bits of byte
            result[pos++] = HEX_ALPHABET[(TOP_4_BITS & bytes[i]) >>> 4];
            // Alphabet character for bottom 4 bits of byte
            result[pos++] = HEX_ALPHABET[(BOTTOM_4_BITS & bytes[i])];
        }

        return result;
    }

    public static String asHexString(byte[] bytes) {
        return new String(asHex(bytes));
    }
}
