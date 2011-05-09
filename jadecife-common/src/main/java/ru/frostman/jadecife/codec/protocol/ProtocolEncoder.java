package ru.frostman.jadecife.codec.protocol;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBufferOutputStream;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandler;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.oneone.OneToOneEncoder;
import ru.frostman.jadecife.codec.message.MessageCodecFactory;
import ru.frostman.jadecife.model.CodecType;
import ru.frostman.jadecife.model.Message;
import ru.frostman.jadecife.model.Version;

import static org.jboss.netty.buffer.ChannelBuffers.dynamicBuffer;

/**
 * @author slukjanov aka Frostman
 */
@ChannelHandler.Sharable
public class ProtocolEncoder extends OneToOneEncoder {
    private static final byte[] LENGTH_PLACEHOLDER = new byte[4];

    private ProtocolEncoder() {
    }

    public static ProtocolEncoder getInstance() {
        return InstanceStorage.INSTANCE;
    }

    @Override
    protected Object encode(ChannelHandlerContext ctx, Channel channel, Object msg) throws Exception {
        if (msg instanceof Message) {
            return encodeMessage(ctx, (Message) msg);
        }

        return msg;
    }

    private ChannelBuffer encodeMessage(ChannelHandlerContext ctx, Message message) throws ProtocolCodecException {
        try {
            ChannelBufferOutputStream bout = new ChannelBufferOutputStream(dynamicBuffer(
                    150, ctx.getChannel().getConfig().getBufferFactory()));
            //todo 150 == estimated size

            bout.write(Version.VERSION_1.getByteValue());
            bout.write(CodecType.JSON.getByteValue());
            bout.write(message.getType().getByteValue());
            bout.write(0xFF);

            bout.write(LENGTH_PLACEHOLDER);

            MessageCodecFactory.getMessageJsonEncoder().encode(bout, message);

            ChannelBuffer encoded = bout.buffer();
            encoded.setInt(4, encoded.writerIndex() - 8);
            return encoded;
        } catch (Exception e) {
            throw new ProtocolCodecException(e);
        }
    }

    private static final class InstanceStorage {
        private static final ProtocolEncoder INSTANCE = new ProtocolEncoder();
    }
}
