package ru.frostman.jadecife.server;

import ru.frostman.jadecife.task.Task;
import ru.frostman.jadecife.task.TaskFactory;

import java.util.LinkedList;
import java.util.NoSuchElementException;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author slukjanov aka Frostman
 */
public class TaskManager {
    private static final AtomicLong currentFactoryId = new AtomicLong(1);
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
                currentFactory = taskFactories.removeFirst();
            }

            if (currentFactory.hasNext()) {
                return currentFactory.next();
            }
        }
    }
}
