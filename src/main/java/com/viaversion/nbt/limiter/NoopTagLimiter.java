package com.viaversion.nbt.limiter;

final class NoopTagLimiter implements TagLimiter {

    static final TagLimiter INSTANCE = new NoopTagLimiter();

    private NoopTagLimiter() {
    }

    @Override
    public void countBytes(long bytes) {
    }

    @Override
    public void countTag() {
    }

    @Override
    public void checkLevel(int nestedLevel) {
    }

    @Override
    public int maxBytes() {
        return Integer.MAX_VALUE;
    }

    @Override
    public int maxLevels() {
        return Integer.MAX_VALUE;
    }

    @Override
    public int maxTags() {
        return Integer.MAX_VALUE;
    }

    @Override
    public long bytes() {
        return 0;
    }

    @Override
    public int tags() {
        return 0;
    }

    @Override
    public void reset() {
    }
}
