package ru.frostman.jadecife.server;

import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.*;
import org.jboss.netty.channel.group.DefaultChannelGroup;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;
import org.jboss.netty.handler.codec.compression.ZlibDecoder;
import org.jboss.netty.handler.codec.compression.ZlibEncoder;
import org.jboss.netty.handler.codec.compression.ZlibWrapper;
import ru.frostman.jadecife.codec.protocol.ProtocolDecoder;
import ru.frostman.jadecife.codec.protocol.ProtocolEncoder;
import ru.frostman.jadecife.common.ByteCounter;

import java.net.InetSocketAddress;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * @author slukjanov aka Frostman
 */
public class Server {
    private final InetSocketAddress address;
    private final int ioWorkersNumber;
    private final boolean gzip;

    private DefaultChannelGroup channelGroup;
    private ServerChannelFactory serverFactory;

    public Server(InetSocketAddress address, int ioWorkersNumber, boolean gzip) {
        this.address = address;
        this.ioWorkersNumber = ioWorkersNumber;
        this.gzip = gzip;
    }

    public boolean start() {
        Executor ioWorkersPool = Executors.newFixedThreadPool(ioWorkersNumber + 1);
        serverFactory = new NioServerSocketChannelFactory(ioWorkersPool, ioWorkersPool, ioWorkersNumber);
        channelGroup = new DefaultChannelGroup(this + "-channelGroup");

        ChannelPipelineFactory pipelineFactory = new ChannelPipelineFactory() {

            @Override
            public ChannelPipeline getPipeline() throws Exception {
                ChannelPipeline pipeline = Channels.pipeline();
                if (gzip) {
                    pipeline.addLast("deflater", new ZlibEncoder(ZlibWrapper.GZIP));
                    pipeline.addLast("inflater", new ZlibDecoder(ZlibWrapper.GZIP));
                }

                pipeline.addLast("byteCounter", new ByteCounter("ClientSideByteCounter"));

                pipeline.addLast("encoder", ProtocolEncoder.getInstance());
                pipeline.addLast("decoder", new ProtocolDecoder());

                pipeline.addLast("handler", new ServerHandler(channelGroup));
                return pipeline;
            }
        };

        ServerBootstrap bootstrap = new ServerBootstrap(this.serverFactory);
        bootstrap.setOption("reuseAddress", true);
        bootstrap.setOption("child.tcpNoDelay", true);
        bootstrap.setOption("child.keepAlive", true);
        bootstrap.setPipelineFactory(pipelineFactory);

        Channel channel = bootstrap.bind(address);
        if (!channel.isBound()) {
            stop();
            return false;
        }

        channelGroup.add(channel);
        return true;
    }

    public void stop() {
        if (this.channelGroup != null) {
            this.channelGroup.close();
        }
        if (this.serverFactory != null) {
            this.serverFactory.releaseExternalResources();
        }
    }
}

