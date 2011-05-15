package ru.frostman.jadecife.client.sample;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.frostman.jadecife.task.Task;
import ru.frostman.jadecife.task.TaskFactory;

import java.util.Set;

/**
 * @author slukjanov aka Frostman
 */
public class HashBruteFactory extends TaskFactory {
    private static final Logger log = LoggerFactory.getLogger(HashBruteFactory.class);

    private volatile boolean hasNext = true;
    private Set<Integer> neededClasses;
    private Integer classId;
    private String expectedMessage;
    private String msg = "" + ((char) ('a' - 1)), baseMsg = "a";

    public HashBruteFactory() {
    }

    public HashBruteFactory(Set<Integer> neededClasses, Integer classId, String expectedMessage) {
        this.neededClasses = neededClasses;
        this.classId = classId;
        this.expectedMessage = expectedMessage;
    }

    @Override
    public Set<Integer> neededClasses() {
        return neededClasses;
    }

    @Override
    public boolean hasNext() {
        return hasNext;
    }

    @Override
    public Task next() {
        msg = BruteForce.next(msg);
        if (msg == null) {
            baseMsg += "a";
            msg = baseMsg;
        }

        return new Task(classId, "brute", new Object[]{expectedMessage, msg, 0});
    }

    @Override
    public Class taskResultType() {
        return String.class;
    }

    @Override
    public void taskInvoked(Object result) {
        if (result instanceof String) {
            log.info("RESULT: " +result);
            hasNext = false;
        }
    }

    public Set<Integer> getNeededClasses() {
        return neededClasses;
    }

    public void setNeededClasses(Set<Integer> neededClasses) {
        this.neededClasses = neededClasses;
    }

    public Integer getClassId() {
        return classId;
    }

    public void setClassId(Integer classId) {
        this.classId = classId;
    }

    public String getExpectedMessage() {
        return expectedMessage;
    }

    public void setExpectedMessage(String expectedMessage) {
        this.expectedMessage = expectedMessage;
    }
}
