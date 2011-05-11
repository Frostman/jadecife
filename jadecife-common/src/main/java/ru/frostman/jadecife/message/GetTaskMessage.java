package ru.frostman.jadecife.message;

import org.codehaus.jackson.annotate.JsonIgnore;
import ru.frostman.jadecife.model.Message;
import ru.frostman.jadecife.model.MessageType;

/**
 * @author slukjanov aka Frostman
 */
public class GetTaskMessage extends Message{

    @JsonIgnore
    @Override
    public MessageType getType() {
        return MessageType.GET_TASK;
    }
}
