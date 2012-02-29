package ru.frostman.jadecife.common;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.channel.*;

import java.util.concurrent.atomic.AtomicLong;

/**
 * Simple upstream handler, that provides counting all read
 * and written bytes, that send throw channels.
 * Use new instance for each new channel, for example in
 * ChannelPipelineFactory implementation.
 *
 * @author slukjanov aka Frostman
 */
public class ByteCounter extends SimpleChannelUpstreamHandler {
    /**
     * ByteCounter unique id, used for output statistics
     */
    private final String id;

    /**
     * Written bytes counter
     */
    private final AtomicLong writtenBytes = new AtomicLong();

    /**
     * Read bytes counter
     */
    private final AtomicLong readBytes = new AtomicLong();

    /**
     * Creates new ByteCounter with specified id
     *
     * @param id to identify counter
     */
    public ByteCounter(String id) {
        this.id = id;
    }

    @Override
    public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
        if (e.getMessage() instanceof ChannelBuffer) {
            readBytes.addAndGet(((ChannelBuffer) e.getMessage()).readableBytes());
        }

        super.messageReceived(ctx, e);
    }

    @Override
    public void writeComplete(ChannelHandlerContext ctx, WriteCompletionEvent e) throws Exception {
        super.writeComplete(ctx, e);
        writtenBytes.addAndGet(e.getWrittenAmount());
    }

    @Override
    public void channelClosed(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {
        super.channelClosed(ctx, e);

        System.out.println(id + ctx.getChannel() + " -> sent: " +
                getWrittenBytes() + "b, recv: " +
                getReadBytes() + "b, ratio: "
                + getReadBytes() * 1.0 / getWrittenBytes());
    }

    public void resetCounters() {
        writtenBytes.set(0);
        readBytes.set(0);
    }

    /**
     * @return written bytes
     */
    public long getWrittenBytes() {
        return writtenBytes.get();
    }

    /**
     * @return read bytes
     */
    public long getReadBytes() {
        return readBytes.get();
    }
}

