package ru.frostman.jadecife;

/**
 * @author slukjanov aka Frostman
 */
public class JadecifeException extends Exception{
    public JadecifeException() {
    }

    public JadecifeException(String message) {
        super(message);
    }

    public JadecifeException(String message, Throwable cause) {
        super(message, cause);
    }

    public JadecifeException(Throwable cause) {
        super(cause);
    }
}
