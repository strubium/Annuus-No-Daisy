package com.github.cao.awa.annuus.information.compressor;

/**
 * Information compressor.
 *
 * @author cao_awa
 *
 * @since 1.0.0
 */
public interface InformationCompressor {
    byte[] EMPTY_BYTES = new byte[0];

    /**
     * Gets the name of this {@link InformationCompressor}. This is used for selecting this compressor in the config.
     *
     * @return the name of this {@link InformationCompressor}
     *
     * @author strubium
     *
     * @since 1.0.15
     */
    String getName();


    /**
     * Gets the ID of this {@link InformationCompressor}
     *
     * @return the ID of this {@link InformationCompressor}
     *
     * @author cao_awa
     *
     * @since 1.0.0
     */
    int getId();

    /**
     * Compress for a data.
     *
     * @param bytes data source
     *
     * @return compress result
     *
     * @author cao_awa
     *
     * @since 1.0.0
     */
    byte[] compress(byte[] bytes);

    /**
     * Decompress for a data.
     *
     * @param bytes data source
     *
     * @return decompress result
     *
     * @author cao_awa
     *
     * @since 1.0.0
     */
    byte[] decompress(byte[] bytes);
}
