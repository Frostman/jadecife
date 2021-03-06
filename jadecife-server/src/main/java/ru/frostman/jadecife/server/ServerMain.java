package ru.frostman.jadecife.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.frostman.jadecife.cli.CommandLineUtil;
import ru.frostman.jadecife.cli.LoggingConfigurator;
import ru.frostman.jadecife.server.cli.ServerCommandLineOpts;

/**
 * @author slukjanov aka Frostman
 */
public class ServerMain {

    //todo  -XX:+UseG1GC -XX:MaxGCPauseMillis=100 -XX:GCPauseIntervalMillis=500 -XX:NewRatio=1 -XX:+PrintTenuringDistribution -XX:+PrintGCDetails
    public static void main(String[] args) {
        ServerCommandLineOpts opts = new ServerCommandLineOpts();
        if (CommandLineUtil.parseArgs(opts, args)) {
            LoggingConfigurator.configure(opts.loggingLevel);
            final Logger log = LoggerFactory.getLogger(ServerMain.class);
            final Server server = new Server(opts.address, opts.ioWorkers, opts.gzip);

            if (!server.start()) {
                System.exit(-1);
                return;
            }

            log.info("Server started successful");

            Runtime.getRuntime().addShutdownHook(new Thread() {

                @Override
                public void run() {
                    server.stop();
                }
            });
        }
    }
}
