package com.viaversion.nbt.stringified;

import com.viaversion.nbt.tag.CompoundTag;
import com.viaversion.nbt.tag.Tag;

/**
 * Serialization of stringifies tags.
 */
public final class SNBT {

    private SNBT() {
    }

    /**
     * Reads a compound tag from a {@link String}.
     *
     * @param snbt SNBT input
     * @return compound tag from the given SNBT input
     * @throws StringifiedTagParseException if an exception was encountered while reading a compound tag
     */
    public static Tag deserialize(final String snbt) {
        final CharBuffer buffer = new CharBuffer(snbt);
        final TagStringReader parser = new TagStringReader(buffer);
        final Tag tag = parser.tag();
        if (buffer.skipWhitespace().hasMore()) {
            throw new StringifiedTagParseException("Input has trailing content", buffer.index());
        }
        return tag;
    }

    public static CompoundTag deserializeCompoundTag(final String snbt) {
        final CharBuffer buffer = new CharBuffer(snbt);
        final TagStringReader reader = new TagStringReader(buffer);
        final CompoundTag tag = reader.compound();
        if (buffer.skipWhitespace().hasMore()) {
            throw new StringifiedTagParseException("Input has trailing content", buffer.index());
        }
        return tag;
    }

    /**
     * Serializes a tag to SNBT.
     *
     * @param tag the compound tag
     * @return serialized SNBT
     * @throws IllegalArgumentException if an unknown tag is provided
     */
    public static String serialize(final Tag tag) {
        final StringBuilder builder = new StringBuilder();
        final TagStringWriter writer = new TagStringWriter(builder);
        writer.writeTag(tag);
        return builder.toString();
    }
}