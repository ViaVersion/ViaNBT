package com.viaversion.nbt.io;

import com.viaversion.nbt.limiter.TagLimiter;
import com.viaversion.nbt.tag.ByteArrayTag;
import com.viaversion.nbt.tag.ByteTag;
import com.viaversion.nbt.tag.CompoundTag;
import com.viaversion.nbt.tag.DoubleTag;
import com.viaversion.nbt.tag.FloatTag;
import com.viaversion.nbt.tag.IntArrayTag;
import com.viaversion.nbt.tag.IntTag;
import com.viaversion.nbt.tag.ListTag;
import com.viaversion.nbt.tag.LongArrayTag;
import com.viaversion.nbt.tag.LongTag;
import com.viaversion.nbt.tag.ShortTag;
import com.viaversion.nbt.tag.StringTag;
import com.viaversion.nbt.tag.Tag;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import java.io.DataInput;
import java.io.IOException;
import org.jetbrains.annotations.Nullable;

/**
 * A registry containing different tag classes.
 */
public final class TagRegistry {
    public static final int END = 0;
    private static final int HIGHEST_ID = LongArrayTag.ID;
    private static final RegisteredTagType[] TAGS = new RegisteredTagType[HIGHEST_ID + 1];
    private static final Object2IntMap<Class<? extends Tag>> TAG_TO_ID = new Object2IntOpenHashMap<>();

    static {
        TAG_TO_ID.defaultReturnValue(-1);

        register(ByteTag.ID, ByteTag.class, (in, tagLimiter, nestingLevel) -> ByteTag.read(in, tagLimiter));
        register(ShortTag.ID, ShortTag.class, (in, tagLimiter, nestingLevel) -> ShortTag.read(in, tagLimiter));
        register(IntTag.ID, IntTag.class, (in, tagLimiter, nestingLevel) -> IntTag.read(in, tagLimiter));
        register(LongTag.ID, LongTag.class, (in, tagLimiter, nestingLevel) -> LongTag.read(in, tagLimiter));
        register(FloatTag.ID, FloatTag.class, (in, tagLimiter, nestingLevel) -> FloatTag.read(in, tagLimiter));
        register(DoubleTag.ID, DoubleTag.class, (in, tagLimiter, nestingLevel) -> DoubleTag.read(in, tagLimiter));
        register(ByteArrayTag.ID, ByteArrayTag.class, (in, tagLimiter, nestingLevel) -> ByteArrayTag.read(in, tagLimiter));
        register(StringTag.ID, StringTag.class, (in, tagLimiter, nestingLevel) -> StringTag.read(in, tagLimiter));
        register(ListTag.ID, ListTag.class, ListTag::read);
        register(CompoundTag.ID, CompoundTag.class, CompoundTag::read);
        register(IntArrayTag.ID, IntArrayTag.class, (in, tagLimiter, nestingLevel) -> IntArrayTag.read(in, tagLimiter));
        register(LongArrayTag.ID, LongArrayTag.class, (in, tagLimiter, nestingLevel) -> LongArrayTag.read(in, tagLimiter));
    }

    /**
     * Registers a tag class.
     *
     * @param id  ID of the tag.
     * @param tag Tag class to register.
     * @throws IllegalArgumentException if the id is unexpectedly out of bounds, or if the id or tag have already been registered
     */
    public static <T extends Tag> void register(int id, Class<T> tag, TagSupplier<T> supplier) {
        if (id < 0 || id > HIGHEST_ID) {
            throw new IllegalArgumentException("Tag ID must be between 0 and " + HIGHEST_ID);
        }
        if (TAGS[id] != null) {
            throw new IllegalArgumentException("Tag ID \"" + id + "\" is already in use.");
        }
        if (TAG_TO_ID.containsKey(tag)) {
            throw new IllegalArgumentException("Tag \"" + tag.getSimpleName() + "\" is already registered.");
        }

        TAGS[id] = new RegisteredTagType(tag, supplier);
        TAG_TO_ID.put(tag, id);
    }

    /**
     * Gets the tag class with the given id.
     *
     * @param id Id of the tag.
     * @return The tag class with the given id, or null if it cannot be found.
     */
    @Nullable
    public static Class<? extends Tag> getClassFor(int id) {
        return id >= 0 && id < TAGS.length ? TAGS[id].type : null;
    }

    /**
     * Gets the id of the given tag class.
     *
     * @param clazz The tag class to get the id of.
     * @return The id of the given tag class, or -1 if it cannot be found.
     */
    public static int getIdFor(Class<? extends Tag> clazz) {
        return TAG_TO_ID.getInt(clazz);
    }

    /**
     * Creates an instance of the tag with the given id, using the String constructor.
     *
     * @param id Id of the tag.
     * @return The created tag.
     * @throws IllegalArgumentException if no tags is registered over the provided id
     */
    public static Tag read(int id, DataInput in, TagLimiter tagLimiter, int nestingLevel) throws IOException {
        TagSupplier<?> supplier = id > 0 && id < TAGS.length ? TAGS[id].supplier : null;
        if (supplier == null) {
            throw new IllegalArgumentException("Could not find tag with ID \"" + id + "\".");
        }
        return supplier.create(in, tagLimiter, nestingLevel);
    }

    private static final class RegisteredTagType {

        private final Class<? extends Tag> type;
        private final TagSupplier<? extends Tag> supplier;

        private <T extends Tag> RegisteredTagType(final Class<T> type, final TagSupplier<T> supplier) {
            this.type = type;
            this.supplier = supplier;
        }
    }

    @FunctionalInterface
    public interface TagSupplier<T extends Tag> {

        T create(DataInput in, TagLimiter tagLimiter, int nestingLevel) throws IOException;
    }
}
