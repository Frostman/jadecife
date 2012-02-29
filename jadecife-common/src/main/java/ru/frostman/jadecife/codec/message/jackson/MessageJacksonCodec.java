package ru.frostman.jadecife.codec.message.jackson;

import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;
import ru.frostman.jadecife.codec.message.MessageCodecException;
import ru.frostman.jadecife.codec.message.MessageDecoder;
import ru.frostman.jadecife.codec.message.MessageEncoder;
import ru.frostman.jadecife.model.Message;

import java.io.InputStream;
import java.io.OutputStream;

/**
 * @author slukjanov aka Frostman
 */
public class MessageJacksonCodec implements MessageEncoder, MessageDecoder {
    private final ObjectMapper objectMapper;

    public MessageJacksonCodec() {
        objectMapper = new ObjectMapper();

        objectMapper.configure(SerializationConfig.Feature.FAIL_ON_EMPTY_BEANS, false);
    }

    public MessageJacksonCodec(JsonFactory factory) {
        objectMapper = new ObjectMapper(factory);
    }

    @Override
    public void encode(OutputStream out, Message message) throws MessageCodecException {
        try {
            objectMapper.writeValue(out, message);
        } catch (Exception e) {
            throw new MessageCodecException("Exception in encoding message", e);
        }
    }

    @Override
    public <T extends Message> T decode(InputStream in, Class<T> messageClass) throws MessageCodecException {
        try {
            return objectMapper.readValue(in, messageClass);
        } catch (Exception e) {
            throw new MessageCodecException("Exception in decoding message", e);
        }
    }
}
