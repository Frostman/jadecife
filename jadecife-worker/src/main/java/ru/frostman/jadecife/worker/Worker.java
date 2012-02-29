package ru.frostman.jadecife.worker;

import com.google.common.base.Preconditions;
import org.jboss.netty.bootstrap.ClientBootstrap;
import org.jboss.netty.channel.ChannelFactory;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.channel.group.ChannelGroup;
import org.jboss.netty.channel.group.DefaultChannelGroup;
import org.jboss.netty.channel.socket.oio.OioClientSocketChannelFactory;
import org.jboss.netty.handler.codec.compression.ZlibDecoder;
import org.jboss.netty.handler.codec.compression.ZlibEncoder;
import org.jboss.netty.handler.codec.compression.ZlibWrapper;
import ru.frostman.jadecife.JadecifeException;
import ru.frostman.jadecife.codec.protocol.ProtocolDecoder;
import ru.frostman.jadecife.codec.protocol.ProtocolEncoder;
import ru.frostman.jadecife.common.ByteCounter;
import ru.frostman.jadecife.message.*;
import ru.frostman.jadecife.model.Message;
import ru.frostman.jadecife.model.MessageType;
import ru.frostman.jadecife.task.Task;
import ru.frostman.jadecife.worker.cli.WorkerHandler;

import java.lang.reflect.Method;
import java.net.InetSocketAddress;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * @author slukjanov aka Frostman
 */
public class Worker implements MessageHandler {
    private ChannelFactory clientFactory;
    private ChannelGroup channelGroup;
    private WorkerHandler handler;

    private WorkerClassLoader loader = new WorkerClassLoader();

    public Worker(int threads, final boolean gzip, String host, int port) throws JadecifeException {
        Preconditions.checkArgument(threads >= 2, "Minimum 2 threads needed");
        Executor executor = Executors.newFixedThreadPool(threads);
        this.clientFactory = new OioClientSocketChannelFactory(executor);
        this.channelGroup = new DefaultChannelGroup(this + "-channelGroup");
        this.handler = new WorkerHandler(this.channelGroup, this);
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
            throw new JadecifeException("Can't connect to server");
        }

        handler.sendMessage(new GetTaskMessage());
    }

    @Override
    public void messageReceived(Message message) {
        MessageType type = message.getType();

        switch (type) {
            case RUN_TASK:
                RunTaskMessage runTaskMessage = (RunTaskMessage) message;
                Task task = runTaskMessage.getTask();

                if (task != null) {
                    try {
                        Class c = loader.findClass(task.getClassId());
                        Preconditions.checkState(c != null);
                        Class[] classes = new Class[task.getArgs().length];
                        int i = 0;
                        for (Object arg : task.getArgs()) {
                            classes[i++] = arg.getClass();
                        }
                        assert c != null;
                        Method method = c.getMethod(task.getMethodName(), classes);
                        Object result = method.invoke(c.newInstance(), task.getArgs());
                        handler.sendMessage(new RunTaskResultMessage(task.getId(), result));
                    } catch (Exception e) {
                        handler.sendMessage(new RunTaskErrorMessage(task.getId(), e));
                    }
                } else {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        //no operations
                    }
                }

                handler.sendMessage(new GetTaskMessage());
                break;
            case CLASS_ENTRY:
                ClassEntryMessage entryMessage = (ClassEntryMessage) message;
                loader.defineClass(entryMessage.getEntry(), entryMessage.getClassId());
                break;
        }

    }

    public void stop() {
        if (this.channelGroup != null) {
            this.channelGroup.close();
        }
        if (this.clientFactory != null) {
            this.clientFactory.releaseExternalResources();
        }
    }
}
