package ru.frostman.jadecife.client.sample;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.frostman.jadecife.cli.LoggingConfigurator;
import ru.frostman.jadecife.cli.LoggingLevel;
import ru.frostman.jadecife.util.hash.HashHelper;
import ru.frostman.jadecife.util.hash.HashUtil;

/**
 * @author slukjanov aka Frostman
 */
public class SimpleHashBrute {
    private static final Logger log = LoggerFactory.getLogger(SimpleHashBrute.class);

    public static void main(String[] args) {
        LoggingConfigurator.configure(LoggingLevel.INFO);
        log.info("start");
        HashHelper hash = HashUtil.createMD5HashHelper();
        String expected = hash.update("zzzzz".getBytes()).digestAsHex();
        String baseMsg = "a";
        String msg = "" + ((char) ('a' - 1));
        while (true) {
            msg = BruteForce.next(msg);
            if (msg == null) {
                baseMsg += "a";
                msg = baseMsg;
            }

            if (expected.equals(hash.update(msg.getBytes()).digestAsHex())) {
                break;
            }
        }

        log.info("end");
    }
}
