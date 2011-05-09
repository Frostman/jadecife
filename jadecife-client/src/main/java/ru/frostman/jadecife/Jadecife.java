package ru.frostman.jadecife;

import com.google.common.base.Preconditions;
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
import org.jboss.netty.channel.socket.nio.NioClientSocketChannelFactory;
import org.jboss.netty.handler.codec.compression.ZlibDecoder;
import org.jboss.netty.handler.codec.compression.ZlibEncoder;
import org.jboss.netty.handler.codec.compression.ZlibWrapper;
import ru.frostman.jadecife.codec.protocol.ProtocolDecoder;
import ru.frostman.jadecife.codec.protocol.ProtocolEncoder;
import ru.frostman.jadecife.common.ByteCounter;
import ru.frostman.jadecife.message.AddTaskFactoryMessage;
import ru.frostman.jadecife.message.ClassRegisterMessage;
import ru.frostman.jadecife.model.ClassEntry;
import ru.frostman.jadecife.model.Message;
import ru.frostman.jadecife.task.TaskFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * @author slukjanov aka Frostman
 */
public class Jadecife implements MessageHandler {
    private ChannelFactory clientFactory;
    private ChannelGroup channelGroup;
    private ClientHandler handler;

    public Jadecife(int threads, final boolean gzip, String host, int port) throws JadecifeException {
        Preconditions.checkArgument(threads >= 2, "Minimum 2 threads needed");
        Executor executor = Executors.newFixedThreadPool(threads);
        this.clientFactory = new NioClientSocketChannelFactory(executor, executor, threads - 1);
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
        //todo impl it
        System.out.println(message);
    }

    public void registerClasses(Class... classes) throws JadecifeException {
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

        for (ClassEntry entry : entries) {
            handler.sendMessage(new ClassRegisterMessage(entry));
        }
    }

    public void addTaskFactory(TaskFactory taskFactory) {
        handler.sendMessage(new AddTaskFactoryMessage(taskFactory));
    }
}
