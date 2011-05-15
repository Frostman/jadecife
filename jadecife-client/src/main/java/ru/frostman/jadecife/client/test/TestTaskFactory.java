package ru.frostman.jadecife.client.test;

import ru.frostman.jadecife.task.Task;
import ru.frostman.jadecife.task.TaskFactory;

import java.util.Set;

/**
 * @author slukjanov aka Frostman
 */
public class TestTaskFactory extends TaskFactory {
    private Set<Integer> neededClasses;
    private int taskClassId;

    public TestTaskFactory() {
    }

    public TestTaskFactory(Set<Integer> neededClasses, int taskClassId) {
        this.neededClasses = neededClasses;
        this.taskClassId = taskClassId;
    }

    @Override
    public Set<Integer> neededClasses() {
        return neededClasses;
    }

    @Override
    public boolean hasNext() {
        return true;
    }

    @Override
    public Task next() {
        return new Task(taskClassId, "printTime", new Object[]{System.nanoTime()});
    }
}
