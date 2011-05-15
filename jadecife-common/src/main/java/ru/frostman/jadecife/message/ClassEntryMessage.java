package ru.frostman.jadecife.message;

import org.codehaus.jackson.annotate.JsonIgnore;
import ru.frostman.jadecife.model.ClassEntry;
import ru.frostman.jadecife.model.Message;
import ru.frostman.jadecife.model.MessageType;

/**
 * @author slukjanov aka Frostman
 */
public class ClassEntryMessage extends Message {
    private ClassEntry entry;
    private int classId;

    public ClassEntryMessage() {
    }

    public ClassEntryMessage(ClassEntry entry, int classId) {
        this.entry = entry;
        this.classId = classId;
    }

    public ClassEntry getEntry() {
        return entry;
    }

    public void setEntry(ClassEntry entry) {
        this.entry = entry;
    }

    public int getClassId() {
        return classId;
    }

    public void setClassId(int classId) {
        this.classId = classId;
    }

    @JsonIgnore
    @Override
    public MessageType getType() {
        return MessageType.CLASS_ENTRY;
    }
}
