package ru.frostman.jadecife.model;

/**
 * @author slukjanov aka Frostman
 */
public enum MessageType {
    PING(((byte) 0x00)),

    UNKNOWN(((byte) 0x00));

    private final byte b;

    private MessageType(byte b) {
        this.b = b;
    }

    public static MessageType fromByte(byte b) {
        for (MessageType code : values()) {
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
