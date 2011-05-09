package ru.frostman.jadecife.cli;

import java.util.HashMap;

/**
 * @author slukjanov aka Frostman
 */
public enum LoggingLevel {
    TRACE,
    DEBUG,
    INFO,
    WARN,
    ERROR;

    private static final HashMap<String, LoggingLevel> abbrevs;

    public static LoggingLevel getByAbbrev(String str) {
        return abbrevs.get(str.toLowerCase());
    }

    static {
        abbrevs = new HashMap<String, LoggingLevel>();
        abbrevs.put("trace", LoggingLevel.TRACE);
        abbrevs.put("debug", LoggingLevel.DEBUG);
        abbrevs.put("info", LoggingLevel.INFO);
        abbrevs.put("warn", LoggingLevel.WARN);
        abbrevs.put("error", LoggingLevel.ERROR);
    }
}
