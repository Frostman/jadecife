package ru.frostman.jadecife.worker.cli;

import org.jboss.netty.channel.*;
import org.jboss.netty.channel.group.ChannelGroup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.frostman.jadecife.message.MessageHandler;
import ru.frostman.jadecife.model.Message;

/**
 * @author slukjanov aka Frostman
 */
public class WorkerHandler extends SimpleChannelUpstreamHandler {
    private static final Logger log = LoggerFactory.getLogger(WorkerHandler.class);

    private final ChannelGroup channelGroup;
    private final MessageHandler messageHandler;
    private Channel channel;

    public WorkerHandler(ChannelGroup channelGroup, MessageHandler messageHandler) {
        this.channelGroup = channelGroup;
        this.messageHandler = messageHandler;
    }

    @Override
    public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
        if (e.getMessage() instanceof Message) {
            messageHandler.messageReceived((Message) e.getMessage());
        } else {
            messageReceived(ctx, e);
        }
    }

    @Override
    public void channelConnected(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {
        this.channel = e.getChannel();
        this.channelGroup.add(e.getChannel());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) throws Exception {
        log.warn("Exception in handling messages", e.getCause());

        e.getChannel().close();
    }

    public ChannelFuture sendMessage(Message message) {
        if (channel != null) {
            return channel.write(message);
        } else {
            throw new IllegalStateException("Channel == null");
        }
    }

    public void stop() {
        channel.close();
    }
}
