package com.viaversion.nbt.limiter;

final class TagLimiterImpl implements TagLimiter {

    private final int maxBytes;
    private final int maxLevels;
    private int bytes;

    TagLimiterImpl(int maxBytes, int maxLevels) {
        this.maxBytes = maxBytes;
        this.maxLevels = maxLevels;
    }

    @Override
    public void countBytes(int bytes) {
        this.bytes += bytes;
        if (this.bytes >= maxBytes) {
            throw new IllegalArgumentException("NBT data larger than expected (capped at " + this.maxBytes + ")");
        }
    }

    @Override
    public void checkLevel(int nestedLevel) {
        if (nestedLevel >= this.maxLevels) {
            throw new IllegalArgumentException("Nesting level higher than expected (capped at " + this.maxLevels + ")");
        }
    }

    @Override
    public int maxBytes() {
        return maxBytes;
    }

    @Override
    public int maxLevels() {
        return maxLevels;
    }

    @Override
    public int bytes() {
        return bytes;
    }

    @Override
    public void reset() {
        this.bytes = 0;
    }
}
