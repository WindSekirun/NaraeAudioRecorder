package com.github.windsekirun.naraeaudiorecorder.config

import android.media.AudioFormat
import android.media.MediaRecorder
import com.github.windsekirun.naraeaudiorecorder.constants.AudioConstants

/**
 * Data class for config of [android.media.AudioRecord]
 *
 * @param audioSource Source for AudioRecord, such as [MediaRecorder.AudioSource.MIC]
 * @param audioEncoding Encoding format for AudioRecord, such as [AudioFormat.ENCODING_PCM_16BIT]
 * @param channel Channel for AudioRecord, such as [AudioFormat.CHANNEL_IN_MONO]
 * @param frequency Frequency of AudioRecord, such as 44100
 */
data class AudioRecordConfig(val audioSource: Int, val audioEncoding: Int, val channel: Int, val frequency: Int) {

    companion object {

        /**
         * get [AudioRecordConfig] with default setting (MIC, PCM 16bit, Mono, 44100)
         */
        @JvmStatic
        fun defaultConfig() = AudioRecordConfig(MediaRecorder.AudioSource.MIC,
                AudioFormat.ENCODING_PCM_16BIT,
                AudioFormat.CHANNEL_IN_MONO,
                AudioConstants.FREQUENCY_44100)
    }
}
