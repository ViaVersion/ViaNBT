package com.github.steveice10.opennbt.conversion.builtin;

import com.github.steveice10.opennbt.conversion.ConverterRegistry;
import com.github.steveice10.opennbt.conversion.TagConverter;
import com.github.steveice10.opennbt.tag.builtin.CompoundTag;
import com.github.steveice10.opennbt.tag.builtin.Tag;

import java.util.HashMap;
import java.util.Map;

/**
 * A converter that converts between CompoundTag and Map.
 */
public class CompoundTagConverter implements TagConverter<CompoundTag, Map> {
    @Override
    public Map convert(CompoundTag tag) {
        Map<String, Object> ret = new HashMap<>();
        Map<String, Tag> tags = tag.getValue();
        for(Map.Entry<String, Tag> entry : tags.entrySet()) {
            ret.put(entry.getKey(), ConverterRegistry.convertToValue(entry.getValue()));
        }

        return ret;
    }

    @Override
    public CompoundTag convert(Map value) {
        Map<String, Tag> tags = new HashMap<>();
        for(Object na : value.keySet()) {
            String n = (String) na;
            tags.put(n, ConverterRegistry.convertToTag(value.get(n)));
        }

        return new CompoundTag(tags);
    }
}
