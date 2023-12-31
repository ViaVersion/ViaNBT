package com.viaversion.nbt.tag;

import com.viaversion.nbt.limiter.TagLimiter;
import com.viaversion.nbt.stringified.SNBT;
import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;

/**
 * A tag containing a byte array.
 */
public final class ByteArrayTag implements NumberArrayTag {
    public static final int ID = 7;
    private static final byte[] EMPTY_ARRAY = new byte[0];
    private byte[] value;

    /**
     * Creates a tag.
     */
    public ByteArrayTag() {
        this(EMPTY_ARRAY);
    }

    /**
     * Creates a tag.
     *
     * @param value The value of the tag.
     */
    public ByteArrayTag(byte[] value) {
        if (value == null) {
            throw new NullPointerException("value cannot be null");
        }
        this.value = value;
    }

    public static ByteArrayTag read(DataInput in, TagLimiter tagLimiter) throws IOException {
        tagLimiter.countInt();
        byte[] value = new byte[in.readInt()];
        tagLimiter.countBytes(value.length);
        in.readFully(value);
        return new ByteArrayTag(value);
    }

    @Override
    public byte[] getValue() {
        return this.value;
    }

    @Override
    public String asRawString() {
        return Arrays.toString(this.value);
    }

    /**
     * Sets the value of this tag.
     *
     * @param value New value of this tag.
     */
    public void setValue(byte[] value) {
        if (value == null) {
            return;
        }

        this.value = value;
    }

    /**
     * Gets a value in this tag's array.
     *
     * @param index Index of the value.
     * @return The value at the given index.
     */
    public byte get(int index) {
        return this.value[index];
    }

    /**
     * Sets a value in this tag's array.
     *
     * @param index Index of the value.
     * @param value Value to set.
     */
    public void set(int index, byte value) {
        this.value[index] = value;
    }

    @Override
    public int length() {
        return this.value.length;
    }

    @Override
    public ListTag<ByteTag> toListTag() {
        final ListTag<ByteTag> list = new ListTag<>(ByteTag.class);
        for (final byte b : this.value) {
            list.add(new ByteTag(b));
        }
        return list;
    }

    @Override
    public void write(DataOutput out) throws IOException {
        out.writeInt(this.value.length);
        out.write(this.value);
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ByteArrayTag that = (ByteArrayTag) o;
        return Arrays.equals(this.value, that.value);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(this.value);
    }

    @Override
    public ByteArrayTag copy() {
        return new ByteArrayTag(this.value.clone());
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
