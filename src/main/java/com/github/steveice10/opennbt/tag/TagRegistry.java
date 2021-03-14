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
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;


/**
 * A registry containing different tag classes.
 */
public class TagRegistry {
    private static final Int2ObjectMap<Class<? extends Tag>> idToTag = new Int2ObjectOpenHashMap<>();
    private static final Object2IntMap<Class<? extends Tag>> tagToId = new Object2IntOpenHashMap<>();
    private static final Int2ObjectMap<Supplier<? extends Tag>> instanceSuppliers = new Int2ObjectOpenHashMap<>();

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
        if(idToTag.containsKey(id)) {
            throw new TagRegisterException("Tag ID \"" + id + "\" is already in use.");
        }

        if(tagToId.containsKey(tag)) {
            throw new TagRegisterException("Tag \"" + tag.getSimpleName() + "\" is already registered.");
        }

        instanceSuppliers.put(id, supplier);
        idToTag.put(id, tag);
        tagToId.put(tag, id);
    }

    /**
     * Unregisters a tag class.
     *
     * @param id  ID of the tag to unregister.
     */
    public static void unregister(int id) {
        tagToId.removeInt(getClassFor(id));
        idToTag.remove(id);
    }

    /**
     * Gets the tag class with the given id.
     *
     * @param id Id of the tag.
     * @return The tag class with the given id, or null if it cannot be found.
     */
    @Nullable
    public static Class<? extends Tag> getClassFor(int id) {
        return idToTag.get(id);
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
        Supplier<? extends Tag> supplier = instanceSuppliers.get(id);
        if(supplier == null) {
            throw new TagCreateException("Could not find tag with ID \"" + id + "\".");
        }

        return supplier.get();
    }
}
