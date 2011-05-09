package ru.frostman.jadecife.codec.message;

import ru.frostman.jadecife.model.Message;

import java.io.OutputStream;

/**
 * @author slukjanov aka Frostman
 */
public interface MessageEncoder {
    void encode(OutputStream out, Message message) throws MessageCodecException;
}
