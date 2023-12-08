package com.github.steveice10.opennbt.conversion;

import com.github.steveice10.opennbt.conversion.converter.ByteArrayTagConverter;
import com.github.steveice10.opennbt.conversion.converter.ByteTagConverter;
import com.github.steveice10.opennbt.conversion.converter.CompoundTagConverter;
import com.github.steveice10.opennbt.conversion.converter.DoubleTagConverter;
import com.github.steveice10.opennbt.conversion.converter.FloatTagConverter;
import com.github.steveice10.opennbt.conversion.converter.IntArrayTagConverter;
import com.github.steveice10.opennbt.conversion.converter.IntTagConverter;
import com.github.steveice10.opennbt.conversion.converter.ListTagConverter;
import com.github.steveice10.opennbt.conversion.converter.LongArrayTagConverter;
import com.github.steveice10.opennbt.conversion.converter.LongTagConverter;
import com.github.steveice10.opennbt.conversion.converter.ShortTagConverter;
import com.github.steveice10.opennbt.conversion.converter.StringTagConverter;
import com.github.steveice10.opennbt.tag.TagRegistry;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.jetbrains.annotations.Nullable;

/**
 * A registry mapping tags and value types to converters.
 */
public class ConverterRegistry {
    private static final Int2ObjectMap<TagConverter<? extends Tag, ?>> TAG_TO_CONVERTER = new Int2ObjectOpenHashMap<>();
    private static final Map<Class<?>, TagConverter<? extends Tag, ?>> TYPE_TO_CONVERTER = new HashMap<>();

    static {
        register(ByteTag.class, Byte.class, new ByteTagConverter());
        register(ShortTag.class, Short.class, new ShortTagConverter());
        register(IntTag.class, Integer.class, new IntTagConverter());
        register(LongTag.class, Long.class, new LongTagConverter());
        register(FloatTag.class, Float.class, new FloatTagConverter());
        register(DoubleTag.class, Double.class, new DoubleTagConverter());
        register(ByteArrayTag.class, byte[].class, new ByteArrayTagConverter());
        register(StringTag.class, String.class, new StringTagConverter());
        register(ListTag.class, List.class, new ListTagConverter());
        register(CompoundTag.class, Map.class, new CompoundTagConverter());
        register(IntArrayTag.class, int[].class, new IntArrayTagConverter());
        register(LongArrayTag.class, long[].class, new LongArrayTagConverter());
    }

    /**
     * Registers a converter.
     *
     * @param <T>       Tag type to convert from.
     * @param <V>       Value type to convert to.
     * @param tag       Tag type class to register the converter to.
     * @param type      Value type class to register the converter to.
     * @param converter Converter to register.
     * @throws IllegalArgumentException if the tag or type are already registered
     */
    public static <T extends Tag, V> void register(Class<T> tag, Class<? extends V> type, TagConverter<T, V> converter) {
        int tagId = TagRegistry.getIdFor(tag);
        if (tagId == -1) {
            throw new IllegalArgumentException("Tag " + tag.getName() + " is not a registered tag.");
        }
        if (TAG_TO_CONVERTER.containsKey(tagId)) {
            throw new IllegalArgumentException("Type conversion to tag " + tag.getName() + " is already registered.");
        }
        if (TYPE_TO_CONVERTER.containsKey(type)) {
            throw new IllegalArgumentException("Tag conversion to type " + type.getName() + " is already registered.");
        }

        TAG_TO_CONVERTER.put(tagId, converter);
        TYPE_TO_CONVERTER.put(type, converter);
    }

    /**
     * Unregisters a converter.
     *
     * @param <T>  Tag type to unregister.
     * @param <V>  Value type to unregister.
     * @param tag  Tag type class to unregister.
     * @param type Value type class to unregister.
     */
    public static <T extends Tag, V> void unregister(Class<T> tag, Class<V> type) {
        TAG_TO_CONVERTER.remove(TagRegistry.getIdFor(tag));
        TYPE_TO_CONVERTER.remove(type);
    }

    /**
     * Converts the given tag to a value.
     *
     * @param <T> Tag type to convert from.
     * @param <V> Value type to convert to.
     * @param tag Tag to convert.
     * @return The converted value.
     * @throws ConversionException If a suitable converter could not be found.
     */
    public static <T extends Tag, V> @Nullable V convertToValue(@Nullable T tag) throws ConversionException {
        if (tag == null || tag.getValue() == null) {
            return null;
        }

        TagConverter<T, ? extends V> converter = (TagConverter<T, ? extends V>) TAG_TO_CONVERTER.get(tag.getTagId());
        if (converter == null) {
            throw new ConversionException("Tag type " + tag.getClass().getName() + " has no converter.");
        }

        return converter.convert(tag);
    }

    /**
     * Converts the given value to a tag.
     *
     * @param <V>   Value type to convert from.
     * @param <T>   Tag type to convert to.
     * @param value Value to convert.
     * @return The converted tag.
     * @throws ConversionException If a suitable converter could not be found.
     */
    public static <V, T extends Tag> @Nullable T convertToTag(@Nullable V value) throws ConversionException {
        if (value == null) {
            return null;
        }

        // No need to check super classes since registering custom tags is not allowed
        // and all the given ones cannot be extended, super class can't be instantiated
        Class<?> valueClass = value.getClass();
        TagConverter<T, ? super V> converter = (TagConverter<T, ? super V>) TYPE_TO_CONVERTER.get(valueClass);
        if (converter == null) {
            throw new ConversionException("Value type " + valueClass.getName() + " has no converter.");
        }

        return converter.convert(value);
    }
}
