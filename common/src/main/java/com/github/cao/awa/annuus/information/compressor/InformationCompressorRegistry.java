package com.github.cao.awa.annuus.information.compressor;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static com.github.cao.awa.annuus.Annuus.LOGGER;

public class InformationCompressorRegistry {
    private static final Map<Integer, InformationCompressor> COMPRESSORS_BY_ID = new HashMap<>();
    private static final Map<String, InformationCompressor> COMPRESSORS_BY_NAME = new HashMap<>();

    public static <X extends InformationCompressor> X register(X compressor) {
        LOGGER.info("Registering Compressor: {}", compressor.getName());

        COMPRESSORS_BY_ID.put(compressor.getId(), compressor);
        COMPRESSORS_BY_NAME.put(compressor.getName(), compressor);

        registerAlias(compressor);

        return compressor;
    }

    private static void registerAlias(InformationCompressor compressor) {
        String name = compressor.getAlias();

        if(name != null){
            InformationCompressor existing = COMPRESSORS_BY_NAME.put(name, compressor);
            LOGGER.info("Registering compressor alias: {} for: {}", name, compressor.getName());

            if (existing != null && existing != compressor) {
                LOGGER.warn(
                        "Compressor name/alias '{}' was already registered by '{}', replaced with '{}'",
                        name,
                        existing.getName(),
                        compressor.getName()
                );
            }
        }
    }

    public static InformationCompressor getCompressor(int id) {
        return COMPRESSORS_BY_ID.get(id);
    }

    public static InformationCompressor getCompressor(String name) {
        return COMPRESSORS_BY_NAME.get(name);
    }

    public static Set<String> getCompressors() {
        return COMPRESSORS_BY_NAME.values()
                .stream()
                .map(InformationCompressor::getName)
                .collect(Collectors.toUnmodifiableSet());
    }
}