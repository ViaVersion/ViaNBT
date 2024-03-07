package com.github.steveice10.opennbt.tag.builtin;

import com.github.steveice10.opennbt.tag.limiter.TagLimiter;
import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

/**
 * A tag containing a float.
 */
public final class FloatTag extends NumberTag {
    public static final int ID = 5;
    private float value;

    /**
     * Creates a tag.
     */
    public FloatTag() {
        this(0);
    }

    /**
     * Creates a tag.
     *
     * @param value The value of the tag.
     */
    public FloatTag(float value) {
        this.value = value;
    }

    public static FloatTag read(DataInput in, TagLimiter tagLimiter) throws IOException {
        tagLimiter.countFloat();
        return new FloatTag(in.readFloat());
    }

    /**
     * @deprecated use {@link #asFloat()}
     */
    @Override
    @Deprecated
    public Float getValue() {
        return this.value;
    }

    @Override
    public String asRawString() {
        return Float.toString(this.value);
    }

    /**
     * Sets the value of this tag.
     *
     * @param value New value of this tag.
     * @deprecated number tags will be immutable in the future
     */
    @Deprecated
    public void setValue(float value) {
        this.value = value;
    }

    @Override
    public void write(DataOutput out) throws IOException {
        out.writeFloat(this.value);
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FloatTag floatTag = (FloatTag) o;
        return this.value == floatTag.value;
    }

    @Override
    public int hashCode() {
        return Float.hashCode(this.value);
    }

    @Override
    public FloatTag copy() {
        return new FloatTag(this.value);
    }

    @Override
    public byte asByte() {
        return (byte) this.value;
    }

    @Override
    public short asShort() {
        return (short) this.value;
    }

    @Override
    public int asInt() {
        return (int) this.value;
    }

    @Override
    public long asLong() {
        return (long) this.value;
    }

    @Override
    public float asFloat() {
        return this.value;
    }

    @Override
    public double asDouble() {
        return this.value;
    }

    @Override
    public int getTagId() {
        return ID;
    }
}
