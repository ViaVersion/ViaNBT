package com.viaversion.nbt.limiter;

public interface TagLimiter {

    /**
     * Returns a new tag limiter with the given max bytes and nesting levels.
     *
     * @param maxBytes  max amount of bytes to be read before an exception is thrown when reading nbt
     * @param maxLevels max levels of nesting before an exception is thrown when reading nbt
     * @return tag limiter
     */
    static TagLimiter create(int maxBytes, int maxLevels) {
        return new TagLimiterImpl(maxBytes, maxLevels);
    }

    /**
     * Returns a noop tag limiter.
     *
     * @return noop tag limiter
     */
    static TagLimiter noop() {
        return NoopTagLimiter.INSTANCE;
    }

    /**
     * Counts the given number of bytes and throws an exception if the max bytes count is exceeded.
     *
     * @param bytes bytes to count
     * @throws IllegalArgumentException if max bytes count is exceeded
     */
    void countBytes(int bytes);

    /**
     * Checks the current level of nesting and throws an exception if it exceeds the max levels.
     *
     * @param nestedLevel current level of nesting
     * @throws IllegalArgumentException if max level count is exceeded
     */
    void checkLevel(int nestedLevel);

    default void countByte() {
        this.countBytes(Byte.BYTES);
    }

    default void countShort() {
        this.countBytes(Short.BYTES);
    }

    default void countInt() {
        this.countBytes(Integer.BYTES);
    }

    default void countFloat() {
        this.countBytes(Double.BYTES);
    }

    default void countLong() {
        this.countBytes(Long.BYTES);
    }

    default void countDouble() {
        this.countBytes(Double.BYTES);
    }

    /**
     * Returns the max number of bytes to be read before an exception is thrown when reading nbt.
     *
     * @return max bytes
     */
    int maxBytes();

    /**
     * Returns the max number of levels of nesting before an exception is thrown when reading nbt.
     *
     * @return max nesting levels
     */
    int maxLevels();

    /**
     * Returns the amount of currently read bytes.
     *
     * @return currently read bytes
     */
    int bytes();

    /**
     * Resets the current byte count.
     */
    void reset();
}
