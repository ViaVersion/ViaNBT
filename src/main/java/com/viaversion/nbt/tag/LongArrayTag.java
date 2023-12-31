package com.viaversion.nbt.tag;

import com.viaversion.nbt.limiter.TagLimiter;
import com.viaversion.nbt.stringified.SNBT;
import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;

/**
 * A tag containing a long array.
 */
public final class LongArrayTag implements NumberArrayTag {
    public static final int ID = 12;
    private static final long[] EMPTY_ARRAY = new long[0];
    private long[] value;

    /**
     * Creates a tag.
     */
    public LongArrayTag() {
        this(EMPTY_ARRAY);
    }

    /**
     * Creates a tag.
     *
     * @param value The value of the tag.
     */
    public LongArrayTag(long[] value) {
        if (value == null) {
            throw new NullPointerException("value cannot be null");
        }
        this.value = value;
    }

    public static LongArrayTag read(DataInput in, TagLimiter tagLimiter) throws IOException {
        tagLimiter.countInt();
        long[] value = new long[in.readInt()];
        tagLimiter.countBytes(Long.BYTES * value.length);
        for (int index = 0; index < value.length; index++) {
            value[index] = in.readLong();
        }
        return new LongArrayTag(value);
    }

    @Override
    public long[] getValue() {
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
    public void setValue(long[] value) {
        if (value == null) {
            throw new NullPointerException("value cannot be null");
        }
        this.value = value;
    }

    /**
     * Gets a value in this tag's array.
     *
     * @param index Index of the value.
     * @return The value at the given index.
     */
    public long get(int index) {
        return this.value[index];
    }

    /**
     * Sets a value in this tag's array.
     *
     * @param index Index of the value.
     * @param value Value to set.
     */
    public void set(int index, long value) {
        this.value[index] = value;
    }

    @Override
    public int length() {
        return this.value.length;
    }

    @Override
    public ListTag<LongTag> toListTag() {
        final ListTag<LongTag> list = new ListTag<>(LongTag.class);
        for (final long l : this.value) {
            list.add(new LongTag(l));
        }
        return list;
    }

    @Override
    public void write(DataOutput out) throws IOException {
        out.writeInt(this.value.length);
        for (long l : this.value) {
            out.writeLong(l);
        }
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LongArrayTag that = (LongArrayTag) o;
        return Arrays.equals(this.value, that.value);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(this.value);
    }

    @Override
    public LongArrayTag copy() {
        return new LongArrayTag(this.value.clone());
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
