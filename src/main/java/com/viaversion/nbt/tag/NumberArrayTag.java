package com.viaversion.nbt.tag;

public interface NumberArrayTag extends Tag {

    /**
     * Gets the length of this tag's array.
     *
     * @return array length
     */
    int length();

    /**
     * Creates a new list tag from this tag.
     *
     * @return a new list tag
     */
    ListTag<? extends NumberTag> toListTag();

    @Override
    NumberArrayTag copy();
}
