package ru.frostman.jadecife.task;

/**
 * @author slukjanov aka Frostman
 */
public class Task {
    private long id;

    private int classId;
    private String methodName;
    private Object[] args;

    public Task() {
    }

    public Task(int classId, String methodName, Object[] args) {
        this.classId = classId;
        this.methodName = methodName;
        this.args = args;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getClassId() {
        return classId;
    }

    public String getMethodName() {
        return methodName;
    }

    public Object[] getArgs() {
        return args;
    }
}
