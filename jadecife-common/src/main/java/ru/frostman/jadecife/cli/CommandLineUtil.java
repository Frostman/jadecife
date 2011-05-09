package ru.frostman.jadecife.cli;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.ParameterException;

/**
 * @author slukjanov aka Frostman
 */
public class CommandLineUtil {
    public static boolean parseArgs(BaseCommandLineOpts opts, String... args) {
        JCommander cli = null;
        try {
            cli = new JCommander(opts);
            cli.parse(args);
            return true;
        } catch (ParameterException pe) {
            System.out.println(pe.getMessage() + "\n");
            if (cli != null) {
                cli.usage();
            }
            return false;
        }
    }
}
