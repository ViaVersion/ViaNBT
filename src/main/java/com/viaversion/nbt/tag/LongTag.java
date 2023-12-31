package com.viaversion.nbt.tag;

import com.viaversion.nbt.stringified.SNBT;
import com.viaversion.nbt.limiter.TagLimiter;
import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

/**
 * A tag containing a long.
 */
public final class LongTag implements NumberTag {
    public static final int ID = 4;
    public static final LongTag ZERO = new LongTag(0);
    private final long value;

    /**
     * Creates a tag.
     *
     * @param value The value of the tag.
     */
    public LongTag(long value) {
        this.value = value;
    }

    public static LongTag read(DataInput in, TagLimiter tagLimiter) throws IOException {
        tagLimiter.countLong();
        return new LongTag(in.readLong());
    }

    /**
     * @deprecated use {@link #asLong()}
     */
    @Override
    @Deprecated
    public Long getValue() {
        return this.value;
    }

    @Override
    public String asRawString() {
        return Long.toString(this.value);
    }

    @Override
    public void write(DataOutput out) throws IOException {
        out.writeLong(this.value);
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LongTag longTag = (LongTag) o;
        return this.value == longTag.value;
    }

    @Override
    public int hashCode() {
        return Long.hashCode(this.value);
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

    @Override
    public String toString() {
        return SNBT.serialize(this);
    }
}
