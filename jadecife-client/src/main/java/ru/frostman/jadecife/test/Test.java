package ru.frostman.jadecife.test;

import ru.frostman.jadecife.Jadecife;
import ru.frostman.jadecife.JadecifeException;
import ru.frostman.jadecife.cli.LoggingConfigurator;
import ru.frostman.jadecife.cli.LoggingLevel;

/**
 * @author slukjanov aka Frostman
 */
public class Test {
    public static void main(String[] args) throws JadecifeException {
        LoggingConfigurator.configure(LoggingLevel.DEBUG);
        Jadecife client = new Jadecife(2, false, "localhost", 7890);
        client.registerClasses(TestClass.class);


    }
}
