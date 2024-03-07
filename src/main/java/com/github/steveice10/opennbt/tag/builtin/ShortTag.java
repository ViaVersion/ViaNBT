package com.github.steveice10.opennbt.tag.builtin;

import com.github.steveice10.opennbt.tag.limiter.TagLimiter;
import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

/**
 * A tag containing a short.
 */
public final class ShortTag extends NumberTag {
    public static final int ID = 2;
    private short value;

    /**
     * Creates a tag.
     */
    public ShortTag() {
        this((short) 0);
    }

    /**
     * Creates a tag.
     *
     * @param value The value of the tag.
     */
    public ShortTag(short value) {
        this.value = value;
    }

    public static ShortTag read(DataInput in, TagLimiter tagLimiter) throws IOException {
        tagLimiter.countShort();
        return new ShortTag(in.readShort());
    }

    /**
     * @deprecated use {@link #asShort()}
     */
    @Override
    @Deprecated
    public Short getValue() {
        return this.value;
    }

    @Override
    public String asRawString() {
        return Short.toString(this.value);
    }

    /**
     * Sets the value of this tag.
     *
     * @param value New value of this tag.
     * @deprecated number tags will be immutable in the future
     */
    @Deprecated
    public void setValue(short value) {
        this.value = value;
    }

    @Override
    public void write(DataOutput out) throws IOException {
        out.writeShort(this.value);
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ShortTag shortTag = (ShortTag) o;
        return this.value == shortTag.value;
    }

    @Override
    public int hashCode() {
        return this.value;
    }

    @Override
    public ShortTag copy() {
        return new ShortTag(this.value);
    }

    @Override
    public byte asByte() {
        return (byte) this.value;
    }

    @Override
    public short asShort() {
        return this.value;
    }

    @Override
    public int asInt() {
        return this.value;
    }

    @Override
    public long asLong() {
        return this.value;
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
