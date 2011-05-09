package ru.frostman.jadecife.server;

import org.jboss.netty.channel.*;
import org.jboss.netty.channel.group.ChannelGroup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.frostman.jadecife.message.PingMessage;
import ru.frostman.jadecife.model.Message;

/**
 * @author slukjanov aka Frostman
 */
public class ServerHandler extends SimpleChannelUpstreamHandler {
    private static final Logger log = LoggerFactory.getLogger(ServerHandler.class);

    private final ChannelGroup channelGroup;

    public ServerHandler(ChannelGroup channelGroup) {
        this.channelGroup = channelGroup;
    }

    @Override
    public void channelConnected(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {
        this.channelGroup.add(e.getChannel());
    }

    @Override
    public void messageReceived(ChannelHandlerContext ctx, final MessageEvent e) throws Exception {
        if (e.getMessage() instanceof Message) {
            final PingMessage message = (PingMessage) e.getMessage();
            message.setCreateTime(2);

            ChannelFuture cf = e.getChannel().write(message);


        } else {
            super.messageReceived(ctx, e);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) throws Exception {
        log.warn("Exception in ServerHandler ", e);

        e.getChannel().close();
    }
}
