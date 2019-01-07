package com.github.windsekirun.naraeaudiorecorder.chunk

/**
 * Chunk file which contains raw data which came from [android.media.AudioRecord]
 */
interface AudioChunk {
    /**
     * get Max value of Amplitude of this [AudioChunk]
     */
    fun getMaxAmplitude(): Int

    /**
     * Convert [ByteArray] to [ShortArray] to calculate
     */
    fun toByteArray(): ByteArray

    /**
     * Convert [ShortArray] to [ByteArray] to calculate
     */
    fun toShortArray(): ShortArray

    /**
     * get Read count of this [AudioChunk]
     */
    fun getReadCount(): Int

    /**
     * set Read count of this [AudioChunk]
     */
    fun setReadCount(readCount: Int): AudioChunk
}