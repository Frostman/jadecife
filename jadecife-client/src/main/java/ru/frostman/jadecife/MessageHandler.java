package ru.frostman.jadecife;

import ru.frostman.jadecife.model.Message;

/**
 * @author slukjanov aka Frostman
 */
interface MessageHandler {
    void messageReceived(Message message);
}
