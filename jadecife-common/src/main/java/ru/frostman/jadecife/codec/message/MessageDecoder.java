package ru.frostman.jadecife.codec.message;

import ru.frostman.jadecife.model.Message;

/**
 * @author slukjanov aka Frostman
 */
public interface MessageDecoder {
    <T extends Message> T decode(byte[] bytes, Class<T> messageClass) throws MessageCodecException;
}
