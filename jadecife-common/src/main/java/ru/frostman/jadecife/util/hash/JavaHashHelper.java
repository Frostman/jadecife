package ru.frostman.jadecife.util.hash;

import com.google.common.base.Preconditions;

import java.security.MessageDigest;

/**
 * @author slukjanov aka Frostman
 */
public class JavaHashHelper extends HashHelper {
    private final MessageDigest md;

    public JavaHashHelper(MessageDigest md) {
        Preconditions.checkArgument(md != null, "MessageDigest can't be null");

        this.md = md;
    }

    @Override
    public byte[] digest() {
        return md.digest();
    }

    @Override
    public String digestAsHex() {
        return Hex.asHexString(digest());
    }

    @Override
    protected void _update(byte[] bytes, int offset, int length) {
        md.update(bytes, offset, length);
    }

    @Override
    protected void _reset() {
        md.reset();
    }

    @Override
    public int getHashLength() {
        return md.getDigestLength();
    }

    @Override
    public String getHashName() {
        return md.getAlgorithm();
    }
}
