package ru.frostman.jadecife.codec.message;

import ru.frostman.jadecife.model.Message;

import java.io.InputStream;

/**
 * @author slukjanov aka Frostman
 */
public interface MessageDecoder {
    <T extends Message> T decode(InputStream in, Class<T> messageClass) throws MessageCodecException;
}
