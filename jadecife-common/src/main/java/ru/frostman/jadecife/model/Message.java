package ru.frostman.jadecife.model;

import java.io.Serializable;

/**
 * Base class for all messages in system. Each message may
 * be sent/received with auto encoding and auto decoding
 * using specified codec type.
 * All fields will be encoded/decoded, so don't use
 * unnecessary fields to improve performance.
 *
 * @author slukjanov aka Frostman
 */
public abstract class Message implements Serializable{

    /**
     * Returns type of the message. Preferable not to use
     * class property to store type, implement this method
     * like return hard-coded value.
     *
     * @return type of the message
     */
    public abstract MessageType getType();
}
