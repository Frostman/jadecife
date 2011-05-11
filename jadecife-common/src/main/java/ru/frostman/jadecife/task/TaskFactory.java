package ru.frostman.jadecife.task;

import java.util.Iterator;
import java.util.Set;

/**
 * @author slukjanov aka Frostman
 */
public abstract class TaskFactory implements Iterable<Task> {
    private long id;

    public abstract Set<Integer> neededClasses();

    public abstract boolean hasNext();

    public abstract Task next();

    @Override
    public Iterator<Task> iterator() {
        return new TaskFactoryIterator();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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
