package ru.frostman.jadecife.codec.message;

import ru.frostman.jadecife.codec.message.java.MessageJavaEncoder;

/**
 * @author slukjanov aka Frostman
 */
public class MessageCodecFactory {

    public static MessageEncoder getMessageJavaEncoder() {
        return InstanceStorage.MESSAGE_JAVA_ENCODER;
    }

    private static final class InstanceStorage {
        private static final MessageEncoder MESSAGE_JAVA_ENCODER = new MessageJavaEncoder();
    }
}
