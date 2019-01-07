package com.github.windsekirun.naraeaudiorecorder.recorder.wav

import android.media.AudioFormat
import com.github.windsekirun.naraeaudiorecorder.source.AudioSource

/**
 * Class for write header information
 */
class WavHeader(private val audioSource: AudioSource, private val length: Long) {

    /**
     * generate wav header using [audioSource], [length] and assign them to [ByteArray]
     */
    fun getWavFileHeaderByteArray(): ByteArray {
        val frequency = audioSource.getAudioConfig().frequency.toLong()
        val channels = if (audioSource.getAudioConfig().channel == AudioFormat.CHANNEL_IN_MONO) 1 else 2
        val bitsPerSample = when (audioSource.getAudioConfig().audioEncoding) {
            AudioFormat.ENCODING_PCM_16BIT -> 16
            AudioFormat.ENCODING_PCM_8BIT -> 8
            else -> 16
        }.toByte()

        return wavFileHeader(length - 44, length - 44 + 36, frequency,
                channels, bitsPerSample.toLong() * frequency * channels.toLong() / 8, bitsPerSample
        )
    }

    private fun wavFileHeader(
            totalAudioLen: Long, totalDataLen: Long, longSampleRate: Long,
            channels: Int, byteRate: Long, bitsPerSample: Byte
    ): ByteArray {
        val header = ByteArray(44)
        header[0] = 'R'.toByte() // RIFF/WAVE header
        header[1] = 'I'.toByte()
        header[2] = 'F'.toByte()
        header[3] = 'F'.toByte()
        header[4] = (totalDataLen and 0xff).toByte()
        header[5] = (totalDataLen shr 8 and 0xff).toByte()
        header[6] = (totalDataLen shr 16 and 0xff).toByte()
        header[7] = (totalDataLen shr 24 and 0xff).toByte()
        header[8] = 'W'.toByte()
        header[9] = 'A'.toByte()
        header[10] = 'V'.toByte()
        header[11] = 'E'.toByte()
        header[12] = 'f'.toByte() // 'fmt ' chunk
        header[13] = 'm'.toByte()
        header[14] = 't'.toByte()
        header[15] = ' '.toByte()
        header[16] = 16 // 4 bytes: size of 'fmt ' chunk
        header[17] = 0
        header[18] = 0
        header[19] = 0
        header[20] = 1 // format = 1
        header[21] = 0
        header[22] = channels.toByte()
        header[23] = 0
        header[24] = (longSampleRate and 0xff).toByte()
        header[25] = (longSampleRate shr 8 and 0xff).toByte()
        header[26] = (longSampleRate shr 16 and 0xff).toByte()
        header[27] = (longSampleRate shr 24 and 0xff).toByte()
        header[28] = (byteRate and 0xff).toByte()
        header[29] = (byteRate shr 8 and 0xff).toByte()
        header[30] = (byteRate shr 16 and 0xff).toByte()
        header[31] = (byteRate shr 24 and 0xff).toByte()
        header[32] = (channels * (bitsPerSample / 8)).toByte()
        header[33] = 0
        header[34] = bitsPerSample // bits per sample
        header[35] = 0
        header[36] = 'd'.toByte()
        header[37] = 'a'.toByte()
        header[38] = 't'.toByte()
        header[39] = 'a'.toByte()
        header[40] = (totalAudioLen and 0xff).toByte()
        header[41] = (totalAudioLen shr 8 and 0xff).toByte()
        header[42] = (totalAudioLen shr 16 and 0xff).toByte()
        header[43] = (totalAudioLen shr 24 and 0xff).toByte()
        return header
    }
}