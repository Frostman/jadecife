package ru.frostman.jadecife.task;

import org.codehaus.jackson.annotate.JsonIgnore;

/**
 * @author slukjanov aka Frostman
 */
public class Task {
    private long id;

    private int classId;
    private String methodName;
    private Object[] args;

    private TaskFactory factory;

    public Task() {
    }

    public Task(int classId, String methodName, Object[] args) {
        this.classId = classId;
        this.methodName = methodName;
        this.args = args;
    }

    public Task(int classId, String methodName, Object[] args, TaskFactory factory) {
        this.classId = classId;
        this.methodName = methodName;
        this.args = args;
        this.factory = factory;
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

    public void setClassId(int classId) {
        this.classId = classId;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public Object[] getArgs() {
        return args;
    }

    public void setArgs(Object[] args) {
        this.args = args;
    }

    @JsonIgnore
    public TaskFactory getFactory() {
        return factory;
    }

    @JsonIgnore
    public void setFactory(TaskFactory factory) {
        this.factory = factory;
    }
}
