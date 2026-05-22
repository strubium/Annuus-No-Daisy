package com.github.cao.awa.annuus.information.compressor;

import java.util.HashMap;
import java.util.Map;

import static com.github.cao.awa.annuus.Annuus.LOGGER;

public class InformationCompressorRegistry {
    private static final Map<Integer, InformationCompressor> COMPRESSORS_BY_ID = new HashMap<>();
    private static final Map<String, InformationCompressor> COMPRESSORS_BY_NAME = new HashMap<>();

    public static <X extends InformationCompressor> X register(X compressor) {
        LOGGER.info("Registering Compressor: {}", compressor.getName());

        COMPRESSORS_BY_ID.put(compressor.getId(), compressor);
        COMPRESSORS_BY_NAME.put(compressor.getName(), compressor);

        return compressor;
    }

    public static InformationCompressor getCompressor(int id) {
        return COMPRESSORS_BY_ID.get(id);
    }

    public static InformationCompressor getCompressor(String name) {
        return COMPRESSORS_BY_NAME.get(name);
    }
}