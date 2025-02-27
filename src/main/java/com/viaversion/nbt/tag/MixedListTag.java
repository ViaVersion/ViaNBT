package com.viaversion.nbt.tag;

import com.viaversion.nbt.io.TagRegistry;
import java.io.DataOutput;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.jetbrains.annotations.Nullable;

/**
 * Vanilla abomination that allows for mixed tag lists, serialized to a list of wrapping compound tags...
 * <p>
 * {@link #getElementType()} will always return {@code null}.
 */
public final class MixedListTag extends ListTag<Tag> {

    public MixedListTag() {
        //noinspection deprecation
        super();
    }

    public MixedListTag(final List<Tag> value) {
        super(value);
    }

    @Override
    public void setValue(final List<Tag> value) {
        this.value = new ArrayList<>(value);
    }

    @Override
    protected void checkAddedTag(final Tag tag) {
        // No-op
    }

    @Override
    public @Nullable Class<? extends Tag> getElementType() {
        return null;
    }

    @Override
    public void write(final DataOutput out) throws IOException {
        if (this.value.isEmpty()) {
            out.writeByte(TagRegistry.END);
        } else {
            out.writeByte(CompoundTag.ID);
        }

        out.writeInt(this.value.size());
        for (final Tag tag : this.value) {
            wrap(tag).write(out);
        }
    }

    private static Tag wrap(final Tag tag) {
        if (tag instanceof CompoundTag) {
            return tag;
        }

        final CompoundTag wrapper = new CompoundTag();
        wrapper.put("", tag);
        return wrapper;
    }
}
