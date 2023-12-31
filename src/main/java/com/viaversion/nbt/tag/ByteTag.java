package com.viaversion.nbt.tag;

import com.viaversion.nbt.stringified.SNBT;
import com.viaversion.nbt.limiter.TagLimiter;
import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

/**
 * A tag containing a byte.
 */
public final class ByteTag implements NumberTag {
    public static final int ID = 1;
    public static final ByteTag ZERO = new ByteTag((byte) 0);
    private final byte value;

    /**
     * Creates a tag.
     *
     * @param value The value of the tag.
     */
    public ByteTag(byte value) {
        this.value = value;
    }

    /**
     * Creates a tag.
     *
     * @param value The value of the tag.
     */
    public ByteTag(boolean value) {
        this.value = (byte) (value ? 1 : 0);
    }

    public static ByteTag read(DataInput in, TagLimiter tagLimiter) throws IOException {
        tagLimiter.countByte();
        return new ByteTag(in.readByte());
    }

    /**
     * @deprecated use {@link #asByte()}
     */
    @Override
    @Deprecated
    public Byte getValue() {
        return this.value;
    }

    @Override
    public String asRawString() {
        return Byte.toString(this.value);
    }

    @Override
    public void write(DataOutput out) throws IOException {
        out.writeByte(this.value);
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ByteTag byteTag = (ByteTag) o;
        return this.value == byteTag.value;
    }

    @Override
    public int hashCode() {
        return value;
    }

    @Override
    public byte asByte() {
        return this.value;
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

    @Override
    public String toString() {
        return SNBT.serialize(this);
    }
}
