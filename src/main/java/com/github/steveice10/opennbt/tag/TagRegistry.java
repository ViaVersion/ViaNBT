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
    private static final Class<? extends Tag>[] idToTag = new Class[HIGHEST_ID + 1];
    private static final Supplier<? extends Tag>[] instanceSuppliers = new Supplier[HIGHEST_ID + 1];
    private static final Object2IntMap<Class<? extends Tag>> tagToId = new Object2IntOpenHashMap<>();

    static {
        tagToId.defaultReturnValue(-1);

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
     * @throws TagRegisterException If an error occurs while registering the tag.
     */
    public static void register(int id, Class<? extends Tag> tag, Supplier<? extends Tag> supplier) throws TagRegisterException {
        if (id < 0 || id > HIGHEST_ID) {
            throw new TagRegisterException("Tag ID must be between 0 and " + HIGHEST_ID);
        }
        if (idToTag[id] != null) {
            throw new TagRegisterException("Tag ID \"" + id + "\" is already in use.");
        }
        if (tagToId.containsKey(tag)) {
            throw new TagRegisterException("Tag \"" + tag.getSimpleName() + "\" is already registered.");
        }

        instanceSuppliers[id] = supplier;
        idToTag[id] = tag;
        tagToId.put(tag, id);
    }

    /**
     * Unregisters a tag class.
     *
     * @param id  ID of the tag to unregister.
     */
    public static void unregister(int id) {
        tagToId.removeInt(getClassFor(id));
        idToTag[id] = null;
        instanceSuppliers[id] = null;
    }

    /**
     * Gets the tag class with the given id.
     *
     * @param id Id of the tag.
     * @return The tag class with the given id, or null if it cannot be found.
     */
    @Nullable
    public static Class<? extends Tag> getClassFor(int id) {
        return id >= 0 && id < idToTag.length ? idToTag[id] : null;
    }

    /**
     * Gets the id of the given tag class.
     *
     * @param clazz The tag class to get the id of.
     * @return The id of the given tag class, or -1 if it cannot be found.
     */
    public static int getIdFor(Class<? extends Tag> clazz) {
        return tagToId.getInt(clazz);
    }

    /**
     * Creates an instance of the tag with the given id, using the String constructor.
     *
     * @param id Id of the tag.
     * @return The created tag.
     * @throws TagCreateException If an error occurs while creating the tag.
     */
    public static Tag createInstance(int id) throws TagCreateException {
        Supplier<? extends Tag> supplier = id > 0 && id < instanceSuppliers.length ? instanceSuppliers[id] : null;
        if (supplier == null) {
            throw new TagCreateException("Could not find tag with ID \"" + id + "\".");
        }

        return supplier.get();
    }
}
