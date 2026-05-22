package com.github.cao.awa.annuus;

import com.github.cao.awa.annuus.config.AnnuusConfig;
import com.github.cao.awa.annuus.information.compressor.InformationCompressorRegistry;
import com.github.cao.awa.annuus.information.compressor.deflate.DeflateCompressor;
import com.github.cao.awa.annuus.information.compressor.inaction.InactionCompressor;
import com.github.cao.awa.annuus.information.compressor.lz4.Lz4Compressor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.zip.Deflater;

public class Annuus {
    public static final Logger LOGGER = LogManager.getLogger("Annuus");
    public static final String VERSION = "1.0.14";
    public static final int PROTOCOL_VERSION_ID = 4;
    public static final AnnuusConfig CONFIG = new AnnuusConfig();
    public static final AnnuusConfig PERSISTENT_CONFIG = new AnnuusConfig();
    public static boolean isServer = true;
    public static long processedChunks = 0;
    public static long processedBytes = 0;
    public static long processedBlockUpdates = 0;
    public static long vanillaBlockUpdateBytes = 0;
    public static long processedBlockUpdateBytes = 0;
    public static double calculatedTimes = 0D;
    public static boolean enableDebugs = false;

    public static final Lz4Compressor LZ4_INSTANCE = InformationCompressorRegistry.register(new Lz4Compressor());
    public static final InactionCompressor INACTION_INSTANCE = InformationCompressorRegistry.register(new InactionCompressor());
    public static final DeflateCompressor BEST_INSTANCE = InformationCompressorRegistry.register(new DeflateCompressor(Deflater.BEST_COMPRESSION));
    public static final DeflateCompressor DEFLATE_8_INSTANCE = InformationCompressorRegistry.register(new DeflateCompressor(8));
    public static final DeflateCompressor DEFLATE_7_INSTANCE = InformationCompressorRegistry.register(new DeflateCompressor(7));
    public static final DeflateCompressor DEFLATE_6_INSTANCE = InformationCompressorRegistry.register(new DeflateCompressor(6));
    public static final DeflateCompressor DEFLATE_5_INSTANCE = InformationCompressorRegistry.register(new DeflateCompressor(5));
    public static final DeflateCompressor DEFLATE_4_INSTANCE = InformationCompressorRegistry.register(new DeflateCompressor(4));
    public static final DeflateCompressor DEFLATE_3_INSTANCE = InformationCompressorRegistry.register(new DeflateCompressor(3));
    public static final DeflateCompressor DEFLATE_2_INSTANCE = InformationCompressorRegistry.register(new DeflateCompressor(2));
    public static final DeflateCompressor FASTEST_INSTANCE = InformationCompressorRegistry.register(new DeflateCompressor(Deflater.BEST_SPEED));

    public static void onInitialize() {
        LOGGER.info("Loading Annuus '{}'", VERSION);
        CONFIG.load();
        PERSISTENT_CONFIG.copyFrom(CONFIG);
        writeConfig();
        CONFIG.print();
    }

    public static void writeConfig() {
        PERSISTENT_CONFIG.write();
    }
}
