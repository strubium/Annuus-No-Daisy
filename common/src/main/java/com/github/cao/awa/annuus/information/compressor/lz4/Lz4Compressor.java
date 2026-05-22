package com.github.cao.awa.annuus.information.compressor.lz4;

import com.github.cao.awa.annuus.information.compressor.InformationCompressor;
import net.jpountz.lz4.LZ4Compressor;
import net.jpountz.lz4.LZ4Factory;

import java.nio.ByteBuffer;
import java.util.Arrays;

public class Lz4Compressor implements InformationCompressor {

    @Override
    public String getName() {
        return "lz4";
    }

    @Override
    public int getId() {
        return 10;
    }

    /**
     * Compress using lz4 with the fastest compression.
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
        LZ4Compressor compressor = LZ4Factory.fastestJavaInstance()
                .fastCompressor();

        int maxLength = compressor.maxCompressedLength(bytes.length);

        ByteBuffer buffer = ByteBuffer.allocate(4 + maxLength);

        buffer.putInt(bytes.length);

        int compressedLength = compressor.compress(
                bytes,
                0,
                bytes.length,
                buffer.array(),
                4,
                maxLength
        );

        return Arrays.copyOf(buffer.array(), 4 + compressedLength);
    }

    /**
     * Decompress using lz4.
     *
     * @param bytes data source
     *
     * @author cao_awa
     *
     * @return decompress result
     */
    public byte[] decompress(byte[] bytes) {
        ByteBuffer buffer = ByteBuffer.wrap(bytes);

        int originalLength = buffer.getInt();

        byte[] result = new byte[originalLength];

        LZ4Factory.fastestJavaInstance()
                .fastDecompressor()
                .decompress(
                        bytes,
                        4,
                        result,
                        0,
                        originalLength
                );

        return result;
    }
}