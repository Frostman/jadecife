package ru.frostman.jadecife;

import org.jboss.netty.bootstrap.ClientBootstrap;
import org.jboss.netty.channel.ChannelFactory;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.channel.group.ChannelGroup;
import org.jboss.netty.channel.group.DefaultChannelGroup;
import org.jboss.netty.channel.socket.nio.NioClientSocketChannelFactory;
import ru.frostman.jadecife.codec.protocol.ProtocolDecoder;
import ru.frostman.jadecife.codec.protocol.ProtocolEncoder;
import ru.frostman.jadecife.common.ByteCounter;
import ru.frostman.jadecife.model.Message;
import ru.frostman.jadecife.model.PingMessage;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author slukjanov aka Frostman
 */
public class FakeClient implements ClientHandlerListener {

    // configuration --------------------------------------------------------------------------------------------------

    private final String host;
    private final int port;
    private final int messages;
    private final int floods;

    private ChannelFactory clientFactory;
    private ChannelGroup channelGroup;
    private ClientHandler handler;
    private final AtomicInteger received;
    private int flood;
    private long startTime;

    public FakeClient(String host, int port, int messages, int floods) {
        this.host = host;
        this.port = port;
        this.messages = messages;
        this.floods = floods;
        this.received = new AtomicInteger(0);
        this.flood = 0;
    }

    @Override
    public void messageReceived(Message message) {
        // System.err.println("Received message " + message);
        if (this.received.incrementAndGet() == this.messages) {
            long stopTime = System.currentTimeMillis();
            float timeInSeconds = (stopTime - this.startTime) / 1000f;
            System.err.println("Sent and received " + this.messages + " in " + timeInSeconds + "s");
            System.err.println("That's " + (this.messages / timeInSeconds) + " echoes per second!");

            // ideally, this should be sent to another thread, since this method is called by a netty worker thread.
            if (this.flood < this.floods) {
                this.received.set(0);
                this.flood++;
                this.flood();
            } else {
                new Thread() {
                    @Override
                    public void run() {
                        client.stop();
                    }
                }.start();
            }
        }
    }

    public boolean start() {
        // For production scenarios, use limited sized thread pools
        this.clientFactory = new NioClientSocketChannelFactory(Executors.newCachedThreadPool(),
                Executors.newCachedThreadPool());
        this.channelGroup = new DefaultChannelGroup(this + "-channelGroup");
        this.handler = new ClientHandler(this, this.channelGroup);
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

                pipeline.addLast("handler", handler);
                return pipeline;
            }
        };

        ClientBootstrap bootstrap = new ClientBootstrap(this.clientFactory);
        bootstrap.setOption("reuseAddress", true);
        bootstrap.setOption("tcpNoDelay", true);
        bootstrap.setOption("keepAlive", true);
        bootstrap.setPipelineFactory(pipelineFactory);


        boolean connected = bootstrap.connect(new InetSocketAddress(host, port)).awaitUninterruptibly().isSuccess();
        if (!connected) {
            this.stop();
        }

        return connected;
    }

    public void stop() {
        if (this.channelGroup != null) {
            this.channelGroup.close();
        }
        if (this.clientFactory != null) {
            this.clientFactory.releaseExternalResources();
        }
    }

    private void flood() {
        if ((this.channelGroup == null) || (this.clientFactory == null)) {
            return;
        }

        this.startTime = System.currentTimeMillis();
        for (int i = 0; i < this.messages; i++) {
            this.handler.sendMessage(PingMessage.create());
        }
    }

    // main -----------------------------------------------------------------------------------------------------------

    static FakeClient client;
    public static void main(String[] args) throws InterruptedException {
        client = new FakeClient("localhost", 9999, 1000000, 1);

        if (!client.start()) {
            System.exit(-1);
            return;
        }

        System.out.println("Client started...");

        client.flood();

        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                client.stop();
            }
        });
    }
}

