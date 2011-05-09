package ru.frostman.jadecife.codec.message;

import ru.frostman.jadecife.codec.message.jackson.MessageJacksonCodec;

/**
 * @author slukjanov aka Frostman
 */
public class MessageCodecFactory {

    public static MessageEncoder getMessageJsonEncoder() {
        return InstanceStorage.MESSAGE_JSON_ENCODER;
    }

    public static MessageDecoder getMessageJsonDecoder() {
        return InstanceStorage.MESSAGE_JSON_DECODER;
    }

    private static final class InstanceStorage {
        private static final MessageJacksonCodec MESSAGE_JACKSON_CODEC = new MessageJacksonCodec();
        private static final MessageEncoder MESSAGE_JSON_ENCODER = MESSAGE_JACKSON_CODEC;
        private static final MessageDecoder MESSAGE_JSON_DECODER = MESSAGE_JACKSON_CODEC;
    }
}
