package ru.frostman.jadecife.codec.message.java;

import ru.frostman.jadecife.codec.message.MessageCodecException;
import ru.frostman.jadecife.codec.message.MessageDecoder;
import ru.frostman.jadecife.model.Message;

import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;

/**
 * @author slukjanov aka Frostman
 */
public class MessageJavaDecoder implements MessageDecoder{
    @Override
    @SuppressWarnings({"unchecked"})
    public <T extends Message> T decode(byte[] bytes, Class<T> messageClass) throws MessageCodecException{
        T result = null;

        try {
            ByteArrayInputStream stream = new ByteArrayInputStream(bytes);
            ObjectInputStream in = new ObjectInputStream(stream);
            result = (T) in.readObject();
            in.close();
        } catch (Exception e) {
            //todo log it
            e.printStackTrace();
            throw new MessageCodecException("Exception in decoding message", e);
        }

        return result;
    }
}
