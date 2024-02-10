package com.github.steveice10.opennbt.tag.builtin;

import com.github.steveice10.opennbt.stringified.SNBT;
import java.io.DataOutput;
import java.io.IOException;

/**
 * Represents an NBT tag.
 * <p>
 * Tags should also have setter methods specific to their value types.
 */
public abstract class Tag {

    /**
     * Returns the value of this tag.
     *
     * @return value of this tag
     */
    public abstract Object getValue();

    /**
     * Returns the raw string representation of the value of this tag.
     * For SNBT, use {@link SNBT#serialize(Tag)}.
     *
     * @return raw string representation of the value of this tag
     */
    public abstract String asRawString();

    /**
     * Returns the unchecked value of this tag.
     * <p>
     * <b>The generic of this method might be removed in a later version</b>
     *
     * @param <T> expected type
     * @return unchecked value of this tag
     */
    public <T> T value() {
        return (T) getValue();
    }

    /**
     * Writes this tag to an output stream.
     *
     * @param out data output to write to
     * @throws IOException if an I/O error occurs
     */
    public abstract void write(DataOutput out) throws IOException;

    /**
     * Returns the NBT tag id of this tag type, used in I/O.
     *
     * @return ID of the tag this class represents
     */
    public abstract int getTagId();

    /**
     * Returns a copy of this tag.
     *
     * @return a copy of this tag
     */
    public abstract Tag copy();

    @Override
    public String toString() {
        return SNBT.serialize(this);
    }
}
