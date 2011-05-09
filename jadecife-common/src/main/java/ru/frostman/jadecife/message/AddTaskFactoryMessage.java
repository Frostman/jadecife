package ru.frostman.jadecife.message;

import ru.frostman.jadecife.model.Message;
import ru.frostman.jadecife.model.MessageType;
import ru.frostman.jadecife.task.TaskFactory;

/**
 * @author slukjanov aka Frostman
 */
public class AddTaskFactoryMessage extends Message {
    private TaskFactory factory;

    public AddTaskFactoryMessage() {
    }

    public AddTaskFactoryMessage(TaskFactory factory) {
        this.factory = factory;
    }

    public TaskFactory getFactory() {
        return factory;
    }

    public void setFactory(TaskFactory factory) {
        this.factory = factory;
    }

    @Override
    public MessageType getType() {
        return MessageType.TASK_FACTORY_ADD;
    }
}
