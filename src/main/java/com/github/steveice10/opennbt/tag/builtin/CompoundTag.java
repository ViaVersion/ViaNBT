package com.github.steveice10.opennbt.tag.builtin;

import com.github.steveice10.opennbt.tag.TagCreateException;
import com.github.steveice10.opennbt.tag.TagRegistry;
import com.google.common.base.Preconditions;
import org.jetbrains.annotations.Nullable;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.EOFException;
import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

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
        Preconditions.checkNotNull(value);
        this.value = value;
    }

    @Override
    public Map<String, Tag> getValue() {
        return this.value;
    }

    /**
     * Sets the value of this tag.
     *
     * @param value New value of this tag.
     */
    public void setValue(Map<String, Tag> value) {
        Preconditions.checkNotNull(value);
        this.value = new LinkedHashMap<>(value);
    }

    /**
     * Sets the value of this tag without wrapping the map.
     *
     * @param value New value of this tag.
     */
    public void setValue(LinkedHashMap<String, Tag> value) {
        Preconditions.checkNotNull(value);
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

    /**
     * Puts the tag into this compound tag.
     *
     * @param <T>  Type of tag to put.
     * @param tagName Name of the tag.
     * @param tag  Tag to put into this compound tag.
     * @return The previous tag associated with its name, or null if there wasn't one.
     */
    @Nullable
    public <T extends Tag> T put(String tagName, T tag) {
        return (T) this.value.put(tagName, tag);
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
    public void read(DataInput in) throws IOException {
        try {
            int id;
            while (true) {
                id = in.readByte();
                if (id == 0) {
                    // End tag
                    break;
                }

                String name = in.readUTF();
                Tag tag = TagRegistry.createInstance(id);
                tag.read(in);
                this.value.put(name, tag);
            }
        } catch(TagCreateException e) {
            throw new IOException("Failed to create tag.", e);
        } catch(EOFException e) {
            throw new IOException("Closing tag was not found!");
        }
    }

    @Override
    public void write(DataOutput out) throws IOException {
        for(Entry<String, Tag> entry : this.value.entrySet()) {
            Tag tag = entry.getValue();
            out.writeByte(tag.getTagId());
            out.writeUTF(entry.getKey());
            tag.write(out);
        }

        // End
        out.writeByte(0);
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
    public final CompoundTag clone() {
        LinkedHashMap<String, Tag> newMap = new LinkedHashMap<>();
        for(Entry<String, Tag> entry : this.value.entrySet()) {
            newMap.put(entry.getKey(), entry.getValue().clone());
        }

        return new CompoundTag(newMap);
    }

    @Override
    public int getTagId() {
        return ID;
    }
}
