package ru.frostman.jadecife.model;

import org.codehaus.jackson.annotate.JsonIgnore;

/**
 * Base class for all messages in system. Each message may
 * be sent/received with auto encoding and auto decoding
 * using specified codec type.
 * All fields will be encoded/decoded, so don't use
 * unnecessary fields to improve performance.
 *
 * @author slukjanov aka Frostman
 */
public abstract class Message {

    /**
     * Returns type of the message. Preferable not to use
     * class property to store type, implement this method
     * like return hard-coded value. Use {@code @JsonIgnore}
     * to exclude it from json.
     *
     * @return type of the message
     */
    @JsonIgnore
    public abstract MessageType getType();
}
