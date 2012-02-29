package ru.frostman.jadecife.server;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import org.codehaus.jackson.map.ObjectMapper;
import ru.frostman.jadecife.task.Task;
import ru.frostman.jadecife.task.TaskFactory;

import java.util.LinkedList;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author slukjanov aka Frostman
 */
public class TaskManager {
    private static final AtomicLong currentFactoryId = new AtomicLong(1);
    private static final AtomicLong currentTaskId = new AtomicLong(1);

    //todo temporary solution
    private static final Map<Long, TaskFactory> taskTaskFactoryMap = Maps.newHashMap();

    private static final LinkedList<TaskFactory> taskFactories = new LinkedList<TaskFactory>();
    private static final LinkedList<Task> rejectedTasks = new LinkedList<Task>();
    private static TaskFactory currentFactory;


    public static synchronized void addTaskFactory(TaskFactory taskFactory) {
        taskFactory.setId(currentFactoryId.getAndIncrement());
        taskFactories.addLast(taskFactory);
    }

    public static synchronized Task nextTask() throws NoSuchElementException {
        while (true) {
            if (currentFactory == null) {
                try {
                    currentFactory = taskFactories.removeFirst();
                } catch (NoSuchElementException e) {
                    return null;
                }
            }

            if (currentFactory.hasNext()) {
                Task task = currentFactory.next();
                task.setId(currentTaskId.getAndIncrement());
                task.setFactory(currentFactory);
                taskTaskFactoryMap.put(task.getId(), currentFactory);
                return task;
            }
        }
    }

    public static synchronized void taskInvoked(long taskId, Object result) {
        TaskFactory factory = taskTaskFactoryMap.get(taskId);
        Preconditions.checkState(factory != null, "Can't find factory that produced task: " + taskId);
        taskTaskFactoryMap.remove(taskId);

        assert factory != null;
        if (result == null) {
            factory.taskInvoked(null);
        } else {
            Class resultType = factory.taskResultType();
            factory.taskInvoked(mapper.convertValue(result, resultType));
        }
    }

    //todo temporary solution
    private static final ObjectMapper mapper = new ObjectMapper();
}
