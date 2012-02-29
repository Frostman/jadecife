package ru.frostman.jadecife.client.sample;

import ru.frostman.jadecife.JadecifeException;
import ru.frostman.jadecife.util.hash.HashUtil;

/**
 * @author slukjanov aka Frostman
 */
public class BruteForceSample {
    public static void main(String[] args) throws JadecifeException {
//        LoggingConfigurator.configure(LoggingLevel.DEBUG);
//        Jadecife jadecife = new Jadecife(2, false, "localhost", 7890);
//        final Map<String, Integer> registeredClasses = jadecife.registerClasses(
//                BruteForce.class, HashBrute.class, HashBruteFactory.class,
//                HashHelper.class, HashUtil.class, Hex.class, JavaHashHelper.class);
//        System.out.println(registeredClasses);

        String hash = HashUtil.createMD5HashHelper().update("zzzz".getBytes()).digestAsHex();
        System.out.println(hash);

//        long factoryId = jadecife.addTaskFactory(registeredClasses.get(HashBruteFactory.class.getName())
//                , new HashBruteFactory(
//                Sets.newHashSet(
//                        registeredClasses.get(BruteForce.class.getName()),
//                        registeredClasses.get(HashBrute.class.getName()),
//                        registeredClasses.get(HashHelper.class.getName()),
//                        registeredClasses.get(HashUtil.class.getName()),
//                        registeredClasses.get(Hex.class.getName()),
//                        registeredClasses.get(JavaHashHelper.class.getName())
//                )
//                , registeredClasses.get(HashBrute.class.getName()), hash));
//
//        System.out.println("Factory registered with id: " + factoryId);
    }
}
