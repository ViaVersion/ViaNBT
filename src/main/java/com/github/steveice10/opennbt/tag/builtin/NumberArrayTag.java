package com.github.steveice10.opennbt.tag.builtin;

public abstract class NumberArrayTag extends Tag {

    /**
     * Gets the length of this tag's array.
     *
     * @return array length
     */
    public abstract int length();

    /**
     * Creates a new list tag from this tag.
     *
     * @return a new list tag
     */
    public abstract ListTag<? extends NumberTag> toListTag();
}
