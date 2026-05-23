package com.github.cao.awa.annuus.config;

import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.JSONWriter;
import com.github.cao.awa.annuus.Annuus;
import com.github.cao.awa.annuus.config.key.AnnuusConfigKey;
import com.github.cao.awa.annuus.information.compressor.InformationCompressor;
import com.github.cao.awa.annuus.information.compressor.InformationCompressorRegistry;
import com.github.cao.awa.annuus.network.packet.client.play.block.update.CollectedBlockUpdatePayload;
import com.github.cao.awa.annuus.network.packet.client.play.chunk.update.CollectedChunkBlockUpdatePayload;
import com.github.cao.awa.annuus.network.packet.client.play.chunk.data.CollectedChunkDataPayload;
import com.github.cao.awa.annuus.network.packet.client.play.recipe.ShortRecipeSyncPayload;
import com.github.cao.awa.annuus.util.IOUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Function;

import static com.github.cao.awa.annuus.Annuus.LOGGER;

public class AnnuusConfig {
    private static final File CONFIG_FILE = new File("config/annuus.json");

    private static final Function<String, InformationCompressor> COMPRESSOR_FETCHER = compressOption -> switch (compressOption) {
        case "best_compress" -> Annuus.BEST_INSTANCE;
        case "best_speed" -> Annuus.FASTEST_INSTANCE;
        default -> {
            InformationCompressor compressor = InformationCompressorRegistry.getCompressor(compressOption);

            if (compressor == null) {
                throw new IllegalStateException("Unexpected value: " + compressOption);
            }

            yield compressor;
        }
    };

    public static final AnnuusConfigKey<String> CHUNK_COMPRESS = AnnuusConfigKey.create(
            "chunk_compression",
            (compressOption) -> CollectedChunkDataPayload.setCurrentCompressor(COMPRESSOR_FETCHER.apply(compressOption)),
            "best_compress",
            InformationCompressorRegistry.getCompressors()
    );

    public static final AnnuusConfigKey<String> BLOCK_UPDATES_COMPRESS = AnnuusConfigKey.create(
            "block_updates_compression",
            (compressOption) -> {
                CollectedBlockUpdatePayload.setCurrentCompressor(COMPRESSOR_FETCHER.apply(compressOption));
                CollectedChunkBlockUpdatePayload.setCurrentCompressor(COMPRESSOR_FETCHER.apply(compressOption));
            },
            "best_compress",
            InformationCompressorRegistry.getCompressors()
    );

    public static final AnnuusConfigKey<Boolean> SHORT_RECIPES = AnnuusConfigKey.create(
            "short_recipes",
            true
    );

    public static final AnnuusConfigKey<String> SHORT_RECIPES_COMPRESS = AnnuusConfigKey.create(
            "short_recipes_compress",
            (compressOption) -> ShortRecipeSyncPayload.setCurrentCompressor(COMPRESSOR_FETCHER.apply(compressOption)),
            "best_compress",
            InformationCompressorRegistry.getCompressors()
    );

    private final JSONObject config = new JSONObject();

    public boolean isEnableChunkCompress() {
        return !getConfig(CHUNK_COMPRESS).equals("no_compress");
    }

    public String chunkCompress() {
        return getConfig(CHUNK_COMPRESS);
    }

    public boolean isEnableBlockUpdatesCompress() {
        return !getConfig(BLOCK_UPDATES_COMPRESS).equals("no_compress");
    }

    public String blockUpdatesCompress() {
        return getConfig(BLOCK_UPDATES_COMPRESS);
    }

    public boolean isEnableShortRecipes() {
        return getConfig(SHORT_RECIPES);
    }

    public boolean isEnableShortRecipesCompress() {
        return !getConfig(SHORT_RECIPES_COMPRESS).equals("no_compress");
    }

    public String shortRecipesCompress() {
        return getConfig(SHORT_RECIPES_COMPRESS);
    }

    public <X> void setConfig(AnnuusConfigKey<X> configKey, X value) {
        this.config.put(configKey.name(), configKey.onChangeCheck(check(configKey, value)));
    }

    public <X> void setConfig(AnnuusConfigKey<X> configKey, JSONObject json) {
        setConfig(configKey, check(configKey, json.get(configKey.name())));
    }

    public <X> X getConfig(AnnuusConfigKey<X> configKey) {
        Object value = this.config.get(configKey.name());
        if (value == null) {
            return configKey.defaultValue();
        }
        return check(configKey, value);
    }

    @Nullable
    private static <X> X check(@NotNull AnnuusConfigKey<X> configKey, @Nullable Object value) {
        if (value == null) {
            return null;
        }

        if (configKey.type().isInstance(value)) {
            return configKey.type().cast(value);
        }

        throw new IllegalArgumentException(
                "Config '" + configKey.name() +
                        "' required '" + configKey.type() +
                        "' but got '" + value.getClass() + "'"
        );
    }

    public void load() {
        loadAsDefault();

        try {
            final JSONObject config = JSONObject.parse(IOUtil.read(new FileReader(CONFIG_FILE, StandardCharsets.UTF_8)));

            setConfig(CHUNK_COMPRESS, config);
            setConfig(BLOCK_UPDATES_COMPRESS, config);
            setConfig(SHORT_RECIPES, config);
            setConfig(SHORT_RECIPES_COMPRESS, config);
        } catch (Exception e) {
            LOGGER.warn("Config not found, use default values", e);
        }

        write();
    }

    public void write() {
        try {
            File parent = CONFIG_FILE.getParentFile();
            if (parent != null && !parent.exists()) {
                boolean ok = parent.mkdirs();
                if (!ok) {
                    LOGGER.warn("Failed to create config directory: {}", parent);
                    return;
                }
            }
            IOUtil.write(
                    new FileWriter(CONFIG_FILE, StandardCharsets.UTF_8),
                    this.config.toString(JSONWriter.Feature.PrettyFormat)
            );
        } catch (Exception e) {
            LOGGER.warn("Failed to save config", e);
        }
    }

    public void loadAsDefault() {
        setConfig(CHUNK_COMPRESS, CHUNK_COMPRESS.defaultValue());
        setConfig(BLOCK_UPDATES_COMPRESS, BLOCK_UPDATES_COMPRESS.defaultValue());
        setConfig(SHORT_RECIPES, SHORT_RECIPES.defaultValue());
        setConfig(SHORT_RECIPES_COMPRESS, SHORT_RECIPES_COMPRESS.defaultValue());
    }

    public void copyFrom(@NotNull AnnuusConfig config) {
        setConfig(CHUNK_COMPRESS, config.chunkCompress());
        setConfig(BLOCK_UPDATES_COMPRESS, config.blockUpdatesCompress());
        setConfig(SHORT_RECIPES, config.isEnableShortRecipes());
        setConfig(SHORT_RECIPES_COMPRESS, config.shortRecipesCompress());
    }

    public void print() {
        if (isEnableChunkCompress()) {
            LOGGER.info("Annuus is enabled chunk compression: {}", chunkCompress());
        }

        if (isEnableBlockUpdatesCompress()) {
            LOGGER.info("Annuus is enabled block updates compression: {}", blockUpdatesCompress());
        }

        if (isEnableShortRecipes()) {
            LOGGER.info("Annuus is enabled short recipes synchronize (replacement v1)");
        }

        if (isEnableShortRecipesCompress()) {
            LOGGER.info("Annuus is enabled short recipes synchronize compression: {}", shortRecipesCompress());
        }
    }

    public Set<AnnuusConfigKey<?>> collectEnabled() {
        Set<AnnuusConfigKey<?>> enabled = new HashSet<>();

        if (isEnableChunkCompress()) {
            enabled.add(CHUNK_COMPRESS);
        }

        if (isEnableBlockUpdatesCompress()) {
            enabled.add(BLOCK_UPDATES_COMPRESS);
        }

        if (isEnableShortRecipes()) {
            enabled.add(SHORT_RECIPES);
        }

        if (isEnableShortRecipesCompress()) {
            enabled.add(SHORT_RECIPES_COMPRESS);
        }

        return enabled;
    }
}
