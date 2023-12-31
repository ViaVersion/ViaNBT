package com.viaversion.nbt.tag;

import com.viaversion.nbt.stringified.SNBT;
import com.viaversion.nbt.limiter.TagLimiter;
import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

/**
 * A tag containing a string.
 */
public final class StringTag implements Tag {
    public static final int ID = 8;
    private String value;

    /**
     * Creates a tag.
     */
    public StringTag() {
        this("");
    }

    /**
     * Creates a tag.
     *
     * @param value The value of the tag.
     */
    public StringTag(String value) {
        if (value == null) {
            throw new NullPointerException("value cannot be null");
        }
        this.value = value;
    }

    public static StringTag read(DataInput in, TagLimiter tagLimiter) throws IOException {
        final String value = in.readUTF();
        tagLimiter.countBytes(2 * value.length()); // More or less, ignoring the length reading
        return new StringTag(value);
    }

    @Override
    public String getValue() {
        return this.value;
    }

    @Override
    public String asRawString() {
        return this.value;
    }

    /**
     * Sets the value of this tag.
     *
     * @param value New value of this tag.
     */
    public void setValue(String value) {
        if (value == null) {
            throw new NullPointerException("value cannot be null");
        }
        this.value = value;
    }

    @Override
    public void write(DataOutput out) throws IOException {
        out.writeUTF(this.value);
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StringTag stringTag = (StringTag) o;
        return this.value.equals(stringTag.value);
    }

    @Override
    public int hashCode() {
        return this.value.hashCode();
    }

    @Override
    public StringTag copy() {
        return new StringTag(this.value);
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
