package ru.frostman.jadecife.message;

import org.codehaus.jackson.annotate.JsonIgnore;
import ru.frostman.jadecife.model.Message;
import ru.frostman.jadecife.model.MessageType;
import ru.frostman.jadecife.task.TaskFactory;

/**
 * @author slukjanov aka Frostman
 */
public class AddTaskFactoryMessage extends Message {
    private int factoryClassId;
    private Object factory;

    public AddTaskFactoryMessage() {
    }

    public AddTaskFactoryMessage(int factoryClassId, TaskFactory factory) {
        this.factoryClassId = factoryClassId;
        this.factory = factory;
    }

    public int getFactoryClassId() {
        return factoryClassId;
    }

    public void setFactoryClassId(int factoryClassId) {
        this.factoryClassId = factoryClassId;
    }

    public Object getFactory() {
        return factory;
    }

    public void setFactory(Object factory) {
        this.factory = factory;
    }

    @JsonIgnore
    @Override
    public MessageType getType() {
        return MessageType.TASK_FACTORY_ADD;
    }
}
