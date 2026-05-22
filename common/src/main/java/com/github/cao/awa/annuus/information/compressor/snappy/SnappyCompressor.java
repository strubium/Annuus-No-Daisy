package com.github.cao.awa.annuus.information.compressor.snappy;

import com.github.cao.awa.annuus.information.compressor.InformationCompressor;
import org.xerial.snappy.Snappy;

import java.io.IOException;

public class SnappyCompressor implements InformationCompressor {


    @Override
    public String getName() {
        return "snappy";
    }

    @Override
    public int getId() {
        return 11;
    }

    @Override
    public byte[] compress(byte[] bytes) {
        try {
            return Snappy.compress(bytes);
        } catch (IOException e) {
            throw new RuntimeException("Failed to compress using Snappy", e);
        }
    }

    @Override
    public byte[] decompress(byte[] bytes) {
        try {
            return Snappy.uncompress(bytes);
        } catch (IOException e) {
            throw new RuntimeException("Failed to decompress using Snappy", e);
        }
    }
}