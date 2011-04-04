package ru.frostman.jadecife.codec.message;

import ru.frostman.jadecife.model.Message;

/**
 * @author slukjanov aka Frostman
 */
public interface MessageEncoder {
    byte[] encode(Message message) throws MessageCodecException;
}
