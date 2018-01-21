package org.xbib.graphics.barcode.output;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.Locale;

/**
 * {@link OutputStreamWriter} extension which provides some convenience methods for writing numbers.
 */
class ExtendedOutputStreamWriter extends OutputStreamWriter {

    /**
     * Format to use when writing doubles to the stream.
     */
    private final String doubleFormat;

    /**
     * Creates a new extended output stream writer, using the UTF-8 charset.
     *
     * @param out          the stream to write to
     * @param doubleFormat the format to use when writing doubles to the stream
     */
    public ExtendedOutputStreamWriter(OutputStream out, String doubleFormat) {
        super(out, StandardCharsets.UTF_8);
        this.doubleFormat = doubleFormat;
    }

    @Override
    public ExtendedOutputStreamWriter append(CharSequence cs) throws IOException {
        super.append(cs);
        return this;
    }

    @Override
    public ExtendedOutputStreamWriter append(CharSequence cs, int start, int end) throws IOException {
        super.append(cs, start, end);
        return this;
    }

    /**
     * Writes the specified double to the stream, formatted according to the format specified in the constructor.
     *
     * @param d the double to write to the stream
     * @return this writer
     * @throws IOException if an I/O error occurs
     */
    public ExtendedOutputStreamWriter append(double d) throws IOException {
        super.append(String.format(Locale.ROOT, doubleFormat, d));
        return this;
    }

    /**
     * Writes the specified integer to the stream.
     *
     * @param i the integer to write to the stream
     * @return this writer
     * @throws IOException if an I/O error occurs
     */
    public ExtendedOutputStreamWriter appendInt(int i) throws IOException {
        super.append(String.valueOf(i));
        return this;
    }
}
