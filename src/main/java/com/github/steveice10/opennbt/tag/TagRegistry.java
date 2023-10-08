package com.github.steveice10.opennbt.tag;

import com.github.steveice10.opennbt.tag.builtin.ByteArrayTag;
import com.github.steveice10.opennbt.tag.builtin.ByteTag;
import com.github.steveice10.opennbt.tag.builtin.CompoundTag;
import com.github.steveice10.opennbt.tag.builtin.DoubleTag;
import com.github.steveice10.opennbt.tag.builtin.FloatTag;
import com.github.steveice10.opennbt.tag.builtin.IntArrayTag;
import com.github.steveice10.opennbt.tag.builtin.IntTag;
import com.github.steveice10.opennbt.tag.builtin.ListTag;
import com.github.steveice10.opennbt.tag.builtin.LongArrayTag;
import com.github.steveice10.opennbt.tag.builtin.LongTag;
import com.github.steveice10.opennbt.tag.builtin.ShortTag;
import com.github.steveice10.opennbt.tag.builtin.StringTag;
import com.github.steveice10.opennbt.tag.builtin.Tag;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import java.util.function.Supplier;
import org.jetbrains.annotations.Nullable;

/**
 * A registry containing different tag classes.
 */
public final class TagRegistry {
    private static final int HIGHEST_ID = LongArrayTag.ID;
    private static final RegisteredTagType[] TAGS = new RegisteredTagType[HIGHEST_ID + 1];
    private static final Object2IntMap<Class<? extends Tag>> TAG_TO_ID = new Object2IntOpenHashMap<>();

    static {
        TAG_TO_ID.defaultReturnValue(-1);

        register(ByteTag.ID, ByteTag.class, ByteTag::new);
        register(ShortTag.ID, ShortTag.class, ShortTag::new);
        register(IntTag.ID, IntTag.class, IntTag::new);
        register(LongTag.ID, LongTag.class, LongTag::new);
        register(FloatTag.ID, FloatTag.class, FloatTag::new);
        register(DoubleTag.ID, DoubleTag.class, DoubleTag::new);
        register(ByteArrayTag.ID, ByteArrayTag.class, ByteArrayTag::new);
        register(StringTag.ID, StringTag.class, StringTag::new);
        register(ListTag.ID, ListTag.class, ListTag::new);
        register(CompoundTag.ID, CompoundTag.class, CompoundTag::new);
        register(IntArrayTag.ID, IntArrayTag.class, IntArrayTag::new);
        register(LongArrayTag.ID, LongArrayTag.class, LongArrayTag::new);
    }

    /**
     * Registers a tag class.
     *
     * @param id  ID of the tag.
     * @param tag Tag class to register.
     * @throws IllegalArgumentException if the id is unexpectedly out of bounds, or if the id or tag have already been registered
     */
    public static void register(int id, Class<? extends Tag> tag, Supplier<? extends Tag> supplier) {
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
     * Unregisters a tag class.
     *
     * @param id  ID of the tag to unregister.
     */
    public static void unregister(int id) {
        TAG_TO_ID.removeInt(getClassFor(id));
        TAGS[id] = null;
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
    public static Tag createInstance(int id) {
        Supplier<? extends Tag> supplier = id > 0 && id < TAGS.length ? TAGS[id].supplier : null;
        if (supplier == null) {
            throw new IllegalArgumentException("Could not find tag with ID \"" + id + "\".");
        }

        return supplier.get();
    }

    private static final class RegisteredTagType {

        private final Class<? extends Tag> type;
        private final Supplier<? extends Tag> supplier;

        private RegisteredTagType(final Class<? extends Tag> type, final Supplier<? extends Tag> supplier) {
            this.type = type;
            this.supplier = supplier;
        }
    }
}
