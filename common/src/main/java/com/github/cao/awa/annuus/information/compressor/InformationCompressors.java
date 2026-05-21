package com.github.cao.awa.annuus.information.compressor;

import java.util.HashMap;
import java.util.Map;

public class InformationCompressors {
    private static final Map<Integer, InformationCompressor> COMPRESSORS = new HashMap<>();

    public static <X extends InformationCompressor> X register(X compressor) {
        COMPRESSORS.put(compressor.getId(), compressor);

        return compressor;
    }

    public static InformationCompressor getCompressor(int id) {
        return COMPRESSORS.get(id);
    }
}
