package com.github.windsekirun.naraeaudiorecorder.chunk

import com.github.windsekirun.naraeaudiorecorder.constants.AudioConstants
import java.nio.ByteBuffer
import java.nio.ByteOrder

/**
 * [AudioChunk] which handle [ByteArray]
 */
class ByteArrayAudioChunk(var bytes: ByteArray) : AudioChunk {
    private var _readCount: Int = 0

    /**
     * see [AudioChunk.getMaxAmplitude]
     */
    override fun getMaxAmplitude(): Int {
        val maxAmp = toShortArray().max()?.toInt() ?: 0
        return (20 * Math.log10(maxAmp / AudioConstants.REFERENCE_MAX_AMP)).toInt()
    }

    /**
     * see [AudioChunk.toByteArray]
     */
    override fun toByteArray(): ByteArray = bytes

    /**
     * see [AudioChunk.toShortArray]
     */
    override fun toShortArray(): ShortArray {
        val shorts = ShortArray(bytes.size / 2)
        ByteBuffer.wrap(bytes).order(ByteOrder.LITTLE_ENDIAN).asShortBuffer().get(shorts)
        return shorts
    }

    /**
     * see [AudioChunk.getReadCount]
     */
    override fun getReadCount(): Int = _readCount

    /**
     * see [AudioChunk.setReadCount]
     */
    override fun setReadCount(readCount: Int) = this.apply { _readCount = readCount }
}