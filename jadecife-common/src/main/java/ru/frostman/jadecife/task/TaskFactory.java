package ru.frostman.jadecife.task;

import java.util.Iterator;

/**
 * @author slukjanov aka Frostman
 */
public abstract class TaskFactory implements Iterable<Task> {

    public abstract boolean hasNext();

    public abstract Task next();

    @Override
    public Iterator<Task> iterator() {
        return new TaskFactoryIterator();
    }

    private final class TaskFactoryIterator implements Iterator<Task>{

        @Override
        public boolean hasNext() {
            return TaskFactory.this.hasNext();
        }

        @Override
        public Task next() {
            return TaskFactory.this.next();
        }

        @Override
        public void remove() {
        }
    }
}
