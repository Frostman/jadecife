package ru.frostman.jadecife.codec.protocol;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.replay.ReplayingDecoder;
import ru.frostman.jadecife.model.CodecType;
import ru.frostman.jadecife.model.MessageType;
import ru.frostman.jadecife.model.PingMessage;
import ru.frostman.jadecife.model.Version;

/**
 * @author slukjanov aka Frostman
 */
public class ProtocolDecoder extends ReplayingDecoder<ProtocolDecoder.DecodingState> {
    private Version version;
    private CodecType codecType;
    private MessageType messageType;
    private int dataSize;

    public ProtocolDecoder() {
        this.reset();
    }

    @Override
    protected Object decode(ChannelHandlerContext ctx, Channel channel, ChannelBuffer buffer, DecodingState state) throws Exception {
        switch (state) {
            case VERSION:
                version = Version.fromByte(buffer.readByte());
                checkpoint(DecodingState.CODEC_TYPE);
            case CODEC_TYPE:
                codecType = CodecType.fromByte(buffer.readByte());
                checkpoint(DecodingState.CODEC_TYPE);
            case MESSAGE_TYPE:
                messageType = MessageType.fromByte(buffer.readByte());
                checkpoint(DecodingState.RESERVED_BYTE);
            case RESERVED_BYTE:
                // read reserved byte
                buffer.readByte();
                checkpoint(DecodingState.DATA_SIZE);
            case DATA_SIZE:
                dataSize = buffer.readInt();
                if (dataSize <= 0) {
                    throw new ProtocolCodecException("Invalid data size");
                }

                checkpoint(DecodingState.DATA);
            case DATA:
                byte[] data = new byte[dataSize];
                buffer.readBytes(data, 0, data.length);

                //todo pass VERSION and MESSAGE_TYPE to decode method
                //todo Message message = MessageCodecFactory.getMessageJavaDecoder().decode(data, PingMessage.class);

                try {
                    return PingMessage.create();
                } finally {
                    this.reset();
                }
            default:
                throw new ProtocolCodecException("Unknown decoding state: " + state);
        }
    }

    private void reset() {
        checkpoint(DecodingState.VERSION);

        version = null;
        codecType = null;
        messageType = null;
        dataSize = 0;
    }

    public enum DecodingState {
        VERSION,
        CODEC_TYPE,
        MESSAGE_TYPE,
        RESERVED_BYTE,
        DATA_SIZE,
        DATA
    }
}
