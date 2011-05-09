package ru.frostman.jadecife;

import org.jboss.netty.channel.*;
import org.jboss.netty.channel.group.ChannelGroup;
import ru.frostman.jadecife.model.Message;

/**
 * @author slukjanov aka Frostman
 */

public class ClientHandler extends SimpleChannelUpstreamHandler {

    private final ClientHandlerListener listener;
    private final ChannelGroup channelGroup;
    private Channel channel;

    public ClientHandler(ClientHandlerListener listener, ChannelGroup channelGroup) {
        this.listener = listener;
        this.channelGroup = channelGroup;
    }

    @Override
    public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
        if (e.getMessage() instanceof Message) {
            this.listener.messageReceived((Message) e.getMessage());
        } else {
            super.messageReceived(ctx, e);
        }
    }

    @Override
    public void channelConnected(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {
        this.channel = e.getChannel();
        this.channelGroup.add(e.getChannel());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) throws Exception {
        e.getCause().printStackTrace();

        e.getChannel().close();
    }

    public void sendMessage(Message message) {
        if (this.channel != null) {
            this.channel.write(message);
        }
    }

    public void stop() {
        channel.close();
    }
}

