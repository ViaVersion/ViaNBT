package com.viaversion.nbt.tag;

/**
 * Abstract class representing a number tag, containing methods to return primitive number types.
 */
public interface NumberTag extends Tag {

    /**
     * Gets the number value of this tag.
     *
     * @return Number value of this tag.
     */
    @Override
    Number getValue();

    /**
     * Gets the byte value of this tag.
     *
     * @return Byte value of this tag.
     */
    byte asByte();

    /**
     * Gets the short value of this tag.
     *
     * @return Short value of this tag.
     */
    short asShort();

    /**
     * Gets the int value of this tag.
     *
     * @return Int value of this tag.
     */
    int asInt();

    /**
     * Gets the long value of this tag.
     *
     * @return Long value of this tag.
     */
    long asLong();

    /**
     * Gets the float value of this tag.
     *
     * @return Float value of this tag.
     */
    float asFloat();

    /**
     * Gets the double value of this tag.
     *
     * @return Double value of this tag.
     */
    double asDouble();

    /**
     * Gets the boolean value of this tag.
     * <p>
     * Booleans do not have a direct nbt representation in NBT per se,
     * but check whether a number value is different to 0.
     *
     * @return Boolean value of this tag.
     */
    default boolean asBoolean() {
        return this.asByte() != 0;
    }

    @Override
    default NumberTag copy() {
        // Immutable, so return this
        return this;
    }
}
