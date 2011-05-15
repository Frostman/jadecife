package ru.frostman.jadecife.util.hash;

/**
 * @author slukjanov aka Frostman
 */
public abstract class HashHelper {
    public abstract byte[] digest();

    public abstract String digestAsHex();

    protected abstract void _update(byte[] bytes, int offset, int length);

    protected abstract void _reset();

    public abstract int getHashLength();

    public abstract String getHashName();

    public HashHelper update(byte[] bytes) {
        _update(bytes, 0, bytes.length);

        return this;
    }

    public HashHelper update(byte[] bytes, int offset, int length) {
        _update(bytes, offset, length);

        return this;
    }

    public HashHelper reset() {
        _reset();

        return this;
    }

    @Override
    public String toString() {
        return "[HashHelper: " + getHashName() + " / " + getHashLength() + " bytes]";
    }
}
