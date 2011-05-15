package ru.frostman.jadecife.server;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import org.codehaus.jackson.map.ObjectMapper;
import org.jboss.netty.channel.*;
import org.jboss.netty.channel.group.ChannelGroup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.frostman.jadecife.message.*;
import ru.frostman.jadecife.model.ClassEntry;
import ru.frostman.jadecife.model.Message;
import ru.frostman.jadecife.task.Task;
import ru.frostman.jadecife.task.TaskFactory;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author slukjanov aka Frostman
 */
public class ServerHandler extends SimpleChannelUpstreamHandler {
    private static final Logger log = LoggerFactory.getLogger(ServerHandler.class);

    private static final AtomicInteger registeredClassId = new AtomicInteger(1);
    private static final Map<Integer, ClassEntry> registeredClasses = Maps.newHashMap();
    private static final Map<String, Integer> registeredClassesNames = Maps.newHashMap();
    private static final ClassLoader classLoader = new ClassLoader() {
        protected Class<?> loadClass(int classId) throws ClassNotFoundException {
            ClassEntry entry = registeredClasses.get(classId);
            String name = entry.getName();
            byte[] bytes = entry.getBytes();

            Class<?> clazz = defineClass(name, bytes, 0, bytes.length);
            resolveClass(clazz);
            return clazz;
        }

        @Override
        protected Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
            if (name.startsWith("###")) {
                return loadClass(Integer.parseInt(name.substring(3)));
            }

            if (registeredClassesNames.containsKey(name)) {
                return loadClass(registeredClassesNames.get(name));
            }

            try {
                return ServerHandler.class.getClassLoader().loadClass(name);
            } catch (ClassNotFoundException e) {
                return super.loadClass(name, resolve);
            }
        }
    };

    private final ChannelGroup channelGroup;

    //todo remove it
    private static final ObjectMapper mapper = new ObjectMapper();

    /**
     * Id of classes that sent to workers
     */
    private final Set<Integer> sentEntries = Sets.newHashSet();

    public ServerHandler(ChannelGroup channelGroup) {
        this.channelGroup = channelGroup;
    }

    @Override
    public void channelConnected(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {
        this.channelGroup.add(e.getChannel());

        log.info("connected: " + e.getChannel());
    }

    @Override
    public void messageReceived(ChannelHandlerContext ctx, final MessageEvent e) throws Exception {
        if (e.getMessage() instanceof Message) {
            Message message = (Message) e.getMessage();

            switch (message.getType()) {
                case PING:
                    ((PingMessage) message).setCreateTime(System.nanoTime());
                    e.getChannel().write(message);
                    break;
                case CLASS_REGISTER:
                    int id = registeredClassId.getAndIncrement();
                    ClassEntry entry = ((ClassRegisterMessage) message).getClassEntry();
                    registeredClasses.put(id, entry);
                    registeredClassesNames.put(entry.getName(), id);
                    e.getChannel().write(new ClassRegisteredMessage(entry.getName(), id));
                    break;
                case TASK_FACTORY_ADD:
                    final AddTaskFactoryMessage addTaskFactoryMessage = (AddTaskFactoryMessage) message;
                    Object factoryObj = addTaskFactoryMessage.getFactory();
                    Class<TaskFactory> factoryClass = (Class<TaskFactory>) classLoader
                            .loadClass("###" + addTaskFactoryMessage.getFactoryClassId());

                    TaskFactory factory = mapper.convertValue(factoryObj, factoryClass);

                    long factoryId = 0;
                    if (factory != null && factory.hasNext()) {
                        TaskManager.addTaskFactory(factory);
                        factoryId = factory.getId();
                    }
                    e.getChannel().write(new TaskFactoryAddedMessage(factoryId));
                    break;
                case GET_TASK:
                    Task task = TaskManager.nextTask();
                    if (task != null) {
                        TaskFactory taskFactory = task.getFactory();
                        for (Integer classId : taskFactory.neededClasses()) {
                            if (sentEntries.contains(classId)) {
                                continue;
                            }

                            e.getChannel().write(new ClassEntryMessage(registeredClasses.get(classId), classId));
                            sentEntries.add(classId);
                        }
                    }
                    e.getChannel().write(new RunTaskMessage(task));
                    break;
                case RUN_RESULT:
                    RunTaskResultMessage resultMessage = (RunTaskResultMessage) message;
                    TaskManager.taskInvoked(resultMessage.getTaskId(), resultMessage.getResult());
                    break;
                default:
                    log.warn("Unsupported message received: " + message.getType());
                    break;
            }

        } else {
            super.messageReceived(ctx, e);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) throws Exception {
        log.warn("Exception in ServerHandler ", e.getCause());

        e.getChannel().close();
    }
}
