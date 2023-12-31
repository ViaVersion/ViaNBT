package com.viaversion.nbt.io;

import com.viaversion.nbt.tag.Tag;
import it.unimi.dsi.fastutil.io.FastBufferedOutputStream;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.zip.GZIPOutputStream;

/**
 * Reusable NBT tag writer.
 *
 * @see NBTIO#writer()
 */
public final class TagWriter {
    private boolean named;

    /**
     * Sets this writer to write a named tag.
     *
     * @return self
     */
    public TagWriter named() {
        this.named = true;
        return this;
    }

    /**
     * Writes the tag to the given data output.
     *
     * @param out output stream to write to
     * @param tag tag to write
     * @throws IOException if an I/O error occurs
     */
    public void write(final DataOutput out, final Tag tag) throws IOException {
        NBTIO.writeTag(out, tag, this.named);
    }

    /**
     * Writes the tag to the given output stream.
     *
     * @param out output stream to write to
     * @param tag tag to write
     * @throws IOException if an I/O error occurs
     */
    public void write(final OutputStream out, final Tag tag) throws IOException {
        NBTIO.writeTag(new DataOutputStream(out), tag, this.named);
    }

    /**
     * Writes the tag to the given path. At least so far, the standard format is always named, so make sure to call {@link #named()}.
     *
     * @param path       path to write to
     * @param tag        tag to write
     * @param compressed whether to compress the file
     * @throws IOException if an I/O error occurs
     */
    public void write(final Path path, final Tag tag, final boolean compressed) throws IOException {
        if (!Files.exists(path)) {
            if (path.getParent() != null) {
                Files.createDirectories(path.getParent());
            }
            Files.createFile(path);
        }

        OutputStream out = new FastBufferedOutputStream(Files.newOutputStream(path));
        try {
            if (compressed) {
                out = new GZIPOutputStream(out);
            }
            this.write(out, tag);
        } finally {
            out.close();
        }
    }
}