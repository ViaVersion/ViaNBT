package com.viaversion.nbt.io;

import com.viaversion.nbt.tag.Tag;
import com.viaversion.nbt.limiter.TagLimiter;
import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import org.jetbrains.annotations.Nullable;

/**
 * Utility to read and write NBT tags.
 */
public final class NBTIO {

    private NBTIO() {
    }

    /**
     * Returns an NBT tag reader. If a tag limiter is set, the reader is reusable, but not thread-safe.
     *
     * @return NBT tag reader
     */
    public static TagReader<Tag> reader() {
        return new TagReader<>(null);
    }

    /**
     * Returns an NBT tag reader to read an expected tag type. If a tag limiter is set, the reader is reusable, but not thread-safe.
     *
     * @param expectedTagType the expected tag type, or null if any is accepted
     * @param <T>             the expected tag type
     * @return NBT tag reader
     */
    public static <T extends Tag> TagReader<T> reader(final Class<T> expectedTagType) {
        return new TagReader<>(expectedTagType);
    }

    /**
     * Returns a reusable NBT tag writer.
     *
     * @return reusable NBT tag writer
     */
    public static TagWriter writer() {
        return new TagWriter();
    }

    /**
     * Reads a named NBT tag from a data input.
     *
     * @param in              input stream to read from
     * @param tagLimiter      tag limiter to use
     * @param named           whether the tag is named
     * @param expectedTagType the expected tag type, or null if any is accepted
     * @return the read tag
     * @throws IOException if an I/O error occurs
     */
    public static <T extends Tag> T readTag(final DataInput in, final TagLimiter tagLimiter, final boolean named, @Nullable final Class<T> expectedTagType) throws IOException {
        final int id = in.readByte();
        if (expectedTagType != null && expectedTagType != TagRegistry.getClassFor(id)) {
            throw new IOException("Expected tag type " + expectedTagType.getSimpleName() + " but got " + TagRegistry.getClassFor(id).getSimpleName());
        }

        if (named) {
            in.skipBytes(in.readUnsignedShort()); // Skip name
        }

        //noinspection unchecked
        return (T) TagRegistry.read(id, in, tagLimiter, 0);
    }

    /**
     * Writes a named NBT tag to a data output.
     *
     * @param out output stream to write to
     * @param tag tag to write
     * @throws IOException if an I/O error occurs
     */
    public static void writeTag(final DataOutput out, final Tag tag, final boolean named) throws IOException {
        out.writeByte(tag.getTagId());
        if (named) {
            out.writeUTF(""); // Empty name
        }
        tag.write(out);
    }
}
