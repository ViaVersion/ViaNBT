package com.github.steveice10.opennbt.tag.builtin;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.lang.reflect.Array;

/**
 * Represents an NBT tag.
 * <p>
 * Tags should also have setter methods specific to their value types.
 */
public abstract class Tag implements Cloneable {

    /**
     * Gets the value of this tag.
     *
     * @return The value of this tag.
     */
    public abstract Object getValue();

    /**
     * Reads this tag from an input stream.
     *
     * @param in Stream to write to.
     * @throws java.io.IOException If an I/O error occurs.
     */
    public abstract void read(DataInput in) throws IOException;

    /**
     * Writes this tag to an output stream.
     *
     * @param out Stream to write to.
     * @throws java.io.IOException If an I/O error occurs.
     */
    public abstract void write(DataOutput out) throws IOException;

    /**
     * Returns the NBT tag id of this tag type, used in I/O.
     *
     * @return Id of the tag this class represents
     */
    public abstract int getTagId();

    @Override
    public abstract Tag clone();

    @Override
    public String toString() {
        //TODO cleanup/push down
        String value = "";
        if(this.getValue() != null) {
            value = this.getValue().toString();
            if(this.getValue().getClass().isArray()) {
                StringBuilder build = new StringBuilder();
                build.append("[");
                for(int index = 0; index < Array.getLength(this.getValue()); index++) {
                    if(index > 0) {
                        build.append(", ");
                    }

                    build.append(Array.get(this.getValue(), index));
                }

                build.append("]");
                value = build.toString();
            }
        }

        return this.getClass().getSimpleName() + " { " + value + " }";
    }
}
