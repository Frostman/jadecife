package ru.frostman.jadecife.message;

import ru.frostman.jadecife.model.Message;

/**
 * @author slukjanov aka Frostman
 */
public interface MessageHandler {
    void messageReceived(Message message);
}
