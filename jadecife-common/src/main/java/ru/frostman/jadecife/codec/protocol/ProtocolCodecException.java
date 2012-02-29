package ru.frostman.jadecife.codec.protocol;

/**
 * @author slukjanov aka Frostman
 */
public class ProtocolCodecException extends Exception{
    public ProtocolCodecException() {
    }

    public ProtocolCodecException(String message) {
        super(message);
    }

    public ProtocolCodecException(String message, Throwable cause) {
        super(message, cause);
    }

    public ProtocolCodecException(Throwable cause) {
        super(cause);
    }
}
