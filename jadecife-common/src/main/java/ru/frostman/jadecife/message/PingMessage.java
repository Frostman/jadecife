package ru.frostman.jadecife.message;

import org.codehaus.jackson.annotate.JsonIgnore;
import ru.frostman.jadecife.model.Message;
import ru.frostman.jadecife.model.MessageType;

/**
 * @author slukjanov aka Frostman
 */
public class PingMessage extends Message {
    private long createTime;

    public PingMessage() {
    }

    public static PingMessage create() {
        PingMessage pingMessage = new PingMessage();
        pingMessage.setCreateTime(System.nanoTime());

        return pingMessage;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    @JsonIgnore
    @Override
    public MessageType getType() {
        return MessageType.PING;
    }
}
