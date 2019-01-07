package com.github.windsekirun.naraeaudiorecorder.chunk

import com.github.windsekirun.naraeaudiorecorder.constants.AudioConstants
import kotlin.experimental.and

/**
 * NaraeAudioRecorder
 * Class: ShortArrayAudioChunk
 * Created by Pyxis on 1/5/19.
 *
 * Description:
 */
class ShortArrayAudioChunk(var shorts: ShortArray) : AudioChunk {
    private var _readCount: Int = 0

    /**
     * see [AudioChunk.getMaxAmplitude]
     */
    override fun getMaxAmplitude(): Int {
        val maxAmp = shorts.max()?.toInt() ?: 0
        return (20 * Math.log10(maxAmp / AudioConstants.REFERENCE_MAX_AMP)).toInt()
    }

    /**
     * see [AudioChunk.toByteArray]
     */
    override fun toByteArray(): ByteArray {
        var shortIndex: Int
        var byteIndex = 0
        val buffer = ByteArray(getReadCount() * 2)
        shortIndex = byteIndex
        while (shortIndex != getReadCount()) {
            val short = shorts[shortIndex]
            val left = short and 0x00FF
            val right = (short and 0xFF00.toShort()).toInt() shr 8

            buffer[byteIndex] = left.toByte()
            buffer[byteIndex + 1] = right.toByte()
            ++shortIndex
            byteIndex += 2
        }
        return buffer
    }

    /**
     * see [AudioChunk.toShortArray]
     */
    override fun toShortArray(): ShortArray = shorts

    /**
     * see [AudioChunk.getReadCount]
     */
    override fun getReadCount(): Int = _readCount

    /**
     * see [AudioChunk.setReadCount]
     */
    override fun setReadCount(readCount: Int) = this.apply { _readCount = readCount }

    /**
     * find first peak value of [shorts]
     */
    fun findFirstIndex(): Int =
            shorts.indexOfFirst { it >= AudioConstants.SILENCE_THRESHOLD || it <= -AudioConstants.SILENCE_THRESHOLD }

}