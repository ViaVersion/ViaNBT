package com.github.steveice10.opennbt.tag.builtin;

import com.github.steveice10.opennbt.tag.TagRegistry;
import com.github.steveice10.opennbt.tag.limiter.TagLimiter;
import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import org.jetbrains.annotations.Nullable;

/**
 * A compound tag containing other tags.
 */
public class CompoundTag extends Tag implements Iterable<Entry<String, Tag>> {
    public static final int ID = 10;
    private Map<String, Tag> value;

    /**
     * Creates a tag.
     */
    public CompoundTag() {
        this(new LinkedHashMap<>());
    }

    /**
     * Creates a tag.
     *
     * @param value The value of the tag.
     */
    public CompoundTag(Map<String, Tag> value) {
        this.value = new LinkedHashMap<>(value);
    }

    /**
     * Creates a tag without wrapping the map.
     *
     * @param value The value of the tag.
     */
    public CompoundTag(LinkedHashMap<String, Tag> value) {
        if (value == null) {
            throw new NullPointerException("value cannot be null");
        }
        this.value = value;
    }

    public static CompoundTag read(DataInput in, TagLimiter tagLimiter, int nestingLevel) throws IOException {
        tagLimiter.checkLevel(nestingLevel);
        int newNestingLevel = nestingLevel + 1;
        int id;

        CompoundTag compoundTag = new CompoundTag();
        while (true) {
            tagLimiter.countByte();
            id = in.readByte();
            if (id == TagRegistry.END) {
                break;
            }

            String name = in.readUTF();
            tagLimiter.countBytes(2 * name.length());

            Tag tag;
            try {
                tag = TagRegistry.read(id, in, tagLimiter, newNestingLevel);
            } catch (IllegalArgumentException e) {
                throw new IOException("Failed to create tag.", e);
            }
            compoundTag.value.put(name, tag);
        }
        return compoundTag;
    }

    @Override
    public Map<String, Tag> getValue() {
        return this.value;
    }

    @Override
    public String asRawString() {
        return this.value.toString();
    }

    /**
     * Sets the value of this tag.
     *
     * @param value New value of this tag.
     */
    public void setValue(Map<String, Tag> value) {
        if (value == null) {
            throw new NullPointerException("value cannot be null");
        }
        this.value = new LinkedHashMap<>(value);
    }

    /**
     * Sets the value of this tag without wrapping the map.
     *
     * @param value New value of this tag.
     */
    public void setValue(LinkedHashMap<String, Tag> value) {
        if (value == null) {
            throw new NullPointerException("value cannot be null");
        }
        this.value = value;
    }

    /**
     * Checks whether the compound tag is empty.
     *
     * @return Whether the compound tag is empty.
     */
    public boolean isEmpty() {
        return this.value.isEmpty();
    }

    /**
     * Checks whether the compound tag contains a tag.
     *
     * @param tagName Name of the tag to check for.
     * @return Whether the compound tag contains a tag.
     */
    public boolean contains(String tagName) {
        return this.value.containsKey(tagName);
    }

    /**
     * Gets the tag.
     *
     * @param <T>     Type of tag to get.
     * @param tagName Name of the tag.
     * @return The tag.
     */
    @Nullable
    public <T extends Tag> T get(String tagName) {
        return (T) this.value.get(tagName);
    }

    public @Nullable StringTag getStringTag(String tagName) {
        final Tag tag = this.value.get(tagName);
        return tag instanceof StringTag ? (StringTag) tag : null;
    }

    public @Nullable CompoundTag getCompoundTag(String tagName) {
        final Tag tag = this.value.get(tagName);
        return tag instanceof CompoundTag ? (CompoundTag) tag : null;
    }

    public @Nullable ListTag getListTag(String tagName) {
        final Tag tag = this.value.get(tagName);
        return tag instanceof ListTag ? (ListTag) tag : null;
    }

    public @Nullable NumberTag getNumberTag(String tagName) {
        final Tag tag = this.value.get(tagName);
        return tag instanceof NumberTag ? (NumberTag) tag : null;
    }

    /**
     * Puts the tag into this compound tag.
     *
     * @param <T>     Type of tag to put.
     * @param tagName Name of the tag.
     * @param tag     Tag to put into this compound tag.
     * @return The previous tag associated with its name, or null if there wasn't one.
     */
    @Nullable
    public <T extends Tag> T put(String tagName, T tag) {
        return (T) this.value.put(tagName, tag);
    }

    public void putString(String tagName, String value) {
        this.value.put(tagName, new StringTag(value));
    }

    public void putByte(String tagName, byte value) {
        this.value.put(tagName, new ByteTag(value));
    }

    public void putInt(String tagName, int value) {
        this.value.put(tagName, new IntTag(value));
    }

    public void putShort(String tagName, short value) {
        this.value.put(tagName, new ShortTag(value));
    }

    public void putLong(String tagName, long value) {
        this.value.put(tagName, new LongTag(value));
    }

    public void putFloat(String tagName, float value) {
        this.value.put(tagName, new FloatTag(value));
    }

    public void putDouble(String tagName, double value) {
        this.value.put(tagName, new DoubleTag(value));
    }

    public void putBoolean(String tagName, boolean value) {
        this.value.put(tagName, new ByteTag((byte) (value ? 1 : 0)));
    }

    public void putAll(CompoundTag compoundTag) {
        this.value.putAll(compoundTag.value);
    }

    /**
     * Removes a tag from this compound tag.
     *
     * @param <T>     Type of tag to remove.
     * @param tagName Name of the tag to remove.
     * @return The removed tag.
     */
    @Nullable
    public <T extends Tag> T remove(String tagName) {
        return (T) this.value.remove(tagName);
    }

    /**
     * Gets a set of keys in this compound tag.
     *
     * @return The compound tag's key set.
     */
    public Set<String> keySet() {
        return this.value.keySet();
    }

    /**
     * Gets a collection of tags in this compound tag.
     *
     * @return This compound tag's tags.
     */
    public Collection<Tag> values() {
        return this.value.values();
    }

    /**
     * Gets the entry set of this compound tag.
     *
     * @return The compound tag's entry set.
     */
    public Set<Entry<String, Tag>> entrySet() {
        return this.value.entrySet();
    }

    /**
     * Gets the number of tags in this compound tag.
     *
     * @return This compound tag's size.
     */
    public int size() {
        return this.value.size();
    }

    /**
     * Clears all tags from this compound tag.
     */
    public void clear() {
        this.value.clear();
    }

    @Override
    public Iterator<Entry<String, Tag>> iterator() {
        return this.value.entrySet().iterator();
    }

    @Override
    public void write(DataOutput out) throws IOException {
        for (Entry<String, Tag> entry : this.value.entrySet()) {
            Tag tag = entry.getValue();
            out.writeByte(tag.getTagId());
            out.writeUTF(entry.getKey());
            tag.write(out);
        }

        out.writeByte(TagRegistry.END);
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CompoundTag tags = (CompoundTag) o;
        return this.value.equals(tags.value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    @Override
    public CompoundTag copy() {
        LinkedHashMap<String, Tag> newMap = new LinkedHashMap<>();
        for (Entry<String, Tag> entry : this.value.entrySet()) {
            newMap.put(entry.getKey(), entry.getValue().copy());
        }

        return new CompoundTag(newMap);
    }

    @Override
    public int getTagId() {
        return ID;
    }
}
