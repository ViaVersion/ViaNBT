package com.github.steveice10.opennbt.tag.builtin;

import com.github.steveice10.opennbt.tag.TagCreateException;
import com.github.steveice10.opennbt.tag.TagRegistry;
import com.google.common.base.Preconditions;
import org.jetbrains.annotations.Nullable;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

/**
 * A tag containing a list of tags.
 */
public class ListTag extends Tag implements Iterable<Tag> {
    public static final int ID = 9;
    private final List<Tag> value;
    private Class<? extends Tag> type;

    /**
     * Creates an empty list tag and no defined type.
     */
    public ListTag() {
        this.value = new ArrayList<>();
    }

    /**
     * Creates an empty list tag and type.
     * @param type Tag type of the list.
     */
    public ListTag(@Nullable Class<? extends Tag> type) {
        this.type = type;
        this.value = new ArrayList<>();
    }

    /**
     * Creates a list tag and value.
     * The list tag's type will be set to that of the first tag being added, or null if the given list is empty.
     *
     * @param value The value of the tag.
     * @throws IllegalArgumentException If all tags in the list are not of the same type.
     */
    public ListTag(List<Tag> value) throws IllegalArgumentException {
        this.value = new ArrayList<>(value.size());
        this.setValue(value);
    }

    @Override
    public List<Tag> getValue() {
        return this.value;
    }

    /**
     * Sets the value of this tag.
     * The list tag's type will be set to that of the first tag being added, or null if the given list is empty.
     *
     * @param value New value of this tag.
     * @throws IllegalArgumentException If all tags in the list are not of the same type.
     */
    public void setValue(List<Tag> value) throws IllegalArgumentException {
        Preconditions.checkNotNull(value);

        this.type = null;
        this.value.clear();

        for(Tag tag : value) {
            this.add(tag);
        }
    }

    /**
     * Gets the element type of the ListTag.
     *
     * @return The ListTag's element type, or null if the list does not yet have a defined type.
     */
    public Class<? extends Tag> getElementType() {
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
    public boolean add(Tag tag) throws IllegalArgumentException {
        Preconditions.checkNotNull(tag);

        // If empty list, use this as tag type.
        if(this.type == null) {
            this.type = tag.getClass();
        } else if(tag.getClass() != this.type) {
            throw new IllegalArgumentException("Tag type cannot differ from ListTag type.");
        }

        return this.value.add(tag);
    }

    /**
     * Removes a tag from this list tag.
     *
     * @param tag Tag to remove.
     * @return If the list contained the tag.
     */
    public boolean remove(Tag tag) {
        return this.value.remove(tag);
    }

    /**
     * Gets the tag at the given index of this list tag.
     *
     * @param <T>   Type of tag to get
     * @param index Index of the tag.
     * @return The tag at the given index.
     */
    public <T extends Tag> T get(int index) {
        return (T) this.value.get(index);
    }

    /**
     * Gets the number of tags in this list tag.
     *
     * @return The size of this list tag.
     */
    public int size() {
        return this.value.size();
    }

    @Override
    public Iterator<Tag> iterator() {
        return this.value.iterator();
    }

    @Override
    public void read(DataInput in) throws IOException {
        this.type = null;

        int id = in.readByte();
        if(id != 0) {
            this.type = TagRegistry.getClassFor(id);
            if(this.type == null) {
                throw new IOException("Unknown tag ID in ListTag: " + id);
            }
        }

        int count = in.readInt();
        for(int index = 0; index < count; index++) {
            Tag tag;
            try {
                tag = TagRegistry.createInstance(id);
            } catch(TagCreateException e) {
                throw new IOException("Failed to create tag.", e);
            }

            tag.read(in);
            this.add(tag);
        }
    }

    @Override
    public void write(DataOutput out) throws IOException {
        if(this.type == null) {
            out.writeByte(0);
        } else {
            int id = TagRegistry.getIdFor(this.type);
            if(id == -1) {
                throw new IOException("ListTag contains unregistered tag class.");
            }

            out.writeByte(id);
        }

        out.writeInt(this.value.size());
        for(Tag tag : this.value) {
            tag.write(out);
        }
    }

    @Override
    public final ListTag clone() {
        List<Tag> newList = new ArrayList<>();
        for(Tag value : this.value) {
            newList.add(value.clone());
        }

        return new ListTag(newList);
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ListTag tags = (ListTag) o;
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
}
