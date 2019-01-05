package com.github.windsekirun.naraeaudiorecorder.source

import android.media.AudioRecord
import com.github.windsekirun.naraeaudiorecorder.config.AudioRecordConfig

/**
 * Default settings of [AudioSource]
 *
 * @param audioRecordConfig [AudioRecordConfig] instance for configure [AudioRecord]
 */
class DefaultAudioSource(private val audioRecordConfig: AudioRecordConfig) : AudioSource {

    /**
     * backing property name for [getMinimumBufferSize]
     * Surprisingly, these naming guidelines are mentioned in the official document.
     * https://kotlinlang.org/docs/reference/coding-conventions.html#property-names
     */
    private val _minimumBufferSize: Int by lazy {
        AudioRecord.getMinBufferSize(
            audioRecordConfig.frequency,
            audioRecordConfig.channel, audioRecordConfig.audioEncoding
        )
    }

    /**
     * backing property name for [getAudioRecord]
     */
    private val _audioRecord: AudioRecord by lazy {
        AudioRecord(
            audioRecordConfig.audioSource,
            audioRecordConfig.frequency,
            audioRecordConfig.channel,
            audioRecordConfig.audioEncoding,
            getMinimumBufferSize()
        )
    }

    override fun getAudioRecord(): AudioRecord = _audioRecord

    override fun getAudioConfig(): AudioRecordConfig = audioRecordConfig

    override fun getMinimumBufferSize(): Int = _minimumBufferSize
}