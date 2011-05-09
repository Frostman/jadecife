package ru.frostman.jadecife.cli;

import com.beust.jcommander.IParameterValidator;
import com.beust.jcommander.IStringConverter;
import com.beust.jcommander.ParameterException;

import java.net.InetSocketAddress;

/**
 * @author slukjanov aka Frostman
 */
public class InetSocketAddressAdapter implements IStringConverter<InetSocketAddress>, IParameterValidator {
    @Override
    public InetSocketAddress convert(String value) {
        String[] split = value.split(":");
        return new InetSocketAddress(split[0], Integer.parseInt(split[1]));
    }

    @Override
    public void validate(String name, String value) throws ParameterException {
        String[] split = value.split(":");
        if (split.length != 2) {
            throw new ParameterException("Parameter " + name + " should be valid formatted " +
                    "(HOST:PORT, found '" + value + "')");
        } else {
            try {
                int port = Integer.parseInt(split[1]);
                if (port < 0 || port > 0xFFFF) {
                    throw new IllegalArgumentException("port out of range:" + port);
                }
            } catch (Exception e) {
                throw new ParameterException("Parameter " + name + " should be valid formatted, port isn't valid " +
                        "(HOST:PORT, found '" + value + "')");
            }
        }
    }
}
