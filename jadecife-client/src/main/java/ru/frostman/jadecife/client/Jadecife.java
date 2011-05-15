package ru.frostman.jadecife.client;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.NotFoundException;
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
import ru.frostman.jadecife.model.ClassEntry;
import ru.frostman.jadecife.model.Message;
import ru.frostman.jadecife.model.MessageType;
import ru.frostman.jadecife.task.TaskFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author slukjanov aka Frostman
 */
public class Jadecife implements MessageHandler {
    private ChannelFactory clientFactory;
    private ChannelGroup channelGroup;
    private ClientHandler handler;

    private MessageHandler currentMessageHandler;

    public Jadecife(int threads, final boolean gzip, String host, int port) throws JadecifeException {
        Preconditions.checkArgument(threads >= 2, "Minimum 2 threads needed");
        Executor executor = Executors.newFixedThreadPool(threads);
        this.clientFactory = new OioClientSocketChannelFactory(executor);
        this.channelGroup = new DefaultChannelGroup(this + "-channelGroup");
        this.handler = new ClientHandler(this.channelGroup, this);
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
    }

    public void stop() {
        if (this.channelGroup != null) {
            this.channelGroup.close();
        }
        if (this.clientFactory != null) {
            this.clientFactory.releaseExternalResources();
        }
    }

    @Override
    public void messageReceived(Message message) {
        if (currentMessageHandler == null) {
            System.out.println(message);
            return;
        }

        currentMessageHandler.messageReceived(message);
    }

    public Map<String, Integer> registerClasses(Class... classes) throws JadecifeException {
        //todo replace default with custom
        ClassPool cp = ClassPool.getDefault();

        List<ClassEntry> entries = new LinkedList<ClassEntry>();
        for (Class clazz : classes) {
            CtClass ctClass = null;
            try {
                ctClass = cp.get(clazz.getName());
            } catch (NotFoundException e) {
                throw new JadecifeException("Class not found", e);
            }

            byte[] bytes = null;
            try {
                bytes = ctClass.toBytecode();
            } catch (IOException e) {
                throw new JadecifeException(e);
            } catch (CannotCompileException e) {
                throw new JadecifeException(e);
            }

            entries.add(new ClassEntry(clazz.getName(), bytes));
        }

        final Object lock = new Object();
        final AtomicInteger count = new AtomicInteger(0);
        final Map<String, Integer> registeredClasses = Maps.newHashMap();
        currentMessageHandler = new MessageHandler() {
            @Override
            public void messageReceived(Message message) {
                Preconditions.checkState(message.getType() == MessageType.CLASS_REGISTERED);
                ClassRegisteredMessage registered = (ClassRegisteredMessage) message;
                registeredClasses.put(registered.getName(), registered.getClassId());
                count.incrementAndGet();

                synchronized (lock) {
                    lock.notify();
                }
            }
        };

        for (ClassEntry entry : entries) {
            handler.sendMessage(new ClassRegisterMessage(entry));
        }

        while (count.get() < entries.size()) {
            try {
                synchronized (lock) {
                    lock.wait(1000);
                }
            } catch (InterruptedException e) {
                //no operations
            }
        }

        return registeredClasses;
    }

    public long addTaskFactory(int taskFactoryClassId, TaskFactory taskFactory) {
        final Object lock = new Object();
        final long[] factoryId = {0, 0};
        currentMessageHandler = new MessageHandler() {
            @Override
            public void messageReceived(Message message) {
                Preconditions.checkState(message.getType() == MessageType.TASK_FACTORY_ADDED);
                TaskFactoryAddedMessage added = (TaskFactoryAddedMessage) message;
                factoryId[0] = 1;
                factoryId[1] = added.getFactoryId();
                synchronized (lock) {
                    lock.notify();
                }
            }
        };

        handler.sendMessage(new AddTaskFactoryMessage(taskFactoryClassId, taskFactory));

        while (factoryId[0] == 0) {
            synchronized (lock) {
                try {
                    lock.wait(1000);
                } catch (InterruptedException e) {
                    //no operations
                }
            }
        }

        return factoryId[1];
    }
}
