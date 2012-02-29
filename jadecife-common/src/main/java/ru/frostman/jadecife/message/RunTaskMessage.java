package ru.frostman.jadecife.message;

import org.codehaus.jackson.annotate.JsonIgnore;
import ru.frostman.jadecife.model.Message;
import ru.frostman.jadecife.model.MessageType;
import ru.frostman.jadecife.task.Task;

/**
 * @author slukjanov aka Frostman
 */
public class RunTaskMessage extends Message {
    private Task task;

    public RunTaskMessage() {
    }

    public RunTaskMessage(Task task) {
        this.task = task;
    }

    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }

    @JsonIgnore
    @Override
    public MessageType getType() {
        return MessageType.RUN_TASK;
    }
}
