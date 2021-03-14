package com.github.steveice10.opennbt.tag.builtin;

/**
 * Abstract class representing a number tag, containing methods to return primitive number types.
 */
public abstract class NumberTag extends Tag {

    /**
     * Gets the byte value of this tag.
     *
     * @return Byte value of this tag.
     */
    public abstract byte asByte();

    /**
     * Gets the short value of this tag.
     *
     * @return Short value of this tag.
     */
    public abstract short asShort();

    /**
     * Gets the int value of this tag.
     *
     * @return Int value of this tag.
     */
    public abstract int asInt();

    /**
     * Gets the long value of this tag.
     *
     * @return Long value of this tag.
     */
    public abstract long asLong();

    /**
     * Gets the float value of this tag.
     *
     * @return Float value of this tag.
     */
    public abstract float asFloat();

    /**
     * Gets the double value of this tag.
     *
     * @return Double value of this tag.
     */
    public abstract double asDouble();
}
