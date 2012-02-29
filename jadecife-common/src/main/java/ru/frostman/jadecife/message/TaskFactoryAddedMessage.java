package ru.frostman.jadecife.message;

import org.codehaus.jackson.annotate.JsonIgnore;
import ru.frostman.jadecife.model.Message;
import ru.frostman.jadecife.model.MessageType;

/**
 * @author slukjanov aka Frostman
 */
public class TaskFactoryAddedMessage extends Message {
    private long factoryId;

    public TaskFactoryAddedMessage() {
    }

    public TaskFactoryAddedMessage(long factoryId) {
        this.factoryId = factoryId;
    }

    public long getFactoryId() {
        return factoryId;
    }

    public void setFactoryId(long factoryId) {
        this.factoryId = factoryId;
    }

    @JsonIgnore
    @Override
    public MessageType getType() {
        return MessageType.TASK_FACTORY_ADDED;
    }
}
