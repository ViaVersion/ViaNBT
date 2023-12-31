package com.viaversion.nbt.io;

import com.viaversion.nbt.tag.Tag;
import com.viaversion.nbt.limiter.TagLimiter;
import it.unimi.dsi.fastutil.io.FastBufferedInputStream;
import java.io.DataInput;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.zip.GZIPInputStream;
import org.jetbrains.annotations.Nullable;

/**
 * NBT tag reader.
 *
 * @param <T> the expected tag type
 * @see NBTIO#reader()
 */
public final class TagReader<T extends Tag> {
    private final Class<T> expectedTagType;
    private TagLimiter tagLimiter = TagLimiter.noop();
    private boolean named;

    TagReader(@Nullable final Class<T> expectedTagType) {
        this.expectedTagType = expectedTagType;
    }

    /**
     * Sets the tag limiter to use per read tag, making the reader no longer thread-safe.
     *
     * @param tagLimiter the tag limiter to use
     * @return self
     */
    public TagReader<T> tagLimiter(final TagLimiter tagLimiter) {
        this.tagLimiter = tagLimiter;
        return this;
    }

    /**
     * Sets this reader to read a named tag.
     *
     * @return self
     */
    public TagReader<T> named() {
        this.named = true;
        return this;
    }

    /**
     * Reads the tag from the given data output.
     *
     * @param in data input to read from
     * @throws IOException if an I/O error occurs
     */
    public T read(final DataInput in) throws IOException {
        this.tagLimiter.reset();
        return NBTIO.readTag(in, this.tagLimiter, this.named, this.expectedTagType);
    }

    /**
     * Reads a tag from the given input stream.
     *
     * @param in input stream to read from
     * @return the read tag
     * @throws IOException if an I/O error occurs
     */
    public T read(final InputStream in) throws IOException {
        final DataInput dataInput = new DataInputStream(in);
        return this.read(dataInput);
    }

    /**
     * Reads a tag from the given path. At least so far, the standard format is always named, so make sure to call {@link #named()}.
     *
     * @param path       path to read from
     * @param compressed whether the file is compressed
     * @throws IOException if an I/O error occurs
     */
    public T read(final Path path, final boolean compressed) throws IOException {
        InputStream in = new FastBufferedInputStream(Files.newInputStream(path));
        try {
            if (compressed) {
                in = new GZIPInputStream(in);
            }
            return this.read(in);
        } finally {
            in.close();
        }
    }
}