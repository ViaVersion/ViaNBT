package com.github.steveice10.opennbt.tag.builtin;

import com.google.common.base.Preconditions;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;

/**
 * A tag containing a long array.
 */
public class LongArrayTag extends Tag {
    public static final int ID = 12;
    private long[] value;

    /**
     * Creates a tag.
     */
    public LongArrayTag() {
        this(new long[0]);
    }

    /**
     * Creates a tag.
     *
     * @param value The value of the tag.
     */
    public LongArrayTag(long[] value) {
        Preconditions.checkNotNull(value);
        this.value = value;
    }

    @Override
    public long[] getValue() {
        return this.value;
    }

    /**
     * Sets the value of this tag.
     *
     * @param value New value of this tag.
     */
    public void setValue(long[] value) {
        Preconditions.checkNotNull(value);
        this.value = value;
    }

    /**
     * Gets a value in this tag's array.
     *
     * @param index Index of the value.
     * @return The value at the given index.
     */
    public long getValue(int index) {
        return this.value[index];
    }

    /**
     * Sets a value in this tag's array.
     *
     * @param index Index of the value.
     * @param value Value to set.
     */
    public void setValue(int index, long value) {
        this.value[index] = value;
    }

    /**
     * Gets the length of this tag's array.
     *
     * @return This tag's array length.
     */
    public int length() {
        return this.value.length;
    }

    @Override
    public void read(DataInput in) throws IOException {
        this.value = new long[in.readInt()];
        for(int index = 0; index < this.value.length; index++) {
            this.value[index] = in.readLong();
        }
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
    public final LongArrayTag clone() {
        return new LongArrayTag(this.value.clone());
    }

    @Override
    public int getTagId() {
        return ID;
    }
}
