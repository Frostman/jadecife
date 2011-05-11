package ru.frostman.jadecife.model;

import ru.frostman.jadecife.message.*;

/**
 * @author slukjanov aka Frostman
 */
public enum MessageType {
    PING(((byte) 0x01), PingMessage.class),

    CLASS_REGISTER(((byte) 0x02), ClassRegisterMessage.class),
    CLASS_REGISTERED(((byte) 0x03), ClassRegisteredMessage.class),

    TASK_FACTORY_ADD(((byte) 0x04), AddTaskFactoryMessage.class),
    TASK_FACTORY_ADDED(((byte) 0x05), TaskFactoryAddedMessage.class),

    GET_TASK(((byte) 0x06), GetTaskMessage.class),
    RUN_TASK(((byte) 0x07), RunTaskMessage.class),

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
