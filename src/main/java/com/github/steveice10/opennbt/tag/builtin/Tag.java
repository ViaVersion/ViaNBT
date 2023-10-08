package com.github.steveice10.opennbt.tag.builtin;

import com.github.steveice10.opennbt.stringified.SNBT;
import com.github.steveice10.opennbt.tag.limiter.TagLimiter;
import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

/**
 * Represents an NBT tag.
 * <p>
 * Tags should also have setter methods specific to their value types.
 */
public abstract class Tag implements Cloneable {

    /**
     * Gets the value of this tag.
     *
     * @return The value of this tag.
     */
    public abstract Object getValue();

    /**
     * Returns the unchecked value of this tag.
     *
     * @return unchecked value of this tag
     * @param <T> expected type
     */
    public <T> T value() {
        return (T) getValue();
    }

    /**
     * Reads this tag from an input stream.
     *
     * @param in Stream to write to.
     * @throws java.io.IOException If an I/O error occurs.
     */
    public final void read(DataInput in) throws IOException {
        this.read(in, TagLimiter.noop(), 0);
    }

    /**
     * Reads this tag from an input stream.
     *
     * @param in Stream to write to.
     * @param tagLimiter taglimiter
     * @throws java.io.IOException If an I/O error occurs.
     */
    public final void read(DataInput in, TagLimiter tagLimiter) throws IOException {
        this.read(in, tagLimiter, 0);
    }

    /**
     * Reads this tag from an input stream.
     *
     * @param in Stream to write to.
     * @param tagLimiter taglimiter
     * @param nestingLevel current level of nesting
     * @throws java.io.IOException If an I/O error occurs.
     */
    public abstract void read(DataInput in, TagLimiter tagLimiter, int nestingLevel) throws IOException;

    /**
     * Writes this tag to an output stream.
     *
     * @param out Stream to write to.
     * @throws java.io.IOException If an I/O error occurs.
     */
    public abstract void write(DataOutput out) throws IOException;

    /**
     * Returns the NBT tag id of this tag type, used in I/O.
     *
     * @return Id of the tag this class represents
     */
    public abstract int getTagId();

    @Override
    public abstract Tag clone();

    @Override
    public String toString() {
        return SNBT.serialize(this);
    }
}
