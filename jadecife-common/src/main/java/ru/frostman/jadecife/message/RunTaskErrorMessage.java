package ru.frostman.jadecife.message;

import org.codehaus.jackson.annotate.JsonIgnore;
import ru.frostman.jadecife.model.Message;
import ru.frostman.jadecife.model.MessageType;

/**
 * @author slukjanov aka Frostman
 */
public class RunTaskErrorMessage extends Message {
    private long taskId;

    private Exception exception;

    public RunTaskErrorMessage() {
    }

    public RunTaskErrorMessage(long taskId, Exception exception) {
        this.taskId = taskId;
        this.exception = exception;
    }

    public long getTaskId() {
        return taskId;
    }

    public void setTaskId(long taskId) {
        this.taskId = taskId;
    }

    public Exception getException() {
        return exception;
    }

    public void setException(Exception exception) {
        this.exception = exception;
    }

    @JsonIgnore
    @Override
    public MessageType getType() {
        return MessageType.RUN_ERROR;
    }
}
