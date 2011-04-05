package ru.frostman.jadecife.server;

import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.*;
import org.jboss.netty.channel.group.DefaultChannelGroup;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;
import ru.frostman.jadecife.codec.protocol.ProtocolDecoder;
import ru.frostman.jadecife.codec.protocol.ProtocolEncoder;
import ru.frostman.jadecife.common.ByteCounter;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

/**
 * @author slukjanov aka Frostman
 */
public class Server {
    private final String host;
    private final int port;
    private DefaultChannelGroup channelGroup;
    private ServerChannelFactory serverFactory;

    public Server(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public boolean start() {
        serverFactory = new NioServerSocketChannelFactory(Executors.newCachedThreadPool(), Executors.newCachedThreadPool());
        channelGroup = new DefaultChannelGroup(this + "-channelGroup");

        ChannelPipelineFactory pipelineFactory = new ChannelPipelineFactory() {

            @Override
            public ChannelPipeline getPipeline() throws Exception {
                ChannelPipeline pipeline = Channels.pipeline();
                // Enable stream compression
                //pipeline.addLast("deflater", new ZlibEncoder(ZlibWrapper.GZIP));
                //pipeline.addLast("inflater", new ZlibDecoder(ZlibWrapper.GZIP));

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

        Channel channel = bootstrap.bind(new InetSocketAddress(this.host, this.port));
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

    public static void main(String[] args) {
        final Server server = new Server("localhost", 9999);

        if (!server.start()) {
            System.exit(-1);
            return;
        }

        System.out.println("Server started...");

        Runtime.getRuntime().addShutdownHook(new Thread() {

            @Override
            public void run() {
                server.stop();
            }
        });
    }
}

