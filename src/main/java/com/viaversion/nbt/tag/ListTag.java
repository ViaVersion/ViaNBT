package com.viaversion.nbt.tag;

import com.viaversion.nbt.io.TagRegistry;
import com.viaversion.nbt.limiter.TagLimiter;
import com.viaversion.nbt.stringified.SNBT;
import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;
import org.jetbrains.annotations.Nullable;

/**
 * A tag containing a list of tags.
 *
 * @see MixedListTag
 */
public class ListTag<T extends Tag> implements Tag, Iterable<T> {
    public static final int ID = 9;
    protected List<T> value;
    private Class<T> type;

    /**
     * Creates an empty list tag and no defined type.
     *
     * @deprecated use {@link MixedListTag} if you need to mix element types
     */
    @Deprecated
    public ListTag() {
        this.value = new ArrayList<>();
    }

    /**
     * Creates an empty list tag and type.
     *
     * @param type Tag type of the list.
     */
    public ListTag(Class<T> type) {
        this.type = type;
        this.value = new ArrayList<>();
    }

    private ListTag(Class<T> type, List<T> value) {
        this.type = type;
        this.value = value;
    }

    /**
     * Creates a list tag and value.
     * The list tag's type will be set to that of the first tag being added, or null if the given list is empty.
     *
     * @param value The value of the tag.
     * @throws IllegalArgumentException If all tags in the list are not of the same type.
     */
    public ListTag(List<T> value) {
        this.setValue(value);
    }

    /**
     * Creates a list tag, possibly of mixed types.
     *
     * @param value list of tags, can be mixed
     * @return a new list tag
     */
    public static ListTag<?> of(List<Tag> value) {
        int type = -1;
        for (final Tag tag : value) {
            if (type == -1) {
                type = tag.getTagId();
            } else if (type != tag.getTagId()) {
                return new MixedListTag(value);
            }
        }
        return new ListTag<>(value);
    }

    public static ListTag<?> read(DataInput in, TagLimiter tagLimiter, int nestingLevel) throws IOException {
        tagLimiter.checkLevel(nestingLevel);
        tagLimiter.countBytes(Byte.BYTES + Integer.BYTES);

        int id = in.readByte();
        Class<? extends Tag> type = null;
        if (id != TagRegistry.END) {
            type = TagRegistry.getClassFor(id);
            if (type == null) {
                throw new IOException("Unknown tag ID in ListTag: " + id);
            }
        }
        return read(in, id, type, tagLimiter, nestingLevel);
    }

    private static <T extends Tag> ListTag<?> read(DataInput in, int id, Class<T> type, TagLimiter tagLimiter, int nestingLevel) throws IOException {
        int count = in.readInt();
        ListTag<T> listTag = new ListTag<>(type, new ArrayList<>(Math.min(count, Short.MAX_VALUE)));
        int newNestingLevel = nestingLevel + 1;
        for (int index = 0; index < count; index++) {
            T tag;
            try {
                //noinspection unchecked
                tag = (T) TagRegistry.read(id, in, tagLimiter, newNestingLevel);
            } catch (IllegalArgumentException e) {
                throw new IOException("Failed to create tag.", e);
            }

            Tag wrappedTag = unwrap(tag);
            if (wrappedTag != null) {
                // Mixed types...
                //noinspection unchecked
                final MixedListTag mixedListTag = new MixedListTag(((ListTag) listTag).value);
                mixedListTag.add(wrappedTag);

                final int remaining = count - index - 1;
                return readMixed(mixedListTag, in, tagLimiter, nestingLevel, remaining);
            }

            listTag.value.add(tag);
        }
        return listTag;
    }

    private static MixedListTag readMixed(MixedListTag listTag, DataInput in, TagLimiter tagLimiter, int nestingLevel, int count) throws IOException {
        for (int index = 0; index < count; index++) {
            Tag tag;
            try {
                tag = TagRegistry.read(CompoundTag.ID, in, tagLimiter, nestingLevel);
            } catch (IllegalArgumentException e) {
                throw new IOException("Failed to create tag.", e);
            }

            Tag wrappedTag = unwrap(tag);
            listTag.add(wrappedTag != null ? wrappedTag : tag);
        }
        return listTag;
    }

    private static @Nullable Tag unwrap(final Tag tag) {
        if (tag instanceof CompoundTag) {
            final CompoundTag compoundTag = (CompoundTag) tag;
            if (compoundTag.size() == 1) {
                return compoundTag.get("");
            }
        }
        return null;
    }

    @Override
    public List<T> getValue() {
        return this.value; // TODO Make unmodifiable
    }

    @Override
    public String asRawString() {
        return this.value.toString();
    }

    /**
     * Sets the value of this tag.
     * The list tag's type will be set to that of the first tag being added, or null if the given list is empty.
     *
     * @param value New value of this tag.
     * @throws IllegalArgumentException If all tags in the list are not of the same type.
     */
    public void setValue(List<T> value) {
        this.value = new ArrayList<>(value);
        if (!value.isEmpty()) {
            if (this.type == null) {
                this.type = (Class<T>) value.get(0).getClass();
            }
            for (T t : value) {
                this.checkType(t);
            }
        }
    }

    /**
     * Gets the element type of the ListTag.
     *
     * @return The ListTag's element type, or null if the list does not yet have a defined type.
     */
    public @Nullable Class<? extends Tag> getElementType() {
        return this.type;
    }

    /**
     * Adds a tag to this list tag.
     * If the list does not yet have a type, it will be set to the type of the tag being added.
     *
     * @param tag Tag to add. Should not be null.
     * @return If the list was changed as a result.
     * @throws IllegalArgumentException If the tag's type differs from the list tag's type.
     */
    public boolean add(T tag) throws IllegalArgumentException {
        this.checkAddedTag(tag);
        return this.value.add(tag);
    }

    protected void checkAddedTag(T tag) {
        if (this.type == null) {
            this.type = (Class<T>) tag.getClass();
        } else {
            this.checkType(tag);
        }
    }

    private void checkType(Tag tag) {
        if (tag.getClass() != this.type) {
            throw new IllegalArgumentException("Tag type " + tag.getClass().getSimpleName() + " differs from list type " + this.type.getSimpleName());
        }
    }

    /**
     * Removes a tag from this list tag.
     *
     * @param tag Tag to remove.
     * @return If the list contained the tag.
     */
    public boolean remove(T tag) {
        return this.value.remove(tag);
    }

    /**
     * Gets the tag at the given index of this list tag.
     *
     * @param index Index of the tag.
     * @return The tag at the given index.
     */
    public T get(int index) {
        return this.value.get(index);
    }

    /**
     * Sets the tag at the given index of this list tag.
     *
     * @param index Index of the tag.
     * @param tag   New tag to set.
     * @return The old tag at the given index.
     */
    public T set(int index, T tag) {
        this.checkAddedTag(tag);
        return this.value.set(index, tag);
    }

    /**
     * Removes the tag at the given index of this list tag.
     *
     * @param index Index of the tag.
     * @return The removed tag at the given index.
     */
    public T remove(int index) {
        return this.value.remove(index);
    }

    /**
     * Gets the number of tags in this list tag.
     *
     * @return The size of this list tag.
     */
    public int size() {
        return this.value.size();
    }

    public boolean isEmpty() {
        return this.value.isEmpty();
    }

    public Stream<T> stream() {
        return this.value.stream();
    }

    @Override
    public Iterator<T> iterator() {
        return this.value.iterator();
    }

    @Override
    public void write(DataOutput out) throws IOException {
        if (this.value.isEmpty()) {
            out.writeByte(TagRegistry.END);
        } else {
            int id = TagRegistry.getIdFor(this.type);
            if (id == -1) {
                throw new IOException("ListTag contains unregistered tag class.");
            }

            out.writeByte(id);
        }

        out.writeInt(this.value.size());
        for (Tag tag : this.value) {
            tag.write(out);
        }
    }

    @Override
    public ListTag<T> copy() {
        ListTag<T> copy = new ListTag<>(this.type);
        copy.value = new ArrayList<>(this.value.size());
        for (T value : this.value) {
            copy.add((T) value.copy());
        }
        return copy;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ListTag<?> tags = (ListTag<?>) o;
        if (!Objects.equals(this.type, tags.type)) return false;
        return this.value.equals(tags.value);
    }

    @Override
    public int hashCode() {
        int result = this.type != null ? this.type.hashCode() : 0;
        result = 31 * result + this.value.hashCode();
        return result;
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
