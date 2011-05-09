package ru.frostman.jadecife.server;

import com.google.common.collect.Maps;
import org.jboss.netty.channel.*;
import org.jboss.netty.channel.group.ChannelGroup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.frostman.jadecife.message.ClassRegisterMessage;
import ru.frostman.jadecife.message.ClassRegisteredMessage;
import ru.frostman.jadecife.message.PingMessage;
import ru.frostman.jadecife.model.ClassEntry;
import ru.frostman.jadecife.model.Message;

import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author slukjanov aka Frostman
 */
public class ServerHandler extends SimpleChannelUpstreamHandler {
    private static final Logger log = LoggerFactory.getLogger(ServerHandler.class);

    private final ChannelGroup channelGroup;

    private final AtomicInteger registeredClassId = new AtomicInteger(1);
    //todo synchronize it?
    private final Map<Integer, ClassEntry> registeredClasses = Maps.newHashMap();

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
            Message message = (Message) e.getMessage();

            switch (message.getType()) {
                case PING:
                    ((PingMessage) message).setCreateTime(System.nanoTime());
                    e.getChannel().write(message);
                    break;
                case CLASS_REGISTER:
                    int id = registeredClassId.getAndIncrement();
                    registeredClasses.put(id, ((ClassRegisterMessage) message).getClassEntry());
                    e.getChannel().write(new ClassRegisteredMessage(id));
                    break;
            }

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
