package ru.frostman.jadecife.test;

import ru.frostman.jadecife.model.Message;

/**
 * @author slukjanov aka Frostman
 */
public interface ClientHandlerListener {
    void messageReceived(Message message);
}
