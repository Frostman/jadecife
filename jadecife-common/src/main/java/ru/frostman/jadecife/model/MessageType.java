package ru.frostman.jadecife.model;

import ru.frostman.jadecife.message.PingMessage;

/**
 * @author slukjanov aka Frostman
 */
public enum MessageType {
    PING(((byte) 0x01), PingMessage.class),

    UNKNOWN(((byte) 0x00), null);

    private final byte b;
    private final Class<? extends Message> messageClass;

    private MessageType(byte b, Class<? extends Message> messageClass) {
        this.b = b;
        this.messageClass = messageClass;
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

    public Class<? extends Message> getMessageClass() {
        return messageClass;
    }
}
