package ru.frostman.jadecife.message;

import org.codehaus.jackson.annotate.JsonIgnore;
import ru.frostman.jadecife.model.Message;
import ru.frostman.jadecife.model.MessageType;

/**
 * @author slukjanov aka Frostman
 */
public class RunTaskResultMessage extends Message {
    private long taskId;

    private Object result;

    public RunTaskResultMessage() {
    }

    public RunTaskResultMessage(long taskId, Object result) {
        this.taskId = taskId;
        this.result = result;
    }

    public long getTaskId() {
        return taskId;
    }

    public void setTaskId(long taskId) {
        this.taskId = taskId;
    }

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }

    @JsonIgnore
    @Override
    public MessageType getType() {
        return MessageType.RUN_RESULT;
    }
}
