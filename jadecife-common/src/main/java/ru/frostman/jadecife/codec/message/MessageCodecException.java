package ru.frostman.jadecife.codec.message;

/**
 * @author slukjanov aka Frostman
 */
public class MessageCodecException extends Exception {
    public MessageCodecException() {
    }

    public MessageCodecException(String message) {
        super(message);
    }

    public MessageCodecException(String message, Throwable cause) {
        super(message, cause);
    }

    public MessageCodecException(Throwable cause) {
        super(cause);
    }
}
