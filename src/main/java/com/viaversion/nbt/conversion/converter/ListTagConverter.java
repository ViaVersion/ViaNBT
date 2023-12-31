package com.viaversion.nbt.conversion.converter;

import com.viaversion.nbt.conversion.ConverterRegistry;
import com.viaversion.nbt.conversion.TagConverter;
import com.viaversion.nbt.tag.ListTag;
import com.viaversion.nbt.tag.Tag;
import java.util.ArrayList;
import java.util.List;

/**
 * A converter that converts between CompoundTag and Map.
 */
public class ListTagConverter implements TagConverter<ListTag, List> {
    @Override
    public List convert(ListTag tag) {
        List<Object> ret = new ArrayList<>();
        List<? extends Tag> tags = tag.getValue();
        for (Tag t : tags) {
            ret.add(ConverterRegistry.convertToValue(t));
        }

        return ret;
    }

    @Override
    public ListTag convert(List value) {
        List<Tag> tags = new ArrayList<>();
        for (Object o : value) {
            tags.add(ConverterRegistry.convertToTag(o));
        }

        return new ListTag(tags);
    }
}
