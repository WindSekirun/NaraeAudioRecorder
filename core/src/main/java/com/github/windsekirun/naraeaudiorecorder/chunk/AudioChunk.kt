package com.github.windsekirun.naraeaudiorecorder.chunk

/**
 * NaraeAudioRecorder
 * Class: AudioChunk
 * Created by Pyxis on 1/5/19.
 *
 * Description:
 */

interface AudioChunk {
    /**
     * get Max value of Amplitude of this [AudioChunk]
     */
    fun getMaxAmplitude(): Int

    /**
     * Convert [ByteArray] to [ShortArray] to calculate
     */
    fun toByteArray() : ByteArray

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