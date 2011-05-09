package ru.frostman.jadecife.cli;

import org.apache.log4j.PropertyConfigurator;
import org.jboss.netty.logging.InternalLoggerFactory;
import org.jboss.netty.logging.Slf4JLoggerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

/**
 * @author slukjanov aka Frostman
 */
public class LoggingConfigurator {
    private static final Logger log = LoggerFactory.getLogger(LoggingConfigurator.class);

    public static void configure(LoggingLevel loggingLevel) {
        Properties properties = new Properties();

        properties.put("log4j.rootLogger", loggingLevel + ", stdout");
        properties.put("log4j.appender.stdout", "org.apache.log4j.ConsoleAppender");
        properties.put("log4j.appender.stdout.layout", "org.apache.log4j.PatternLayout");

        if (loggingLevel.compareTo(LoggingLevel.DEBUG) > 0) {
            properties.put("log4j.appender.stdout.layout.ConversionPattern", "%d %-5p - %m%n");
        } else {
            properties.put("log4j.appender.stdout.layout.ConversionPattern", "%d [%t] %-5p %c - %m%n");
        }

        PropertyConfigurator.configure(properties);

        log.info("Logger configured successfully, logging level: {}", loggingLevel);

        InternalLoggerFactory.setDefaultFactory(new Slf4JLoggerFactory());

        log.debug("Netty InternalLoggerFactory set to Slf4jLoggerFactory ");
    }

}
