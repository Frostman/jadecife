package ru.frostman.jadecife.cli;

import com.beust.jcommander.IParameterValidator;
import com.beust.jcommander.ParameterException;

/**
 * @author slukjanov aka Frostman
 */
public class WorkersValidator implements IParameterValidator {
    @Override
    public void validate(String name, String value) throws ParameterException {
        try {
            int t = Integer.parseInt(value);

            if (t <= 0 || t > Runtime.getRuntime().availableProcessors()) {
                throw new ParameterException("Parameter " + name + " should be valid integer " +
                        "from 1 to NUMBER_OF_CORES (found '" + value + "')");
            }
        } catch (NumberFormatException nfe) {
            throw new ParameterException("Parameter " + name + " should be valid integer (found '" + value + "')");
        }
    }
}
