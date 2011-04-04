package ru.frostman.jadecife.codec.protocol;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.replay.ReplayingDecoder;
import ru.frostman.jadecife.model.CodecType;
import ru.frostman.jadecife.model.Message;
import ru.frostman.jadecife.model.MessageType;
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
        //todo see this (if it always more than 4 it's very good, iff more than 8 it's COOL!!!
        System.out.println(buffer.readableBytes());

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
                if (dataSize < 0) {
                    throw new ProtocolCodecException("Invalid data size");
                }

                checkpoint(DecodingState.DATA);
            case DATA:
                // check if dataSize == 0
                byte[] data = new byte[dataSize];
                buffer.readBytes(data, 0, data.length);

                Message message = null;

                try {
                    return message;
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
