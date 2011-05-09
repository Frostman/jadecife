package ru.frostman.jadecife.cli;

import com.beust.jcommander.Parameter;
import com.google.common.base.Objects;
import com.google.common.collect.Lists;

import java.net.InetSocketAddress;
import java.util.List;

/**
 * @author slukjanov aka Frostman
 */
public class BaseCommandLineOpts {
    @Parameter
    public List<String> parameters = Lists.newArrayList();

    @Parameter(
            names = {"-l", "--log", "--verbose"},
            description = "Level of verbosity (one of the logging levels: TRACE, DEBUG, INFO, WARN, ERROR)",
            converter = LoggingLevelAdapter.class,
            validateWith = LoggingLevelAdapter.class
    )
    public LoggingLevel loggingLevel = LoggingLevel.INFO;

    @Parameter(names = {"-z", "--gzip"}, description = "Use gzip to compress traffic")
    public boolean gzip = false;


    @Parameter(
            names = {"-w", "--ioworkers"},
            description = "Number of i/o workers",
            validateWith = WorkersValidator.class
    )
    public int ioWorkers = Math.max(1, Runtime.getRuntime().availableProcessors());

    @Parameter(
            names = {"-h", "--address"},
            required = true,
            description = "InetSocketAddress of server to bind or client to connect in format HOST:PORT",
            converter = InetSocketAddressAdapter.class,
            validateWith = InetSocketAddressAdapter.class
    )
    public InetSocketAddress address;

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
                .add("loggingLevel", loggingLevel)
                .add("gzip", gzip)
                .add("ioworkers", ioWorkers)
                .add("address", address)
                .add("parameters", parameters)
                .toString();
    }
}
