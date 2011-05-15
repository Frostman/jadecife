package ru.frostman.jadecife.client.sample;

/**
 * @author slukjanov aka Frostman
 */
public class BruteForce {
    public static String next(String msg) {
        char[] str = msg.toCharArray();

        try {
            for (int i = str.length - 1; ; i--) {
                if (str[i] < 'z') {
                    str[i]++;
                    break;
                } else {
                    str[i] = 'a';
                }
            }
        } catch (IndexOutOfBoundsException e) {
            return null;
        }

        return new String(str);
    }
}
