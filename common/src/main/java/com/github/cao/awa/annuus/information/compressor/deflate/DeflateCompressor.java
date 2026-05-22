package com.github.cao.awa.annuus.information.compressor.deflate;

import com.github.cao.awa.annuus.information.compressor.InformationCompressor;
import com.github.cao.awa.annuus.util.IOUtil;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.zip.Deflater;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.InflaterInputStream;

public class DeflateCompressor implements InformationCompressor {

    private final int compressLevel;
    private final String name;

    public DeflateCompressor(int compressLevel) {
        this.compressLevel = compressLevel;
        this.name = "deflate_" + compressLevel;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public int getId() {
        return this.compressLevel;
    }

    /**
     * Compress using 'deflate' with the best compression.
     *
     * @param bytes data source
     *
     * @return compress result
     *
     * @author cao_awa
     *
     * @since 1.0.0
     */
    public byte[] compress(byte[] bytes) {
        if (bytes.length == 0) {
            return EMPTY_BYTES;
        }
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            IOUtil.write(
                    new DeflaterOutputStream(
                            out,
                            new Deflater(this.compressLevel)
                    ),
                    bytes
            );
            return out.toByteArray();
        } catch (Exception e) {
            return bytes;
        }
    }

    /**
     * Decompress using 'inflate'.
     *
     * @param bytes data source
     *
     * @return decompress result
     *
     * @author cao_awa
     *
     * @since 1.0.0
     */
    public byte[] decompress(byte[] bytes) {
        if (bytes.length == 0) {
            return EMPTY_BYTES;
        }
        try (ByteArrayOutputStream result = new ByteArrayOutputStream()) {
            InflaterInputStream inflater = new InflaterInputStream(new ByteArrayInputStream(bytes));
            IOUtil.write(
                    result,
                    inflater
            );
            return result.toByteArray();
        } catch (Exception ex) {
            return bytes;
        }
    }
}
