package ru.frostman.jadecife.util.hash;

import java.security.MessageDigest;

/**
 * @author slukjanov aka Frostman
 */
public class HashUtil {

    public static HashHelper createMD5HashHelper() {
        return createJavaHashHelper("MD5");
    }

    public static HashHelper createSHA1HashHelper() {
        return createJavaHashHelper("SHA-1");
    }

    public static HashHelper createSHA256HashHelper() {
        return createJavaHashHelper("SHA-256");
    }

    public static HashHelper createSHA384HashHelper() {
        return createJavaHashHelper("SHA-384");
    }

    public static HashHelper createSHA512HashHelper() {
        return createJavaHashHelper("SHA-512");
    }

    private static HashHelper createJavaHashHelper(String algorithm) {
        try {
            MessageDigest md = MessageDigest.getInstance(algorithm);
            return new JavaHashHelper(md);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
