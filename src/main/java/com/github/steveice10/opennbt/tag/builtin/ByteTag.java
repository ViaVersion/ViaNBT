package com.github.steveice10.opennbt.tag.builtin;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

/**
 * A tag containing a byte.
 */
public class ByteTag extends NumberTag {
    public static final int ID = 1;
    private byte value;

    /**
     * Creates a tag.
     */
    public ByteTag() {
        this((byte) 0);
    }

    /**
     * Creates a tag.
     *
     * @param value The value of the tag.
     */
    public ByteTag(byte value) {
        this.value = value;
    }

    /**
     * @deprecated use {@link #asByte()}
     */
    @Override
    @Deprecated
    public Byte getValue() {
        return this.value;
    }

    /**
     * Sets the value of this tag.
     *
     * @param value New value of this tag.
     */
    public void setValue(byte value) {
        this.value = value;
    }

    @Override
    public void read(DataInput in) throws IOException {
        this.value = in.readByte();
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
    public final ByteTag clone() {
        return new ByteTag(this.value);
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
}
