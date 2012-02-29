package ru.frostman.jadecife.worker;

import com.google.common.collect.Maps;
import ru.frostman.jadecife.model.ClassEntry;

import java.util.Map;

/**
 * @author slukjanov aka Frostman
 */
public class WorkerClassLoader extends ClassLoader {
    private final Map<Integer, Class> classes = Maps.newHashMap();

    public void defineClass(ClassEntry entry, int classId) {
        String name = entry.getName();
        byte[] bytes = entry.getBytes();

        Class<?> clazz = defineClass(name, bytes, 0, bytes.length);
        classes.put(classId, clazz);
    }

    public Class<?> findClass(int classId) {
        return classes.get(classId);
    }
}
