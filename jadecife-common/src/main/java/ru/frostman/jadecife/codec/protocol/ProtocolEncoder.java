package ru.frostman.jadecife.codec.protocol;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandler;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.oneone.OneToOneEncoder;
import ru.frostman.jadecife.codec.message.MessageCodecException;
import ru.frostman.jadecife.codec.message.MessageCodecFactory;
import ru.frostman.jadecife.model.CodecType;
import ru.frostman.jadecife.model.Message;
import ru.frostman.jadecife.model.MessageType;
import ru.frostman.jadecife.model.Version;

/**
 * @author slukjanov aka Frostman
 */
@ChannelHandler.Sharable
public class ProtocolEncoder extends OneToOneEncoder {

    private ProtocolEncoder() {
    }

    public static ProtocolEncoder getInstance() {
        return InstanceStorage.INSTANCE;
    }

    @Override
    protected Object encode(ChannelHandlerContext ctx, Channel channel, Object msg) throws Exception {
        if (msg instanceof Message) {
            return encodeMessage((Message) msg);
        }

        return msg;
    }

    private ChannelBuffer encodeMessage(Message message) throws ProtocolCodecException {
        byte[] encodedMessage = new byte[0];
        try {
            encodedMessage = MessageCodecFactory.getMessageJavaEncoder().encode(message);
        } catch (MessageCodecException e) {
            throw new ProtocolCodecException(e);
        }
        int bufferSize = 8 + encodedMessage.length;

        ChannelBuffer buffer = ChannelBuffers.buffer(bufferSize);
        //todo add options
        buffer.writeByte(Version.VERSION_1.getByteValue());
        buffer.writeByte(CodecType.JAVA_SERIALIZATION.getByteValue());
        buffer.writeByte(MessageType.UNKNOWN.getByteValue());
        //reserved byte
        buffer.writeByte(0xFF);
        buffer.writeBytes(encodedMessage);

        return buffer;
    }

    private static final class InstanceStorage {
        private static final ProtocolEncoder INSTANCE = new ProtocolEncoder();
    }
}
