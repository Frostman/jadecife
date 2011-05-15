package ru.frostman.jadecife.client.test;

/**
 * @author slukjanov aka Frostman
 */
public class Test {

    public String printTime(Long time) {
        System.out.println("time: " + time);

        if (time % 2 == 0) {
            return "OK";
        }
        return null;
    }
}
