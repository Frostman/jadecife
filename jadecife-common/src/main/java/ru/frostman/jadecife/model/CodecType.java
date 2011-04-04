package ru.frostman.jadecife.model;

/**
 * @author slukjanov aka Frostman
 */
public enum CodecType {
    JSON((byte) 0x01),
    BSON((byte) 0x02),
    XML((byte) 0x03),
    JAVA_SERIALIZATION((byte) 0x04),

    UNKNOWN((byte) 0x00);

    private final byte b;

    private CodecType(byte b) {
        this.b = b;
    }

    public static CodecType fromByte(byte b) {
        for (CodecType code : values()) {
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
