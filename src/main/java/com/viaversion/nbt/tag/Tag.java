package com.viaversion.nbt.tag;

import com.viaversion.nbt.stringified.SNBT;
import java.io.DataOutput;
import java.io.IOException;

public interface Tag {

    /**
     * Returns the value of this tag.
     *
     * @return value of this tag
     */
    Object getValue();

    /**
     * Returns the raw string representation of the value of this tag.
     * For SNBT, use {@link SNBT#serialize(Tag)}.
     *
     * @return raw string representation of the value of this tag
     */
    String asRawString();

    /**
     * Writes this tag to an output stream.
     *
     * @param out data output to write to
     * @throws IOException if an I/O error occurs
     */
    void write(DataOutput out) throws IOException;

    /**
     * Returns the NBT tag id of this tag type, used in I/O.
     *
     * @return ID of the tag this class represents
     */
    int getTagId();

    /**
     * Returns a copy of this tag.
     *
     * @return a copy of this tag
     */
    Tag copy();
}
