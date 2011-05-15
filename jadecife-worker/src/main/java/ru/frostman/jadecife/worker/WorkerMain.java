package ru.frostman.jadecife.worker;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.frostman.jadecife.cli.CommandLineUtil;
import ru.frostman.jadecife.cli.LoggingConfigurator;
import ru.frostman.jadecife.worker.cli.WorkerCommandLineOpts;

/**
 * @author slukjanov aka Frostman
 */
public class WorkerMain {
    public static void main(String[] args) {
        WorkerCommandLineOpts opts = new WorkerCommandLineOpts();
        if (CommandLineUtil.parseArgs(opts, args)) {
            LoggingConfigurator.configure(opts.loggingLevel);
            final Logger log = LoggerFactory.getLogger(WorkerMain.class);
            //final Server server = new Server(opts.address, opts.ioWorkers, opts.gzip);

//            if (!server.start()) {
//                System.exit(-1);
//                return;
//            }

            log.info("Worker started successful");

            Runtime.getRuntime().addShutdownHook(new Thread() {

                @Override
                public void run() {
//                    worker.stop();
                }
            });
        }
    }
}
