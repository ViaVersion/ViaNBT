package com.viaversion.nbt.tag;

import com.viaversion.nbt.stringified.SNBT;
import com.viaversion.nbt.limiter.TagLimiter;
import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

/**
 * A tag containing a double.
 */
public final class DoubleTag implements NumberTag {
    public static final int ID = 6;
    public static final DoubleTag ZERO = new DoubleTag(0);
    private final double value;

    /**
     * Creates a tag.
     *
     * @param value The value of the tag.
     */
    public DoubleTag(double value) {
        this.value = value;
    }

    public static DoubleTag read(DataInput in, TagLimiter tagLimiter) throws IOException {
        tagLimiter.countDouble();
        return new DoubleTag(in.readDouble());
    }

    /**
     * @deprecated use {@link #asDouble()}
     */
    @Override
    @Deprecated
    public Double getValue() {
        return this.value;
    }

    @Override
    public String asRawString() {
        return Double.toString(this.value);
    }

    @Override
    public void write(DataOutput out) throws IOException {
        out.writeDouble(this.value);
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DoubleTag doubleTag = (DoubleTag) o;
        return this.value == doubleTag.value;
    }

    @Override
    public int hashCode() {
        return Double.hashCode(this.value);
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
        return (float) this.value;
    }

    @Override
    public double asDouble() {
        return this.value;
    }

    @Override
    public int getTagId() {
        return ID;
    }

    @Override
    public String toString() {
        return SNBT.serialize(this);
    }
}
