package ru.frostman.jadecife.client;

import ru.frostman.jadecife.model.Message;

/**
 * @author slukjanov aka Frostman
 */
interface MessageHandler {
    void messageReceived(Message message);
}
