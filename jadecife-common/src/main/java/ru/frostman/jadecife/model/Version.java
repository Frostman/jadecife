package ru.frostman.jadecife.model;

/**
 * @author slukjanov aka Frostman
 */
public enum Version {
    VERSION_1((byte) 0x01),

    UNKNOWN((byte) 0x00);

    private final byte b;

    private Version(byte b) {
        this.b = b;
    }

    public static Version fromByte(byte b) {
        for (Version code : values()) {
            if (code.b == b) {
                return code;
            }
        }

        return UNKNOWN;
    }

    public byte getByteValue() {
        return b;
    }
}
