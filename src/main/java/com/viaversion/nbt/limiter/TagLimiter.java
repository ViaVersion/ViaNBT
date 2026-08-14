package com.viaversion.nbt.limiter;

public interface TagLimiter {

    int DEFAULT_MAX_BYTES = 2097152; // 2mb
    int DEFAULT_MAX_NESTING_LEVEL = 512;
    int DEFAULT_MAX_TAGS = 262144; // 256k

    /**
     * Returns a new tag limiter with the given max bytes, nesting levels and the default max tag count.
     *
     * @param maxBytes  max amount of bytes to be read before an exception is thrown when reading nbt
     * @param maxLevels max levels of nesting before an exception is thrown when reading nbt
     * @return tag limiter
     * @see #create(int, int, int)
     */
    static TagLimiter create(int maxBytes, int maxLevels) {
        return create(maxBytes, maxLevels, DEFAULT_MAX_TAGS);
    }

    /**
     * Returns a new tag limiter with the given max bytes, nesting levels and tag count.
     *
     * @param maxBytes  max amount of bytes to be read before an exception is thrown when reading nbt
     * @param maxLevels max levels of nesting before an exception is thrown when reading nbt
     * @param maxTags   max amount of tags to be created before an exception is thrown when reading nbt
     * @return tag limiter
     */
    static TagLimiter create(int maxBytes, int maxLevels, int maxTags) {
        return new TagLimiterImpl(maxBytes, maxLevels, maxTags);
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
     * Counts a single created tag and throws an exception if the max tag count is exceeded.
     * <p>
     * Called once per tag produced while reading, regardless of the tag's wire size, to bound the
     * number of heap objects a single read can allocate.
     *
     * @throws IllegalArgumentException if the max tag count is exceeded
     */
    void countTag();

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
        this.countBytes(Float.BYTES);
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
     * Returns the max number of tags to be created before an exception is thrown when reading nbt.
     *
     * @return max tags
     */
    int maxTags();

    /**
     * Returns the amount of currently read bytes.
     *
     * @return currently read bytes
     */
    int bytes();

    /**
     * Returns the number of tags created so far.
     *
     * @return currently created tag count
     */
    int tags();

    /**
     * Resets the current byte and tag counts.
     */
    void reset();
}
