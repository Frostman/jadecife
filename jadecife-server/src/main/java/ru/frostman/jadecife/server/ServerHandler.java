package ru.frostman.jadecife.server;

import org.jboss.netty.channel.*;
import org.jboss.netty.channel.group.ChannelGroup;
import ru.frostman.jadecife.model.Message;

/**
 * @author slukjanov aka Frostman
 */
public class ServerHandler extends SimpleChannelUpstreamHandler {
    private final ChannelGroup channelGroup;

    public ServerHandler(ChannelGroup channelGroup) {
        this.channelGroup = channelGroup;
    }

    @Override
    public void channelConnected(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {
        this.channelGroup.add(e.getChannel());
    }

    @Override
    public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
        if (e.getMessage() instanceof Message) {
            // echo it...
            e.getChannel().write(e.getMessage());
        } else {
            super.messageReceived(ctx, e);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) throws Exception {
        //todo log it
        e.getCause().printStackTrace();

        //super.exceptionCaught(ctx, e);
    }
}
