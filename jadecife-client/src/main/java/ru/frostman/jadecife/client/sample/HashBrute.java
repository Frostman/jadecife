package ru.frostman.jadecife.client.sample;

import ru.frostman.jadecife.util.hash.HashHelper;
import ru.frostman.jadecife.util.hash.HashUtil;

/**
 * @author slukjanov aka Frostman
 */
public class HashBrute {
    private static final HashHelper hash = HashUtil.createMD5HashHelper();

    public String brute(String expected, String msg, Integer bruteSuffix) {
        //System.out.println(msg);
        if (expected.equals(hash.update(msg.getBytes()).digestAsHex())) {
            return msg;
        }

        return null;
    }

}
