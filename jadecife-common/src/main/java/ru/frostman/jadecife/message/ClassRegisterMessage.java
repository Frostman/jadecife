package ru.frostman.jadecife.message;

import org.codehaus.jackson.annotate.JsonIgnore;
import ru.frostman.jadecife.model.ClassEntry;
import ru.frostman.jadecife.model.Message;
import ru.frostman.jadecife.model.MessageType;

/**
 * @author slukjanov aka Frostman
 */
public class ClassRegisterMessage extends Message{
    private ClassEntry classEntry;

    public ClassRegisterMessage() {
    }

    public ClassRegisterMessage(ClassEntry classEntry) {
        this.classEntry = classEntry;
    }

    public ClassEntry getClassEntry() {
        return classEntry;
    }

    public void setClassEntry(ClassEntry classEntry) {
        this.classEntry = classEntry;
    }

    @JsonIgnore
    @Override
    public MessageType getType() {
        return MessageType.CLASS_REGISTER;
    }
}
