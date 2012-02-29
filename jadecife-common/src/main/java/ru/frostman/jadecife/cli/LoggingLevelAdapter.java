package ru.frostman.jadecife.cli;

import com.beust.jcommander.IParameterValidator;
import com.beust.jcommander.IStringConverter;
import com.beust.jcommander.ParameterException;

/**
 * @author slukjanov aka Frostman
 */
public class LoggingLevelAdapter implements IStringConverter<LoggingLevel>, IParameterValidator {
    @Override
    public LoggingLevel convert(String value) {
        return LoggingLevel.getByAbbrev(value.toLowerCase());
    }

    @Override
    public void validate(String name, String value) throws ParameterException {
        if (LoggingLevel.getByAbbrev(value) == null) {
            throw new ParameterException("Parameter " + name + " should be valid logging level (found '" + value + "')");
        }
    }
}
