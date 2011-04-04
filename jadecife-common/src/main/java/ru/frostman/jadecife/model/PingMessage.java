package ru.frostman.jadecife.model;

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

    @Override
    public MessageType getType() {
        return MessageType.PING;
    }
}
