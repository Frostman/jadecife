package ru.frostman.jadecife.codec.message.java;

import ru.frostman.jadecife.codec.message.MessageCodecException;
import ru.frostman.jadecife.codec.message.MessageEncoder;
import ru.frostman.jadecife.model.Message;

import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;

/**
 * @author slukjanov aka Frostman
 */
public class MessageJavaEncoder implements MessageEncoder {
    @Override
    public byte[] encode(Message message) throws MessageCodecException {
        byte[] result = null;

        try {
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            ObjectOutputStream out = new ObjectOutputStream(bytes);
            out.writeObject(message);
            out.close();
            result = bytes.toByteArray();
        } catch (Exception e) {
            //todo log it
            e.printStackTrace();
            throw new MessageCodecException("Exception in encoding message",e);
        }

        return result;
    }
}
